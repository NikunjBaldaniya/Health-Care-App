package com.example.healthcareapp.Admin.Adapters;

import android.app.Activity;
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

import com.example.healthcareapp.Admin.Models.AdminUserModel;
import com.example.healthcareapp.Admin.UpdateUsersActivity;
import com.example.healthcareapp.Database.DatabaseHelper;
import com.example.healthcareapp.R;

import java.util.List;

public class AdminUserAdapter extends RecyclerView.Adapter<AdminUserAdapter.ViewHolder> {

    List<AdminUserModel> userList;
    Context context;
    DatabaseHelper dbHelper;

    public AdminUserAdapter(Context context, List<AdminUserModel> userList) {
        this.userList = userList;
        this.context = context;
        this.dbHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_admin_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.img.setImageBitmap(userList.get(position).getImg());
        holder.txtName.setText(userList.get(position).getName());
        holder.txtEmail.setText(userList.get(position).getEmail());

        // activity intent
        holder.mainUser.setOnClickListener(v -> {
            int currentPos = holder.getAdapterPosition();
            if (currentPos != RecyclerView.NO_POSITION) {
                Intent userIntent = new Intent(context, UpdateUsersActivity.class);
                userIntent.putExtra("ID", userList.get(currentPos).getId());
                context.startActivity(userIntent);
            }
        });

        // Delete user
        holder.btnDelete.setOnClickListener(v -> {
            int currentPos = holder.getAdapterPosition();
            if (currentPos != RecyclerView.NO_POSITION) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Delete User");
                alert.setMessage("Are you sure you want to delete this user?");

                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean result = dbHelper.deleteUser(userList.get(currentPos).getId());
                        if (result){
                            userList.remove(currentPos);  // remove from list
                            notifyItemRemoved(currentPos); // refresh UI
                            notifyItemRangeChanged(currentPos, userList.size());
                        } else {
                            Toast.makeText(context, "No one Record deleted", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                alert.setNegativeButton("No", (dialog, which) -> dialog.cancel());
                alert.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView txtName, txtEmail;
        ImageButton btnDelete;
        ConstraintLayout mainUser;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            txtName = itemView.findViewById(R.id.txtName);
            txtEmail = itemView.findViewById(R.id.txtEmail);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            mainUser = itemView.findViewById(R.id.mainUser);
        }
    }
}
