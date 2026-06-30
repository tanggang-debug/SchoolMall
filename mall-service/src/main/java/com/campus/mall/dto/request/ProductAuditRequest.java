package com.campus.mall.dto.request;

import javax.validation.constraints.*;
import lombok.Data;

@Data
public class ProductAuditRequest {
    @NotNull
    private Boolean approved;
    private String rejectReason;
}
