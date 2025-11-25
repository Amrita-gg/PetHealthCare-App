package com.example.pethealthcare.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pethealthcare.R;

public class AddMealActivity extends AppCompatActivity {

    private EditText mealNameInput, caloriesInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal);

        mealNameInput = findViewById(R.id.meal_name_input);
        caloriesInput = findViewById(R.id.calories_input);
        Button saveMealButton = findViewById(R.id.save_meal_button);

        saveMealButton.setOnClickListener(v -> {
            String mealName = mealNameInput.getText().toString();
            String caloriesStr = caloriesInput.getText().toString();

            if (!mealName.isEmpty() && !caloriesStr.isEmpty()) {
                int calories = Integer.parseInt(caloriesStr);

                // Send meal data back
                Intent resultIntent = new Intent();
                resultIntent.putExtra("MEAL_NAME", mealName);
                resultIntent.putExtra("CALORIES", calories);
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                Toast.makeText(this, "Please enter all details", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
