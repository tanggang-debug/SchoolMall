package com.campus.mall.notify;

import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.common.core.constant.ErrorCode;
import com.campus.common.core.exception.BusinessException;
import com.campus.common.security.UserContext;
import com.campus.mall.dto.response.NotificationResponse;
import com.campus.mall.dto.response.PageResult;
import com.campus.mall.entity.Notification;
import com.campus.mall.mapper.NotificationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationMapper notificationMapper;

    public PageResult<NotificationResponse> list(int page, int size) {
        Long userId = UserContext.getUserId();
        Page<Notification> result = notificationMapper.selectPage(new Page<>(page, size),
                new LambdaQueryWrapper<Notification>()
                        .eq(Notification::getUserId, userId)
                        .orderByDesc(Notification::getCreateTime));
        return PageResult.of(result.getTotal(), result.getCurrent(), result.getSize(),
                result.getRecords().stream().map(this::toResponse).collect(java.util.stream.Collectors.toList()));
    }

    @Transactional
    public void markRead(Long id) {
        Notification notification = notificationMapper.selectById(id);
        if (notification == null || !notification.getUserId().equals(UserContext.getUserId())) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "Error");
        }
        notification.setStatus(1);
        notificationMapper.updateById(notification);
    }

    @Transactional
    public void send(Long userId, String title, String content, String templateCode, String businessId) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setTemplateCode(templateCode);
        notification.setBusinessId(businessId);
        notification.setStatus(0);
        notification.setCreateTime(LocalDateTime.now());
        notificationMapper.insert(notification);
    }

    private NotificationResponse toResponse(Notification notification) {
        NotificationResponse resp = new NotificationResponse();
        resp.setId(notification.getId());
        resp.setTitle(notification.getTitle());
        resp.setContent(notification.getContent());
        resp.setStatus(notification.getStatus());
        resp.setCreateTime(notification.getCreateTime() == null ? null : notification.getCreateTime().toString());
        return resp;
    }
}
