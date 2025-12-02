package com.example.healthcareapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.healthcareapp.Model.MailSender;

public class EmailVerificationActivity extends AppCompatActivity {

    EditText etOTP1, etOTP2, etOTP3, etOTP4;
    Button btnVerify, btnBack;
    TextView txtResend, txtEmail;
    private String getOTP;
    private String getEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_email_verification);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etOTP1 = findViewById(R.id.etOTP1);
        etOTP2 = findViewById(R.id.etOTP2);
        etOTP3 = findViewById(R.id.etOTP3);
        etOTP4 = findViewById(R.id.etOTP4);
        btnVerify = findViewById(R.id.btnVerify);
        txtResend = findViewById(R.id.txtResend);
        txtEmail = findViewById(R.id.txtEmail);
        btnBack = findViewById(R.id.btnBack);

        OTPVerification(etOTP1, etOTP2);
        OTPVerification(etOTP2, etOTP3);
        OTPVerification(etOTP3, etOTP4);

        btnBack.setOnClickListener(v ->{
            startActivity(new Intent(EmailVerificationActivity.this, SignInActivity.class));
            finish();
        });

        countDown();

        getOTP = getIntent().getStringExtra("OTP");
        getEmail = getIntent().getStringExtra("Email");

        txtEmail.setText(getEmail);

        txtResend.setOnClickListener(v ->{
            String otp = MailSender.generateOTP().trim();
            MailSender.sendMail(this, getEmail, otp);
            getOTP = otp;
            countDown();
        });

        btnVerify.setOnClickListener( v -> {
            String otp1 = etOTP1.getText().toString().trim();
            String otp2 = etOTP2.getText().toString().trim();
            String otp3 = etOTP3.getText().toString().trim();
            String otp4 = etOTP4.getText().toString().trim();

            String OTP = (otp1 + otp2 + otp3 + otp4).trim();

            if (OTP.equals(getOTP)){
                startActivity(new Intent(this, ResetPasswordActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Please Enter Correct OTP.", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void OTPVerification(EditText et1, EditText et2) {
        et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (!et1.getText().toString().trim().isEmpty() && et1.getText().toString().trim().length() == 1) {
                    et2.requestFocus();
                }
            }
        });

        et2.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                if (et2.getText().toString().isEmpty()) {
                    et1.requestFocus();
                    et1.setSelection(et1.getText().length());
                    return true;
                }
            }
            return false;
        });
    }
    private void countDown(){
        new CountDownTimer(59000, 1000){
            @Override
            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;

                String timeFormatted = String.format("%02d:%02d", minutes, seconds);
                txtResend.setText(timeFormatted);
                txtResend.setClickable(false);
            }
            @Override
            public void onFinish() {
                txtResend.setText(R.string.click_to_resend);
                txtResend.setClickable(true);
            }

        }.start();
    }
}