package com.campus.mall.dto.request;

import javax.validation.constraints.*;
import lombok.Data;

@Data
public class UpdateUserRequest {
    private String phone;
    private String email;
    private String avatar;
}
