package com.campus.mall.dto.response;

import lombok.Data;

@Data
public class HotProductResponse {
    private Long productId;
    private String title;
    private Integer salesCount;
}
