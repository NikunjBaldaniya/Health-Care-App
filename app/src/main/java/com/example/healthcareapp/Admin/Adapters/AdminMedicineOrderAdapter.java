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

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthcareapp.Admin.AdminMedicineOrderDetailsActivity;
import com.example.healthcareapp.Admin.Models.AdminMedicineOrderModel;
import com.example.healthcareapp.Database.DatabaseHelper;
import com.example.healthcareapp.ImageHelper;
import com.example.healthcareapp.R;

import java.util.ArrayList;

public class AdminMedicineOrderAdapter extends RecyclerView.Adapter<AdminMedicineOrderAdapter.ViewHolder> {
    Context context;
    ArrayList<AdminMedicineOrderModel> medicineOrderList;
    DatabaseHelper dbHelper;
    public AdminMedicineOrderAdapter(Context context, ArrayList<AdminMedicineOrderModel> medicineOrderList) {
        this.context = context;
        this.medicineOrderList = medicineOrderList;
        dbHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_medicine_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int id = medicineOrderList.get(position).getId();
        int medicineId = medicineOrderList.get(position).getMedicineId();
        int userId = medicineOrderList.get(position).getUserId();
        holder.txtMedicineName.setText(dbHelper.getMedicineName(medicineId));
        holder.txtUserName.setText(dbHelper.getNameByUserId(userId));
        holder.imgMedicine.setImageBitmap(ImageHelper.bytesToBitmap(dbHelper.getMedicineImage(medicineId)));

        holder.btnDelete.setOnClickListener(v ->{
            String medicineName = dbHelper.getMedicineName(medicineId);
            String Title = "Cancel Order";
            String message = "Oops! " + medicineName + " is unavailable, so your order has been cancelled.";
            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            alert.setTitle("Cancel Product");
            alert.setMessage("Are you sure you want to cancel this product?");
            alert.setPositiveButton("Yes", (dialog, which) -> {
                dbHelper.deleteMedicineOrder(id);
                dbHelper.insertNotification(userId, medicineId, Title, message);
                medicineOrderList.remove(position);
                notifyDataSetChanged();
            });
            alert.setNegativeButton("No", (dialog, which) -> {
                dialog.dismiss();
            });
            alert.show();
        });

        holder.lytMedicineOrder.setOnClickListener(v ->{
            Intent intent = new Intent(context, AdminMedicineOrderDetailsActivity.class);
            intent.putExtra("ID", id);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return medicineOrderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView lytMedicineOrder;
        ImageView imgMedicine;
        TextView txtMedicineName, txtUserName;
        ImageButton btnDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            lytMedicineOrder = itemView.findViewById(R.id.lytMedicineOrder);
            imgMedicine = itemView.findViewById(R.id.imgMedicine);
            txtMedicineName = itemView.findViewById(R.id.txtMedicineName);
            txtUserName = itemView.findViewById(R.id.txtUserName);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
