package com.example.healthcareapp.Admin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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

public class UpdateDoctorsActivity extends AppCompatActivity {


    private ImageView imgDoctor;
    private ImageButton btnSelectDoctorImage;
    private EditText etDoctorID, etDoctorName, etJobTitle, etWhatsAppNumber, etPhoneNumber, etInstaId, etLocation, etRating, etDescription;
    private Button btnUpdate, btnDelete, btnBack;
    private int doctorId;
    private String name, jobTitle, phone, whatsapp, insta, description, rating, location;

    String doctorName, doctorJobTitle, doctorPhone, doctorWhatsapp, doctorInstaId, doctorDescription, doctorLocation;
    Double doctorRating;

    private byte[] imageBytes;
    private Bitmap imagebitmap;
    private ImageHelper imageHelper = new ImageHelper();
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_doctors);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imgDoctor = findViewById(R.id.imgDoctor);
        btnSelectDoctorImage = findViewById(R.id.btnSelectDoctorImage);
        etDoctorID = findViewById(R.id.etDoctorID);
        etDoctorName = findViewById(R.id.etDoctorName);
        etJobTitle = findViewById(R.id.etJobTitle);
        etWhatsAppNumber = findViewById(R.id.etWhatsAppNumber);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etInstaId = findViewById(R.id.etInstaId);
        etLocation = findViewById(R.id.etLocation);
        etDescription = findViewById(R.id.etDescription);
        etRating = findViewById(R.id.etRating);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        btnBack = findViewById(R.id.btnBack);
        doctorId = getIntent().getIntExtra("ID", 0);

        dbHelper = new DatabaseHelper(this);

        btnBack.setOnClickListener(v -> finish());

        btnSelectDoctorImage.setOnClickListener(v -> imageHelper.openGallery(this));

        // get and set doctor id
        etDoctorID.setText(String.valueOf(doctorId));
        etDoctorID.setEnabled(false);

        // get and set doctor name
        etDoctorName.setText(dbHelper.getDoctorName(doctorId));

        // get and set doctor image
        imageBytes = dbHelper.getDoctorImage(doctorId);
        imgDoctor.setImageBitmap(imageHelper.bytesToBitmap(imageBytes));

        // get and set doctor job title
        etJobTitle.setText(dbHelper.getDoctorJobTitle(doctorId));

        // get and set doctor phone number
        etPhoneNumber.setText(dbHelper.getDoctorPhoneNumber(doctorId));

        // get and set doctor whatsapp number
        etWhatsAppNumber.setText(dbHelper.getDoctorWhatsAppNumber(doctorId));

        // get and set doctor instagram id
        etInstaId.setText(dbHelper.getDoctorInstagramUsername(doctorId));

        // get and set doctor location
        etLocation.setText(dbHelper.getDoctorLocation(doctorId));

        // get and set doctor rating
        etRating.setText(String.valueOf(dbHelper.getDoctorRating(doctorId)));

        // get and set doctor description
        etDescription.setText(dbHelper.getDoctorDescription(doctorId));

        try {
            doctorRating = Double.parseDouble(etRating.getText().toString().trim());
        } catch (NumberFormatException e) {
            doctorRating = 0.0;
        }

        btnUpdate.setOnClickListener(v ->{

            doctorName = etDoctorName.getText().toString().trim();
            doctorJobTitle = etJobTitle.getText().toString().trim();
            doctorPhone = etPhoneNumber.getText().toString().trim();
            doctorWhatsapp = etWhatsAppNumber.getText().toString().trim();
            doctorInstaId = etInstaId.getText().toString().trim();
            doctorDescription = etDescription.getText().toString().trim();
            doctorLocation = etLocation.getText().toString().trim();

            if (checkValide()){
                boolean result = dbHelper.updateDoctors(doctorId, doctorName, imageBytes, doctorJobTitle, doctorWhatsapp, doctorPhone, doctorInstaId, doctorLocation, doctorRating, doctorDescription);
                if (result){
                    Toast.makeText(this, "Doctor Updated Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Doctor Update Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnDelete.setOnClickListener(v ->{
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Delete Doctor");
            alert.setMessage("Are you sure you want to delete this doctor?");
            alert.setPositiveButton("Yes", (dialog, when)->{
                boolean result = dbHelper.deleteDoctor(doctorId);
                if (result){
                    Toast.makeText(this, "Doctor Deleted Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Doctor Deletion Failed", Toast.LENGTH_SHORT).show();
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
                imgDoctor.setImageBitmap(imagebitmap);
                imageBytes = imageHelper.bitmapToBytes(imagebitmap);

            } catch (IOException e) {
                Toast.makeText(this, "Image Upload Failed!!" + e, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean checkValide(){
        name = etDoctorName.getText().toString().trim();
        jobTitle = etJobTitle.getText().toString().trim();
        phone = etPhoneNumber.getText().toString().trim();
        whatsapp = etWhatsAppNumber.getText().toString().trim();
        insta = etInstaId.getText().toString().trim();
        description = etDescription.getText().toString().trim();
        location = etLocation.getText().toString().trim();
        rating = etRating.getText().toString().trim();

        boolean isValid = true;


        if (imageBytes == null){
            Toast.makeText(this, "Please Select Image!!", Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        if (name.isEmpty()){
            etDoctorName.setError("Please Enter Name");
            isValid = false;
        }

        if (jobTitle.isEmpty()){
            etJobTitle.setError("Please Enter Job Title");
            isValid = false;
        }

        if (phone.isEmpty()){
            etPhoneNumber.setError("Please Enter Phone Number");
            isValid = false;
        }
        if (phone.length() != 10){
            etPhoneNumber.setError("Phone Number Must be 10 digits");
            isValid = false;
        }

        if (whatsapp.isEmpty()){
            etWhatsAppNumber.setError("Please Enter WhatsApp number");
            isValid = false;
        }
        if (whatsapp.length() != 10){
            etWhatsAppNumber.setError("WhatsApp Numbert Must Be 10 digits");
            isValid = false;
        }

        if (insta.isEmpty()){
            etInstaId.setError("Please Enter Instagram ID");
            isValid = false;
        }

        if (location.isEmpty()){
            etLocation.setError("Please Enter Location");
            isValid = false;
        } else if (!location.startsWith("https")) {
            etLocation.setError("Please enter valid location");
            isValid = false;
        }

        if (rating.isEmpty()){
            etRating.setError("Please Enter Location");
            isValid = false;
        }

        if (description.isEmpty()){
            etDescription.setError("Please Enter description");
            isValid = false;
        }
        return isValid;
    }
}