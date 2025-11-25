package com.example.pethealthcare.models;

import com.google.firebase.Timestamp;
import java.util.List;

public class Mood {
    private String id; // Firestore document ID
    private String petName;
    private List<String> moodTypes;
    private String moodNotes;
    private Timestamp timestamp;

    public Mood() {
        // Empty constructor required for Firestore
    }

    public Mood(String id, String petName, List<String> moodTypes, String moodNotes, Timestamp timestamp) {
        this.id = id;
        this.petName = petName;
        this.moodTypes = moodTypes;
        this.moodNotes = moodNotes;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public String getPetName() {
        return petName;
    }

    public List<String> getMoodTypes() {
        return moodTypes;
    }

    public String getMoodNotes() {
        return moodNotes;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}
