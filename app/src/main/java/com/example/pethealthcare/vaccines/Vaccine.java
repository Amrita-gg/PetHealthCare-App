package com.example.pethealthcare.vaccines;

import java.util.List;

public class Vaccine {
    private String id;
    private  String type;
    private String vaccineName;
    private String desc;

    public Vaccine() {
        // Required for Firebase
    }

    // Constructor for single description

    public Vaccine(String id, String type, String vaccineName, String desc) {
        this.id = id;
        this.type = type;
        this.vaccineName = vaccineName;
        this.desc = desc;
    }


    // Constructor for bullet point list

    public String getId() {
        return id;
    }

    public String getVaccineName() {
        return vaccineName;
    }

    public String getDesc() {
        return desc;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setVaccineName(String vaccineName) {
        this.vaccineName = vaccineName;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
