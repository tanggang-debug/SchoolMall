package com.campus.mall.dto.response;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private Long userId;
    private Integer role;
    private String username;
}
