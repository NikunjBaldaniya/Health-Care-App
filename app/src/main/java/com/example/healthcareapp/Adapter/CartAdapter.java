package com.example.healthcareapp.Adapter;

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
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthcareapp.CartActivity;
import com.example.healthcareapp.Database.DatabaseHelper;
import com.example.healthcareapp.MedicineDetailsActivity;
import com.example.healthcareapp.Model.CartModel;
import com.example.healthcareapp.R;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    Context context;
    ArrayList<CartModel> cartList;
    DatabaseHelper dbHelper;

    public CartAdapter(Context context, ArrayList<CartModel> cartList) {
        this.context = context;
        this.cartList = cartList;
        dbHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_add_to_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtMedicineName.setText(cartList.get(position).getTxtMedicineName());
        holder.imgMedicine.setImageBitmap(cartList.get(position).getImgMedicine());
        holder.txtTotalPrice.setText(String.valueOf(cartList.get(position).getTxtTotalPrice()));
        holder.txtCount.setText(String.valueOf(cartList.get(position).getTxtCount()));

        int id = cartList.get(position).getID();
        double[] price = {dbHelper.getCartItemPrice(id)};
        int[] quantity = {dbHelper.getCartItemQuantity(id)};

        int medicineId = cartList.get(position).getMedicineID();

        holder.btnPlus.setOnClickListener(v ->{
            quantity[0]++;
            int productQuantity = quantity[0];
            double productPrice = price[0] * quantity[0];
            boolean result = dbHelper.updateCart(id, productQuantity, productPrice);
            if (result){
                holder.txtCount.setText(String.valueOf(productQuantity));
                String totalPrice = String.format("%.2f", productPrice);
                holder.txtTotalPrice.setText(totalPrice);

                // Update model
                cartList.get(position).setTxtCount(productQuantity);
                cartList.get(position).setTxtTotalPrice(Double.parseDouble(totalPrice));

                // Update total price in activity
                if (context instanceof CartActivity) {
                    ((CartActivity) context).updateTotalPrice();
                }
            }
        });

        holder.btnMinus.setOnClickListener(v ->{
            if (quantity[0] > 1){
                quantity[0]--;
                int productQuantity = quantity[0];
                double productPrice = price[0] * quantity[0];
                boolean result = dbHelper.updateCart(id, productQuantity, productPrice);
                if (result){
                    holder.txtCount.setText(String.valueOf(productQuantity));
                    String totalPrice = String.format("%.2f", productPrice);
                    holder.txtTotalPrice.setText(String.valueOf(totalPrice));

                    // Update model
                    cartList.get(position).setTxtCount(productQuantity);
                    cartList.get(position).setTxtTotalPrice(Double.parseDouble(totalPrice));

                    // Update total price in activity
                    if (context instanceof CartActivity) {
                        ((CartActivity) context).updateTotalPrice();
                    }
                }
            } else {
                Toast.makeText(context, "Minimum Limit is 1 Product", Toast.LENGTH_SHORT).show();
            }
        });

        holder.btnDelete.setOnClickListener(v ->{
            int currentPos = holder.getAdapterPosition();
            if (currentPos != RecyclerView.NO_POSITION){
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Delete Item");
                alert.setMessage("Are you sure you want to delete item?");
                alert.setPositiveButton("Yes", ((dialog, which) -> {
                    boolean result = dbHelper.deleteCart(id);
                    if (result){
                        Toast.makeText(context, "Item Deleted", Toast.LENGTH_SHORT).show();
                        cartList.remove(currentPos); // remove from list
                        notifyItemRemoved(currentPos); // refresh UI
                        notifyItemRangeChanged(currentPos, cartList.size());
                    }
                    if (context instanceof CartActivity) {
                        ((CartActivity) context).updateTotalPrice();
                    }
                }));
                alert.setNegativeButton("No", ((dialog, which) -> {
                    dialog.cancel();
                }));
                alert.show();
            }
        });

        holder.lytCart.setOnClickListener(v ->{
            Intent intent = new Intent(context, MedicineDetailsActivity.class);
            intent.putExtra("ID", medicineId);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout lytCart;
        ImageView imgMedicine;
        TextView txtMedicineName, txtTotalPrice, txtCount;
        ImageButton btnMinus, btnPlus, btnDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            lytCart = itemView.findViewById(R.id.lytCart);
            imgMedicine = itemView.findViewById(R.id.imgMedicine);
            txtMedicineName = itemView.findViewById(R.id.txtMedicineName);
            txtTotalPrice = itemView.findViewById(R.id.txtTotalPrice);
            txtCount = itemView.findViewById(R.id.txtCount);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
