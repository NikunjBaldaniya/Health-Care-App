package com.example.healthcareapp.Admin.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.healthcareapp.Admin.Adapters.DoctorAdapter;
import com.example.healthcareapp.Admin.Adapters.NewUserAdapter;
import com.example.healthcareapp.Admin.Models.NewUserModel;
import com.example.healthcareapp.Database.DatabaseHelper;
import com.example.healthcareapp.Model.DoctorListModel;
import com.example.healthcareapp.R;

import java.util.List;

public class AdminHomeFragment extends Fragment {

    RecyclerView rcNewDoctor, rcNewUsers;
    TextView txtUsers, txtDoctors, txtMedicine;
    ImageView imgDoctorNotFound, imgUserNotFound;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_home, container, false);

        rcNewDoctor = view.findViewById(R.id.rcNewDoctor);
        rcNewUsers = view.findViewById(R.id.rcNewUsers);
        txtUsers = view.findViewById(R.id.txtUsers);
        txtDoctors = view.findViewById(R.id.txtDoctors);
        txtMedicine = view.findViewById(R.id.txtMedicine);
        imgDoctorNotFound = view.findViewById(R.id.imgDoctorNotFound);
        imgUserNotFound = view.findViewById(R.id.imgUserNotFound);

        DatabaseHelper dbHelper = new DatabaseHelper(getContext());

        int userCount = dbHelper.getUserCount();
        int doctorCount = dbHelper.getDoctorCount();
        int medicineCount = dbHelper.getMedicineCount();

        txtUsers.setText(String.valueOf(userCount));
        txtUsers.setSelected(true);
        txtDoctors.setText(String.valueOf(doctorCount));
        txtDoctors.setSelected(true);
        txtMedicine.setText(String.valueOf(medicineCount));
        txtMedicine.setSelected(true);

        List<DoctorListModel> popularDoctorList = dbHelper.getNewDoctors();
        if (popularDoctorList.isEmpty()) {
            imgDoctorNotFound.setVisibility(View.VISIBLE);
        } else {
            imgDoctorNotFound.setVisibility(View.GONE);
        }
        DoctorAdapter doctorAdapter = new DoctorAdapter(getContext(), popularDoctorList);
        rcNewDoctor.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rcNewDoctor.setAdapter(doctorAdapter);

        List<NewUserModel> newUserModelList = dbHelper.getNewUsers();
        if (newUserModelList.isEmpty()) {
            imgUserNotFound.setVisibility(View.VISIBLE);
        } else {
            imgUserNotFound.setVisibility(View.GONE);
        }
        NewUserAdapter userAdapter = new NewUserAdapter(getContext(), newUserModelList);
        rcNewUsers.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rcNewUsers.setAdapter(userAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}