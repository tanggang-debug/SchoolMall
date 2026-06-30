package com.campus.mall.dto.request;

import javax.validation.constraints.*;
import lombok.Data;

@Data
public class AddressRequest {
    @NotBlank
    private String receiverName;
    @NotBlank
    private String phone;
    private String province;
    private String city;
    private String district;
    @NotBlank
    private String detail;
    private Boolean isDefault;
}
