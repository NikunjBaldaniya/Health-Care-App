package com.example.healthcareapp;

import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthcareapp.Adapter.MedicineAdapter;
import com.example.healthcareapp.Adapter.PopularProductAdapter;
import com.example.healthcareapp.Database.DatabaseHelper;
import com.example.healthcareapp.Model.PopularProductListModel;

import java.util.List;

public class TopMedicinesActivity extends AppCompatActivity {

    private Button btnBack;
    private RecyclerView rvTopMedicines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_top_medicines);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnBack = findViewById(R.id.btnBack);
        rvTopMedicines = findViewById(R.id.rvTopMedicines);
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        List<PopularProductListModel> popularProductListModels = dbHelper.getTopFiveMedicines();
        MedicineAdapter medicineAdapter = new MedicineAdapter(this, popularProductListModels);
        rvTopMedicines.setLayoutManager(new LinearLayoutManager(this));
        rvTopMedicines.setAdapter(medicineAdapter);

        btnBack.setOnClickListener(v -> finish());
    }
}