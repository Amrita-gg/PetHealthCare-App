package com.example.pethealthcare.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pethealthcare.ApiService;
import com.example.pethealthcare.R;
import com.example.pethealthcare.RetrofitClient;
import com.example.pethealthcare.models.AnimalResponse;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultActivity extends AppCompatActivity {
    private TextView tvAnimalResult;
    private View progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);


        tvAnimalResult = findViewById(R.id.tv_animal_result);
        progressBar = findViewById(R.id.progress_bar);

        String animalName = getIntent().getStringExtra("ANIMAL_NAME");
        fetchAnimalData(animalName);
    }

    private void fetchAnimalData(String animalName) {
        progressBar.setVisibility(View.VISIBLE);
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<List<AnimalResponse>> call = apiService.getAnimal(animalName);

        call.enqueue(new Callback<List<AnimalResponse>>() {
            @Override
            public void onResponse(Call<List<AnimalResponse>> call, Response<List<AnimalResponse>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    AnimalResponse animal = response.body().get(0);
                    String info = "Name: " + animal.getName() + "\n" +
                            "Lifespan: " + animal.getCharacteristics().getLifespan() + "\n" +
                            "Kingdom: " + animal.getTaxonomy().getKingdom();
                    tvAnimalResult.setText(info);
                } else {
                    tvAnimalResult.setText("No data found for " + animalName);
                }
            }

            @Override
            public void onFailure(Call<List<AnimalResponse>> call, Throwable t) {
                tvAnimalResult.setText("API Call Failed!");
                Log.e("API_ERROR", t.getMessage());
            }
        });
    }
}
