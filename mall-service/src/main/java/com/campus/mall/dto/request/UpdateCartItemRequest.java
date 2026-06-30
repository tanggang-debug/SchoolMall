package com.campus.mall.dto.request;

import javax.validation.constraints.*;
import lombok.Data;

@Data
public class UpdateCartItemRequest {
    @NotNull @Min(1)
    private Integer quantity;
    private Boolean selected;
}
