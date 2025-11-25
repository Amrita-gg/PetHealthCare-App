package com.example.pethealthcare.adapters;

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
import com.example.pethealthcare.activities.PetDetailedProfileActivity;
import com.example.pethealthcare.models.PetProfile;
import java.util.List;

public class PetListAdapter extends RecyclerView.Adapter<PetListAdapter.PetProfileViewHolder> {
    private Context context;
    private List<PetProfile> petProfileList;

    public PetListAdapter(Context context, List<PetProfile> petProfileList) {
        this.context = context;
        this.petProfileList = petProfileList;
    }

    @NonNull
    @Override
    public PetProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pet, parent, false);
        return new PetProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PetProfileViewHolder holder, int position) {
        PetProfile petProfile = petProfileList.get(position);
        holder.petName.setText(petProfile.getName());
        holder.petBreed.setText("Breed: " + petProfile.getBreed());
        holder.petAge.setText("Age: " + petProfile.getAge());

        // Load image with Glide
        Glide.with(context)
                .load(petProfile.getImageUrl())
                .placeholder(R.drawable.add_pet) // Default image if URL is empty
                .error(R.drawable.add_pet) // Error image if loading fails
                .into(holder.petImage);

        // Click listener to open detailed profile with full pet data
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, PetDetailedProfileActivity.class);
            intent.putExtra("selectedPet", petProfile); // Pass full object
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return petProfileList.size();
    }

    public static class PetProfileViewHolder extends RecyclerView.ViewHolder {
        TextView petName, petBreed, petAge;
        ImageView petImage;

        public PetProfileViewHolder(@NonNull View itemView) {
            super(itemView);
            petName = itemView.findViewById(R.id.tv_pet_name);
            petBreed = itemView.findViewById(R.id.tv_pet_breed);
            petAge = itemView.findViewById(R.id.tv_pet_age);
            petImage = itemView.findViewById(R.id.iv_pet_image);
        }
    }
}
