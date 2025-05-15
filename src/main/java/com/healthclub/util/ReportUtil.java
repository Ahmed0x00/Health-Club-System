package com.healthclub.util;

import com.healthclub.model.Member;
import com.healthclub.model.Billing;
import com.healthclub.model.Notification;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ReportUtil {
    public static List<Member> generateMemberSummary() {
        return MemberUtil.getAllMembers();
    }

    public static List<Member> generateExpiringSubscriptions(int days) {
        LocalDate today = LocalDate.now();
        LocalDate threshold = today.plusDays(days);
        return MemberUtil.getAllMembers().stream()
                .filter(m -> {
                    try {
                        LocalDate expiry = LocalDate.parse(m.getExpiryDate());
                        return !expiry.isBefore(today) && !expiry.isAfter(threshold);
                    } catch (Exception e) {
                        LoggerUtil.getLogger().warning("Invalid expiry date for member: " + m.getId());
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }

    public static List<Billing> generateBillingSummary() {
        return BillingUtil.getAllBillingRecords();
    }

    public static String exportToCsv(List<?> data, String reportType) {
        StringBuilder csv = new StringBuilder();
        if (reportType.equals("Member Summary")) {
            csv.append("ID,Name,Email,Coach Name,Expiry Date\n");
            for (Object obj : data) {
                Member m = (Member) obj;
                csv.append(String.format("%s,%s,%s,%s,%s\n",
                        m.getId(), m.getName(), m.getEmail(), m.getName(), m.getExpiryDate()));
            }
        } else if (reportType.equals("Expiring Subscriptions")) {
            csv.append("ID,Name,Email,Coach Name,Expiry Date\n");
            for (Object obj : data) {
                Member m = (Member) obj;
                csv.append(String.format("%s,%s,%s,%s,%s\n",
                        m.getId(), m.getName(), m.getEmail(), m.getName(), m.getExpiryDate()));
            }
        } else if (reportType.equals("Billing Summary")) {
            csv.append("Member ID,Member Name,Amount,Subscription Date,Expiry Date\n");
            for (Object obj : data) {
                Billing b = (Billing) obj;
                Member m = MemberUtil.getById(b.getMemberId());
                String name = m != null ? m.getName() : "Unknown";
                csv.append(String.format("%s,%s,%.2f,%s,%s\n",
                        b.getMemberId(), name, b.getAmount(), b.getDateAdded(), b.getExpiryDate()));
            }
        }
        return csv.toString();
    }
}