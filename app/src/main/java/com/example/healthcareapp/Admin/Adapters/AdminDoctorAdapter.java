package com.example.healthcareapp.Admin.Adapters;

import android.app.AlertDialog;
import android.content.Context;
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

import com.example.healthcareapp.Admin.Models.AdminDoctorModel;
import com.example.healthcareapp.Admin.UpdateDoctorsActivity;
import com.example.healthcareapp.Database.DatabaseHelper;
import com.example.healthcareapp.R;

import java.util.List;

public class AdminDoctorAdapter extends RecyclerView.Adapter<AdminDoctorAdapter.ViewHolder> {

    Context context;
    List<AdminDoctorModel> doctorList;
    DatabaseHelper dbHelper;

    public AdminDoctorAdapter(Context context, List<AdminDoctorModel> doctorList) {
        this.context = context;
        this.doctorList = doctorList;
        this.dbHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_admin_doctor, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imgDoctor.setImageBitmap(doctorList.get(position).getImg());
        holder.txtName.setText(doctorList.get(position).getName());
        holder.txtJobTitle.setText(doctorList.get(position).getEmail());

        // Update doctor
        holder.lytDoctor.setOnClickListener(v -> {
            int currentPos = holder.getAdapterPosition();
            if (currentPos != RecyclerView.NO_POSITION) {
                Intent doctorIntent = new Intent(context, UpdateDoctorsActivity.class);
                doctorIntent.putExtra("ID", doctorList.get(currentPos).getId());
                context.startActivity(doctorIntent);
            }
        });

        // Delete doctor
        holder.btnDelete.setOnClickListener(v -> {
            int currentPos = holder.getAdapterPosition();
            if (currentPos != RecyclerView.NO_POSITION) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Delete Doctor");
                alert.setMessage("Are you sure you want to delete this doctor?");

                alert.setPositiveButton("Yes", (dialog, which) -> {
                    boolean result = dbHelper.deleteDoctor(doctorList.get(currentPos).getId());
                    if (result) {
                        doctorList.remove(currentPos);       // remove from list
                        notifyItemRemoved(currentPos);       // update UI
                        notifyItemRangeChanged(currentPos, doctorList.size());
                        Toast.makeText(context, "Doctor Deleted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "No one Record deleted", Toast.LENGTH_SHORT).show();
                    }
                });

                alert.setNegativeButton("No", (dialog, which) -> dialog.cancel());
                alert.show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return doctorList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout lytDoctor;
        ImageView imgDoctor;
        TextView txtName, txtJobTitle;
        ImageButton btnDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            lytDoctor = itemView.findViewById(R.id.lytDoctor);
            imgDoctor = itemView.findViewById(R.id.imgDoctor);
            txtName = itemView.findViewById(R.id.txtName);
            txtJobTitle = itemView.findViewById(R.id.txtJobTitle);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
