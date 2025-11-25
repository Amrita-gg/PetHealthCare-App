package com.example.pethealthcare.models;

import android.os.Parcel;
import android.os.Parcelable;

public class PetProfile implements Parcelable {
    private String id, name, age, breed, type, healthStatus, parentExperience, additionalNotes, imageUrl;

    // Constructor
    public PetProfile(String id, String name, String age, String breed, String type, String healthStatus, String parentExperience, String additionalNotes, String imageUrl) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.breed = breed;
        this.type = type;
        this.healthStatus = healthStatus;
        this.parentExperience = parentExperience;
        this.additionalNotes = additionalNotes;
        this.imageUrl = imageUrl;
    }

    // Parcelable implementation
    protected PetProfile(Parcel in) {
        id = in.readString();
        name = in.readString();
        age = in.readString();
        breed = in.readString();
        type = in.readString();
        healthStatus = in.readString();
        parentExperience = in.readString();
        additionalNotes = in.readString();
        imageUrl = in.readString();
    }

    public static final Creator<PetProfile> CREATOR = new Creator<PetProfile>() {
        @Override
        public PetProfile createFromParcel(Parcel in) {
            return new PetProfile(in);
        }

        @Override
        public PetProfile[] newArray(int size) {
            return new PetProfile[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(age);
        dest.writeString(breed);
        dest.writeString(type);
        dest.writeString(healthStatus);
        dest.writeString(parentExperience);
        dest.writeString(additionalNotes);
        dest.writeString(imageUrl);
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getAge() { return age; }
    public String getBreed() { return breed; }
    public String getType() { return type; }
    public String getHealthStatus() { return healthStatus; }
    public String getParentExperience() { return parentExperience; }
    public String getAdditionalNotes() { return additionalNotes; }
    public String getImageUrl() { return imageUrl; }
}
