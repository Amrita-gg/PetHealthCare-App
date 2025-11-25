package com.example.pethealthcare.vaccines;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pethealthcare.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class VaccinesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView tvNoVaccines;
    private FirebaseFirestore db;
    private String petType; // Store pet type for filtering

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccines);

        recyclerView = findViewById(R.id.recyclerViewVaccines);
        tvNoVaccines = findViewById(R.id.tvNoVaccines);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Get pet details from intent
        Intent intent = getIntent();
        String petId = intent.getStringExtra("PET_ID");
        petType = intent.getStringExtra("PET_TYPE");

        // Debug: Show received pet type
        Toast.makeText(this, "Received Pet Type: " + petType, Toast.LENGTH_LONG).show();

        // Fetch vaccines based on pet type
        fetchAndDisplayVaccines();
    }

    private void fetchAndDisplayVaccines() {
        db.collection("vaccinations")
                .whereEqualTo("type", petType) // Ensure correct matching
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Vaccine> vaccineList = new ArrayList<>();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String id = document.getId();
                        String name = document.getString("vaccineName");
                        String description = document.getString("desc");
                        String type = document.getString("type");

                        // Debugging: Print values
                        Toast.makeText(this, "Firestore Type: " + type, Toast.LENGTH_SHORT).show();

                        if (name != null && description != null && type != null) {
                            vaccineList.add(new Vaccine(id, name.trim(), description.trim(), type.trim()));
                        }
                    }

                    // Debugging: Show number of results
                    Toast.makeText(this, "Found " + vaccineList.size() + " vaccines", Toast.LENGTH_LONG).show();

                    // Display vaccines or show "No Vaccines Found"
                    if (vaccineList.isEmpty()) {
                        tvNoVaccines.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        tvNoVaccines.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        recyclerView.setAdapter(new VaccinesAdapter(this, vaccineList));
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(VaccinesActivity.this, "Error fetching vaccines: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }

}
