package com.healthclub.util;

import com.healthclub.model.Member;
import com.healthclub.model.User;
import com.fasterxml.jackson.core.type.TypeReference;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;

public class MemberUtil {
    private static final String MEMBER_FILE = "src/main/resources/data/members.json";
    private static final TypeReference<List<Member>> TYPE = new TypeReference<>() {};

    public static List<Member> getAllMembers() {
        return JsonUtil.getAll(MEMBER_FILE, TYPE);
    }

    public static boolean addMember(Member member, String username, String password) {
        // Validate username, password, coachId, and expiryDate
        if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty() ||
                member.getCoachId() == null || member.getCoachId().trim().isEmpty() ||
                member.getExpiryDate() == null || member.getExpiryDate().trim().isEmpty()) {
            return false;
        }
        // Validate expiryDate is in the future
        try {
            LocalDate expiry = LocalDate.parse(member.getExpiryDate());
            if (expiry.isBefore(LocalDate.now())) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        // Check for duplicate username
        User existingUser = UserUtil.findByUsername(username);
        if (existingUser != null) {
            return false;
        }
        // Add to members.json
        JsonUtil.add(MEMBER_FILE, member, TYPE);
        // Add to users.json
        User user = new User(member.getId(), username, password, User.Role.MEMBER);
        UserUtil.addUser(user);
        // Create billing record
        BillingUtil.addBilling(member.getId(), member.getExpiryDate());
        return true;
    }

    public static boolean updateMember(Member member, String username) {
        // Validate username, coachId, and expiryDate
        if (username == null || username.trim().isEmpty() ||
                member.getCoachId() == null || member.getCoachId().trim().isEmpty() ||
                member.getExpiryDate() == null || member.getExpiryDate().trim().isEmpty()) {
            return false;
        }
        // Validate expiryDate is in the future
        try {
            LocalDate expiry = LocalDate.parse(member.getExpiryDate());
            if (expiry.isBefore(LocalDate.now())) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        // Check for duplicate username (excluding current user)
        User existingUser = UserUtil.findByUsername(username);
        if (existingUser != null && !existingUser.getId().equals(member.getId())) {
            return false;
        }
        // Update members.json
        JsonUtil.edit(MEMBER_FILE, m -> m.getId().equals(member.getId()), member, TYPE);
        // Update users.json
        User user = UserUtil.getById(member.getId());
        if (user != null) {
            user.setUsername(username);
            UserUtil.updateUser(user);
        }
        // Update or create billing record
        BillingUtil.addBilling(member.getId(), member.getExpiryDate());
        return true;
    }

    public static void deleteMemberById(String id) {
        JsonUtil.delete(MEMBER_FILE, m -> m.getId().equals(id), TYPE);
        UserUtil.deleteUserById(id);
        BillingUtil.deleteBilling(id);
        NotificationUtil.deleteByMemberId(id);
    }

    public static Member getById(String id) {
        return JsonUtil.getOne(MEMBER_FILE, TYPE, m -> m.getId().equals(id));
    }

    public static void checkExpiries() {
        LocalDate today = LocalDate.now();
        LocalDate threshold = today.plusDays(7);
        List<Member> members = getAllMembers();

        for (Member member : members) {
            if (member.getExpiryDate() == null) continue;
            try {
                LocalDate expiry = LocalDate.parse(member.getExpiryDate());
                if (expiry.isBefore(today)) {
                    NotificationUtil.addNotification(
                            member.getId(),
                            "Subscription for " + member.getName() + " expired on " + expiry
                    );
                } else if (!expiry.isAfter(threshold)) {
                    NotificationUtil.addNotification(
                            member.getId(),
                            "Subscription for " + member.getName() + " expires on " + expiry
                    );
                }
            } catch (Exception e) {
                LoggerUtil.getLogger().warning("Invalid expiry date for member: " + member.getId());
            }
        }
    }
}