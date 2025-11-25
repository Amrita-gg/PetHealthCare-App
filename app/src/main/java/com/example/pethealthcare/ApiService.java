package com.example.pethealthcare;

import com.example.pethealthcare.models.AnimalResponse;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface ApiService {
    @Headers("X-Api-Key: 8KlMdcGr+M4B4O4MIsRP9A==c53wOdMzWPFYeXxz") // Replace with your actual API key
    @GET("animals")
    Call<List<AnimalResponse>> getAnimal(@Query("name") String name);
}

