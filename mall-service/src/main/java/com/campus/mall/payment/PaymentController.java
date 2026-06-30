package com.campus.mall.payment;

import com.campus.common.core.result.Result;
import com.campus.common.security.RequireLogin;
import com.campus.mall.dto.request.CreatePaymentRequest;
import com.campus.mall.dto.request.PaymentCallbackRequest;
import com.campus.mall.dto.response.PaymentResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    @RequireLogin
    public Result<PaymentResponse> create(@Valid @RequestBody CreatePaymentRequest request) {
        return Result.ok(paymentService.create(request));
    }

    @GetMapping("/{id}")
    @RequireLogin
    public Result<PaymentResponse> getById(@PathVariable Long id) {
        return Result.ok(paymentService.getById(id));
    }

    @PostMapping("/callback")
    public Result<PaymentResponse> callback(@Valid @RequestBody PaymentCallbackRequest request) {
        return Result.ok(paymentService.callback(request));
    }

    @PostMapping("/{id}/mock-pay")
    @RequireLogin
    public Result<PaymentResponse> mockPay(@PathVariable Long id) {
        return Result.ok(paymentService.mockPay(id));
    }
}
