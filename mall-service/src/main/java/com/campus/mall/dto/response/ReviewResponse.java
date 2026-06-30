package com.campus.mall.dto.response;

import lombok.Data;

@Data
public class ReviewResponse {
    private Long id;
    private Long orderId;
    private Long productId;
    private Long userId;
    private String username;
    private Integer rating;
    private String content;
    private String images;
    private String createTime;
    private ReviewReplyResponse reply;
}
