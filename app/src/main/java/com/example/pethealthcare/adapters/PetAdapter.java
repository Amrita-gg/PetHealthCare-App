package com.example.pethealthcare.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ArrayAdapter;

import com.bumptech.glide.Glide;
import com.example.pethealthcare.R;
import com.example.pethealthcare.models.Pet;

import java.util.List;

public class PetAdapter extends ArrayAdapter<Pet> {
    private Context context;
    private List<Pet> petList;

    public PetAdapter(Context context, List<Pet> petList) {
        super(context, R.layout.item_pet_spinner, petList);
        this.context = context;
        this.petList = petList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return createView(position, convertView, parent);
    }

    private View createView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_pet_spinner, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.petImageView);
        TextView petNameTextView = convertView.findViewById(R.id.petNameTextView);
        TextView petTypeTextView = convertView.findViewById(R.id.petTypeTextView); // Added Pet Type

        Pet pet = petList.get(position);
        petNameTextView.setText(pet.getName());
        petTypeTextView.setText(pet.getType()); // Set Pet Type

        Glide.with(context)
                .load(pet.getImageUrl()) // Load image from URL
                .placeholder(R.drawable.add_pet) // Default image
                .into(imageView);

        return convertView;
    }
}
