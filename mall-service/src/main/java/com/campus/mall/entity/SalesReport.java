package com.campus.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@TableName("sales_report")
public class SalesReport {
    @TableId(type = IdType.AUTO)
    private Long id;
    private LocalDate reportDate;
    private BigDecimal totalAmount;
    private Integer orderCount;
}
