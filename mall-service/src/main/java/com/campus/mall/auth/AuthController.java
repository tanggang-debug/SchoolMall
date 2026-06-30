package com.campus.mall.auth;

import com.campus.common.core.result.Result;
import com.campus.common.security.RequireLogin;
import com.campus.mall.dto.request.LoginRequest;
import com.campus.mall.dto.request.RegisterRequest;
import com.campus.mall.dto.response.AuthResponse;
import com.campus.mall.dto.response.UserResponse;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public Result<AuthResponse> login(@Valid @RequestBody LoginRequest request,
                                      HttpServletRequest httpRequest) {
        return Result.ok(authService.login(request, httpRequest));
    }

    @PostMapping("/register")
    public Result<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return Result.ok(authService.register(request));
    }

    @GetMapping("/me")
    @RequireLogin
    public Result<UserResponse> me() {
        return Result.ok(authService.me());
    }

    @PostMapping("/logout")
    @RequireLogin
    public Result<Void> logout() {
        authService.logout();
        return Result.ok();
    }
}
