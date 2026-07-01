package com.campus.mall;

import com.campus.common.core.result.Result;
import com.campus.mall.dto.request.CreateOrderRequest;
import com.campus.mall.dto.request.OrderItemRequest;
import com.campus.mall.dto.request.ShipOrderRequest;
import com.campus.mall.dto.response.OrderItemResponse;
import com.campus.mall.dto.response.OrderResponse;
import com.campus.mall.dto.response.PageResult;
import com.campus.mall.order.OrderController;
import com.campus.mall.order.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * 订单控制器接口集成测试
 * 测试编号：IT-ORDER-001 ~ IT-ORDER-003
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("订单控制器接口集成测试")
class OrderControllerIntegrationTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private OrderResponse createTestOrderResponse() {
        OrderResponse response = new OrderResponse();
        response.setId(1L);
        response.setOrderNo("ORD202607010001");
        response.setUserId(100L);
        response.setMerchantId(200L);
        response.setTotalAmount(BigDecimal.valueOf(199.99));
        response.setPayAmount(BigDecimal.valueOf(199.99));
        response.setStatus(0); // 待支付
        response.setItems(new ArrayList<>());
        return response;
    }

    // ==================== 创建订单测试 ====================

    @Test
    @DisplayName("IT-ORDER-001: 用户创建待支付订单成功")
    void testCreate_Success() {
        CreateOrderRequest request = new CreateOrderRequest();
        request.setAddressId(1L);
        List<OrderItemRequest> items = new ArrayList<>();
        OrderItemRequest item = new OrderItemRequest();
        item.setProductId(100L);
        item.setQuantity(2);
        items.add(item);
        request.setItems(items);

        OrderResponse orderResponse = createTestOrderResponse();
        List<OrderResponse> orderList = new ArrayList<>();
        orderList.add(orderResponse);

        when(orderService.create(any(CreateOrderRequest.class))).thenReturn(orderList);

        Result<List<OrderResponse>> result = orderController.create(request);

        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertNotNull(result.getData());
        assertEquals(1, result.getData().size());
        assertEquals(0, result.getData().get(0).getStatus());
    }

    // ==================== 查询订单测试 ====================

    @Test
    @DisplayName("IT-ORDER-002: 订单列表查询成功")
    void testList_Success() {
        PageResult<OrderResponse> pageResult = new PageResult<>();
        pageResult.setTotal(10L);
        pageResult.setPage(1L);
        pageResult.setSize(10L);
        pageResult.setRecords(new ArrayList<>());

        when(orderService.list(anyInt(), anyInt(), any())).thenReturn(pageResult);

        Result<PageResult<OrderResponse>> result = orderController.list(1, 10, null);

        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertNotNull(result.getData());
        assertEquals(10L, result.getData().getTotal());
    }

    @Test
    @DisplayName("IT-ORDER-003: 订单详情查询成功")
    void testGetById_Success() {
        OrderResponse orderResponse = createTestOrderResponse();

        when(orderService.getById(anyLong())).thenReturn(orderResponse);

        Result<OrderResponse> result = orderController.getById(1L);

        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals("ORD202607010001", result.getData().getOrderNo());
    }

    // ==================== 订单操作测试 ====================

    @Test
    @DisplayName("IT-CANCEL-001: 用户取消未支付订单成功")
    void testCancel_Success() {
        OrderResponse orderResponse = createTestOrderResponse();
        orderResponse.setStatus(5); // 已取消

        when(orderService.cancel(anyLong())).thenReturn(orderResponse);

        Result<OrderResponse> result = orderController.cancel(1L);

        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals(5, result.getData().getStatus());
    }

    @Test
    @DisplayName("IT-CONFIRM-001: 用户确认收货成功")
    void testConfirm_Success() {
        OrderResponse orderResponse = createTestOrderResponse();
        orderResponse.setStatus(4); // 已完成

        when(orderService.confirm(anyLong())).thenReturn(orderResponse);

        Result<OrderResponse> result = orderController.confirm(1L);

        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals(4, result.getData().getStatus());
    }

    @Test
    @DisplayName("IT-SHIP-001: 商户发货成功")
    void testShip_Success() {
        ShipOrderRequest request = new ShipOrderRequest();
        request.setLogisticsNo("SF123456789");

        OrderResponse orderResponse = createTestOrderResponse();
        orderResponse.setStatus(3); // 已发货
        orderResponse.setLogisticsNo("SF123456789");

        when(orderService.ship(anyLong(), anyString())).thenReturn(orderResponse);

        Result<OrderResponse> result = orderController.ship(1L, request);

        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals(3, result.getData().getStatus());
        assertEquals("SF123456789", result.getData().getLogisticsNo());
    }

    @Test
    @DisplayName("IT-REFUND-001: 申请售后退款成功")
    void testRefund_Success() {
        OrderResponse orderResponse = createTestOrderResponse();
        orderResponse.setStatus(6); // 售后中

        // 使用 doReturn-when 语法避免严格参数匹配问题
        doReturn(orderResponse).when(orderService).refund(anyLong(), any());

        Result<OrderResponse> result = orderController.refund(1L, null);

        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals(6, result.getData().getStatus());
    }
}