package com.campus.mall.dto.response;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class MerchantAnalyticsResponse {
    private BigDecimal totalSales;
    private Integer orderCount;
    private Integer productCount;
}
