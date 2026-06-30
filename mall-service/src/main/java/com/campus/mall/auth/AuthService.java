package com.campus.mall.auth;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.common.core.constant.ErrorCode;
import com.campus.common.core.enums.UserRole;
import com.campus.common.core.exception.BusinessException;
import com.campus.common.security.JwtTokenProvider;
import com.campus.common.security.UserContext;
import com.campus.mall.dto.request.LoginRequest;
import com.campus.mall.dto.request.RegisterRequest;
import com.campus.mall.dto.response.AuthResponse;
import com.campus.mall.dto.response.UserResponse;
import com.campus.mall.entity.LoginLog;
import com.campus.mall.entity.User;
import com.campus.mall.mapper.LoginLogMapper;
import com.campus.mall.mapper.UserMapper;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserMapper userMapper;
    private final LoginLogMapper loginLogMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public AuthResponse login(LoginRequest request, HttpServletRequest httpRequest) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, request.getUsername()));
        LoginLog log = new LoginLog();
        log.setLoginTime(LocalDateTime.now());
        log.setIp(httpRequest.getRemoteAddr());
        if (user == null) {
            log.setResult(0);
            loginLogMapper.insert(log);
            throw new BusinessException(ErrorCode.USER_NOT_FOUND, "Error");
        }
        log.setUserId(user.getId());
        if (user.getStatus() != null && user.getStatus() == 1) {
            log.setResult(0);
            loginLogMapper.insert(log);
            throw new BusinessException(ErrorCode.FORBIDDEN, "Error");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.setResult(0);
            loginLogMapper.insert(log);
            throw new BusinessException(ErrorCode.PASSWORD_ERROR, "密码错误");
        }
        log.setResult(1);
        loginLogMapper.insert(log);
        String token = jwtTokenProvider.createToken(user.getId(), user.getUsername(), user.getRole());
        AuthResponse resp = new AuthResponse();
        resp.setToken(token);
        resp.setUserId(user.getId());
        resp.setRole(user.getRole());
        resp.setUsername(user.getUsername());
        return resp;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        Long count = userMapper.selectCount(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, request.getUsername()));
        if (count > 0) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "用户名已存在");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setRole(request.getRole() != null ? request.getRole() : UserRole.STUDENT.getCode());
        user.setStatus(0);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userMapper.insert(user);
        String token = jwtTokenProvider.createToken(user.getId(), user.getUsername(), user.getRole());
        AuthResponse resp = new AuthResponse();
        resp.setToken(token);
        resp.setUserId(user.getId());
        resp.setRole(user.getRole());
        resp.setUsername(user.getUsername());
        return resp;
    }

    public UserResponse me() {
        Long userId = UserContext.getUserId();
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND, "Error");
        }
        return toResponse(user);
    }

    public void logout() {
        // JWT 无状态，客户端丢�?token 即可
    }

    private UserResponse toResponse(User user) {
        UserResponse resp = new UserResponse();
        resp.setId(user.getId());
        resp.setUsername(user.getUsername());
        resp.setPhone(user.getPhone());
        resp.setEmail(user.getEmail());
        resp.setAvatar(user.getAvatar());
        resp.setRole(user.getRole());
        resp.setStatus(user.getStatus());
        resp.setCreateTime(user.getCreateTime() == null ? null : user.getCreateTime().toString());
        return resp;
    }
}
