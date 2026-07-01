package com.campus.mall.order;

import com.campus.common.core.constant.ErrorCode;
import com.campus.common.core.enums.OrderStatus;
import com.campus.common.core.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 订单状态机单元测试
 * 测试编号：UT-ORDER-002 ~ UT-ORDER-007
 */
@DisplayName("订单状态机单元测试")
class OrderStateMachineTest {

    // ==================== 状态流转验证测试 ====================

    @Test
    @DisplayName("UT-ORDER-002: 待支付状态支付成功后可流转到已支付状态")
    void testValidateTransition_UnpaidToPaid_Success() {
        // 正常状态流转，不应抛出异常
        assertDoesNotThrow(() -> {
            OrderStateMachine.validateTransition(OrderStatus.UNPAID, OrderStatus.PAID);
        });
    }

    @Test
    @DisplayName("UT-ORDER-002-2: 待支付状态可取消")
    void testValidateTransition_UnpaidToCancelled_Success() {
        assertDoesNotThrow(() -> {
            OrderStateMachine.validateTransition(OrderStatus.UNPAID, OrderStatus.CANCELLED);
        });
    }

    @Test
    @DisplayName("UT-ORDER-003: 已支付状态可流转到待发货")
    void testValidateTransition_PaidToToShip_Success() {
        assertDoesNotThrow(() -> {
            OrderStateMachine.validateTransition(OrderStatus.PAID, OrderStatus.TO_SHIP);
        });
    }

    @Test
    @DisplayName("UT-ORDER-003-2: 已支付状态可申请售后")
    void testValidateTransition_PaidToRefunding_Success() {
        assertDoesNotThrow(() -> {
            OrderStateMachine.validateTransition(OrderStatus.PAID, OrderStatus.REFUNDING);
        });
    }

    @Test
    @DisplayName("UT-ORDER-003-3: 待发货状态可发货")
    void testValidateTransition_ToShipToShipped_Success() {
        assertDoesNotThrow(() -> {
            OrderStateMachine.validateTransition(OrderStatus.TO_SHIP, OrderStatus.SHIPPED);
        });
    }

    @Test
    @DisplayName("UT-ORDER-003-4: 待发货状态可申请售后")
    void testValidateTransition_ToShipToRefunding_Success() {
        assertDoesNotThrow(() -> {
            OrderStateMachine.validateTransition(OrderStatus.TO_SHIP, OrderStatus.REFUNDING);
        });
    }

    @Test
    @DisplayName("UT-ORDER-003-5: 已发货状态可确认收货")
    void testValidateTransition_ShippedToCompleted_Success() {
        assertDoesNotThrow(() -> {
            OrderStateMachine.validateTransition(OrderStatus.SHIPPED, OrderStatus.COMPLETED);
        });
    }

    @Test
    @DisplayName("UT-ORDER-003-6: 已发货状态可申请售后")
    void testValidateTransition_ShippedToRefunding_Success() {
        assertDoesNotThrow(() -> {
            OrderStateMachine.validateTransition(OrderStatus.SHIPPED, OrderStatus.REFUNDING);
        });
    }

    @Test
    @DisplayName("UT-ORDER-007-1: 已完成状态可申请售后")
    void testValidateTransition_CompletedToRefunding_Success() {
        assertDoesNotThrow(() -> {
            OrderStateMachine.validateTransition(OrderStatus.COMPLETED, OrderStatus.REFUNDING);
        });
    }

    // ==================== 非法状态流转测试 ====================

    @Test
    @DisplayName("UT-ORDER-004: 已完成订单不能进入已发货状态")
    void testValidateTransition_CompletedToShipped_Fail() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            OrderStateMachine.validateTransition(OrderStatus.COMPLETED, OrderStatus.SHIPPED);
        });
        assertEquals(ErrorCode.ORDER_STATUS_ERROR, exception.getCode());
    }

    @Test
    @DisplayName("UT-ORDER-004-2: 已完成订单不能进入待支付状态")
    void testValidateTransition_CompletedToUnpaid_Fail() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            OrderStateMachine.validateTransition(OrderStatus.COMPLETED, OrderStatus.UNPAID);
        });
        assertEquals(ErrorCode.ORDER_STATUS_ERROR, exception.getCode());
    }

    @Test
    @DisplayName("UT-ORDER-004-3: 已取消订单不能进入任何状态")
    void testValidateTransition_CancelledToAny_Fail() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            OrderStateMachine.validateTransition(OrderStatus.CANCELLED, OrderStatus.PAID);
        });
        assertEquals(ErrorCode.ORDER_STATUS_ERROR, exception.getCode());
    }

    @Test
    @DisplayName("UT-ORDER-004-4: 售后中订单不能进入其他状态")
    void testValidateTransition_RefundingToAny_Fail() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            OrderStateMachine.validateTransition(OrderStatus.REFUNDING, OrderStatus.COMPLETED);
        });
        assertEquals(ErrorCode.ORDER_STATUS_ERROR, exception.getCode());
    }

    @Test
    @DisplayName("UT-ORDER-004-5: 待支付不能直接到已发货")
    void testValidateTransition_UnpaidToShipped_Fail() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            OrderStateMachine.validateTransition(OrderStatus.UNPAID, OrderStatus.SHIPPED);
        });
        assertEquals(ErrorCode.ORDER_STATUS_ERROR, exception.getCode());
    }

    @Test
    @DisplayName("UT-ORDER-004-6: 已支付不能直接到已发货")
    void testValidateTransition_PaidToShipped_Fail() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            OrderStateMachine.validateTransition(OrderStatus.PAID, OrderStatus.SHIPPED);
        });
        assertEquals(ErrorCode.ORDER_STATUS_ERROR, exception.getCode());
    }

    // ==================== 操作条件校验测试 ====================

    @Test
    @DisplayName("UT-ORDER-005: 仅待支付状态可取消")
    void testCanCancel() {
        assertTrue(OrderStateMachine.canCancel(OrderStatus.UNPAID));
        assertFalse(OrderStateMachine.canCancel(OrderStatus.PAID));
        assertFalse(OrderStateMachine.canCancel(OrderStatus.SHIPPED));
        assertFalse(OrderStateMachine.canCancel(OrderStatus.COMPLETED));
        assertFalse(OrderStateMachine.canCancel(OrderStatus.CANCELLED));
    }

    @Test
    @DisplayName("UT-ORDER-006: 仅已发货状态可确认收货")
    void testCanConfirm() {
        assertTrue(OrderStateMachine.canConfirm(OrderStatus.SHIPPED));
        assertFalse(OrderStateMachine.canConfirm(OrderStatus.UNPAID));
        assertFalse(OrderStateMachine.canConfirm(OrderStatus.PAID));
        assertFalse(OrderStateMachine.canConfirm(OrderStatus.TO_SHIP));
        assertFalse(OrderStateMachine.canConfirm(OrderStatus.COMPLETED));
    }

    @Test
    @DisplayName("UT-ORDER-007: 多种状态可申请售后")
    void testCanRefund() {
        assertTrue(OrderStateMachine.canRefund(OrderStatus.PAID));
        assertTrue(OrderStateMachine.canRefund(OrderStatus.TO_SHIP));
        assertTrue(OrderStateMachine.canRefund(OrderStatus.SHIPPED));
        assertTrue(OrderStateMachine.canRefund(OrderStatus.COMPLETED));
        assertFalse(OrderStateMachine.canRefund(OrderStatus.UNPAID));
        assertFalse(OrderStateMachine.canRefund(OrderStatus.CANCELLED));
        assertFalse(OrderStateMachine.canRefund(OrderStatus.REFUNDING));
    }

    // ==================== 状态码解析测试 ====================

    @Test
    @DisplayName("UT-ORDER-001-1: 状态码正确解析")
    void testOf_ValidCode() {
        assertEquals(OrderStatus.UNPAID, OrderStateMachine.of(0));
        assertEquals(OrderStatus.PAID, OrderStateMachine.of(1));
        assertEquals(OrderStatus.TO_SHIP, OrderStateMachine.of(2));
        assertEquals(OrderStatus.SHIPPED, OrderStateMachine.of(3));
        assertEquals(OrderStatus.COMPLETED, OrderStateMachine.of(4));
        assertEquals(OrderStatus.CANCELLED, OrderStateMachine.of(5));
        assertEquals(OrderStatus.REFUNDING, OrderStateMachine.of(6));
    }

    @Test
    @DisplayName("UT-ORDER-001-2: 无效状态码抛出异常")
    void testOf_InvalidCode() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            OrderStateMachine.of(999);
        });
        assertEquals(ErrorCode.ORDER_NOT_FOUND, exception.getCode());
    }
}