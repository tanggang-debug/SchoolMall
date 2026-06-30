package com.campus.mall.dto.response;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class SalesReportResponse {
    private String date;
    private BigDecimal totalAmount;
    private Integer orderCount;
}
