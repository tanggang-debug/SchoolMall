package com.campus.mall.user;

import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.common.core.constant.ErrorCode;
import com.campus.common.core.exception.BusinessException;
import com.campus.common.security.UserContext;
import com.campus.mall.dto.request.UpdateUserRequest;
import com.campus.mall.dto.response.PageResult;
import com.campus.mall.dto.response.UserResponse;
import com.campus.mall.entity.User;
import com.campus.mall.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;

    public UserResponse getById(Long id) {
        User user = requireUser(id);
        checkSelfOrAdmin(user.getId());
        return toResponse(user);
    }

    @Transactional
    public UserResponse update(Long id, UpdateUserRequest request) {
        User user = requireUser(id);
        checkSelfOrAdmin(user.getId());
        if (StringUtils.hasText(request.getPhone())) {
            user.setPhone(request.getPhone());
        }
        if (StringUtils.hasText(request.getEmail())) {
            user.setEmail(request.getEmail());
        }
        if (StringUtils.hasText(request.getAvatar())) {
            user.setAvatar(request.getAvatar());
        }
        user.setUpdateTime(LocalDateTime.now());
        userMapper.updateById(user);
        return toResponse(user);
    }

    public PageResult<UserResponse> adminList(int page, int size, String keyword) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like(User::getUsername, keyword).or().like(User::getPhone, keyword));
        }
        wrapper.orderByDesc(User::getCreateTime);
        Page<User> result = userMapper.selectPage(new Page<>(page, size), wrapper);
        return PageResult.of(result.getTotal(), result.getCurrent(), result.getSize(),
                result.getRecords().stream().map(this::toResponse).collect(java.util.stream.Collectors.toList()));
    }

    @Transactional
    public void updateStatus(Long id, Integer status) {
        User user = requireUser(id);
        if (status != 0 && status != 1) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "Error");
        }
        user.setStatus(status);
        user.setUpdateTime(LocalDateTime.now());
        userMapper.updateById(user);
    }

    public User requireUser(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND, "Error");
        }
        return user;
    }

    private void checkSelfOrAdmin(Long targetUserId) {
        Long current = UserContext.getUserId();
        if (current == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "请先登录");
        }
        if (!current.equals(targetUserId) && UserContext.get().getRole() != 3) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "无权操作");
        }
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
