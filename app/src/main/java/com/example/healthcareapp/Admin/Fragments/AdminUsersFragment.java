package com.example.healthcareapp.Admin.Fragments;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.healthcareapp.Admin.Adapters.AdminUserAdapter;
import com.example.healthcareapp.Admin.AdminCreateNewUsersActivity;
import com.example.healthcareapp.Admin.Models.AdminUserModel;
import com.example.healthcareapp.Database.DatabaseHelper;
import com.example.healthcareapp.ImageHelper;
import com.example.healthcareapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class AdminUsersFragment extends Fragment {

    private RecyclerView rvUsers;
    private FloatingActionButton fab;
    private DatabaseHelper dbHelper;
    private List<AdminUserModel> list;
    private ImageHelper imageHelper;
    private AdminUserAdapter adapter;
    private AutoCompleteTextView actSearch;
    private ImageButton btnSearch;
    private String search;
    private List<String> userList;
    private ImageView imgUserNotFound;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_users, container, false);

        rvUsers = view.findViewById(R.id.rvUsers);
        fab = view.findViewById(R.id.fab);
        btnSearch = view.findViewById(R.id.btnSearch);
        actSearch = view.findViewById(R.id.actSearch);
        imgUserNotFound = view.findViewById(R.id.imgUserNotFound);
        dbHelper = new DatabaseHelper(getContext());

        userList = dbHelper.getAllUserNames();
        ArrayAdapter<String> searchAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, userList);
        actSearch.setAdapter(searchAdapter);


        list = new ArrayList<>();
        adapter = new AdminUserAdapter(getContext(), list);
        rvUsers.setLayoutManager(new LinearLayoutManager(getContext()));
        rvUsers.setAdapter(adapter);

        btnSearch.setOnClickListener(v ->{
            search = actSearch.getText().toString().trim();
            displayUserSearchData();
        });
        displayUserData();

        fab.setOnClickListener(v -> startActivity(new Intent(getContext(), AdminCreateNewUsersActivity.class)));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        String currentSearch = actSearch.getText().toString().trim();
        if(currentSearch.isEmpty()){
            displayUserData();
        } else {
            search = currentSearch;
            displayUserSearchData();
        }
    }

    void displayUserData(){
        imageHelper = new ImageHelper();
        list.clear();
        Cursor cursor = dbHelper.getAllUsers();
        if (cursor.getCount() == 0){
            actSearch.setEnabled(false);
            imgUserNotFound.setVisibility(View.VISIBLE);
        } else {
            actSearch.setEnabled(true);
            imgUserNotFound.setVisibility(View.GONE);
            while (cursor.moveToNext()){
                byte[] imgBytes = cursor.getBlob(2); // Image BLOB
                Bitmap bmp = null;
                if (imgBytes != null) {
                    bmp = imageHelper.bytesToBitmap(imgBytes);
                }
                list.add(new AdminUserModel(
                        cursor.getInt(0),   // ID
                        cursor.getString(1),// Name
                        bmp, // Image
                        cursor.getString(3) // Email
                ));
            }
        }
        adapter.notifyDataSetChanged();
    }


    void displayUserSearchData(){
        imageHelper = new ImageHelper();
        list.clear();
        Cursor cursor = dbHelper.getSearchedUser(search);
        if (cursor.getCount() == 0){
            Toast.makeText(getContext(), "No Data!", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()){
                byte[] imgBytes = cursor.getBlob(2); // Image BLOB
                Bitmap bmp = null;
                if (imgBytes != null) {
                    bmp = imageHelper.bytesToBitmap(imgBytes);
                }
                list.add(new AdminUserModel(
                        cursor.getInt(0),   // ID
                        cursor.getString(1),// Name
                        bmp, // Image
                        cursor.getString(3) // Email
                ));
            }
        }
        adapter.notifyDataSetChanged();
    }
}