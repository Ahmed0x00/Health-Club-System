package com.healthclub.util;

import com.healthclub.model.Coach;
import com.healthclub.model.User;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;
import java.util.function.Predicate;

public class CoachUtil {
    private static final String COACH_FILE = "src/main/resources/data/coaches.json";
    private static final TypeReference<List<Coach>> TYPE = new TypeReference<>() {};

    public static List<Coach> getAllCoaches() {
        return JsonUtil.getAll(COACH_FILE, TYPE);
    }

    public static boolean addCoach(Coach coach, String username, String password) {
        // Validate username and password
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return false;
        }
        // Check for duplicate username
        User existingUser = UserUtil.findByUsername(username);
        if (existingUser != null) {
            return false;
        }
        // Add to coaches.json
        JsonUtil.add(COACH_FILE, coach, TYPE);
        // Add to users.json
        User user = new User(coach.getId(), username, password, User.Role.COACH);
        UserUtil.addUser(user);
        return true;
    }

    public static boolean updateCoach(Coach coach, String username) {
        // Validate username
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        // Check for duplicate username (excluding current user)
        User existingUser = UserUtil.findByUsername(username);
        if (existingUser != null && !existingUser.getId().equals(coach.getId())) {
            return false;
        }
        // Update coaches.json
        JsonUtil.edit(COACH_FILE, c -> c.getId().equals(coach.getId()), coach, TYPE);
        // Update users.json
        User user = UserUtil.getById(coach.getId());
        if (user != null) {
            user.setUsername(username);
            UserUtil.updateUser(user);
        }
        return true;
    }

    public static void deleteCoachById(String id) {
        JsonUtil.delete(COACH_FILE, c -> c.getId().equals(id), TYPE);
        UserUtil.deleteUserById(id);
    }

    public static Coach getById(String id) {
        return JsonUtil.getOne(COACH_FILE, TYPE, c -> c.getId().equals(id));
    }
}