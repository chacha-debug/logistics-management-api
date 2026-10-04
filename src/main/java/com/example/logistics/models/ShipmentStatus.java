package com.example.logistics.models;

import java.util.List;
import java.util.Map;

/**
 * Valid status values and the allowed transitions between them.
 *
 * Pending → Confirmed → In Transit → Out for Delivery → Delivered
 * Any non-Delivered status → Cancelled
 */
public final class ShipmentStatus {

    public static final String PENDING = "Pending";
    public static final String CONFIRMED = "Confirmed";
    public static final String IN_TRANSIT = "In Transit";
    public static final String OUT_FOR_DELIVERY = "Out for Delivery";
    public static final String DELIVERED = "Delivered";
    public static final String CANCELLED = "Cancelled";

    public static final List<String> ALL = List.of(
            PENDING, CONFIRMED, IN_TRANSIT, OUT_FOR_DELIVERY, DELIVERED, CANCELLED
    );

    private static final Map<String, List<String>> TRANSITIONS = Map.of(
            PENDING, List.of(CONFIRMED, CANCELLED),
            CONFIRMED, List.of(IN_TRANSIT, CANCELLED),
            IN_TRANSIT, List.of(OUT_FOR_DELIVERY, CANCELLED),
            OUT_FOR_DELIVERY, List.of(DELIVERED, CANCELLED),
            DELIVERED, List.of(),   // terminal
            CANCELLED, List.of()    // terminal
    );

    private ShipmentStatus() {}

    public static boolean isValidStatus(String status) {
        return status != null && ALL.contains(status);
    }

    public static boolean isValidTransition(String from, String to) {
        if (!isValidStatus(from) || !isValidStatus(to)) {
            return false;
        }
        return TRANSITIONS.getOrDefault(from, List.of()).contains(to);
    }

    public static List<String> nextStatuses(String from) {
        return TRANSITIONS.getOrDefault(from, List.of());
    }
}