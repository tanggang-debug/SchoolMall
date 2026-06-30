package com.campus.mall.product;

import com.campus.common.core.constant.ErrorCode;
import com.campus.common.core.enums.ProductStatus;
import com.campus.common.core.exception.BusinessException;

public final class ProductStateMachine {

    private ProductStateMachine() {}

    public static void validateTransition(ProductStatus from, ProductStatus to) {
        boolean allowed = false;
        switch (from) {
            case PENDING:
                allowed = to == ProductStatus.ON_SALE || to == ProductStatus.REJECTED;
                break;
            case ON_SALE:
                allowed = to == ProductStatus.OFF_SHELF;
                break;
            case OFF_SHELF:
                allowed = to == ProductStatus.ON_SALE || to == ProductStatus.PENDING;
                break;
            case REJECTED:
                allowed = to == ProductStatus.PENDING;
                break;
            default:
                allowed = false;
        }
        if (!allowed) {
            throw new BusinessException(ErrorCode.ORDER_STATUS_ERROR,
                    "Invalid product status transition: " + from.name() + " -> " + to.name());
        }
    }

    public static ProductStatus of(int code) {
        for (ProductStatus status : ProductStatus.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new BusinessException(ErrorCode.PRODUCT_NOT_FOUND, "Unknown product status");
    }
}
