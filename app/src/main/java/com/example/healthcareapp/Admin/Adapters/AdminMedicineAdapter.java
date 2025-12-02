package com.example.healthcareapp.Admin.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthcareapp.Admin.Models.AdminMedicineModel;
import com.example.healthcareapp.Admin.UpdateMedicinesActivity;
import com.example.healthcareapp.Database.DatabaseHelper;
import com.example.healthcareapp.R;

import java.util.List;

public class AdminMedicineAdapter extends RecyclerView.Adapter<AdminMedicineAdapter.ViewHolder> {
    Context context;
    List<AdminMedicineModel> medicineList;
    DatabaseHelper dbHelper;

    public AdminMedicineAdapter(Context context, List<AdminMedicineModel> medicineList) {
        this.context = context;
        this.medicineList = medicineList;
        this.dbHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.admin_medicine_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imgMedicine.setImageBitmap(medicineList.get(position).getImgMedicine());
        holder.txtMedicineName.setText(medicineList.get(position).getTxtMedicineName());
        holder.txtMedicinePrice.setText(String.valueOf(medicineList.get(position).getTxtMedicinePrice()));

        // activity intent
        holder.lytMedicine.setOnClickListener(v ->{
            int currentPos = holder.getAdapterPosition();
            if (currentPos != RecyclerView.NO_POSITION){
                Intent medicineIntent = new Intent(context, UpdateMedicinesActivity.class);
                medicineIntent.putExtra("ID", medicineList.get(currentPos).getMedicineID());
                context.startActivity(medicineIntent);
            }
        });

        // Delete Medicine
        holder.btnDelete.setOnClickListener(v ->{
            int currentPos = holder.getAdapterPosition();
            if (currentPos != RecyclerView.NO_POSITION){
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Delete Medicine");
                alert.setMessage("Are you sure you want to delete this medicine?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean result = dbHelper.deleteMedicine(medicineList.get(currentPos).getMedicineID());
                        if (result){
                            medicineList.remove(currentPos);       // remove from list
                            notifyItemRemoved(currentPos);       // update UI
                            notifyItemRangeChanged(currentPos, medicineList.size());
                            Toast.makeText(context, "Medicine Deleted!!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "No one Record deleted", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alert.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout lytMedicine;
        ImageView imgMedicine;
        TextView txtMedicineName, txtMedicinePrice;
        ImageButton btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            lytMedicine = itemView.findViewById(R.id.lytMedicine);
            imgMedicine = itemView.findViewById(R.id.imgMedicine);
            txtMedicineName = itemView.findViewById(R.id.txtMedicineName);
            txtMedicinePrice = itemView.findViewById(R.id.txtMedicinePrice);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
