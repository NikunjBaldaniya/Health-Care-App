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

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthcareapp.Admin.Adapters.AdminDoctorAdapter;
import com.example.healthcareapp.Admin.AdminAddDoctorActivity;
import com.example.healthcareapp.Admin.Models.AdminDoctorModel;
import com.example.healthcareapp.Database.DatabaseHelper;
import com.example.healthcareapp.ImageHelper;
import com.example.healthcareapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class AdminDoctorsFragment extends Fragment {

    RecyclerView rvDoctors;
    FloatingActionButton fab;
    List<AdminDoctorModel> doctorList;
    ImageHelper imageHelper;
    DatabaseHelper dbHelper;
    AdminDoctorAdapter adapter;
    private ImageButton btnSearch;
    private AutoCompleteTextView actSearch;
    private List<String> doctorNameSpecification;
    private ImageView imgDoctorNotFound;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_doctors, container, false);

        fab = view.findViewById(R.id.fab);
        rvDoctors = view.findViewById(R.id.rvDoctors);
        btnSearch = view.findViewById(R.id.btnSearch);
        actSearch = view.findViewById(R.id.actSearch);
        imgDoctorNotFound = view.findViewById(R.id.imgDoctorNotFound);
        dbHelper = new DatabaseHelper(getContext());
        imageHelper = new ImageHelper();

        fab.setOnClickListener(v -> startActivity(new Intent(getContext(), AdminAddDoctorActivity.class)));

        // Main doctor list
        doctorList = new ArrayList<>();
        adapter = new AdminDoctorAdapter(getContext(), doctorList);
        rvDoctors.setLayoutManager(new LinearLayoutManager(getContext()));
        rvDoctors.setAdapter(adapter);

        // AutoCompleteTextView setup
        doctorNameSpecification = new ArrayList<>();
        doctorNameSpecification.addAll(dbHelper.getAllDoctorNames());
        doctorNameSpecification.addAll(dbHelper.getAllDoctorSpecification());

        ArrayAdapter<String> searchAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line, doctorNameSpecification);
        actSearch.setAdapter(searchAdapter);

        btnSearch.setOnClickListener(v -> {
            String search = actSearch.getText().toString().trim();
            if(!search.isEmpty()){
                displaySearchedDoctor(search);
            } else {
                displayAllDoctors();
            }
        });

        // Initially display all doctors
        displayAllDoctors();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        String search = actSearch.getText().toString().trim();
        if(!search.isEmpty()){
            displaySearchedDoctor(search);
        } else {
            displayAllDoctors();
        }
    }

    // Display all doctors
    private void displayAllDoctors(){
        doctorList.clear();
        Cursor cursor = dbHelper.getAllDoctors();
        if(cursor.getCount() == 0){
            actSearch.setEnabled(false);
            imgDoctorNotFound.setVisibility(View.VISIBLE);
        } else {
            actSearch.setEnabled(true);
            imgDoctorNotFound.setVisibility(View.GONE);
            while(cursor.moveToNext()){
                byte[] imageByte = cursor.getBlob(cursor.getColumnIndexOrThrow("Image"));
                Bitmap imageBit = null;
                if(imageByte != null){
                    imageBit = imageHelper.bytesToBitmap(imageByte);
                }
                doctorList.add(new AdminDoctorModel(
                        cursor.getInt(cursor.getColumnIndexOrThrow("ID")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Name")),
                        imageBit,
                        cursor.getString(cursor.getColumnIndexOrThrow("JobTitle"))
                ));
            }
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }

    // Display searched doctors
    private void displaySearchedDoctor(String search){
        doctorList.clear();
        Cursor cursor = dbHelper.getSearchedDoctor(search);
        if(cursor.getCount() == 0){
            Toast.makeText(getContext(), "Doctor Not Found", Toast.LENGTH_SHORT).show();
        } else {
            while(cursor.moveToNext()){
                byte[] imageByte = cursor.getBlob(cursor.getColumnIndexOrThrow("Image"));
                Bitmap imageBit = null;
                if(imageByte != null){
                    imageBit = imageHelper.bytesToBitmap(imageByte);
                }
                doctorList.add(new AdminDoctorModel(
                        cursor.getInt(cursor.getColumnIndexOrThrow("ID")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Name")),
                        imageBit,
                        cursor.getString(cursor.getColumnIndexOrThrow("JobTitle"))
                ));
            }
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }
}
