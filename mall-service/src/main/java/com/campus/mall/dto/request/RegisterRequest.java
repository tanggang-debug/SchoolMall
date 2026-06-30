package com.campus.mall.dto.request;

import javax.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank @Size(min=3,max=50)
    private String username;
    @NotBlank @Size(min=6,max=32)
    private String password;
    @Pattern(regexp="^1\\d{10}$")
    private String phone;
    private Integer role;
}
