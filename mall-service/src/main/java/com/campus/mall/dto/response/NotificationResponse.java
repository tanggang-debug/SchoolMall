package com.campus.mall.dto.response;

import lombok.Data;

@Data
public class NotificationResponse {
    private Long id;
    private String title;
    private String content;
    private Integer status;
    private String createTime;
}
