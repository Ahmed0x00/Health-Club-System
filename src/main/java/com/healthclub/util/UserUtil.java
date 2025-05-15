package com.healthclub.util;

import com.healthclub.model.User;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;
import java.util.function.Predicate;

public class UserUtil {
    private static final String USER_FILE = "src/main/resources/data/users.json";
    private static final TypeReference<List<User>> TYPE = new TypeReference<>() {};
    private static User loggedInUser;

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static void setLoggedInUser(User user) {
        loggedInUser = user;
    }

    public static List<User> getAllUsers() {
        return JsonUtil.getAll(USER_FILE, TYPE);
    }

    public static User findByUsername(String username) {
        Predicate<User> predicate = u -> u.getUsername().equalsIgnoreCase(username);
        return JsonUtil.getOne(USER_FILE, TYPE, predicate);
    }

    public static void addUser(User user) {
        JsonUtil.add(USER_FILE, user, TYPE);
    }

    public static boolean updateUser(User user) {
        // Validate username and password
        if (user.getUsername() == null || user.getUsername().trim().isEmpty() ||
                user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            return false;
        }
        // Check for duplicate username (excluding current user)
        User existing = findByUsername(user.getUsername());
        if (existing != null && !existing.getId().equals(user.getId())) {
            return false;
        }
        JsonUtil.edit(USER_FILE, u -> u.getId().equals(user.getId()), user, TYPE);
        return true;
    }

    public static void deleteUserById(String id) {
        JsonUtil.delete(USER_FILE, u -> u.getId().equals(id), TYPE);
    }

    public static User getById(String id) {
        return JsonUtil.getOne(USER_FILE, TYPE, u -> u.getId().equals(id));
    }
}