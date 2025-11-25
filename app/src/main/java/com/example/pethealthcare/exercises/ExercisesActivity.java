package com.example.pethealthcare.exercises;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pethealthcare.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ExercisesActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ExerciseAdapter exerciseAdapter;
    List<ExerciseModel> exerciseList;
    FirebaseFirestore firestore;
    private String petType, petId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);

        firestore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.exercise_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get intent extras
        Intent intent = getIntent();
        String category = intent.getStringExtra("type");
        petId = intent.getStringExtra("PET_ID");
        petType = intent.getStringExtra("PET_TYPE");

        exerciseList = new ArrayList<>();

        // Fix: Pass petId and petType to the adapter
        exerciseAdapter = new ExerciseAdapter(this, exerciseList, petId, petType);
        recyclerView.setAdapter(exerciseAdapter);

        firestore.collection("Exercises")
                .whereEqualTo("type", petType)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            ExerciseModel exercise = doc.toObject(ExerciseModel.class);
                            exerciseList.add(exercise);
                        }
                        exerciseAdapter.notifyDataSetChanged();
                    }
                });
    }
}
