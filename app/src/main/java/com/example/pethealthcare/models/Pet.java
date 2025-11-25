package com.example.pethealthcare.models;

public class Pet {
    private String id; // Add pet ID field

    private String name;
    private String imageUrl;
    private String type;

    public Pet(String id, String name, String imageUrl, String type) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
