package com.healthclub.model;

public class Plan {
    private String id;
    private String coachId;
    private String date;
    private String workoutPlan;
    private String timeSlot;

    public Plan() {}

    public Plan(String id, String coachId, String date, String workoutPlan, String timeSlot) {
        this.id = id;
        this.coachId = coachId;
        this.date = date;
        this.workoutPlan = workoutPlan;
        this.timeSlot = timeSlot;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCoachId() { return coachId; }
    public void setCoachId(String coachId) { this.coachId = coachId; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getWorkoutPlan() { return workoutPlan; }
    public void setWorkoutPlan(String workoutPlan) { this.workoutPlan = workoutPlan; }

    public String getTimeSlot() { return timeSlot; }
    public void setTimeSlot(String timeSlot) { this.timeSlot = timeSlot; }
}