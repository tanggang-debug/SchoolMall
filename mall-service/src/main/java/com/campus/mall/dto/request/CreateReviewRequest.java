package com.campus.mall.dto.request;

import javax.validation.constraints.*;
import lombok.Data;

@Data
public class CreateReviewRequest {
    @NotNull
    private Long orderId;
    @NotNull
    private Long productId;
    @NotNull @Min(1) @Max(5)
    private Integer rating;
    private String content;
    private String images;
}
