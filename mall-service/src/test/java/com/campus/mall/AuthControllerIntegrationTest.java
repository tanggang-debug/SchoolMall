package com.campus.mall;

import com.campus.common.core.result.Result;
import com.campus.mall.auth.AuthController;
import com.campus.mall.auth.AuthService;
import com.campus.mall.dto.request.LoginRequest;
import com.campus.mall.dto.request.RegisterRequest;
import com.campus.mall.dto.response.AuthResponse;
import com.campus.mall.dto.response.UserResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 认证控制器接口集成测试
 * 测试编号：IT-AUTH-001 ~ IT-AUTH-003
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("认证控制器接口集成测试")
class AuthControllerIntegrationTest {

    @Mock
    private AuthService authService;

    @Mock
    private HttpServletRequest httpRequest;

    @InjectMocks
    private AuthController authController;

    // ==================== 登录接口测试 ====================

    @Test
    @DisplayName("IT-AUTH-001: 用户登录成功返回token")
    void testLogin_Success() {
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("password123");

        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken("test-token");
        authResponse.setUserId(1L);
        authResponse.setUsername("testuser");
        authResponse.setRole(0);

        when(authService.login(any(LoginRequest.class), any(HttpServletRequest.class)))
                .thenReturn(authResponse);

        Result<AuthResponse> result = authController.login(request, httpRequest);

        assertNotNull(result);
        assertEquals(200, result.getCode()); // Result.ok() 返回 code=200
        assertNotNull(result.getData());
        assertEquals("test-token", result.getData().getToken());
        assertEquals(1L, result.getData().getUserId());
    }

    @Test
    @DisplayName("IT-AUTH-002: 用户注册成功返回token")
    void testRegister_Success() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("newuser");
        request.setPassword("password123");
        request.setPhone("13800138000");

        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken("new-token");
        authResponse.setUserId(2L);
        authResponse.setUsername("newuser");
        authResponse.setRole(0);

        when(authService.register(any(RegisterRequest.class))).thenReturn(authResponse);

        Result<AuthResponse> result = authController.register(request);

        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertNotNull(result.getData());
        assertEquals("new-token", result.getData().getToken());
    }

    @Test
    @DisplayName("IT-AUTH-003: 获取当前用户信息成功")
    void testMe_Success() {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setUsername("testuser");
        userResponse.setRole(0);

        when(authService.me()).thenReturn(userResponse);

        Result<UserResponse> result = authController.me();

        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals(1L, result.getData().getId());
    }

    @Test
    @DisplayName("IT-AUTH-004: 用户登出成功")
    void testLogout_Success() {
        doNothing().when(authService).logout();

        Result<Void> result = authController.logout();

        assertNotNull(result);
        assertEquals(200, result.getCode());
        verify(authService, times(1)).logout();
    }
}