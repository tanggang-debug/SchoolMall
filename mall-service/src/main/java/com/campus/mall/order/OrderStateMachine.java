package com.campus.mall.order;

import com.campus.common.core.constant.ErrorCode;
import com.campus.common.core.enums.OrderStatus;
import com.campus.common.core.exception.BusinessException;

public final class OrderStateMachine {

    private OrderStateMachine() {}

    public static void validateTransition(OrderStatus from, OrderStatus to) {
        boolean allowed = false;
        switch (from) {
            case UNPAID:
                allowed = to == OrderStatus.PAID || to == OrderStatus.CANCELLED;
                break;
            case PAID:
                allowed = to == OrderStatus.TO_SHIP || to == OrderStatus.REFUNDING;
                break;
            case TO_SHIP:
                allowed = to == OrderStatus.SHIPPED || to == OrderStatus.REFUNDING;
                break;
            case SHIPPED:
                allowed = to == OrderStatus.COMPLETED || to == OrderStatus.REFUNDING;
                break;
            case COMPLETED:
                allowed = to == OrderStatus.REFUNDING;
                break;
            case CANCELLED:
            case REFUNDING:
                allowed = false;
                break;
            default:
                allowed = false;
        }
        if (!allowed) {
            throw new BusinessException(ErrorCode.ORDER_STATUS_ERROR,
                    "Invalid order status transition: " + from.name() + " -> " + to.name());
        }
    }

    public static OrderStatus of(int code) {
        for (OrderStatus status : OrderStatus.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new BusinessException(ErrorCode.ORDER_NOT_FOUND, "Unknown order status");
    }

    public static boolean canCancel(OrderStatus status) {
        return status == OrderStatus.UNPAID;
    }

    public static boolean canConfirm(OrderStatus status) {
        return status == OrderStatus.SHIPPED;
    }

    public static boolean canShip(OrderStatus status) {
        return status == OrderStatus.TO_SHIP;
    }

    public static boolean canRefund(OrderStatus status) {
        return status == OrderStatus.PAID
                || status == OrderStatus.TO_SHIP
                || status == OrderStatus.SHIPPED
                || status == OrderStatus.COMPLETED;
    }
}
