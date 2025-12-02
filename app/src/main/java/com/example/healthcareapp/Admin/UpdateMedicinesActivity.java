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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.healthcareapp.Database.DatabaseHelper;
import com.example.healthcareapp.ImageHelper;
import com.example.healthcareapp.R;

import java.io.IOException;
import java.util.Calendar;

public class UpdateMedicinesActivity extends AppCompatActivity {

    private ImageView imgMedicine;
    private ImageButton btnSelectMedicineImage;
    private EditText etDoctorID, etMedicineName, etPrice, etCategory, etDate, etRating, etDescription;
    private Button btnUpdate, btnDelete, btnBack;
    private Bitmap imageBitmap, medicineImageBitmap;
    private byte[] imageBytes, medicineImageBytes;

    private final ImageHelper imageHelper = new ImageHelper();
    private int medicineID;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_medicines);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // init views
        etDoctorID = findViewById(R.id.etDoctorID);
        imgMedicine = findViewById(R.id.imgMedicine);
        btnSelectMedicineImage = findViewById(R.id.btnSelectMedicineImage);
        etMedicineName = findViewById(R.id.etMedicineName);
        etPrice = findViewById(R.id.etPrice);
        etCategory = findViewById(R.id.etCategory);
        etDate = findViewById(R.id.etDate);
        etRating = findViewById(R.id.etRating);
        etDescription = findViewById(R.id.etDescription);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        btnBack = findViewById(R.id.btnBack);

        dbHelper = new DatabaseHelper(this);
        medicineID = getIntent().getIntExtra("ID", 0);

        btnBack.setOnClickListener(v -> finish());
        btnSelectMedicineImage.setOnClickListener(v -> imageHelper.openGallery(this));

        // get and set medicine id
        etDoctorID.setText(String.valueOf(medicineID));
        etDoctorID.setEnabled(false);

        // Calendar instance
        Calendar calendar = Calendar.getInstance();

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

        // load medicine image
        medicineImageBytes = dbHelper.getMedicineImage(medicineID);
        if (medicineImageBytes != null) {
            medicineImageBitmap = imageHelper.bytesToBitmap(medicineImageBytes);
            imgMedicine.setImageBitmap(medicineImageBitmap);
        }

        // load and set medicine name
        String medicineName = dbHelper.getMedicineName(medicineID);
        etMedicineName.setText(medicineName);

        // load and set medicine price
        double medicinePrice = dbHelper.getMedicinePrice(medicineID);
        etPrice.setText(String.valueOf(medicinePrice));

        // load and set category
        String medicineCategory = dbHelper.getMedicineCategory(medicineID);
        etCategory.setText(medicineCategory);

        // load and set expiry date
        String medicineExpiryDate = dbHelper.getMedicineExpiryDate(medicineID);
        etDate.setText(medicineExpiryDate);

        // load and set rating
        double medicineRating = dbHelper.getMedicineRating(medicineID);
        etRating.setText(String.valueOf(medicineRating));

        // load and set description
        String medicineDescription = dbHelper.getMedicineDescription(medicineID);
        etDescription.setText(medicineDescription);

        // Update button
        btnUpdate.setOnClickListener(v -> {
            if (checkValidation()) {
                String name = etMedicineName.getText().toString().trim();
                String category = etCategory.getText().toString().trim();
                String expiryDate = etDate.getText().toString().trim();
                String description = etDescription.getText().toString().trim();

                double price = safeParseDouble(etPrice.getText().toString().trim());
                double rating = safeParseDouble(etRating.getText().toString().trim());

                // If user did not pick new image, use existing
                if (imageBitmap != null) {
                    imageBytes = imageHelper.bitmapToBytes(imageBitmap);
                } else if (medicineImageBytes != null) {
                    imageBytes = medicineImageBytes;
                }

                boolean isUpdated = dbHelper.updateMedicine(
                        medicineID,
                        imageBytes,
                        name,
                        price,
                        category,
                        expiryDate,
                        rating,
                        description
                );

                if (isUpdated) {
                    Toast.makeText(this, "Medicine Updated Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Medicine Update Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Delete button
        btnDelete.setOnClickListener(v -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Delete Medicine");
            alert.setMessage("Are you sure you want to delete this medicine?");
            alert.setPositiveButton("Yes", (dialog, when) -> {
                boolean result = dbHelper.deleteMedicine(medicineID);
                if (result) {
                    Toast.makeText(this, "Medicine Deleted Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Medicine Deletion Failed", Toast.LENGTH_SHORT).show();
                }
                finish();
            });
            alert.setNegativeButton("No", (dialog, when) -> dialog.cancel());
            alert.show();
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

        if (imageBitmap == null && medicineImageBytes == null) {
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
        if (ratingStr.isEmpty()) {
            etRating.setError("Medicine Rating is required");
            isValid = false;
        } else {
            double ratingVal = safeParseDouble(ratingStr);
            if (ratingVal < 1 || ratingVal > 5) {
                etRating.setError("Rating must be between 1 and 5");
                isValid = false;
            }
        }

        if (etDescription.getText().toString().trim().isEmpty()) {
            etDescription.setError("Medicine Description is required");
            isValid = false;
        }

        return isValid;
    }

    private double safeParseDouble(String value) {
        try {
            if (value == null || value.trim().isEmpty()) return 0.0;
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}
