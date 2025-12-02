package com.example.healthcareapp.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthcareapp.AiChatActivity;
import com.example.healthcareapp.Database.DatabaseHelper;
import com.example.healthcareapp.EditProfileActivity;
import com.example.healthcareapp.ImageHelper;
import com.example.healthcareapp.NotificationActivity;
import com.example.healthcareapp.R;
import com.example.healthcareapp.SettingsActivity;
import com.example.healthcareapp.SignUpLoginScreenActivity;
import com.example.healthcareapp.TopDoctorsActivity;
import com.example.healthcareapp.TopMedicinesActivity;

public class ProfileFragment extends Fragment {

    TextView txtUserName, txtUserEmail, txtHeight, txtAge, txtWeight;
    double height = 0, weight = 0;
    int age = 0;
    String name, email;
    Bitmap userImage;
    Button btnEditProfile;
    ImageView imgUser;
    private ConstraintLayout lytTopDoctors, lytTopMedicines, lytChatWithAI, lytLogOut;
    private ImageButton btnSetting, btnNotification;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        txtUserName = view.findViewById(R.id.txtUserName);
        txtUserEmail = view.findViewById(R.id.txtUserEmail);
        txtHeight = view.findViewById(R.id.txtHeight);
        txtAge = view.findViewById(R.id.txtAge);
        txtWeight = view.findViewById(R.id.txtWeight);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        imgUser = view.findViewById(R.id.imgUser);
        lytTopDoctors = view.findViewById(R.id.lytTopDoctors);
        lytTopMedicines = view.findViewById(R.id.lytTopMedicines);
        lytChatWithAI = view.findViewById(R.id.lytChatWithAI);
        lytLogOut = view.findViewById(R.id.lytLogOut);
        btnSetting = view.findViewById(R.id.btnSetting);
        btnNotification = view.findViewById(R.id.btnNotification);

        // SharedPreferences access
        SharedPreferences prefs = getActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        email = prefs.getString("userEmail", null);

        // get data from database
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        ImageHelper imageHelper = new ImageHelper();

        //Get User ID
        int userId = dbHelper.getUserIdByEmail(email);

        // Get and Set User Name
        name = dbHelper.getNameByUserId(userId);
        txtUserName.setText(name);

        // Get and Set User Email
        email = dbHelper.getEmailByUserId(userId);
        txtUserEmail.setText(email);

        // Get and Set User Age
        age = dbHelper.getAgeByUserId(userId);
        txtAge.setText(String.valueOf(age));

        // Get and Set User Height
        height = dbHelper.getHeightByUserId(userId);
        txtHeight.setText(String.valueOf(height));

        // Get and Set User Weight
        weight = dbHelper.getWeightByUserId(userId);
        txtWeight.setText(String.valueOf(weight));

        // Get and Set User Image
        userImage = imageHelper.bytesToBitmap(dbHelper.getImageByUserId(userId));
        if (userImage != null){
            imgUser.setImageBitmap(userImage);
        }

        lytTopDoctors.setOnClickListener(v ->startActivity(new Intent(getActivity(), TopDoctorsActivity.class)));
        lytTopMedicines.setOnClickListener(v ->startActivity(new Intent(getActivity(), TopMedicinesActivity.class)));
        lytChatWithAI.setOnClickListener(v ->startActivity(new Intent(getActivity(), AiChatActivity.class)));
        btnSetting.setOnClickListener(v ->startActivity(new Intent(getActivity(), SettingsActivity.class)));
        btnEditProfile.setOnClickListener(v ->startActivity(new Intent(getActivity(), EditProfileActivity.class)));
        btnNotification.setOnClickListener(v ->startActivity(new Intent(getActivity(), NotificationActivity.class)));

        lytLogOut.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Log Out");
            builder.setMessage("Are you sure you want to log out from your account?");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                SharedPreferences.Editor editor = prefs.edit();
                editor.clear();
                editor.apply();
                Toast.makeText(getContext(), "Logged Out", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getContext(), SignUpLoginScreenActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish();
            });
            builder.setNegativeButton("No", (dialog, which) -> {
                dialog.dismiss();
            });
            builder.show();
        });


        return view;
    }
}