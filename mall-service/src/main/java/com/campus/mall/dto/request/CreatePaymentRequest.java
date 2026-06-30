package com.campus.mall.dto.request;

import javax.validation.constraints.*;
import lombok.Data;

@Data
public class CreatePaymentRequest {
    @NotBlank
    private String orderNo;
    @NotNull
    private Integer channel;
}
