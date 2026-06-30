package com.campus.mall.dto.request;

import lombok.Data;
import javax.validation.constraints.*;
import java.util.List;

@Data
public class CreateOrderRequest {
    @NotNull
    private Long addressId;
    private List<Long> cartItemIds;
    private List<Long> productIds;
    private List<OrderItemRequest> items;
    private String remark;
}
