package com.campus.mall.order;

import com.campus.common.core.result.Result;
import com.campus.common.security.RequireLogin;
import com.campus.common.security.RequireRole;
import com.campus.mall.dto.request.CreateOrderRequest;
import com.campus.mall.dto.request.RefundRequest;
import com.campus.mall.dto.request.ShipOrderRequest;
import com.campus.mall.dto.response.OrderResponse;
import com.campus.mall.dto.response.PageResult;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @RequireLogin
    public Result<List<OrderResponse>> create(@Valid @RequestBody CreateOrderRequest request) {
        return Result.ok(orderService.create(request));
    }

    @GetMapping
    @RequireLogin
    public Result<PageResult<OrderResponse>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Integer status) {
        return Result.ok(orderService.list(page, size, status));
    }

    @GetMapping("/{id}")
    @RequireLogin
    public Result<OrderResponse> getById(@PathVariable Long id) {
        return Result.ok(orderService.getById(id));
    }

    @PutMapping("/{id}/cancel")
    @RequireLogin
    public Result<OrderResponse> cancel(@PathVariable Long id) {
        return Result.ok(orderService.cancel(id));
    }

    @PutMapping("/{id}/confirm")
    @RequireLogin
    public Result<OrderResponse> confirm(@PathVariable Long id) {
        return Result.ok(orderService.confirm(id));
    }

    @PutMapping("/{id}/ship")
    @RequireRole(2)
    public Result<OrderResponse> ship(@PathVariable Long id, @Valid @RequestBody ShipOrderRequest request) {
        return Result.ok(orderService.ship(id, request.getLogisticsNo()));
    }

    @PostMapping("/{id}/refund")
    @RequireLogin
    public Result<OrderResponse> refund(@PathVariable Long id, @RequestBody(required = false) RefundRequest request) {
        String reason = request == null ? null : request.getReason();
        return Result.ok(orderService.refund(id, reason));
    }
}
