package com.campus.common.core.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {
    UNPAID(0, "unpaid"),
    PAID(1, "paid"),
    TO_SHIP(2, "to_ship"),
    SHIPPED(3, "shipped"),
    COMPLETED(4, "completed"),
    CANCELLED(5, "cancelled"),
    REFUNDING(6, "refunding");

    private final int code;
    private final String label;

    OrderStatus(int code, String label) {
        this.code = code;
        this.label = label;
    }
}
