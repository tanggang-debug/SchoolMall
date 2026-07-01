package com.campus.mall.auth;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.common.core.constant.ErrorCode;
import com.campus.common.core.enums.UserRole;
import com.campus.common.core.exception.BusinessException;
import com.campus.common.security.JwtTokenProvider;
import com.campus.mall.dto.request.LoginRequest;
import com.campus.mall.dto.request.RegisterRequest;
import com.campus.mall.dto.response.AuthResponse;
import com.campus.mall.entity.LoginLog;
import com.campus.mall.entity.User;
import com.campus.mall.mapper.LoginLogMapper;
import com.campus.mall.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * 认证服务单元测试
 * 测试编号：UT-AUTH-001 ~ UT-AUTH-003
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("认证服务单元测试")
class AuthServiceTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private LoginLogMapper loginLogMapper;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private HttpServletRequest httpRequest;

    @InjectMocks
    private AuthService authService;

    private User testUser;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword(passwordEncoder.encode("password123"));
        testUser.setRole(UserRole.STUDENT.getCode());
        testUser.setStatus(0);
    }

    // ==================== 登录测试 ====================

    @Test
    @DisplayName("UT-AUTH-001: 登录密码正确返回token和用户信息")
    void testLogin_Success() {
        // 准备数据
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("password123");

        lenient().when(httpRequest.getRemoteAddr()).thenReturn("127.0.0.1");
        when(userMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(testUser);
        when(loginLogMapper.insert(any(LoginLog.class))).thenReturn(1);
        when(jwtTokenProvider.createToken(1L, "testuser", 0)).thenReturn("test-token");

        // 执行
        AuthResponse response = authService.login(request, httpRequest);

        // 验证
        assertNotNull(response);
        assertEquals("test-token", response.getToken());
        assertEquals(1L, response.getUserId());
        assertEquals("testuser", response.getUsername());
        assertEquals(0, response.getRole());

        // 验证登录日志插入成功记录
        verify(loginLogMapper, times(1)).insert(any(LoginLog.class));
    }

    @Test
    @DisplayName("UT-AUTH-002: 账号被禁用抛出异常")
    void testLogin_AccountDisabled() {
        // 准备数据
        testUser.setStatus(1); // 禁用状态
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("password123");

        lenient().when(httpRequest.getRemoteAddr()).thenReturn("127.0.0.1");
        when(userMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(testUser);
        when(loginLogMapper.insert(any(LoginLog.class))).thenReturn(1);

        // 执行并验证
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            authService.login(request, httpRequest);
        });
        assertEquals(ErrorCode.FORBIDDEN, exception.getCode());

        // 验证登录日志插入失败记录
        verify(loginLogMapper, times(1)).insert(any(LoginLog.class));
    }

    @Test
    @DisplayName("UT-AUTH-001-2: 用户名不存在抛出异常")
    void testLogin_UserNotFound() {
        // 准备数据
        LoginRequest request = new LoginRequest();
        request.setUsername("nonexistent");
        request.setPassword("password123");

        lenient().when(httpRequest.getRemoteAddr()).thenReturn("127.0.0.1");
        when(userMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(null);
        when(loginLogMapper.insert(any(LoginLog.class))).thenReturn(1);

        // 执行并验证
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            authService.login(request, httpRequest);
        });
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getCode());
    }

    @Test
    @DisplayName("UT-AUTH-001-3: 密码错误抛出异常")
    void testLogin_PasswordError() {
        // 准备数据
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("wrongpassword");

        lenient().when(httpRequest.getRemoteAddr()).thenReturn("127.0.0.1");
        when(userMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(testUser);
        when(loginLogMapper.insert(any(LoginLog.class))).thenReturn(1);

        // 执行并验证
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            authService.login(request, httpRequest);
        });
        assertEquals(ErrorCode.PASSWORD_ERROR, exception.getCode());
    }

    // ==================== 注册测试 ====================

    @Test
    @DisplayName("UT-AUTH-003: 用户注册成功返回token和新用户信息")
    void testRegister_Success() {
        // 准备数据
        RegisterRequest request = new RegisterRequest();
        request.setUsername("newuser");
        request.setPassword("password123");
        request.setPhone("13800138000");

        when(userMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(0L);
        // 使用 Answer 来模拟 insert 后自动填充 ID
        when(userMapper.insert(any(User.class))).thenAnswer((Answer<Integer>) invocation -> {
            User user = invocation.getArgument(0);
            user.setId(2L); // 模拟 MyBatis Plus 自动填充 ID
            return 1;
        });
        when(jwtTokenProvider.createToken(anyLong(), anyString(), anyInt())).thenReturn("new-token");

        // 执行
        AuthResponse response = authService.register(request);

        // 验证
        assertNotNull(response);
        assertEquals("new-token", response.getToken());
        assertEquals("newuser", response.getUsername());
        assertEquals(UserRole.STUDENT.getCode(), response.getRole());

        // 验证用户插入
        verify(userMapper, times(1)).insert(any(User.class));
    }

    @Test
    @DisplayName("UT-AUTH-003-2: 用户名已存在抛出异常")
    void testRegister_UsernameExists() {
        // 准备数据
        RegisterRequest request = new RegisterRequest();
        request.setUsername("existinguser");
        request.setPassword("password123");

        when(userMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(1L);

        // 执行并验证
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            authService.register(request);
        });
        assertEquals(ErrorCode.FORBIDDEN, exception.getCode());

        // 验证不插入用户
        verify(userMapper, never()).insert(any(User.class));
    }

    @Test
    @DisplayName("UT-AUTH-003-3: 注册指定角色成功")
    void testRegister_WithRole() {
        // 准备数据
        RegisterRequest request = new RegisterRequest();
        request.setUsername("merchant");
        request.setPassword("password123");
        request.setRole(UserRole.MERCHANT.getCode());

        when(userMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(0L);
        when(userMapper.insert(any(User.class))).thenAnswer((Answer<Integer>) invocation -> {
            User user = invocation.getArgument(0);
            user.setId(3L);
            return 1;
        });
        when(jwtTokenProvider.createToken(anyLong(), anyString(), anyInt())).thenReturn("merchant-token");

        // 执行
        AuthResponse response = authService.register(request);

        // 验证
        assertNotNull(response);
        assertEquals(UserRole.MERCHANT.getCode(), response.getRole());
    }
}