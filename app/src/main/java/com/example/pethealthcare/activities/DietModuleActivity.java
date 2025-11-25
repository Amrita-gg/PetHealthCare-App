package com.example.pethealthcare.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pethealthcare.R;
import com.example.pethealthcare.adapters.MealAdapter;
import com.example.pethealthcare.activities.AddMealActivity;
import com.example.pethealthcare.models.Meal;

import java.util.ArrayList;
import java.util.List;

public class DietModuleActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MealAdapter mealAdapter;
    private List<Meal> mealList;
    private TextView nutritionSummary;
    private int totalCalories = 0; // Stores total calorie intake
    private final int dailyCalorieLimit = 500; // Daily recommended calorie intake

    private final ActivityResultLauncher<Intent> mealResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String mealName = result.getData().getStringExtra("MEAL_NAME");
                    int calories = result.getData().getIntExtra("CALORIES", 0);

                    mealList.add(new Meal(mealName, calories));
                    totalCalories += calories; // Update total calorie count
                    updateCalorieDisplay(); // Refresh TextView
                    mealAdapter.notifyDataSetChanged();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_module);

        recyclerView = findViewById(R.id.recycler_view_meals);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mealList = new ArrayList<>();
        mealAdapter = new MealAdapter(mealList);
        recyclerView.setAdapter(mealAdapter);

        nutritionSummary = findViewById(R.id.nutrition_summary);
        Button addMealButton = findViewById(R.id.add_meal_button);

        addMealButton.setOnClickListener(v -> {
            Intent intent = new Intent(DietModuleActivity.this, AddMealActivity.class);
            mealResultLauncher.launch(intent);
        });

        loadMeals();
    }

    private void loadMeals() {
        // Example meals
        mealList.add(new Meal("Chicken & Rice", 200));
        mealList.add(new Meal("Dry Kibble", 150));
        mealList.add(new Meal("Wet Food", 180));

        for (Meal meal : mealList) {
            totalCalories += meal.getCalories();
        }

        updateCalorieDisplay();
        mealAdapter.notifyDataSetChanged();
    }

    private void updateCalorieDisplay() {
        String summaryText = "Today's Intake: Calories " + totalCalories + " / " + dailyCalorieLimit;
        nutritionSummary.setText(summaryText);
    }
}