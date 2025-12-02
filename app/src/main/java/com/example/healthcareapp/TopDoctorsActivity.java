package com.example.healthcareapp;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthcareapp.Adapter.DoctorListAdapter;
import com.example.healthcareapp.Database.DatabaseHelper;
import com.example.healthcareapp.Model.DoctorListModel;


import java.util.List;

public class TopDoctorsActivity extends AppCompatActivity {

    private Button btnBack;
    private RecyclerView rvTopDoctors;
    private List<DoctorListModel> listDoctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_doctors);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnBack = findViewById(R.id.btnBack);
        rvTopDoctors = findViewById(R.id.rvTopDoctors);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        ImageHelper imageHelper = new ImageHelper();

        listDoctor = dbHelper.getTopFiveDoctors();
        DoctorListAdapter doctorAdapter = new DoctorListAdapter(this, listDoctor);
        rvTopDoctors.setLayoutManager(new LinearLayoutManager(this));
        rvTopDoctors.setAdapter(doctorAdapter);

        btnBack.setOnClickListener(v -> finish());
    }
}