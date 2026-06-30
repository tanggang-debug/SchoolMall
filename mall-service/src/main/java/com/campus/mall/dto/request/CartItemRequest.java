package com.campus.mall.dto.request;

import javax.validation.constraints.*;
import lombok.Data;

@Data
public class CartItemRequest {
    @NotNull
    private Long productId;
    @NotNull @Min(1)
    private Integer quantity;
    private Boolean selected;
}
