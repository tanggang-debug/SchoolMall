package com.campus.common.core.enums;

import lombok.Getter;

@Getter
public enum PaymentStatus {
    INIT(0, "init"),
    SUCCESS(1, "success"),
    FAILED(2, "failed"),
    REFUNDING(3, "refunding"),
    REFUNDED(4, "refunded");

    private final int code;
    private final String label;

    PaymentStatus(int code, String label) {
        this.code = code;
        this.label = label;
    }
}
