package com.example.healthcareapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HeartRateActivity extends AppCompatActivity {

    TextView txtWarmUp, txtFatBurn, txtCardio, txtHardTraining, txtMaximumEffort, txtHR;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_heart_rate);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtWarmUp = findViewById(R.id.txtWarmUp);
        txtFatBurn = findViewById(R.id.txtFatBurn);
        txtCardio = findViewById(R.id.txtCardio);
        txtHardTraining = findViewById(R.id.txtHardTraining);
        txtMaximumEffort = findViewById(R.id.txtMaximumEffort);
        btnBack = findViewById(R.id.btnBack);
        txtHR = findViewById(R.id.txtHR);

        int age = getIntent().getIntExtra("USER_AGE", 0);

        int hr = 220 - age;

        // Calculation of Heart Rates
        txtHR.setText(String.valueOf(hr));
        txtWarmUp.setText(String.valueOf((int)(hr * 0.50)));
        txtFatBurn.setText(String.valueOf((int)(hr * 0.60)));
        txtCardio.setText(String.valueOf((int)(hr * 0.70)));
        txtHardTraining.setText(String.valueOf((int)(hr * 0.80)));
        txtMaximumEffort.setText(String.valueOf((int)(hr * 0.90)));

        btnBack.setOnClickListener(v ->{
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        });
    }
}