package com.example.pethealthcare.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pethealthcare.R;
import com.example.pethealthcare.adapters.PetAdapter;
import com.example.pethealthcare.exercises.ExerciseCategoryActivity;
import com.example.pethealthcare.models.Pet;
import com.example.pethealthcare.vaccines.VaccinesActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Spinner petProfileSpinner;
    private List<Pet> petList = new ArrayList<>();
    private PetAdapter petAdapter;
    private EditText etAnimalName;
    private Button btnSearch;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private String selectedPetId = null;
    private String selectedPetType = null; // Store the selected pet's type

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize UI components
        petProfileSpinner = findViewById(R.id.petProfileSpinner);
        etAnimalName = findViewById(R.id.et_animal_name);
        btnSearch = findViewById(R.id.btn_search);

        ImageView imageDiet = findViewById(R.id.image1);
        ImageView imageVaccination = findViewById(R.id.image2);
        ImageView imageMoodTracker = findViewById(R.id.image3);
        ImageView imageExercise = findViewById(R.id.image4);
        ImageView addPetImage = findViewById(R.id.add_pet_image);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Click Listeners for Feature Images
        imageDiet.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, DietModuleActivity.class)));

        imageVaccination.setOnClickListener(v -> {
            if (selectedPetId != null && selectedPetType != null) {
                Intent intent = new Intent(MainActivity.this, VaccinesActivity.class);
                intent.putExtra("PET_ID", selectedPetId);
                intent.putExtra("PET_TYPE", selectedPetType);
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "Please select a pet first", Toast.LENGTH_SHORT).show();
            }
        });

        imageMoodTracker.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AddMoodActivity.class)));
        imageExercise.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ExerciseCategoryActivity.class)));
        addPetImage.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, PetDetailsFormActivity.class)));

        // Fetch pets from Firestore
        fetchPetsFromFirestore();

        // Search Button Click
        btnSearch.setOnClickListener(v -> {
            String animalName = etAnimalName.getText().toString().trim();
            if (!animalName.isEmpty()) {
                Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                intent.putExtra("ANIMAL_NAME", animalName);
                startActivity(intent);
            } else {
                etAnimalName.setError("Please enter an animal name");
            }
        });

        // Bottom Navigation Profile Click
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                return true;
            } else if (itemId == R.id.nav_profile) {
                if (selectedPetId != null) {
                    Intent intent = new Intent(MainActivity.this, PetDetailedProfileActivity.class);
                    intent.putExtra("PET_ID", selectedPetId);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                } else {
                    Toast.makeText(this, "Please select a pet first", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
            return false;
        });

        // Pet Profile Selection Listener
        petProfileSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0 && position < petList.size()) {
                    Pet selectedPet = petList.get(position);
                    fetchPetDetails(selectedPet.getName());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void fetchPetsFromFirestore() {
        String userId = auth.getCurrentUser().getUid();

        db.collection("pets")
                .whereEqualTo("userid", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    petList.clear();
                    if (queryDocumentSnapshots.isEmpty()) {
                        Toast.makeText(MainActivity.this, "No pets found", Toast.LENGTH_SHORT).show();
                    } else {
                        for (DocumentSnapshot doc : queryDocumentSnapshots) {
                            String id = doc.getId();
                            String name = doc.getString("name");
                            String imageUrl = doc.getString("imageUrl");
                            String type = doc.getString("type");

                            if (name != null && imageUrl != null && type != null) {
                                petList.add(new Pet(id, name, imageUrl, type));
                            }
                        }
                    }

                    if (petAdapter == null) {
                        petAdapter = new PetAdapter(MainActivity.this, petList);
                        petProfileSpinner.setAdapter(petAdapter);
                    } else {
                        petAdapter.notifyDataSetChanged();
                    }

                    if (!petList.isEmpty()) {
                        petProfileSpinner.setSelection(0);
                        fetchPetDetails(petList.get(0).getName());
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Error loading pets: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void fetchPetDetails(String petName) {
        String userId = auth.getCurrentUser().getUid();

        db.collection("pets")
                .whereEqualTo("name", petName)
                .whereEqualTo("userid", userId)
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                        selectedPetId = document.getId();
                        selectedPetType = document.getString("type");
                        Toast.makeText(MainActivity.this, "Selected Pet: " + petName, Toast.LENGTH_SHORT).show();
                    } else {
                        selectedPetId = null;
                        selectedPetType = null;
                        Toast.makeText(MainActivity.this, "Pet details not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Error fetching pet details: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}