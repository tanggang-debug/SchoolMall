package com.campus.mall.dto.response;

import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String username;
    private String phone;
    private String email;
    private String avatar;
    private Integer role;
    private Integer status;
    private String createTime;
}
