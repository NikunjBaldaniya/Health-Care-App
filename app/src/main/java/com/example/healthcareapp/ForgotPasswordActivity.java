package com.example.healthcareapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.healthcareapp.Database.DatabaseHelper;
import com.example.healthcareapp.Model.MailSender;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText etEmail;
    Button btnSendVerification, btnBack;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etEmail = findViewById(R.id.etName);
        btnSendVerification = findViewById(R.id.btnSendVerification);
        btnBack = findViewById(R.id.btnBack);
        dbHelper = new DatabaseHelper(this);
        SharedPreferences prefs = this.getSharedPreferences("Forgot", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();



        btnSendVerification.setOnClickListener(v ->{
            String email = etEmail.getText().toString().trim();
            String otp = MailSender.generateOTP().trim();

            if (email.isEmpty()){
                etEmail.setError("Please Enter Email");
            } else {
                boolean isIn = dbHelper.isEmailInDB(email);
                if (isIn){
                    MailSender.sendMail(this, email, otp);
                    editor.putString("Email", email);
                    editor.apply();
                    Intent intent = new Intent(this, EmailVerificationActivity.class);
                    intent.putExtra("OTP", otp);
                    intent.putExtra("Email", email);
                    startActivity(intent);
                    finish();
                } else {
                    etEmail.setError("Please enter Valid Email");
                    Toast.makeText(this, "Email Not Found in Database !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnBack.setOnClickListener( v ->{
            startActivity(new Intent(ForgotPasswordActivity.this, SignInActivity.class));
            finish();
        });
    }
}