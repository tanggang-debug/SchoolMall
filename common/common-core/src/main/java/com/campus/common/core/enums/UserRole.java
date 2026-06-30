package com.campus.common.core.enums;

import lombok.Getter;

@Getter
public enum UserRole {
    STUDENT(0, "student"),
    TEACHER(1, "teacher"),
    MERCHANT(2, "merchant"),
    ADMIN(3, "admin");

    private final int code;
    private final String label;

    UserRole(int code, String label) {
        this.code = code;
        this.label = label;
    }

    public static UserRole of(int code) {
        for (UserRole role : values()) {
            if (role.code == code) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown role: " + code);
    }
}
