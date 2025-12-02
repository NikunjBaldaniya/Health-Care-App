package com.example.healthcareapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.airbnb.lottie.LottieAnimationView;

public class PaymentSuccessActivity extends AppCompatActivity {

    LottieAnimationView successAnimation;
    TextView txtAmount;
    Button btnDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_success);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        successAnimation = findViewById(R.id.successAnimation);
        txtAmount = findViewById(R.id.txtAmount);
        btnDone = findViewById(R.id.btnDone);

        successAnimation.setAnimation(R.raw.payment_success);
        successAnimation.playAnimation();

        Double total = getIntent().getDoubleExtra("Price", 1);
        txtAmount.setText(String.format("%.2f", total));

        btnDone.setOnClickListener(v ->{
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        });
    }
}