package com.example.pethealthcare.exercises;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pethealthcare.R;

import java.util.List;

public class ExerciseCategoryAdapter extends RecyclerView.Adapter<ExerciseCategoryAdapter.ViewHolder> {
    private Context context;
    private List<ExerciseCategoryModel> categoryList;
    private String petType, petId; // Store pet details

    public ExerciseCategoryAdapter(Context context, List<ExerciseCategoryModel> categoryList, String petType, String petId) {
        this.context = context;
        this.categoryList = categoryList;
        this.petType = petType;
        this.petId = petId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ExerciseCategoryModel category = categoryList.get(position);
        holder.categoryName.setText(category.getName());
        holder.categoryBenefits.setText(category.getBenefits());
        Glide.with(context).load(category.getImg_url()).into(holder.categoryImage);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ExercisesActivity.class);
            intent.putExtra("EXERCISE_TYPE", category.getType());
            intent.putExtra("PET_TYPE", petType);
            intent.putExtra("PET_ID", petId);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName, categoryBenefits;
        ImageView categoryImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.category_name);
            categoryBenefits = itemView.findViewById(R.id.category_benefits);
            categoryImage = itemView.findViewById(R.id.category_image);
        }
    }
}
