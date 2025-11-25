package com.example.pethealthcare.models;

public class Meal {
    private String name;
    private int calories;

    public Meal(String name, int calories) {
        this.name = name;
        this.calories = calories;
    }

    public String getName() {
        return name;
    }

    public int getCalories() {
        return calories;
    }
}

