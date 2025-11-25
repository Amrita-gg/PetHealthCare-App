package com.example.pethealthcare.models;

public class Vaccination {
    private String id, petId, name, date, nextDueDate, status, notes;

    public Vaccination() {
        // Required for Firebase
    }

    public Vaccination(String id, String petId, String name, String date, String nextDueDate, String status, String notes) {
        this.id = id;
        this.petId = petId;
        this.name = name;
        this.date = date;
        this.nextDueDate = nextDueDate;
        this.status = status;
        this.notes = notes;
    }

    public String getId() { return id; }
    public String getPetId() { return petId; }
    public String getName() { return name; }
    public String getDate() { return date; }
    public String getNextDueDate() { return nextDueDate; }
    public String getStatus() { return status; }
    public String getNotes() { return notes; }
}
