package com.example.pethealthcare.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pethealthcare.R;
import com.example.pethealthcare.adapters.MoodAdapter;
import com.example.pethealthcare.models.Mood;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddMoodActivity extends AppCompatActivity {

    private EditText etPetName, etMoodNotes;
    private RecyclerView recyclerViewMoodHistory;
    private MoodAdapter moodAdapter;
    private List<Mood> moodList = new ArrayList<>();
    private Button btnHappy, btnSad, btnHungry, btnAngry, btnSick, btnSleepy, btnSaveMood;
    private List<String> selectedMoods = new ArrayList<>(); // Store selected moods
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mood);

        // Initialize Firestore & FirebaseAuth
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Initialize UI elements
        etPetName = findViewById(R.id.etPetName);
        etMoodNotes = findViewById(R.id.etMoodNotes);
        btnSaveMood = findViewById(R.id.btnSaveMood);

        btnHappy = findViewById(R.id.btnHappy);
        btnSad = findViewById(R.id.btnSad);
        btnHungry = findViewById(R.id.btnHungry);
        btnAngry = findViewById(R.id.btnAngry);
        btnSick = findViewById(R.id.btnSick);
        btnSleepy = findViewById(R.id.btnSleepy);
        recyclerViewMoodHistory = findViewById(R.id.recyclerViewMoodHistory);
        recyclerViewMoodHistory.setLayoutManager(new LinearLayoutManager(this));

        moodAdapter = new MoodAdapter(this, moodList);
        recyclerViewMoodHistory.setAdapter(moodAdapter);

        loadMoodHistory();

        // Set click listeners for mood buttons
        setMoodButtonListener(btnHappy, "Happy");
        setMoodButtonListener(btnSad, "Sad");
        setMoodButtonListener(btnHungry, "Hungry");
        setMoodButtonListener(btnAngry, "Angry");
        setMoodButtonListener(btnSick, "Sick");
        setMoodButtonListener(btnSleepy, "Sleepy");

        // Save Mood button click
        btnSaveMood.setOnClickListener(view -> saveMoodToFirestore());
    }

    private void setMoodButtonListener(Button button, String mood) {
        button.setOnClickListener(view -> {
            if (selectedMoods.contains(mood)) {
                // If already selected, remove it and reset background
                selectedMoods.remove(mood);
                button.setBackgroundColor(Color.LTGRAY);
            } else {
                // If not selected, add it and highlight the button
                selectedMoods.add(mood);
                button.setBackgroundColor(Color.parseColor("#FFEB3B")); // Yellow highlight
            }
        });
    }

    private void saveMoodToFirestore() {
        String petName = etPetName.getText().toString().trim();
        String moodNotes = etMoodNotes.getText().toString().trim();

        // Ensure the user is logged in
        if (auth.getCurrentUser() == null) {
            Toast.makeText(AddMoodActivity.this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = auth.getCurrentUser().getUid();
        Timestamp timestamp = Timestamp.now(); // Firestore's Timestamp

        if (petName.isEmpty() || selectedMoods.isEmpty()) {
            Toast.makeText(AddMoodActivity.this, "Please enter pet name and select at least one mood!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create mood data
        Map<String, Object> moodData = new HashMap<>();
        moodData.put("userId", userId);
        moodData.put("petName", petName);
        moodData.put("moodTypes", selectedMoods); // Store multiple moods
        moodData.put("moodNotes", moodNotes);
        moodData.put("timestamp", timestamp);

        // Save to Firestore in "Moods" collection
        db.collection("Moods")
                .add(moodData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(AddMoodActivity.this, "Mood(s) saved successfully!", Toast.LENGTH_SHORT).show();
                    finish(); // Close activity after saving
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AddMoodActivity.this, "Error saving mood: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void loadMoodHistory() {
        String userId = auth.getCurrentUser().getUid();
        CollectionReference moodsRef = db.collection("Moods");

        moodsRef.whereEqualTo("userId", userId).addSnapshotListener((value, error) -> {
            if (error != null) {
                Toast.makeText(AddMoodActivity.this, "Error loading moods", Toast.LENGTH_SHORT).show();
                return;
            }

            moodList.clear();
            for (QueryDocumentSnapshot document : value) {
                Mood mood = document.toObject(Mood.class);
                moodList.add(new Mood(
                        document.getId(),
                        mood.getPetName(),
                        mood.getMoodTypes(),
                        mood.getMoodNotes(),
                        mood.getTimestamp()
                ));
            }
            moodAdapter.notifyDataSetChanged();
        });
    }
}
