package com.example.healthcareapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthcareapp.Database.DatabaseHelper;
import com.example.healthcareapp.ImageHelper;
import com.example.healthcareapp.MedicineDetailsActivity;
import com.example.healthcareapp.Model.OrderListModel;
import com.example.healthcareapp.R;

import java.util.ArrayList;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder>{
    Context context;
    ArrayList<OrderListModel> orderList;
    DatabaseHelper dbHelper;

    public OrderListAdapter(Context context, ArrayList<OrderListModel> orderList) {
        this.context = context;
        this.orderList = orderList;
        dbHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_ordered_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int id = orderList.get(position).getId();
        int medicineId = orderList.get(position).getMedicineId();

        holder.imgMedicine.setImageBitmap(ImageHelper.bytesToBitmap(dbHelper.getMedicineImage(medicineId)));
        holder.txtMedicineName.setText(dbHelper.getMedicineName(medicineId));
        holder.txtOrderDate.setText(orderList.get(position).getOrderDate());

        holder.btnDelete.setOnClickListener(v ->{
            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            alert.setTitle("Cancel Order");
            alert.setMessage("Are you sure you want to cancel this order?");
            alert.setPositiveButton("Yes", (dialog, which)->{
                dbHelper.deleteMedicineOrder(id);
                orderList.remove(position);
                notifyDataSetChanged();
            });
            alert.setNegativeButton("No", ((dialogInterface, i) -> {
                dialogInterface.dismiss();
            }));
            alert.show();
        });

        holder.lytOrder.setOnClickListener(v ->{
            Intent intent = new Intent(context, MedicineDetailsActivity.class);
            intent.putExtra("ID", medicineId);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView lytOrder;
        ImageView imgMedicine;
        TextView txtMedicineName, txtOrderDate;
        ImageButton btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lytOrder = itemView.findViewById(R.id.lytOrder);
            imgMedicine = itemView.findViewById(R.id.imgMedicine);
            txtMedicineName = itemView.findViewById(R.id.txtMedicineName);
            txtOrderDate = itemView.findViewById(R.id.txtOrderDate);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
