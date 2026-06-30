package com.campus.mall.dto.response;

import lombok.Data;

@Data
public class UserActivityResponse {
    private Integer activeUsers;
    private Integer newUsers;
    private Integer orderCount;
}
