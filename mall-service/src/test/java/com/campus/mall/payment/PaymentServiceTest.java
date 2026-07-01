package com.campus.mall.payment;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.common.core.constant.ErrorCode;
import com.campus.common.core.enums.OrderStatus;
import com.campus.common.core.enums.PaymentStatus;
import com.campus.common.core.exception.BusinessException;
import com.campus.common.security.LoginUser;
import com.campus.common.security.UserContext;
import com.campus.mall.dto.request.CreatePaymentRequest;
import com.campus.mall.dto.request.PaymentCallbackRequest;
import com.campus.mall.dto.response.PaymentResponse;
import com.campus.mall.entity.Order;
import com.campus.mall.entity.PaymentRecord;
import com.campus.mall.mapper.PaymentRecordMapper;
import com.campus.mall.order.OrderService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * 支付服务单元测试
 * 测试编号：UT-PAY-001 ~ UT-PAY-003
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("支付服务单元测试")
class PaymentServiceTest {

    @Mock
    private PaymentRecordMapper paymentRecordMapper;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private PaymentService paymentService;

    private MockedStatic<UserContext> userContextMockedStatic;

    private Order testOrder;
    private PaymentRecord testPayment;
    private LoginUser loginUser;

    @BeforeEach
    void setUp() {
        loginUser = new LoginUser();
        loginUser.setUserId(1L);
        loginUser.setRole(0);

        userContextMockedStatic = mockStatic(UserContext.class);
        userContextMockedStatic.when(UserContext::getUserId).thenReturn(1L);
        userContextMockedStatic.when(UserContext::get).thenReturn(loginUser);

        testOrder = new Order();
        testOrder.setId(1L);
        testOrder.setOrderNo("ORD202607010001");
        testOrder.setUserId(1L);
        testOrder.setPayAmount(BigDecimal.valueOf(100.00));
        testOrder.setStatus(OrderStatus.UNPAID.getCode());

        testPayment = new PaymentRecord();
        testPayment.setId(1L);
        testPayment.setOrderNo("ORD202607010001");
        testPayment.setPaymentNo("PAY202607010001");
        testPayment.setAmount(BigDecimal.valueOf(100.00));
        testPayment.setChannel(1);
        testPayment.setStatus(PaymentStatus.INIT.getCode());
        testPayment.setCreateTime(LocalDateTime.now());
    }

    @AfterEach
    void tearDown() {
        userContextMockedStatic.close();
    }

    // ==================== 创建支付单测试 ====================

    @Test
    @DisplayName("UT-PAY-001: 支付创建成功返回支付单信息")
    void testCreate_Success() {
        // 准备数据
        CreatePaymentRequest request = new CreatePaymentRequest();
        request.setOrderNo("ORD202607010001");
        request.setChannel(1);

        when(orderService.requireByOrderNo("ORD202607010001")).thenReturn(testOrder);
        when(paymentRecordMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(null);
        when(paymentRecordMapper.insert(any(PaymentRecord.class))).thenReturn(1);

        // 执行
        PaymentResponse response = paymentService.create(request);

        // 验证
        assertNotNull(response);
        assertEquals("ORD202607010001", response.getOrderNo());
        assertEquals(BigDecimal.valueOf(100.00), response.getAmount());

        // 验证插入
        verify(paymentRecordMapper, times(1)).insert(any(PaymentRecord.class));
    }

    @Test
    @DisplayName("UT-PAY-001-2: 已存在支付单直接返回")
    void testCreate_ExistingPayment() {
        // 准备数据
        CreatePaymentRequest request = new CreatePaymentRequest();
        request.setOrderNo("ORD202607010001");
        request.setChannel(1);

        testPayment.setStatus(PaymentStatus.INIT.getCode());

        when(orderService.requireByOrderNo("ORD202607010001")).thenReturn(testOrder);
        when(paymentRecordMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(testPayment);

        // 执行
        PaymentResponse response = paymentService.create(request);

        // 验证
        assertNotNull(response);
        assertEquals("PAY202607010001", response.getPaymentNo());

        // 验证不插入新支付单
        verify(paymentRecordMapper, never()).insert(any(PaymentRecord.class));
    }

    @Test
    @DisplayName("UT-PAY-001-3: 非订单用户无权创建支付")
    void testCreate_Forbidden() {
        // 准备数据 - 模拟不同用户
        userContextMockedStatic.when(UserContext::getUserId).thenReturn(2L);
        
        CreatePaymentRequest request = new CreatePaymentRequest();
        request.setOrderNo("ORD202607010001");
        request.setChannel(1);

        when(orderService.requireByOrderNo("ORD202607010001")).thenReturn(testOrder);

        // 执行并验证
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            paymentService.create(request);
        });
        assertEquals(ErrorCode.FORBIDDEN, exception.getCode());
    }

    @Test
    @DisplayName("UT-PAY-001-4: 订单状态非待支付抛出异常")
    void testCreate_OrderStatusError() {
        // 准备数据
        testOrder.setStatus(OrderStatus.PAID.getCode());
        CreatePaymentRequest request = new CreatePaymentRequest();
        request.setOrderNo("ORD202607010001");
        request.setChannel(1);

        when(orderService.requireByOrderNo("ORD202607010001")).thenReturn(testOrder);

        // 执行并验证
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            paymentService.create(request);
        });
        assertEquals(ErrorCode.ORDER_STATUS_ERROR, exception.getCode());
    }

    // ==================== 支付回调幂等测试 ====================

    @Test
    @DisplayName("UT-PAY-002: 重复callbackNo不重复处理")
    void testCallback_Idempotent() {
        // 准备数据
        testPayment.setStatus(PaymentStatus.SUCCESS.getCode());
        testPayment.setCallbackNo("CB-TEST-001");

        PaymentCallbackRequest request = new PaymentCallbackRequest();
        request.setPaymentNo("PAY202607010001");
        request.setCallbackNo("CB-TEST-001");
        request.setTradeNo("TRADE001");
        request.setSuccess(true);

        when(paymentRecordMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(testPayment);

        // 执行
        PaymentResponse response = paymentService.callback(request);

        // 验证返回成功但不更新
        assertNotNull(response);
        assertEquals(PaymentStatus.SUCCESS.getCode(), response.getStatus());

        // 验证不更新支付单，不调用订单服务
        verify(paymentRecordMapper, never()).updateById(any());
        verify(orderService, never()).onPaid(anyString());
    }

    @Test
    @DisplayName("UT-PAY-002-2: 支付已成功不重复处理")
    void testCallback_AlreadySuccess() {
        // 准备数据
        testPayment.setStatus(PaymentStatus.SUCCESS.getCode());
        testPayment.setCallbackNo("CB-OLD-001");

        PaymentCallbackRequest request = new PaymentCallbackRequest();
        request.setPaymentNo("PAY202607010001");
        request.setCallbackNo("CB-NEW-001"); // 新的callbackNo
        request.setTradeNo("TRADE002");
        request.setSuccess(true);

        when(paymentRecordMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(testPayment);

        // 执行
        PaymentResponse response = paymentService.callback(request);

        // 验证返回成功但不更新
        assertNotNull(response);

        // 验证不更新支付单
        verify(paymentRecordMapper, never()).updateById(any());
    }

    @Test
    @DisplayName("UT-PAY-002-3: 首次回调成功更新状态")
    void testCallback_FirstSuccess() {
        // 准备数据
        testPayment.setStatus(PaymentStatus.INIT.getCode());

        PaymentCallbackRequest request = new PaymentCallbackRequest();
        request.setPaymentNo("PAY202607010001");
        request.setCallbackNo("CB-FIRST-001");
        request.setTradeNo("TRADE001");
        request.setCallbackData("{\"test\":true}");
        request.setSuccess(true);

        when(paymentRecordMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(testPayment);
        when(paymentRecordMapper.updateById(any(PaymentRecord.class))).thenReturn(1);
        doNothing().when(orderService).onPaid(anyString());

        // 执行
        PaymentResponse response = paymentService.callback(request);

        // 验证
        assertNotNull(response);

        // 验证更新支付单和订单
        verify(paymentRecordMapper, times(1)).updateById(any(PaymentRecord.class));
        verify(orderService, times(1)).onPaid("ORD202607010001");
    }

    // ==================== 模拟支付测试 ====================

    @Test
    @DisplayName("UT-PAY-003: 模拟支付成功")
    void testMockPay_Success() {
        // 准备数据
        testPayment.setStatus(PaymentStatus.INIT.getCode());

        when(paymentRecordMapper.selectById(1L)).thenReturn(testPayment);
        when(orderService.requireByOrderNo("ORD202607010001")).thenReturn(testOrder);
        lenient().when(paymentRecordMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(testPayment);
        lenient().when(paymentRecordMapper.updateById(any(PaymentRecord.class))).thenReturn(1);
        lenient().doNothing().when(orderService).onPaid(anyString());

        // 执行
        PaymentResponse response = paymentService.mockPay(1L);

        // 验证
        assertNotNull(response);
    }

    @Test
    @DisplayName("UT-PAY-003-2: 已成功支付不重复处理")
    void testMockPay_AlreadySuccess() {
        // 准备数据
        testPayment.setStatus(PaymentStatus.SUCCESS.getCode());

        when(paymentRecordMapper.selectById(1L)).thenReturn(testPayment);
        when(orderService.requireByOrderNo("ORD202607010001")).thenReturn(testOrder);

        // 执行
        PaymentResponse response = paymentService.mockPay(1L);

        // 验证返回当前状态
        assertNotNull(response);
        assertEquals(PaymentStatus.SUCCESS.getCode(), response.getStatus());
    }

    // ==================== 异常测试 ====================

    @Test
    @DisplayName("UT-PAY-001-5: 支付单不存在抛出异常")
    void testGetById_NotFound() {
        when(paymentRecordMapper.selectById(999L)).thenReturn(null);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            paymentService.getById(999L);
        });
        assertEquals(ErrorCode.PAYMENT_FAILED, exception.getCode());
    }

    @Test
    @DisplayName("UT-PAY-002-4: 回调支付单不存在抛出异常")
    void testCallback_NotFound() {
        PaymentCallbackRequest request = new PaymentCallbackRequest();
        request.setPaymentNo("INVALID-PAY-NO");

        when(paymentRecordMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(null);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            paymentService.callback(request);
        });
        assertEquals(ErrorCode.PAYMENT_FAILED, exception.getCode());
    }
}