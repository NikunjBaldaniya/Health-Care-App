package com.example.healthcareapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthcareapp.Database.DatabaseHelper;
import com.example.healthcareapp.DoctorSearchResultActivity;
import com.example.healthcareapp.Model.HistoryModel;
import com.example.healthcareapp.R;

import java.util.List;

public class DoctorSearchHistoryAdapter extends RecyclerView.Adapter<DoctorSearchHistoryAdapter.ViewHolder>{
    Context context;
    List<HistoryModel> historyList;
    DatabaseHelper dbHelper;

    public DoctorSearchHistoryAdapter(Context context, List<HistoryModel> historyList) {
        this.context = context;
        this.historyList = historyList;
        dbHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_seach_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String history = historyList.get(position).getHistory();
        holder.txtHistory.setText(history);
        holder.txtHistory.setSelected(true);

        int id = historyList.get(position).getID();
        holder.btnDelete.setOnClickListener(v ->{
            int currentPos = holder.getAdapterPosition();
            dbHelper.deleteDoctorSearchHistory(id);
            historyList.remove(currentPos); // remove from list
            notifyItemRemoved(currentPos); // refresh UI
            notifyItemRangeChanged(currentPos, historyList.size());
        });

        holder.lytHistory.setOnClickListener(v ->{
            Intent intent = new Intent(context, DoctorSearchResultActivity.class);
            intent.putExtra("SearchItem", history);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtHistory;
        ConstraintLayout lytHistory;
        ImageButton btnDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtHistory = itemView.findViewById(R.id.txtHistory);
            lytHistory = itemView.findViewById(R.id.lytHistory);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
