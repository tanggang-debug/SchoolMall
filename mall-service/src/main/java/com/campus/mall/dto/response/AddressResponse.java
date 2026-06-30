package com.campus.mall.dto.response;

import lombok.Data;

@Data
public class AddressResponse {
    private Long id;
    private String receiverName;
    private String phone;
    private String province;
    private String city;
    private String district;
    private String detail;
    private Boolean isDefault;
}
