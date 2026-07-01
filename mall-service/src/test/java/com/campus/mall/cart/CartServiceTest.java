package com.campus.mall.cart;

import com.campus.common.core.constant.ErrorCode;
import com.campus.common.core.exception.BusinessException;
import com.campus.common.security.LoginUser;
import com.campus.common.security.UserContext;
import com.campus.mall.dto.request.CartItemRequest;
import com.campus.mall.dto.request.UpdateCartItemRequest;
import com.campus.mall.entity.Product;
import com.campus.mall.product.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * 购物车服务单元测试
 * 测试编号：UT-CART-001 ~ UT-CART-003
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("购物车服务单元测试")
class CartServiceTest {

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private HashOperations<String, Object, Object> hashOperations;

    @Mock
    private ProductService productService;

    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private CartService cartService;

    private MockedStatic<UserContext> userContextMockedStatic;
    private LoginUser loginUser;
    private Product testProduct;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() {
        loginUser = new LoginUser();
        loginUser.setUserId(1L);
        loginUser.setRole(0);

        userContextMockedStatic = mockStatic(UserContext.class);
        userContextMockedStatic.when(UserContext::getUserId).thenReturn(1L);
        userContextMockedStatic.when(UserContext::get).thenReturn(loginUser);

        testProduct = new Product();
        testProduct.setId(100L);
        testProduct.setTitle("测试商品");
        testProduct.setPrice(BigDecimal.valueOf(99.99));
        testProduct.setStock(50);
        testProduct.setImages("[\"http://example.com/image.jpg\"]");
        testProduct.setStatus(1);

        lenient().when(redisTemplate.opsForHash()).thenReturn(hashOperations);
    }

    @AfterEach
    void tearDown() {
        userContextMockedStatic.close();
    }

    // ==================== 添加商品测试 ====================

    @Test
    @DisplayName("UT-CART-001: 添加商品到购物车成功")
    void testAddItem_Success() {
        // 准备数据
        CartItemRequest request = new CartItemRequest();
        request.setProductId(100L);
        request.setQuantity(2);
        request.setSelected(true);

        when(productService.requireOnSaleProduct(100L)).thenReturn(testProduct);
        when(hashOperations.get(anyString(), eq("100"))).thenReturn(null);

        // 执行
        cartService.addItem(request);

        // 验证
        verify(hashOperations, times(1)).put(anyString(), any(), anyString());
    }

    @Test
    @DisplayName("UT-CART-001-2: 添加已存在商品数量累加")
    void testAddItem_IncrementQuantity() throws Exception {
        // 准备数据
        CartService.CartEntry existingEntry = new CartService.CartEntry();
        existingEntry.setQuantity(3);
        existingEntry.setSelected(true);
        String existingJson = objectMapper.writeValueAsString(existingEntry);

        CartItemRequest request = new CartItemRequest();
        request.setProductId(100L);
        request.setQuantity(2);
        request.setSelected(true);

        when(productService.requireOnSaleProduct(100L)).thenReturn(testProduct);
        when(hashOperations.get(anyString(), eq("100"))).thenReturn(existingJson);

        // 执行
        cartService.addItem(request);

        // 验证
        verify(hashOperations, times(1)).put(anyString(), any(), anyString());
    }

    @Test
    @DisplayName("UT-CART-001-3: 商品不存在抛出异常")
    void testAddItem_ProductNotFound() {
        CartItemRequest request = new CartItemRequest();
        request.setProductId(999L);
        request.setQuantity(1);

        when(productService.requireOnSaleProduct(999L))
                .thenThrow(new BusinessException(ErrorCode.PRODUCT_NOT_FOUND, "商品不存在"));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            cartService.addItem(request);
        });
        assertEquals(ErrorCode.PRODUCT_NOT_FOUND, exception.getCode());
    }

    // ==================== 修改商品测试 ====================

    @Test
    @DisplayName("UT-CART-002: 修改购物车商品数量成功")
    void testUpdateItem_Success() throws Exception {
        // 准备数据
        CartService.CartEntry existingEntry = new CartService.CartEntry();
        existingEntry.setQuantity(3);
        existingEntry.setSelected(true);
        String existingJson = objectMapper.writeValueAsString(existingEntry);

        UpdateCartItemRequest request = new UpdateCartItemRequest();
        request.setQuantity(5);
        request.setSelected(false);

        when(hashOperations.get(anyString(), eq("100"))).thenReturn(existingJson);

        // 执行
        cartService.updateItem(100L, request);

        // 验证
        verify(hashOperations, times(1)).put(anyString(), any(), anyString());
    }

    @Test
    @DisplayName("UT-CART-002-2: 购物车中无商品抛出异常")
    void testUpdateItem_ItemNotFound() {
        UpdateCartItemRequest request = new UpdateCartItemRequest();
        request.setQuantity(5);

        when(hashOperations.get(anyString(), eq("100"))).thenReturn(null);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            cartService.updateItem(100L, request);
        });
        assertEquals(ErrorCode.PRODUCT_NOT_FOUND, exception.getCode());
    }

    // ==================== 删除商品测试 ====================

    @Test
    @DisplayName("UT-CART-003: 删除购物车商品成功")
    void testRemoveItem_Success() {
        // 执行
        cartService.removeItem(100L);

        // 验证
        verify(hashOperations, times(1)).delete(anyString(), any());
    }

    @Test
    @DisplayName("UT-CART-003-2: 清空购物车成功")
    void testClear_Success() {
        // 执行
        cartService.clear();

        // 验证
        verify(redisTemplate, times(1)).delete(anyString());
    }

    // ==================== 查询测试 ====================

    @Test
    @DisplayName("UT-CART-004: 获取选中商品成功")
    void testGetSelectedItems_Success() throws Exception {
        // 准备数据
        Map<Object, Object> entries = new HashMap<>();
        CartService.CartEntry entry1 = new CartService.CartEntry();
        entry1.setQuantity(2);
        entry1.setSelected(true);
        entries.put("100", objectMapper.writeValueAsString(entry1));

        CartService.CartEntry entry2 = new CartService.CartEntry();
        entry2.setQuantity(1);
        entry2.setSelected(false);
        entries.put("101", objectMapper.writeValueAsString(entry2));

        when(hashOperations.entries(anyString())).thenReturn(entries);

        // 执行
        Map<Long, CartService.CartEntry> result = cartService.getSelectedItems();

        // 验证
        assertEquals(1, result.size());
        assertTrue(result.containsKey(100L));
        assertEquals(2, result.get(100L).getQuantity());
    }

    @Test
    @DisplayName("UT-CART-005: 获取单个商品成功")
    void testGetItem_Success() {
        // 准备数据
        CartService.CartEntry entry = new CartService.CartEntry();
        entry.setQuantity(3);
        entry.setSelected(true);
        
        try {
            String json = objectMapper.writeValueAsString(entry);
            when(hashOperations.get(anyString(), eq("100"))).thenReturn(json);

            CartService.CartEntry result = cartService.getItem(100L);

            assertNotNull(result);
            assertEquals(3, result.getQuantity());
        } catch (Exception e) {
            fail("JSON serialization failed: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("UT-CART-005-2: 商品不存在返回null")
    void testGetItem_NotFound() {
        when(hashOperations.get(anyString(), eq("100"))).thenReturn(null);

        CartService.CartEntry result = cartService.getItem(100L);

        assertNull(result);
    }

    // ==================== 权限测试 ====================

    @Test
    @DisplayName("UT-CART-006: 未登录用户抛出异常")
    void testUnauthorized() {
        userContextMockedStatic.when(UserContext::getUserId).thenReturn(null);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            cartService.list();
        });
        assertEquals(ErrorCode.UNAUTHORIZED, exception.getCode());
    }
}