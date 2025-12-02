package com.example.healthcareapp.Admin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.healthcareapp.Database.DatabaseHelper;
import com.example.healthcareapp.ImageHelper;
import com.example.healthcareapp.R;

import java.io.IOException;

public class AdminAddDoctorActivity extends AppCompatActivity {

    private ImageView imgDoctor;
    private ImageButton btnSelectDoctorImage;
    private EditText etDoctorName, etJobTitle, etWhatsAppNumber, etPhoneNumber, etInstaId, etLocation, etRating, etDescription;
    private Button btnSave, btnBack;

    private Bitmap imageBitmap;
    private byte[] imageBytes;

    private final ImageHelper imageHelper = new ImageHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_doctor);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        // Initialize UI
        imgDoctor = findViewById(R.id.imgDoctor);
        btnSelectDoctorImage = findViewById(R.id.btnSelectDoctorImage);
        etDoctorName = findViewById(R.id.etDoctorName);
        etJobTitle = findViewById(R.id.etJobTitle);
        etWhatsAppNumber = findViewById(R.id.etWhatsAppNumber);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etInstaId = findViewById(R.id.etInstaId);
        etLocation = findViewById(R.id.etLocation);
        etRating = findViewById(R.id.etRating);
        etDescription = findViewById(R.id.etDescription);
        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        btnSave.setOnClickListener(v -> {
            if (checkValidation()) {
                // collect values
                String doctorName = etDoctorName.getText().toString().trim();
                String doctorJobTitle = etJobTitle.getText().toString().trim();
                String doctorWhatsAppNumber = etWhatsAppNumber.getText().toString().trim();
                String doctorPhoneNumber = etPhoneNumber.getText().toString().trim();
                String doctorInstaId = etInstaId.getText().toString().trim();
                String doctorLocation = etLocation.getText().toString().trim();
                String doctorRating = etRating.getText().toString().trim();
                String doctorDescription = etDescription.getText().toString().trim();

                // Convert bitmap to bytes
                imageBytes = imageHelper.bitmapToBytes(imageBitmap);

                // DB insert
                DatabaseHelper dbHelper = new DatabaseHelper(this);
                boolean result = dbHelper.insertDoctor(
                        doctorName,
                        imageBytes,
                        doctorJobTitle,
                        doctorWhatsAppNumber,
                        doctorPhoneNumber,
                        doctorInstaId,
                        doctorLocation,
                        Double.parseDouble(doctorRating),
                        doctorDescription
                );

                if (result) {
                    Toast.makeText(this, "Doctor added successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Doctor not added", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSelectDoctorImage.setOnClickListener(v -> imageHelper.openGallery(this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == imageHelper.PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                imageBitmap = imageHelper.uriToBitmap(this, imageUri);
                imgDoctor.setImageBitmap(imageBitmap);
                imageBytes = imageHelper.bitmapToBytes(imageBitmap);
            } catch (IOException e) {
                Toast.makeText(this, "Image Upload Failed!! " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean checkValidation() {
        boolean isValid = true;

        if (imageBitmap == null) {
            Toast.makeText(this, "Please select an image!", Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        if (etDoctorName.getText().toString().trim().isEmpty()) {
            etDoctorName.setError("Please enter doctor name");
            isValid = false;
        }
        if (etJobTitle.getText().toString().trim().isEmpty()) {
            etJobTitle.setError("Please enter job title");
            isValid = false;
        }
        if (etWhatsAppNumber.getText().toString().trim().isEmpty() ||
                etWhatsAppNumber.getText().toString().trim().length() != 10) {
            etWhatsAppNumber.setError("Please enter valid WhatsApp number");
            isValid = false;
        }
        if (etPhoneNumber.getText().toString().trim().isEmpty() ||
                etPhoneNumber.getText().toString().trim().length() != 10) {
            etPhoneNumber.setError("Please enter valid phone number");
            isValid = false;
        }
        if (etInstaId.getText().toString().trim().isEmpty()) {
            etInstaId.setError("Please enter Instagram ID");
            isValid = false;
        }
        String location = etLocation.getText().toString().trim();
        if (location.isEmpty()) {
            etLocation.setError("Please enter location");
            isValid = false;
        } else if (!location.startsWith("https")) {
            etLocation.setError("Please enter valid location");
            isValid = false;
        }
        double ratingValue = Double.parseDouble(etRating.getText().toString().trim());
        if (etRating.getText().toString().trim().isEmpty()) {
            etRating.setError("Please enter rating");
            isValid = false;
        } else if (ratingValue < 1 || ratingValue > 5) {
            etRating.setError("Please enter rating between 1 to 5");
            isValid = false;
        }
        if (etDescription.getText().toString().trim().isEmpty()) {
            etDescription.setError("Please enter description");
            isValid = false;
        }

        return isValid;
    }
}
