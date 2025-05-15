package com.healthclub.model;

public class Member {
    private String id;
    private String name;
    private String phoneNumber;
    private String email;
    private String coachId;
    private String expiryDate;

    public Member() {}

    public Member(String id, String name, String phoneNumber, String email, String coachId, String expiryDate) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.coachId = coachId;
        this.expiryDate = expiryDate;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCoachId() { return coachId; }
    public void setCoachId(String coachId) { this.coachId = coachId; }

    public String getExpiryDate() { return expiryDate; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }
}