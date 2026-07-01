package com.campus.mall;

import com.campus.common.core.result.Result;
import com.campus.mall.dto.request.CreatePaymentRequest;
import com.campus.mall.dto.request.PaymentCallbackRequest;
import com.campus.mall.dto.response.PaymentResponse;
import com.campus.mall.payment.PaymentController;
import com.campus.mall.payment.PaymentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * 支付控制器接口集成测试
 * 测试编号：IT-PAY-001 ~ IT-PAY-003
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("支付控制器接口集成测试")
class PaymentControllerIntegrationTest {

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    private PaymentResponse createTestPaymentResponse() {
        PaymentResponse response = new PaymentResponse();
        response.setId(1L);
        response.setOrderNo("ORD202607010001");
        response.setPaymentNo("PAY202607010001");
        response.setAmount(BigDecimal.valueOf(199.99));
        response.setStatus(0); // 待支付
        return response;
    }

    // ==================== 创建支付单测试 ====================

    @Test
    @DisplayName("IT-PAY-001: 创建支付单成功")
    void testCreate_Success() {
        CreatePaymentRequest request = new CreatePaymentRequest();
        request.setOrderNo("ORD202607010001");
        request.setChannel(1);

        PaymentResponse paymentResponse = createTestPaymentResponse();

        when(paymentService.create(any(CreatePaymentRequest.class))).thenReturn(paymentResponse);

        Result<PaymentResponse> result = paymentController.create(request);

        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertNotNull(result.getData());
        assertEquals("ORD202607010001", result.getData().getOrderNo());
    }

    // ==================== 查询支付单测试 ====================

    @Test
    @DisplayName("IT-PAY-002: 查询支付单详情成功")
    void testGetById_Success() {
        PaymentResponse paymentResponse = createTestPaymentResponse();
        paymentResponse.setStatus(1); // 支付成功

        when(paymentService.getById(anyLong())).thenReturn(paymentResponse);

        Result<PaymentResponse> result = paymentController.getById(1L);

        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals(1, result.getData().getStatus());
    }

    // ==================== 支付回调测试 ====================

    @Test
    @DisplayName("IT-PAY-001-2: 支付回调处理成功")
    void testCallback_Success() {
        PaymentCallbackRequest request = new PaymentCallbackRequest();
        request.setPaymentNo("PAY202607010001");
        request.setCallbackNo("CB-001");
        request.setTradeNo("TRADE001");
        request.setSuccess(true);

        PaymentResponse paymentResponse = createTestPaymentResponse();
        paymentResponse.setStatus(1); // 支付成功

        when(paymentService.callback(any(PaymentCallbackRequest.class))).thenReturn(paymentResponse);

        Result<PaymentResponse> result = paymentController.callback(request);

        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals(1, result.getData().getStatus());
    }

    // ==================== 模拟支付测试 ====================

    @Test
    @DisplayName("IT-PAY-003: 模拟支付成功")
    void testMockPay_Success() {
        PaymentResponse paymentResponse = createTestPaymentResponse();
        paymentResponse.setStatus(1); // 支付成功

        when(paymentService.mockPay(anyLong())).thenReturn(paymentResponse);

        Result<PaymentResponse> result = paymentController.mockPay(1L);

        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals(1, result.getData().getStatus());
    }
}