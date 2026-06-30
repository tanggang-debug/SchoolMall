package com.campus.mall.dto.request;

import java.math.BigDecimal;
import javax.validation.constraints.*;
import lombok.Data;

@Data
public class ProductRequest {
    @NotNull
    private Long categoryId;
    @NotBlank
    private String title;
    private String description;
    @NotNull
    private BigDecimal price;
    private BigDecimal originalPrice;
    @NotNull
    private Integer stock;
    private String images;
}
