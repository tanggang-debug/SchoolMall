package com.campus.mall.dto.response;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class OrderItemResponse {
    private Long productId;
    private String productTitle;
    private String productImage;
    private BigDecimal price;
    private Integer quantity;
}
