package com.example.pethealthcare.exercises;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pethealthcare.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class ExerciseCategoryActivity extends AppCompatActivity {
    private RecyclerView categoryRecycler;
    private ExerciseCategoryAdapter categoryAdapter;
    private List<ExerciseCategoryModel> categoryList;
    private FirebaseFirestore firestore;
    private String petId, petType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_category);

        firestore = FirebaseFirestore.getInstance();
        categoryRecycler = findViewById(R.id.category_recycler);
        categoryRecycler.setLayoutManager(new LinearLayoutManager(this));

        categoryList = new ArrayList<>();

        // ✅ Get pet ID and pet type from Intent
        Intent intent = getIntent();
        petId = intent.getStringExtra("PET_ID");
        petType = intent.getStringExtra("PET_TYPE");

        // ✅ Fix: Pass all required parameters
        categoryAdapter = new ExerciseCategoryAdapter(this, categoryList, petId, petType);
        categoryRecycler.setAdapter(categoryAdapter);

        fetchExerciseCategories();
    }

    private void fetchExerciseCategories() {
        firestore.collection("ExerciseCategory")
                .whereEqualTo("type", petType)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (DocumentSnapshot doc : task.getResult()) {
                            categoryList.add(doc.toObject(ExerciseCategoryModel.class));
                        }
                        categoryAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "Failed to load exercises", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
