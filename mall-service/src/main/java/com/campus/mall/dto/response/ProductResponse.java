package com.campus.mall.dto.response;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class ProductResponse {
    private Long id;
    private Long merchantId;
    private Long categoryId;
    private String title;
    private String description;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private Integer stock;
    private String images;
    private Integer status;
    private String statusLabel;
    private Integer viewCount;
    private Integer salesCount;
    private String createTime;
}
