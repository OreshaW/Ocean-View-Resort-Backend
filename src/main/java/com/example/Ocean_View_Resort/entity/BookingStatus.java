package com.example.Ocean_View_Resort.entity;

import java.util.Locale;

public enum BookingStatus {
    PENDING,
    CONFIRMED,
    CANCELLED,
    COMPLETED;

    public static BookingStatus fromString(String status) {
        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException("Status is required (PENDING, CONFIRMED, CANCELLED, COMPLETED)");
        }
        try {
            return BookingStatus.valueOf(status.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid status: " + status + " (allowed: PENDING, CONFIRMED, CANCELLED, COMPLETED)");
        }
    }
}

