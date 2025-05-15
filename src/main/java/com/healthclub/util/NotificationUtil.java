package com.healthclub.util;

import com.healthclub.model.Notification;
import com.fasterxml.jackson.core.type.TypeReference;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class NotificationUtil {
    private static final String NOTIFICATION_FILE = "src/main/resources/data/notifications.json";
    private static final TypeReference<List<Notification>> TYPE = new TypeReference<>() {};

    public static List<Notification> getAllNotifications() {
        return JsonUtil.getAll(NOTIFICATION_FILE, TYPE);
    }

    public static List<Notification> getByMemberId(String memberId) {
        Predicate<Notification> filter = n -> n != null && n.getMemberId() != null && n.getMemberId().equals(memberId);
        Notification notification = JsonUtil.getOne(NOTIFICATION_FILE, TYPE, filter);
        return notification != null ? List.of(notification) : List.of();
    }

    public static void addNotification(String memberId, String message) {
        // Delete existing notifications for the member to avoid duplicates
        deleteByMemberId(memberId);
        Notification notification = new Notification(
                UUID.randomUUID().toString(),
                memberId,
                message,
                LocalDate.now().toString()
        );
        JsonUtil.add(NOTIFICATION_FILE, notification, TYPE);
    }

    public static void deleteByMemberId(String memberId) {
        Predicate<Notification> filter = n -> n != null && n.getMemberId() != null && n.getMemberId().equals(memberId);
        JsonUtil.delete(NOTIFICATION_FILE, filter, TYPE);
    }
}