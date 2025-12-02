package com.example.healthcareapp.Admin.Fragments;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthcareapp.Admin.Adapters.AdminMedicineAdapter;
import com.example.healthcareapp.Admin.AdminAddMedicinesActivity;
import com.example.healthcareapp.Admin.Models.AdminMedicineModel;
import com.example.healthcareapp.Database.DatabaseHelper;
import com.example.healthcareapp.ImageHelper;
import com.example.healthcareapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class AdminMedicinsFragment extends Fragment {

    RecyclerView rvMedicines;
    FloatingActionButton fab;
    List<AdminMedicineModel> medicineList;
    ImageHelper imageHelper;
    DatabaseHelper dbHelper;
    AdminMedicineAdapter adapter;
    private ImageButton btnSearch;
    private AutoCompleteTextView actSearch;
    private List<String> medicineNames;
    private ImageView imgMedicineNotFound;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_medicins, container, false);
        rvMedicines = view.findViewById(R.id.rvMedicines);
        fab = view.findViewById(R.id.fab);
        btnSearch = view.findViewById(R.id.btnSearch);
        actSearch = view.findViewById(R.id.actSearch);
        imgMedicineNotFound = view.findViewById(R.id.imgMedicineNotFound);
        dbHelper = new DatabaseHelper(getContext());
        imageHelper = new ImageHelper();

        fab.setOnClickListener(v -> startActivity(new Intent(getContext(), AdminAddMedicinesActivity.class)));

        // Main medicine list
        medicineList = new ArrayList<>();
        adapter = new AdminMedicineAdapter(getContext(), medicineList);
        rvMedicines.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMedicines.setAdapter(adapter);

        // AutoCompleteTextView setup
        medicineNames = dbHelper.getAllMedicineNames();
        ArrayAdapter<String> searchAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line, medicineNames);
        actSearch.setAdapter(searchAdapter);

        btnSearch.setOnClickListener(v -> {
            String search = actSearch.getText().toString().trim();
            if(!search.isEmpty()){
                displaySearchedMedicine(search);
            } else {
                displayAllMedicines();
            }
        });

        // Initially display all medicines
        displayAllMedicines();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        String currentSearch = actSearch.getText().toString().trim();
        if(currentSearch.isEmpty()){
            displayAllMedicines();
        } else {
            displaySearchedMedicine(currentSearch);
        }
    }


    private void displayAllMedicines() {
        medicineList.clear();
        Cursor cursor = dbHelper.getAllMedicines();
        if(cursor.getCount() == 0){
            actSearch.setEnabled(false);
            imgMedicineNotFound.setVisibility(View.VISIBLE);
        } else {
            actSearch.setEnabled(true);
            imgMedicineNotFound.setVisibility(View.GONE);
            while(cursor.moveToNext()){
                byte[] imageByte = cursor.getBlob(cursor.getColumnIndexOrThrow("Image"));
                Bitmap imageBit = null;
                if(imageByte != null){
                    imageBit = imageHelper.bytesToBitmap(imageByte);
                }
                medicineList.add(new AdminMedicineModel(
                        cursor.getInt(cursor.getColumnIndexOrThrow("ID")),
                        imageBit,
                        cursor.getString(cursor.getColumnIndexOrThrow("Name")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("Price"))
                ));
            }
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }

    private void displaySearchedMedicine(String search){
        medicineList.clear();
        Cursor cursor = dbHelper.getSearchedMedicine(search);
        if(cursor.getCount() == 0){
            Toast.makeText(getContext(), "Medicine Not Found", Toast.LENGTH_SHORT).show();
        } else {
            while(cursor.moveToNext()){
                byte[] imageByte = cursor.getBlob(cursor.getColumnIndexOrThrow("Image"));
                Bitmap imageBit = null;
                if(imageByte != null){
                    imageBit = imageHelper.bytesToBitmap(imageByte);
                }
                medicineList.add(new AdminMedicineModel(
                        cursor.getInt(cursor.getColumnIndexOrThrow("ID")),
                        imageBit,
                        cursor.getString(cursor.getColumnIndexOrThrow("Name")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("Price"))
                ));
            }
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }
}
