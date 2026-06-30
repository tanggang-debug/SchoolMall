package com.campus.mall.dto.request;

import javax.validation.constraints.*;
import lombok.Data;

@Data
public class ShipOrderRequest {
    @NotBlank
    private String logisticsNo;
}
