package com.campus.mall.dto.request;

import javax.validation.constraints.*;
import lombok.Data;

@Data
public class ReviewReplyRequest {
    @NotBlank
    private String content;
}
