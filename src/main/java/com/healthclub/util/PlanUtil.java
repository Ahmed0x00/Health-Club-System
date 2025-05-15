package com.healthclub.util;

import com.healthclub.model.Plan;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PlanUtil {
    private static final String PLAN_FILE = "src/main/resources/data/plans.json";
    private static final TypeReference<List<Plan>> TYPE = new TypeReference<>() {};

    public static List<Plan> getPlansByCoachId(String coachId) {
        Predicate<Plan> filter = p -> p != null && p.getCoachId() != null && p.getCoachId().equals(coachId);
        return JsonUtil.getAll(PLAN_FILE, TYPE).stream()
                .filter(filter)
                .collect(Collectors.toList());
    }

    public static Plan getPlanByCoachIdAndDate(String coachId, String date) {
        Predicate<Plan> filter = p -> p != null && p.getCoachId() != null && p.getCoachId().equals(coachId)
                && p.getDate() != null && p.getDate().equals(date);
        return JsonUtil.getOne(PLAN_FILE, TYPE, filter);
    }

    public static boolean savePlan(String coachId, String date, String workoutPlan, String timeSlot) {
        if (coachId == null || date == null || workoutPlan == null || workoutPlan.trim().isEmpty() ||
                timeSlot == null || timeSlot.trim().isEmpty()) {
            return false;
        }
        // Delete existing plan for the coach and date to avoid duplicates
        deletePlanByCoachIdAndDate(coachId, date);
        Plan plan = new Plan(
                UUID.randomUUID().toString(),
                coachId,
                date,
                workoutPlan.trim(),
                timeSlot.trim()
        );
        JsonUtil.add(PLAN_FILE, plan, TYPE);
        return true;
    }

    public static void deletePlanByCoachIdAndDate(String coachId, String date) {
        Predicate<Plan> filter = p -> p != null && p.getCoachId() != null && p.getCoachId().equals(coachId)
                && p.getDate() != null && p.getDate().equals(date);
        JsonUtil.delete(PLAN_FILE, filter, TYPE);
    }

    public static void deletePlansByCoachId(String coachId) {
        Predicate<Plan> filter = p -> p != null && p.getCoachId() != null && p.getCoachId().equals(coachId);
        JsonUtil.delete(PLAN_FILE, filter, TYPE);
    }
}