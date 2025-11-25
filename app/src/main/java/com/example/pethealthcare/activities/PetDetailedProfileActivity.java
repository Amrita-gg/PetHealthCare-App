package com.example.pethealthcare.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.pethealthcare.R;
import com.example.pethealthcare.models.PetProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class PetDetailedProfileActivity extends AppCompatActivity {

    private TextView petName, petAge, petBreed, petType, petHealthStatus, petParentExperience, petAdditionalNotes;
    private ImageView petImage;
    private FirebaseFirestore db;
    private String petId; // ID of the selected pet
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_detailed_profile);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Initialize views
        petName = findViewById(R.id.tv_pet_name);
        petAge = findViewById(R.id.tv_pet_age);
        petBreed = findViewById(R.id.tv_pet_breed);
        petType = findViewById(R.id.tv_pet_type);
        petHealthStatus = findViewById(R.id.tv_pet_health_status);
        petParentExperience = findViewById(R.id.tv_pet_parent_experience);
        petAdditionalNotes = findViewById(R.id.tv_pet_additional_notes);
        petImage = findViewById(R.id.iv_pet_image);

        // Retrieve Pet ID from Intent
        petId = getIntent().getStringExtra("PET_ID");

        if (petId != null && !petId.isEmpty()) {
            fetchPetDetailsFromFirestore(petId); // Fetch pet details using ID
        } else {
            Toast.makeText(this, "Pet ID not received", Toast.LENGTH_SHORT).show();
            finish(); // Close activity if no pet ID is received
        }


    }

    private void fetchPetDetailsFromFirestore(String petId) {
        db.collection("pets").document(petId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Extract data from Firestore document
                        String name = documentSnapshot.getString("name");
                        String age = documentSnapshot.getString("age");
                        String breed = documentSnapshot.getString("breed");
                        String type = documentSnapshot.getString("type");
                        String healthStatus = documentSnapshot.getString("healthStatus");
                        String parentExperience = documentSnapshot.getString("parentExperience");
                        String additionalNotes = documentSnapshot.getString("additionalNotes");
                        String imageUrl = documentSnapshot.getString("imageUrl");

                        // Set data to UI elements
                        petName.setText(name);
                        petAge.setText("Age: " + age);
                        petBreed.setText("Breed: " + breed);
                        petType.setText("Type: " + type);
                        petHealthStatus.setText("Health Status: " + healthStatus);
                        petParentExperience.setText("Parent Experience: " + parentExperience);
                        petAdditionalNotes.setText("Notes: " + additionalNotes);

                        // Load Image
                        Glide.with(this)
                                .load(imageUrl)
                                .placeholder(R.drawable.add_pet)
                                .error(R.drawable.add_pet)
                                .into(petImage);
                    } else {
                        Toast.makeText(this, "Pet details not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error fetching details: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
