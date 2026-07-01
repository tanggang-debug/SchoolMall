package com.campus.mall.product;

import com.campus.common.core.constant.ErrorCode;
import com.campus.common.core.enums.ProductStatus;
import com.campus.common.core.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 商品状态机单元测试
 * 测试编号：UT-PRODUCT-002 ~ UT-PRODUCT-004
 */
@DisplayName("商品状态机单元测试")
class ProductStateMachineTest {

    // ==================== 正常状态流转测试 ====================

    @Test
    @DisplayName("UT-PRODUCT-002: 待审核状态审核通过后可流转到已上架状态")
    void testValidateTransition_PendingToOnSale_Success() {
        assertDoesNotThrow(() -> {
            ProductStateMachine.validateTransition(ProductStatus.PENDING, ProductStatus.ON_SALE);
        });
    }

    @Test
    @DisplayName("UT-PRODUCT-003: 待审核状态审核拒绝后可流转到审核拒绝状态")
    void testValidateTransition_PendingToRejected_Success() {
        assertDoesNotThrow(() -> {
            ProductStateMachine.validateTransition(ProductStatus.PENDING, ProductStatus.REJECTED);
        });
    }

    @Test
    @DisplayName("UT-PRODUCT-002-2: 已上架状态可下架")
    void testValidateTransition_OnSaleToOffShelf_Success() {
        assertDoesNotThrow(() -> {
            ProductStateMachine.validateTransition(ProductStatus.ON_SALE, ProductStatus.OFF_SHELF);
        });
    }

    @Test
    @DisplayName("UT-PRODUCT-002-3: 已下架状态可重新上架")
    void testValidateTransition_OffShelfToOnSale_Success() {
        assertDoesNotThrow(() -> {
            ProductStateMachine.validateTransition(ProductStatus.OFF_SHELF, ProductStatus.ON_SALE);
        });
    }

    @Test
    @DisplayName("UT-PRODUCT-002-4: 已下架状态可重新提交审核")
    void testValidateTransition_OffShelfToPending_Success() {
        assertDoesNotThrow(() -> {
            ProductStateMachine.validateTransition(ProductStatus.OFF_SHELF, ProductStatus.PENDING);
        });
    }

    @Test
    @DisplayName("UT-PRODUCT-003-2: 审核拒绝状态可修改后重新提交审核")
    void testValidateTransition_RejectedToPending_Success() {
        assertDoesNotThrow(() -> {
            ProductStateMachine.validateTransition(ProductStatus.REJECTED, ProductStatus.PENDING);
        });
    }

    // ==================== 非法状态流转测试 ====================

    @Test
    @DisplayName("UT-PRODUCT-004: 已上架商品不能直接进入审核拒绝状态")
    void testValidateTransition_OnSaleToRejected_Fail() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            ProductStateMachine.validateTransition(ProductStatus.ON_SALE, ProductStatus.REJECTED);
        });
        assertEquals(ErrorCode.ORDER_STATUS_ERROR, exception.getCode());
    }

    @Test
    @DisplayName("UT-PRODUCT-004-2: 已上架商品不能进入待审核状态")
    void testValidateTransition_OnSaleToPending_Fail() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            ProductStateMachine.validateTransition(ProductStatus.ON_SALE, ProductStatus.PENDING);
        });
        assertEquals(ErrorCode.ORDER_STATUS_ERROR, exception.getCode());
    }

    @Test
    @DisplayName("UT-PRODUCT-004-3: 待审核商品不能直接下架")
    void testValidateTransition_PendingToOffShelf_Fail() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            ProductStateMachine.validateTransition(ProductStatus.PENDING, ProductStatus.OFF_SHELF);
        });
        assertEquals(ErrorCode.ORDER_STATUS_ERROR, exception.getCode());
    }

    @Test
    @DisplayName("UT-PRODUCT-004-4: 审核拒绝商品不能上架")
    void testValidateTransition_RejectedToOnSale_Fail() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            ProductStateMachine.validateTransition(ProductStatus.REJECTED, ProductStatus.ON_SALE);
        });
        assertEquals(ErrorCode.ORDER_STATUS_ERROR, exception.getCode());
    }

    @Test
    @DisplayName("UT-PRODUCT-004-5: 审核拒绝商品不能直接下架")
    void testValidateTransition_RejectedToOffShelf_Fail() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            ProductStateMachine.validateTransition(ProductStatus.REJECTED, ProductStatus.OFF_SHELF);
        });
        assertEquals(ErrorCode.ORDER_STATUS_ERROR, exception.getCode());
    }

    // ==================== 状态码解析测试 ====================

    @Test
    @DisplayName("UT-PRODUCT-001-1: 商品状态码正确解析")
    void testOf_ValidCode() {
        assertEquals(ProductStatus.PENDING, ProductStateMachine.of(0));
        assertEquals(ProductStatus.ON_SALE, ProductStateMachine.of(1));
        assertEquals(ProductStatus.OFF_SHELF, ProductStateMachine.of(2));
        assertEquals(ProductStatus.REJECTED, ProductStateMachine.of(3));
    }

    @Test
    @DisplayName("UT-PRODUCT-001-2: 无效商品状态码抛出异常")
    void testOf_InvalidCode() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            ProductStateMachine.of(999);
        });
        assertEquals(ErrorCode.PRODUCT_NOT_FOUND, exception.getCode());
    }
}