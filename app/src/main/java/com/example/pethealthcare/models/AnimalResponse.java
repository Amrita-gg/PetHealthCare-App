package com.example.pethealthcare.models;

import com.google.gson.annotations.SerializedName;

public class AnimalResponse {
    @SerializedName("name")
    private String name;

    @SerializedName("characteristics")
    private Characteristics characteristics;

    @SerializedName("taxonomy")
    private Taxonomy taxonomy;

    public String getName() { return name; }
    public Characteristics getCharacteristics() { return characteristics; }
    public Taxonomy getTaxonomy() { return taxonomy; }
}

