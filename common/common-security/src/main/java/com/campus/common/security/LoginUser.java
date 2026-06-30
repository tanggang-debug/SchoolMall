package com.campus.common.security;

import lombok.Data;

@Data
public class LoginUser {
    private Long userId;
    private String username;
    private int role;
}
