package com.campus.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("review_reply")
public class ReviewReply {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long reviewId;
    private Long merchantId;
    private String content;
    private LocalDateTime createTime;
}
