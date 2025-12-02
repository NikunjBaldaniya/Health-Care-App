package com.example.healthcareapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthcareapp.Model.OnboardingItem;
import com.example.healthcareapp.R;

import java.util.ArrayList;

public class OnboadingAdapter extends RecyclerView.Adapter<OnboadingAdapter.ViewHolder>{

    ArrayList<OnboardingItem> slideList;

    public OnboadingAdapter(ArrayList<OnboardingItem> slideList) {
        this.slideList = slideList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.onboarding_slide, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.img.setImageResource(slideList.get(position).getImg());
        holder.txtTitle.setText(slideList.get(position).getTxtTitle());
        holder.txtDescription.setText(slideList.get(position).getTxtDescription());
    }

    @Override
    public int getItemCount() {
        return slideList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView txtTitle, txtDescription;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtDescription = itemView.findViewById(R.id.txtDescription);
        }
    }
}
