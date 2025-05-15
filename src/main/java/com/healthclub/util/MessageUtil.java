package com.healthclub.util;

import com.healthclub.model.Message;
import com.healthclub.model.Member;
import com.fasterxml.jackson.core.type.TypeReference;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MessageUtil {
    private static final String MESSAGE_FILE = "src/main/resources/data/messages.json";
    private static final TypeReference<List<Message>> TYPE = new TypeReference<>() {};

    public static List<Message> getMessagesBySender(String senderId) {
        Predicate<Message> filter = m -> m != null && m.getSenderId() != null && m.getSenderId().equals(senderId);
        return JsonUtil.getAll(MESSAGE_FILE, TYPE).stream()
                .filter(filter)
                .collect(Collectors.toList());
    }

    public static List<Message> getMessagesByRecipient(String recipientId) {
        Predicate<Message> filter = m -> m != null && m.getRecipientId() != null && m.getRecipientId().equals(recipientId);
        return JsonUtil.getAll(MESSAGE_FILE, TYPE).stream()
                .filter(filter)
                .collect(Collectors.toList());
    }

    public static boolean sendMessage(String senderId, String content) {
        if (content == null || content.trim().isEmpty() || senderId == null) {
            return false;
        }
        List<Member> members = MemberUtil.getAllMembers().stream()
                .filter(m -> senderId.equals(m.getCoachId()))
                .collect(Collectors.toList());
        if (members.isEmpty()) {
            return false;
        }
        String timestamp = LocalDateTime.now().toString();
        for (Member member : members) {
            Message message = new Message(
                    UUID.randomUUID().toString(),
                    senderId,
                    member.getId(),
                    content.trim(),
                    timestamp
            );
            JsonUtil.add(MESSAGE_FILE, message, TYPE);
        }
        return true;
    }

    public static void deleteMessagesByRecipient(String recipientId) {
        Predicate<Message> filter = m -> m != null && m.getRecipientId() != null && m.getRecipientId().equals(recipientId);
        JsonUtil.delete(MESSAGE_FILE, filter, TYPE);
    }
}