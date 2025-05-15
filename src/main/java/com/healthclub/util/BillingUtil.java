package com.healthclub.util;

import com.healthclub.model.Billing;
import com.fasterxml.jackson.core.type.TypeReference;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

public class BillingUtil {
    private static final String BILLING_FILE = "src/main/resources/data/billing.json";
    private static final TypeReference<List<Billing>> TYPE = new TypeReference<>() {};

    public static List<Billing> getAllBillingRecords() {
        return JsonUtil.getAll(BILLING_FILE, TYPE);
    }

    public static void addBilling(String memberId, String expiryDate) {
        // Calculate amount based on duration
        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.parse(expiryDate);
        long months = ChronoUnit.MONTHS.between(start, end);
        double amount = months <= 1 ? 50.0 : months <= 3 ? 120.0 : 400.0;

        // Delete existing billing record for member, if any
        deleteBilling(memberId);

        Billing billing = new Billing(
                UUID.randomUUID().toString(),
                memberId,
                amount,
                LocalDate.now().toString(),
                expiryDate
        );
        JsonUtil.add(BILLING_FILE, billing, TYPE);
    }

    public static void updateBilling(Billing billing) {
        JsonUtil.edit(BILLING_FILE, b -> b.getId().equals(billing.getId()), billing, TYPE);
    }

    public static void deleteBilling(String memberId) {
        JsonUtil.delete(BILLING_FILE, b -> b.getMemberId().equals(memberId), TYPE);
    }

    public static Billing getByMemberId(String memberId) {
        return JsonUtil.getOne(BILLING_FILE, TYPE, b -> b.getMemberId().equals(memberId));
    }
}