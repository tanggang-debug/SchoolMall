package com.campus.mall.dto.response;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class PaymentResponse {
    private Long id;
    private String paymentNo;
    private String orderNo;
    private BigDecimal amount;
    private Integer status;
    private String qrCode;
    private String expireTime;
}
