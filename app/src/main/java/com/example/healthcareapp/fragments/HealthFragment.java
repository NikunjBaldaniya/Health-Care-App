package com.example.healthcareapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.healthcareapp.Database.DatabaseHelper;
import com.example.healthcareapp.EditProfileActivity;
import com.example.healthcareapp.HeartRateActivity;
import com.example.healthcareapp.R;

public class HealthFragment extends Fragment {
    double bmr;
    CardView heart_rate_card;
    double height = 0, weight = 0;
    int age = 0;
    String bloodGroup;
    String gender="Male";
    String activityLevel = "Active";
    TextView txtWeight, txtHeight, txtBMI, txtBMR, txtTDEE, txtHR, txtAge, txtBloodGroup, txtWaterDrink;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_health, container, false);

        // Find View By Ids
        txtWeight = view.findViewById(R.id.txtWeight);
        txtHeight = view.findViewById(R.id.txtHeight);
        txtBMI = view.findViewById(R.id.txtBMI);
        txtBMR = view.findViewById(R.id.txtBMR);
        txtTDEE = view.findViewById(R.id.txtTDEE);
        txtHR = view.findViewById(R.id.txtHR);
        txtWaterDrink = view.findViewById(R.id.txtWaterDrink);
        txtAge = view.findViewById(R.id.txtAge);
        txtBloodGroup = view.findViewById(R.id.txtBloodGroup);
        heart_rate_card = view.findViewById(R.id.heart_rate_card);

        // SharedPreferences access
        SharedPreferences prefs = getActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        String email = prefs.getString("userEmail", null);

        // Get Data From Database
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());

        //Get User ID
        int userId = dbHelper.getUserIdByEmail(email);

        height = dbHelper.getHeightByUserId(userId);
        weight = dbHelper.getWeightByUserId(userId);
        age = dbHelper.getAgeByUserId(userId);
        bloodGroup = dbHelper.getBloodGroupByUserId(userId);
        gender = dbHelper.getGenderByUserId(userId);
        activityLevel = dbHelper.getActivityLevelByUserId(userId);


        // Set to TextViews
        txtWeight.setText(String.valueOf(weight));
        txtHeight.setText(String.valueOf(height));
        txtAge.setText(String.valueOf(age));
        txtBloodGroup.setText(bloodGroup);

        // Calculations
        txtBMI.setText(getBMI(weight, height));
        bmr = getBMR(gender, age, height, weight);
        txtBMR.setText(String.format("%.2f", bmr));

        double tdee = getTDEE(bmr, activityLevel);
        txtTDEE.setText(String.format("%.2f", tdee));

        txtHR.setText(String.valueOf(getHR(age)));

        double water = getWaterIntake(weight);
        txtWaterDrink.setText(String.format("%.2f", water));

        heart_rate_card.setOnClickListener(v ->{
            Intent intent = new Intent(getContext(), HeartRateActivity.class);
            intent.putExtra("USER_AGE", age);
            startActivity(intent);
        });

        return view;
    }

    // Body Mass Index
    String getBMI(double weightKg, double heightCm) {
        double heightM = heightCm / 100.0f;
        double bmi = weightKg / (heightM * heightM);

        if (bmi < 18.5f) {
            return "Underweight";
        } else if (bmi < 25f) {
            return "Normal";
        } else if (bmi < 30f) {
            return "Overweight";
        } else {
            return "Obese";
        }
    }

    // Basal Metabolic Rate
    double getBMR(String gender, int age, double heightCm, double weightKg) {
        if (gender.equalsIgnoreCase("male")) {
            return 10 * weightKg + 6.25 * heightCm - 5 * age + 5;
        } else if (gender.equalsIgnoreCase("female")) {
            return 10 * weightKg + 6.25 * heightCm - 5 * age - 161;
        } else {
            return 0;
        }
    }

    double getTDEE(double bmr, String activityLevel) {
        switch (activityLevel.toLowerCase()) {
            case "sedentary":
                return bmr * 1.2;
            case "moderate":
                return bmr * 1.55;
            case "active":
                return bmr * 1.9;
            default:
                return bmr;
        }
    }

    // Daily Water Intake in Litres
    double getWaterIntake(double weightKg) {
        return weightKg * 0.033f;
    }

    // Max Heart Rate
    public int getHR(int age) {
        return 220 - age;
    }
}
