package com.example.healthcareapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthcareapp.Database.DatabaseHelper;
import com.example.healthcareapp.DoctorDetailsActivity;
import com.example.healthcareapp.Model.DoctorListModel;
import com.example.healthcareapp.R;

import java.util.List;

public class DoctorListAdapter extends RecyclerView.Adapter<DoctorListAdapter.ViewHolder> {

    Context context;
    List<DoctorListModel> list;
    DatabaseHelper dbHelper;

    public DoctorListAdapter(Context context, List<DoctorListModel> list) {
        this.context = context;
        this.list = list;
        dbHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.doctor_list_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imgDoctor.setImageBitmap(list.get(position).imgDoctor);
        holder.txtDoctorName.setText(list.get(position).txtDoctorName);
        holder.txtJobTittle.setText(list.get(position).txtDoctorSpeciality);
        holder.txtRating.setText(String.valueOf(list.get(position).txtRating));

        holder.lytDoctor.setOnClickListener(v -> {
            int currentPos = holder.getAdapterPosition();
            if (currentPos != RecyclerView.NO_POSITION) {
                Intent doctorIntent = new Intent(context, DoctorDetailsActivity.class);
                doctorIntent.putExtra("ID", list.get(currentPos).doctorId);
                context.startActivity(doctorIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView lytDoctor;
        ImageView imgDoctor;
        TextView txtDoctorName, txtJobTittle, txtRating;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            lytDoctor = itemView.findViewById(R.id.lytDoctor);
            imgDoctor = itemView.findViewById(R.id.imgDoctor);
            txtDoctorName = itemView.findViewById(R.id.txtDoctorName);
            txtJobTittle = itemView.findViewById(R.id.txtJobTittle);
            txtRating = itemView.findViewById(R.id.txtRating);
        }
    }
}
