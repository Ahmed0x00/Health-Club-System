package com.healthclub.model;

public class Billing {
    private String id;
    private String memberId;
    private double amount;
    private String dateAdded;
    private String expiryDate;

    public Billing() {}

    public Billing(String id, String memberId, double amount, String dateAdded, String expiryDate) {
        this.id = id;
        this.memberId = memberId;
        this.amount = amount;
        this.dateAdded = dateAdded;
        this.expiryDate = expiryDate;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getMemberId() { return memberId; }
    public void setMemberId(String memberId) { this.memberId = memberId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getDateAdded() { return dateAdded; }
    public void setDateAdded(String dateAdded) { this.dateAdded = dateAdded; }

    public String getExpiryDate() { return expiryDate; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }
}