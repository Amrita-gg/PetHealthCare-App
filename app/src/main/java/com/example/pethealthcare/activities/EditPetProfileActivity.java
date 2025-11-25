package com.example.pethealthcare.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.pethealthcare.R;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class EditPetProfileActivity extends AppCompatActivity {

    private EditText etPetName, etPetAge, etPetBreed, etPetType, etPetHealthStatus, etPetParentExperience, etPetAdditionalNotes;
    private Button btnSaveChanges;
    private FirebaseFirestore db;
    private String petId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pet_profile);

        db = FirebaseFirestore.getInstance();

        etPetName = findViewById(R.id.et_pet_name);
        etPetAge = findViewById(R.id.et_pet_age);
        etPetBreed = findViewById(R.id.et_pet_breed);
        etPetType = findViewById(R.id.et_pet_type);
        etPetHealthStatus = findViewById(R.id.et_pet_health_status);
        etPetParentExperience = findViewById(R.id.et_pet_parent_experience);
        etPetAdditionalNotes = findViewById(R.id.et_pet_additional_notes);
        btnSaveChanges = findViewById(R.id.btn_save_changes);

        petId = getIntent().getStringExtra("PET_ID");

        if (petId == null || petId.trim().isEmpty()) {
            Toast.makeText(this, "Pet ID not received", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        fetchPetDetails();

        btnSaveChanges.setOnClickListener(v -> savePetDetails());
    }

    private void fetchPetDetails() {
        db.collection("pets").document(petId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        etPetName.setText(documentSnapshot.getString("name"));

                        // Fix for 'age' field type issue (Ensure it is retrieved as Long)
                        Long ageLong = documentSnapshot.getLong("age");
                        String age = (ageLong != null) ? String.valueOf(ageLong) : "";
                        etPetAge.setText(age);

                        etPetBreed.setText(documentSnapshot.getString("breed"));
                        etPetType.setText(documentSnapshot.getString("type"));
                        etPetHealthStatus.setText(documentSnapshot.getString("healthStatus"));
                        etPetParentExperience.setText(documentSnapshot.getString("parentExperience"));
                        etPetAdditionalNotes.setText(documentSnapshot.getString("additionalNotes"));
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error loading details: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("FirestoreError", "Error fetching pet details", e);
                });
    }

    private void savePetDetails() {
        Map<String, Object> petUpdates = new HashMap<>();
        petUpdates.put("name", etPetName.getText().toString().trim());

        // Convert age input safely to a number
        String ageInput = etPetAge.getText().toString().trim();
        if (!ageInput.isEmpty()) {
            try {
                petUpdates.put("age", Long.parseLong(ageInput)); // Store as Long
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid age. Please enter a valid number.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        petUpdates.put("breed", etPetBreed.getText().toString().trim());
        petUpdates.put("type", etPetType.getText().toString().trim());
        petUpdates.put("healthStatus", etPetHealthStatus.getText().toString().trim());
        petUpdates.put("parentExperience", etPetParentExperience.getText().toString().trim());
        petUpdates.put("additionalNotes", etPetAdditionalNotes.getText().toString().trim());

        db.collection("pets").document(petId)
                .update(petUpdates)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Pet profile updated!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
                    Log.e("FirestoreError", "Error updating pet details", e);
                });
    }
}
