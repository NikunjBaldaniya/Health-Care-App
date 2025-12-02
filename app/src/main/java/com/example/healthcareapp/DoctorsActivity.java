package com.example.healthcareapp;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthcareapp.Adapter.DoctorListAdapter;
import com.example.healthcareapp.Database.DatabaseHelper;
import com.example.healthcareapp.Model.DoctorListModel;

import java.util.ArrayList;
import java.util.List;

public class DoctorsActivity extends AppCompatActivity {

    private Button btnBack;
    private ImageButton btnSearch;
    private AutoCompleteTextView actSearch;
    private RecyclerView rvResult;
    private DatabaseHelper dbHelper;
    private ImageHelper imageHelper;
    private List<String> doctorNameSpecification;
    private List<DoctorListModel> doctorList;
    private DoctorListAdapter doctorAdapter;
    private String search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_doctors);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnBack = findViewById(R.id.btnBack);
        actSearch = findViewById(R.id.actSearch);
        rvResult = findViewById(R.id.rvResult);
        btnSearch = findViewById(R.id.btnSearch);
        dbHelper = new DatabaseHelper(this);
        imageHelper = new ImageHelper();
        dbHelper = new DatabaseHelper(this);

        List<String> doctorName = dbHelper.getAllDoctorNames();
        List<String> doctorSpecification = dbHelper.getAllDoctorSpecification();
        doctorNameSpecification = new ArrayList<>();

        doctorNameSpecification.addAll(doctorName);
        doctorNameSpecification.addAll(doctorSpecification);

        ArrayAdapter<String> searchAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, doctorNameSpecification);
        actSearch.setAdapter(searchAdapter);

        doctorList = new ArrayList<>();
        rvResult.setLayoutManager(new LinearLayoutManager(this));
        doctorAdapter = new DoctorListAdapter(this, doctorList);
        rvResult.setAdapter(doctorAdapter);

        btnSearch.setOnClickListener(v ->{
            search = actSearch.getText().toString().trim();
            if (!search.isEmpty()){
                displaySearchedData();
            } else {
                displayAllData();
            }
        });

        displayAllData();

        btnBack.setOnClickListener(v ->finish());
    }


    void displayAllData(){
        imageHelper = new ImageHelper();
        doctorList.clear();
        Cursor cursor = dbHelper.getAllDoctors();
        if (cursor.getCount() == 0){
            Toast.makeText(this, "Medicine Not Found", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()){
                byte[] imageByte = cursor.getBlob(cursor.getColumnIndexOrThrow("Image"));
                Bitmap imageBit = null;
                if (imageByte != null){
                    imageBit = imageHelper.bytesToBitmap(imageByte);
                }
                doctorList.add(new DoctorListModel(cursor.getInt(cursor.getColumnIndexOrThrow("ID")),
                        imageBit,
                        cursor.getString(cursor.getColumnIndexOrThrow("Name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("JobTitle")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("Rating"))
                ));
            }
        }
        cursor.close();
        doctorAdapter.notifyDataSetChanged();
    }

    void displaySearchedData(){
        imageHelper = new ImageHelper();
        doctorList.clear();
        Cursor cursor = dbHelper.getSearchedDoctor(search);
        if (cursor.getCount() == 0){
            Toast.makeText(this, "Medicine Not Found", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()){
                byte[] imageByte = cursor.getBlob(cursor.getColumnIndexOrThrow("Image"));
                Bitmap imageBit = null;
                if (imageByte != null){
                    imageBit = imageHelper.bytesToBitmap(imageByte);
                }
                doctorList.add(new DoctorListModel(cursor.getInt(cursor.getColumnIndexOrThrow("ID")),
                        imageBit,
                        cursor.getString(cursor.getColumnIndexOrThrow("Name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("JobTitle")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("Rating"))
                ));
            }
        }
        cursor.close();
        doctorAdapter.notifyDataSetChanged();
    }
}