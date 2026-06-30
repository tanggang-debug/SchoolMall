package com.campus.mall.user;

import com.campus.common.core.result.Result;
import com.campus.common.security.RequireLogin;
import com.campus.common.security.RequireRole;
import com.campus.mall.dto.request.AddressRequest;
import com.campus.mall.dto.request.UpdateUserRequest;
import com.campus.mall.dto.request.UpdateUserStatusRequest;
import com.campus.mall.dto.response.AddressResponse;
import com.campus.mall.dto.response.PageResult;
import com.campus.mall.dto.response.UserResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AddressService addressService;

    @GetMapping("/{id}")
    @RequireLogin
    public Result<UserResponse> getById(@PathVariable Long id) {
        return Result.ok(userService.getById(id));
    }

    @PutMapping("/{id}")
    @RequireLogin
    public Result<UserResponse> update(@PathVariable Long id, @Valid @RequestBody UpdateUserRequest request) {
        return Result.ok(userService.update(id, request));
    }

    @GetMapping("/{id}/addresses")
    @RequireLogin
    public Result<List<AddressResponse>> listAddresses(@PathVariable Long id) {
        return Result.ok(addressService.listByUserId(id));
    }

    @PostMapping("/{id}/addresses")
    @RequireLogin
    public Result<AddressResponse> createAddress(@PathVariable Long id,
                                                 @Valid @RequestBody AddressRequest request) {
        return Result.ok(addressService.create(id, request));
    }

    @PutMapping("/{id}/addresses/{addrId}/default")
    @RequireLogin
    public Result<Void> setDefaultAddress(@PathVariable Long id, @PathVariable Long addrId) {
        addressService.setDefault(id, addrId);
        return Result.ok();
    }

    @GetMapping("/admin/list")
    @RequireRole(3)
    public Result<PageResult<UserResponse>> adminList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        return Result.ok(userService.adminList(page, size, keyword));
    }

    @PutMapping("/{id}/status")
    @RequireRole(3)
    public Result<Void> updateStatus(@PathVariable Long id,
                                     @Valid @RequestBody UpdateUserStatusRequest request) {
        userService.updateStatus(id, request.getStatus());
        return Result.ok();
    }
}
