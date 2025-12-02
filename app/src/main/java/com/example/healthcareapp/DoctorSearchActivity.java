package com.example.healthcareapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthcareapp.Adapter.DoctorSearchHistoryAdapter;
import com.example.healthcareapp.Database.DatabaseHelper;
import com.example.healthcareapp.Model.HistoryModel;

import java.util.ArrayList;
import java.util.List;

public class DoctorSearchActivity extends AppCompatActivity {

    private AutoCompleteTextView etSearch;
    private DatabaseHelper dbHelper;
    private int userId;
    private List<HistoryModel> historyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_doctor_search);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etSearch = findViewById(R.id.etSearch);
        RecyclerView rvDoctorSearchHistory = findViewById(R.id.rvDoctorSearchHistory);
        Button btnSearch = findViewById(R.id.btnSearch);
        Button btnBack = findViewById(R.id.btnBack);

        SharedPreferences prefs = this.getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        String email = prefs.getString("userEmail", null);

        dbHelper = new DatabaseHelper(this);
        userId = dbHelper.getUserIdByEmail(email);

        List<String> doctorNameList = dbHelper.getAllDoctorNames();
        List<String> doctorSpecification = dbHelper.getAllDoctorSpecification();

        List<String> fullData = new ArrayList<>();
        fullData.addAll(doctorNameList);
        fullData.addAll(doctorSpecification);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, fullData);
        etSearch.setAdapter(adapter);

        btnBack.setOnClickListener(v -> finish());

        historyList = new ArrayList<>();
        rvDoctorSearchHistory.setLayoutManager(new LinearLayoutManager(this));
        DoctorSearchHistoryAdapter historyAdapter = new DoctorSearchHistoryAdapter(this, historyList);
        rvDoctorSearchHistory.setAdapter(historyAdapter);

        displayData();

        btnSearch.setOnClickListener(v ->{
            String search = etSearch.getText().toString().trim();
            if (!search.isEmpty()) {
                boolean isInHistory = dbHelper.isDoctorHistoryIn(search);
                if(!isInHistory){
                    dbHelper.insertDoctorSearchHistory(search, userId);
                    Intent intent = new Intent(this, DoctorSearchResultActivity.class);
                    intent.putExtra("SearchItem", search);
                    startActivity(intent);
                    displayData();
                }
            }
        });
    }

    private void displayData(){
        historyList.clear();
        Cursor cursor = dbHelper.getAllDoctorSearchHistory(userId);
        if (cursor.getCount() == 0){
            Toast.makeText(this, "No Data!", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()){
                historyList.add(new HistoryModel(
                        cursor.getInt(cursor.getColumnIndexOrThrow("ID")),
                        cursor.getString(cursor.getColumnIndexOrThrow("DoctorNameSpecification"))
                ));
            }
        }

    }
}