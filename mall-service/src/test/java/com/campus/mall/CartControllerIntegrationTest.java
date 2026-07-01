package com.campus.mall;

import com.campus.common.core.result.Result;
import com.campus.mall.cart.CartController;
import com.campus.mall.cart.CartService;
import com.campus.mall.dto.request.CartItemRequest;
import com.campus.mall.dto.request.UpdateCartItemRequest;
import com.campus.mall.dto.response.CartItemResponse;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * 购物车控制器接口集成测试
 * 测试编号：IT-CART-001 ~ IT-CART-002
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("购物车控制器接口集成测试")
class CartControllerIntegrationTest {

    @Mock
    private CartService cartService;

    @InjectMocks
    private CartController cartController;

    private CartItemResponse createTestCartItemResponse() {
        CartItemResponse response = new CartItemResponse();
        response.setProductId(100L);
        response.setTitle("测试商品");
        response.setPrice(BigDecimal.valueOf(99.99));
        response.setQuantity(2);
        response.setSelected(true);
        response.setStock(50);
        return response;
    }

    // ==================== 查询购物车测试 ====================

    @Test
    @DisplayName("IT-CART-002: 购物车列表查询成功")
    void testList_Success() {
        List<CartItemResponse> items = new ArrayList<>();
        items.add(createTestCartItemResponse());

        when(cartService.list()).thenReturn(items);

        Result<List<CartItemResponse>> result = cartController.list();

        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertNotNull(result.getData());
        assertEquals(1, result.getData().size());
        assertEquals("测试商品", result.getData().get(0).getTitle());
    }

    // ==================== 添加商品测试 ====================

    @Test
    @DisplayName("IT-CART-001: 添加商品到购物车成功")
    void testAddItem_Success() {
        CartItemRequest request = new CartItemRequest();
        request.setProductId(100L);
        request.setQuantity(2);
        request.setSelected(true);

        doNothing().when(cartService).addItem(any(CartItemRequest.class));

        Result<Void> result = cartController.addItem(request);

        assertNotNull(result);
        assertEquals(200, result.getCode());
        verify(cartService, times(1)).addItem(request);
    }

    // ==================== 修改商品测试 ====================

    @Test
    @DisplayName("IT-CART-001-2: 修改购物车商品数量成功")
    void testUpdateItem_Success() {
        UpdateCartItemRequest request = new UpdateCartItemRequest();
        request.setQuantity(5);
        request.setSelected(false);

        doNothing().when(cartService).updateItem(anyLong(), any(UpdateCartItemRequest.class));

        Result<Void> result = cartController.updateItem(100L, request);

        assertNotNull(result);
        assertEquals(200, result.getCode());
        verify(cartService, times(1)).updateItem(100L, request);
    }

    // ==================== 删除商品测试 ====================

    @Test
    @DisplayName("IT-CART-003: 删除购物车商品成功")
    void testRemoveItem_Success() {
        doNothing().when(cartService).removeItem(anyLong());

        Result<Void> result = cartController.removeItem(100L);

        assertNotNull(result);
        assertEquals(200, result.getCode());
        verify(cartService, times(1)).removeItem(100L);
    }

    @Test
    @DisplayName("IT-CART-003-2: 清空购物车成功")
    void testClear_Success() {
        doNothing().when(cartService).clear();

        Result<Void> result = cartController.clear();

        assertNotNull(result);
        assertEquals(200, result.getCode());
        verify(cartService, times(1)).clear();
    }
}