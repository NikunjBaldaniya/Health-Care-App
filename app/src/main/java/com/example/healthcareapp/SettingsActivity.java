package com.example.healthcareapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.healthcareapp.Database.DatabaseHelper;

public class SettingsActivity extends AppCompatActivity {

    private ConstraintLayout lytTopDoctors, lytTopMedicines, lytChatWithAI, lytLogOut, lytDeleteAccount, lytPolicy, lytTerms, lytCart, lytOrder, lytNotification;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        lytTopDoctors = findViewById(R.id.lytTopDoctors);
        lytTopMedicines = findViewById(R.id.lytTopMedicines);
        lytChatWithAI = findViewById(R.id.lytChatWithAI);
        lytLogOut = findViewById(R.id.lytLogOut);
        lytDeleteAccount = findViewById(R.id.lytDeleteAccount);
        lytPolicy = findViewById(R.id.lytPolicy);
        lytTerms = findViewById(R.id.lytTerms);
        lytCart = findViewById(R.id.lytCart);
        lytOrder = findViewById(R.id.lytOrder);
        btnBack = findViewById(R.id.btnBack);
        lytNotification = findViewById(R.id.lytNotification);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SharedPreferences prefs = this.getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        String email = prefs.getString("userEmail", null);
        int userId = dbHelper.getUserIdByEmail(email);

        lytTopDoctors.setOnClickListener(v ->startActivity(new Intent(this, TopDoctorsActivity.class)));
        lytTopMedicines.setOnClickListener(v ->startActivity(new Intent(this, TopMedicinesActivity.class)));
        lytChatWithAI.setOnClickListener(v ->startActivity(new Intent(this, AiChatActivity.class)));
        lytPolicy.setOnClickListener(v ->startActivity(new Intent(this, PrivacyPolicyActivity.class)));
        lytTerms.setOnClickListener(v ->startActivity(new Intent(this, TermsConditionsActivity.class)));
        lytCart.setOnClickListener(v ->startActivity(new Intent(this, CartActivity.class)));
        lytOrder.setOnClickListener(v ->startActivity(new Intent(this, MedicineOrderProductsActivity.class)));
        lytNotification.setOnClickListener(v ->startActivity(new Intent(this, NotificationActivity.class)));

        btnBack.setOnClickListener(v ->finish());
        lytDeleteAccount.setOnClickListener(v ->{
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Delete Account");
            alert.setMessage("Are you sure you want to delete your account? Once you delete Account You not recover it.");
            alert.setPositiveButton("Yes", (dialog, which) -> {
                dbHelper.deleteUser(userId);
                Toast.makeText(this, "Account Deleted", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, SignUpLoginScreenActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            });
            alert.setNegativeButton("No", (dialog, which) -> {
                dialog.dismiss();
            });
            alert.show();
        });

        lytLogOut.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Log Out");
            builder.setMessage("Are you sure you want to log out from your account?");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                SharedPreferences.Editor editor = prefs.edit();
                editor.clear();
                editor.apply();
                Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, SignUpLoginScreenActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            });
            builder.setNegativeButton("No", (dialog, which) -> {
                dialog.dismiss();
            });
            builder.show();
        });
    }
}