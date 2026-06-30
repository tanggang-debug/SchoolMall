package com.campus.mall.dto.response;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class CartItemResponse {
    private Long productId;
    private String title;
    private String image;
    private BigDecimal price;
    private Integer quantity;
    private Boolean selected;
    private Integer stock;
}
