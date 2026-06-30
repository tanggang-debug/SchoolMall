package com.campus.mall.dto.request;

import javax.validation.constraints.*;
import lombok.Data;

@Data
public class UpdateUserStatusRequest {
    @NotNull
    private Integer status;
}
