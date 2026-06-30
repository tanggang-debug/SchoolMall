package com.campus.common.core.enums;

import lombok.Getter;

@Getter
public enum ProductStatus {
    PENDING(0, "pending"),
    ON_SALE(1, "on_sale"),
    OFF_SHELF(2, "off_shelf"),
    REJECTED(3, "rejected");

    private final int code;
    private final String label;

    ProductStatus(int code, String label) {
        this.code = code;
        this.label = label;
    }
}
