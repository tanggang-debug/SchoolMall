package com.campus.mall.dto.response;

import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class OrderResponse {
    private Long id;
    private String orderNo;
    private Long userId;
    private Long merchantId;
    private BigDecimal totalAmount;
    private BigDecimal payAmount;
    private Integer status;
    private String statusLabel;
    private String addressSnapshot;
    private String remark;
    private String logisticsNo;
    private String createTime;
    private List<OrderItemResponse> items;
}
