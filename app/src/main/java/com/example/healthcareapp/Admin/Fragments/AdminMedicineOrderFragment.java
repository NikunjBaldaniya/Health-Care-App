package com.example.healthcareapp.Admin.Fragments;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.healthcareapp.Admin.Adapters.AdminMedicineOrderAdapter;
import com.example.healthcareapp.Admin.Models.AdminMedicineOrderModel;
import com.example.healthcareapp.Database.DatabaseHelper;
import com.example.healthcareapp.R;

import java.util.ArrayList;
import java.util.List;

public class AdminMedicineOrderFragment extends Fragment {


    private DatabaseHelper dbHelper;
    private RecyclerView rvMedicinesOrder;
    private ArrayList<AdminMedicineOrderModel> orderList;
    private AdminMedicineOrderAdapter orderAdapter;
    private ImageView imgOrderNotFound;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_admin_medicine_order, container, false);
        rvMedicinesOrder = view.findViewById(R.id.rvMedicinesOrder);
        imgOrderNotFound = view.findViewById(R.id.imgOrderNotFound);

        dbHelper = new DatabaseHelper(getContext());
        orderList = new ArrayList<>();
        orderAdapter = new AdminMedicineOrderAdapter(getContext(), orderList);
        rvMedicinesOrder.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMedicinesOrder.setAdapter(orderAdapter);

        displayMedicineOrderData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        displayMedicineOrderData();
    }

    private void displayMedicineOrderData(){
        orderList.clear();
        Cursor cursor = dbHelper.getAllMedicineOrder();
        if (cursor.getCount() == 0){
            imgOrderNotFound.setVisibility(View.VISIBLE);
        } else {
            imgOrderNotFound.setVisibility(View.GONE);
            while (cursor.moveToNext()){
                orderList.add(new AdminMedicineOrderModel(
                        cursor.getInt(cursor.getColumnIndexOrThrow("ID")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("MedicineID")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("UserID")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("Price")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("Quantity"))
                ));
            }
        }
        cursor.close();
        orderAdapter.notifyDataSetChanged();
    }
}