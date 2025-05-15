package com.healthclub.model;

public class Coach {
    private String id;
    private String name;
    private String phoneNumber;
    private String specialization;

    public Coach() {}

    public Coach(String id, String name, String phoneNumber, String specialization) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.specialization = specialization;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
}