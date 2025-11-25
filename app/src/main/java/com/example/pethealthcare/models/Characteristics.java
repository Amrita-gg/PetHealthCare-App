package com.example.pethealthcare.models;

import com.google.gson.annotations.SerializedName;

public class Characteristics {
    @SerializedName("lifespan")
    private String lifespan;

    @SerializedName("weight")
    private String weight;

    @SerializedName("height")
    private String height;

    public String getLifespan() { return lifespan; }
    public String getWeight() { return weight; }
    public String getHeight() { return height; }
}
