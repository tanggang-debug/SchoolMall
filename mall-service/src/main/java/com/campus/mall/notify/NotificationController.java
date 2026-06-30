package com.campus.mall.notify;

import com.campus.common.core.result.Result;
import com.campus.common.security.RequireLogin;
import com.campus.mall.dto.response.NotificationResponse;
import com.campus.mall.dto.response.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    @RequireLogin
    public Result<PageResult<NotificationResponse>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.ok(notificationService.list(page, size));
    }

    @PutMapping("/{id}/read")
    @RequireLogin
    public Result<Void> markRead(@PathVariable Long id) {
        notificationService.markRead(id);
        return Result.ok();
    }
}
