package com.example.pethealthcare.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.pethealthcare.R;

public class PetDetailActivity extends AppCompatActivity {
    private ImageView petImageView;
    private TextView petNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_detail);

        petImageView = findViewById(R.id.petImageView);
        petNameTextView = findViewById(R.id.petNameTextView);

        // Get data from Intent
        String petName = getIntent().getStringExtra("name");
        String petImageUrl = getIntent().getStringExtra("imageUrl");

        // Set data to views
        petNameTextView.setText(petName);
        Glide.with(this).load(petImageUrl).into(petImageView);
    }
}