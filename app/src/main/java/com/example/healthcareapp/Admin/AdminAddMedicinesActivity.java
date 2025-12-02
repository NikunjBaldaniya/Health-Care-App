package com.example.healthcareapp.Admin;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
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
import java.util.Calendar;

public class AdminAddMedicinesActivity extends AppCompatActivity {

    private ImageView imgMedicine;
    private ImageButton btnSelectMedicineImage;
    private EditText etMedicineName, etPrice, etCategory, etDate, etRating, etDescription;
    private Button btnSave, btnBack;
    private Bitmap imageBitmap;
    private byte[] imageBytes;

    private ImageHelper imageHelper = new ImageHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_medicines);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imgMedicine = findViewById(R.id.imgMedicine);
        btnSelectMedicineImage = findViewById(R.id.btnSelectMedicineImage);
        etMedicineName = findViewById(R.id.etMedicineName);
        etPrice = findViewById(R.id.etPrice);
        etCategory = findViewById(R.id.etCategory);
        etDate = findViewById(R.id.etDate);
        etRating = findViewById(R.id.etRating);
        etDescription = findViewById(R.id.etDescription);
        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        btnSelectMedicineImage.setOnClickListener(v -> imageHelper.openGallery(this));

        // Calendar instance
        Calendar  calendar = Calendar.getInstance();
        etDate.setFocusable(false);
        etDate.setOnClickListener(v ->{
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                    etDate.setText(selectedDate);
                }
            }, year, month, day);
            dialog.show();
        });
        btnSave.setOnClickListener(v -> {
            if (checkValidation()) {
                String name = etMedicineName.getText().toString().trim();
                double price = Double.parseDouble(etPrice.getText().toString().trim());
                String category = etCategory.getText().toString().trim();
                String expiryDate = etDate.getText().toString().trim();
                double rating = Double.parseDouble(etRating.getText().toString().trim());
                String description = etDescription.getText().toString().trim();
                imageBytes = imageHelper.bitmapToBytes(imageBitmap);

                DatabaseHelper dbHelper = new DatabaseHelper(this);
                boolean result = dbHelper.insertMedicine(imageBytes, name, price, category, expiryDate, rating, description);

                if (result) {
                    Toast.makeText(this, "Medicine Added Successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Database Insertion Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == imageHelper.PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                imageBitmap = imageHelper.uriToBitmap(this, imageUri);
                imgMedicine.setImageBitmap(imageBitmap);
            } catch (IOException e) {
                Toast.makeText(this, "Image Upload Failed! " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean checkValidation() {
        boolean isValid = true;

        if (imageBitmap == null) {
            Toast.makeText(this, "Please Select Medicine Image", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        if (etMedicineName.getText().toString().trim().isEmpty()) {
            etMedicineName.setError("Medicine Name is required");
            isValid = false;
        }

        String priceStr = etPrice.getText().toString().trim();
        if (priceStr.isEmpty()) {
            etPrice.setError("Medicine Price is required");
            isValid = false;
        }

        if (etCategory.getText().toString().trim().isEmpty()) {
            etCategory.setError("Medicine Category is required");
            isValid = false;
        }

        if (etDate.getText().toString().trim().isEmpty()) {
            etDate.setError("Medicine Expiry Date required");
            isValid = false;
        }

        String ratingStr = etRating.getText().toString().trim();
        double rating = Double.parseDouble(ratingStr);
        if (ratingStr.isEmpty()) {
            etRating.setError("Medicine Rating is required");
            isValid = false;
        } else if (rating < 1 || rating > 5) {
            etRating.setError("Rating must be between 1 and 5");
            isValid = false;
        }

        if (etDescription.getText().toString().trim().isEmpty()) {
            etDescription.setError("Medicine Description is required");
            isValid = false;
        }

        return isValid;
    }
}