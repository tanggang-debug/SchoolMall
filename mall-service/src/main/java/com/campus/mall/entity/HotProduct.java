package com.campus.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;

@Data
@TableName("hot_product")
public class HotProduct {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long productId;
    private Integer salesCount;
    private LocalDate reportDate;
}
