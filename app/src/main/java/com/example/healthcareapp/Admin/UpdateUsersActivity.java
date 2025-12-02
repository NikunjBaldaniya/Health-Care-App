package com.example.healthcareapp.Admin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.healthcareapp.Database.DatabaseHelper;
import com.example.healthcareapp.ImageHelper;
import com.example.healthcareapp.R;

import java.io.IOException;

public class UpdateUsersActivity extends AppCompatActivity {

    private int userId;
    private ImageView imgUser;
    private ImageButton btnUploadImage;
    private EditText etUserID ,etName, etEmail, etPassword, etAge, etHeight, etWeight, etAddress;
    private AutoCompleteTextView bloodGroupDropDown, activityleveldropdown;
    private RadioButton rbMale, rbFemale;
    private Button btnBack, btnDelete, btnUpdate;
    private TextView txtPasswordError, txtGenderError, txtBloodGroupError, txtActivityLevelError;

    private String name, email, password, age, height, weight, blood, activitylevel, gender, address;
    private byte[] imageBytes;
    private Bitmap imagebitmap;
    private ImageHelper imageHelper = new ImageHelper();
    private DatabaseHelper dbHelper;
    String userName;
    String userEmail;
    Bitmap userImage;
    String userPassword;
    String userGender;
    int userAge;
    double userHeight;
    double userWeight;
    String userBloodGroup;
    String userActivityLevel;
    String userAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_users);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // Initialize Views
        imgUser = findViewById(R.id.imgUser);
        btnUploadImage = findViewById(R.id.btnUploadImage);
        etUserID = findViewById(R.id.etUserID);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etAge = findViewById(R.id.etAge);
        etHeight = findViewById(R.id.etHeight);
        etWeight = findViewById(R.id.etWeight);
        bloodGroupDropDown = findViewById(R.id.bloodGroupDropDown);
        activityleveldropdown = findViewById(R.id.activityleveldropdown);
        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);
        btnBack = findViewById(R.id.btnBack);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        txtPasswordError = findViewById(R.id.txtPasswordError);
        txtGenderError = findViewById(R.id.txtGenderError);
        txtBloodGroupError = findViewById(R.id.txtBloodGroupError);
        txtActivityLevelError = findViewById(R.id.txtActivityLevelError);
        etAddress = findViewById(R.id.etAddress);
        userId = getIntent().getIntExtra("ID", 0);

        // Get Data From Database
        dbHelper = new DatabaseHelper(this);

        // get and set user ID
        etUserID.setText(String.valueOf(userId));
        etUserID.setEnabled(false);

        // get and set user name
        userName = dbHelper.getNameByUserId(userId);
        etName.setText(userName);

        // get and set user image
        imageBytes = dbHelper.getImageByUserId(userId);
        userImage = imageHelper.bytesToBitmap(imageBytes);
        if (userImage != null){
            imgUser.setImageBitmap(userImage);
        }

        // get and set user email
        userEmail = dbHelper.getEmailByUserId(userId);
        etEmail.setText(userEmail);

        // get and set user password
        userPassword = dbHelper.getUserPassword(userId);
        etPassword.setText(userPassword);

        // get and set user age
        userAge = dbHelper.getAgeByUserId(userId);
        etAge.setText(String.valueOf(userAge));

        // get and set user height
        userHeight = dbHelper.getHeightByUserId(userId);
        etHeight.setText(String.valueOf(userHeight));

        // get and set user weight
        userWeight = dbHelper.getWeightByUserId(userId);
        etWeight.setText(String.valueOf(userWeight));

        // get and set user gender
        userGender = dbHelper.getGenderByUserId(userId);
        if ("male".equalsIgnoreCase(userGender)){
            rbMale.setChecked(true);
        } else {
            rbFemale.setChecked(true);
        }

        // get and set user address
        userAddress = dbHelper.getAddressByUserId(userId);
        etAddress.setText(userAddress);

        // get and set user Blood Group
        userBloodGroup = dbHelper.getBloodGroupByUserId(userId);
        bloodGroupDropDown.setText(userBloodGroup);

        // get and set user activity level
        userActivityLevel = dbHelper.getActivityLevelByUserId(userId);
        activityleveldropdown.setText(userActivityLevel);

        String[] bloodGroups = {"A⁺", "A⁻", "B⁺", "B⁻", "AB⁺", "AB⁻", "O⁺", "O⁻"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, bloodGroups);
        bloodGroupDropDown.setAdapter(adapter);

        String[] activityLevel = {"Sedentary", "Moderate", "Active"};
        ArrayAdapter<String> activityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, activityLevel);
        activityleveldropdown.setAdapter(activityAdapter);

        btnBack.setOnClickListener(v -> finish());

        //Image Picker
        btnUploadImage.setOnClickListener(v -> imageHelper.openGallery(this));


        // Update User Data in Database
        dbHelper = new DatabaseHelper(this);
        btnUpdate.setOnClickListener(v ->{
            if (checkValidation()){
                boolean isUpdated = dbHelper.updateUser(
                        userId, name, imageBytes, email, password, gender, address, Integer.parseInt(age), Double.parseDouble(height), Double.parseDouble(weight), blood, activitylevel);
                if (isUpdated){
                    Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnDelete.setOnClickListener(v ->{
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Delete Doctor");
            alert.setMessage("Are you sure you want to delete this doctor?");
            alert.setPositiveButton("Yes", (dialog, when)->{
                boolean isDeleted = dbHelper.deleteUser(userId);
                if (isDeleted){
                    Toast.makeText(this, "User Deleted!!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "User Deletion Failed!!", Toast.LENGTH_SHORT).show();
                }
                finish();
            });
            alert.setNegativeButton("No", (dialog, when) ->{
                dialog.cancel();
            });
            alert.show();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == imageHelper.PICK_IMAGE_REQUEST && resultCode ==RESULT_OK && data != null && data.getData() != null){
            Uri imageUri = data.getData();
            try {
                // Convert URI to Bitmap
                imagebitmap = imageHelper.uriToBitmap(this, imageUri);
                imgUser.setImageBitmap(imagebitmap);
                imageBytes = imageHelper.bitmapToBytes(imagebitmap);

            } catch (IOException e) {
                Toast.makeText(this, "Image Upload Failed!!" + e, Toast.LENGTH_SHORT).show();
            }
        }
    }

    // safe Validation Method
    private Boolean checkValidation() {
        boolean isValid = true;

        // Clear old errors
        txtPasswordError.setVisibility(View.GONE);
        txtGenderError.setVisibility(View.GONE);
        txtBloodGroupError.setVisibility(View.GONE);
        txtActivityLevelError.setVisibility(View.GONE);

        name = etName.getText().toString().trim();
        email = etEmail.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        age = etAge.getText().toString().trim();
        height = etHeight.getText().toString().trim();
        weight = etWeight.getText().toString().trim();
        blood = bloodGroupDropDown.getText().toString().trim();
        activitylevel = activityleveldropdown.getText().toString().trim();
        gender = rbMale.isChecked() ? "Male" : rbFemale.isChecked() ? "Female" : "";
        address = etAddress.getText().toString().trim();

        // Image
        if (imageBytes == null){
            Toast.makeText(this, "Please Select Image!!", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        // Name
        if (name.isEmpty()) {
            etName.setError("Please enter name");
            isValid = false;
        }

        // Email
        if (email.isEmpty()) {
            etEmail.setError("Please enter Email");
            isValid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Please Enter Correct Email!");
            isValid = false;
        }

        // Password
        if (password.isEmpty()) {
            txtPasswordError.setText("ⓘ Please Enter Your Password");
            txtPasswordError.setVisibility(View.VISIBLE);
            isValid = false;
        } else if (password.length() < 8) {
            txtPasswordError.setText("ⓘ Please enter at least 8 characters");
            txtPasswordError.setVisibility(View.VISIBLE);
            isValid = false;
        }

        // Age
        if (age.isEmpty()) {
            etAge.setError("Please enter age");
            isValid = false;
        }

        // Height
        if (height.isEmpty()) {
            etHeight.setError("Please enter height");
            isValid = false;
        } else {
            double h = Double.parseDouble(height);
            if (h < 50 || h > 300) {
                etHeight.setError("Please enter height (50-300)");
                isValid = false;
            }
        }

        // Weight
        if (weight.isEmpty()) {
            etWeight.setError("Please enter weight");
            isValid = false;
        } else {
            double w = Double.parseDouble(weight);
            if (w < 10 || w > 700) {
                etWeight.setError("Please enter weight (10-700)");
                isValid = false;
            }
        }

        // Blood Group
        if (blood.isEmpty()) {
            txtBloodGroupError.setText("ⓘ Please Select Blood Group");
            txtBloodGroupError.setVisibility(View.VISIBLE);
            isValid = false;
        }

        // Activity Level
        if (activitylevel.isEmpty()) {
            txtActivityLevelError.setText("ⓘ Please Select Activity Level");
            txtActivityLevelError.setVisibility(View.VISIBLE);
            isValid = false;
        }

        // Gender
        if (gender.isEmpty()) {
            txtGenderError.setText("ⓘ Please Select Gender");
            txtGenderError.setVisibility(View.VISIBLE);
            isValid = false;
        }

        // Address
        if (address.isEmpty()) {
            etAddress.setError("Please enter address");
            isValid = false;
        }

        // Profile Image
        if (imageBytes == null) {
            Toast.makeText(this, "Please upload profile image", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        return isValid;
    }
}