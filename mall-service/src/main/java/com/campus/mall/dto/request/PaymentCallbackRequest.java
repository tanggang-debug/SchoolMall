package com.campus.mall.dto.request;

import lombok.Data;
import javax.validation.constraints.*;

@Data
public class PaymentCallbackRequest {
    @NotBlank
    private String paymentNo;
    @NotBlank
    private String callbackNo;
    private String orderNo;
    private String tradeNo;
    private String callbackData;
    private Boolean success;
}
