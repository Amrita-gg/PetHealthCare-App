package com.example.pethealthcare.models;

import com.google.gson.annotations.SerializedName;

public class Taxonomy {
    @SerializedName("kingdom")
    private String kingdom;

    @SerializedName("phylum")
    private String phylum;

    @SerializedName("class")
    private String classType;

    @SerializedName("breed")
    private String breed;

    public String getKingdom() { return kingdom; }
    public String getPhylum() { return phylum; }
    public String getClassType() { return classType; }

    public String getBreed(){return breed;}
}
