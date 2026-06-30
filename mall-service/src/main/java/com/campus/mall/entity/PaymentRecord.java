package com.campus.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("payment_record")
public class PaymentRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String orderNo;
    private String paymentNo;
    private BigDecimal amount;
    private Integer channel;
    private Integer status;
    private String tradeNo;
    private String callbackNo;
    private String callbackData;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
