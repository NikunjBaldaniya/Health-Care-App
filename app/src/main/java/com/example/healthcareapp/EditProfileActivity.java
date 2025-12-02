package com.example.healthcareapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.*;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthcareapp.Database.DatabaseHelper;

import java.io.IOException;

public class EditProfileActivity extends AppCompatActivity {

    private ImageView imgUser;
    private EditText etName, etEmail, etAge, etHeight, etWeight, etAddress;
    private AutoCompleteTextView bloodGroupDropDown, activityleveldropdown;
    private TextView txtGenderError, txtBloodGroupError, txtActivityLevelError;
    private RadioButton rbMale, rbFemale;
    private String gender = "";
    private String address;
    int userId;
    Bitmap imagebitmap;
    ImageHelper imageHelper = new ImageHelper();
    byte[] imageBytes;
    String userName;
    String userEmail;
    Bitmap userImage;
    String userGender;
    int userAge;
    double userHeight;
    double userWeight;
    String userBloodGroup;
    String userActivityLevel;
    String userAddress;
    DatabaseHelper dbHelper;
    String name, email, age, height, weight, blood, activitylevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        imgUser = findViewById(R.id.imgUser);
        ImageButton btnEditProfileImage = findViewById(R.id.btnEditProfileImage);
        Button btnBack = findViewById(R.id.btnBack);
        Button btnSave = findViewById(R.id.btnSave);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etAge = findViewById(R.id.etAge);
        etHeight = findViewById(R.id.etHeight);
        etWeight = findViewById(R.id.etWeight);
        bloodGroupDropDown = findViewById(R.id.bloodGroupDropDown);
        activityleveldropdown = findViewById(R.id.activityleveldropdown);
        txtGenderError = findViewById(R.id.txtGenderError);
        txtBloodGroupError = findViewById(R.id.txtBloodGroupError);
        txtActivityLevelError = findViewById(R.id.txtActivityLevelError);
        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);
        etAddress = findViewById(R.id.etAddress);

        // SharedPreferences access
        SharedPreferences prefs = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        userEmail = prefs.getString("userEmail", null);


        // Get Data From Database
        dbHelper = new DatabaseHelper(this);

        // get and set user image
        byte[] dbImage = dbHelper.getImageByUserId(userId);
        if (dbImage != null) {
            imageBytes = dbImage;
            userImage = ImageHelper.bytesToBitmap(dbImage);
            imgUser.setImageBitmap(userImage);
        }

        //Get User ID
        userId = dbHelper.getUserIdByEmail(userEmail);

        // get and set user name
        userName = dbHelper.getNameByUserId(userId);
        etName.setText(userName);

        // get and set user image
        userImage = imageHelper.bytesToBitmap(dbHelper.getImageByUserId(userId));
        if (userImage != null){
            imgUser.setImageBitmap(userImage);
        }

        // get and set user email
        etEmail.setText(userEmail);

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

        // get and ser user address
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

        // Back Button
        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        });

        //Image Picker
        btnEditProfileImage.setOnClickListener(v -> imageHelper.openGallery(this));


        DatabaseHelper db = new DatabaseHelper(this);
        //Save Profile Button
        btnSave.setOnClickListener(v -> {
            if (checkValidation()){
                boolean isUpdated = db.updateUserProfile(
                        userId, name, imageBytes, email, gender, address, Integer.parseInt(age), Double.parseDouble(height), Double.parseDouble(weight), blood, activitylevel);
                if (isUpdated){
                    Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show();
                }
            }
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
    private Boolean checkValidation() {
        boolean isValid = true;

        // Clear old errors
        txtGenderError.setVisibility(View.GONE);
        txtBloodGroupError.setVisibility(View.GONE);
        txtActivityLevelError.setVisibility(View.GONE);

        name = etName.getText().toString().trim();
        email = etEmail.getText().toString().trim();
        age = etAge.getText().toString().trim();
        height = etHeight.getText().toString().trim();
        weight = etWeight.getText().toString().trim();
        blood = bloodGroupDropDown.getText().toString().trim();
        activitylevel = activityleveldropdown.getText().toString().trim();
        gender = rbMale.isChecked() ? "Male" : rbFemale.isChecked() ? "Female" : "";
        address = etAddress.getText().toString().trim();

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
