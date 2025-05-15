package com.healthclub.model;

public class Notification {
    private String id;
    private String memberId;
    private String message;
    private String date;

    public Notification() {}

    public Notification(String id, String memberId, String message, String date) {
        this.id = id;
        this.memberId = memberId;
        this.message = message;
        this.date = date;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getMemberId() { return memberId; }
    public void setMemberId(String memberId) { this.memberId = memberId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}