package com.campus.mall.cart;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.campus.common.core.constant.ErrorCode;
import com.campus.common.core.exception.BusinessException;
import com.campus.common.security.UserContext;
import com.campus.mall.dto.request.CartItemRequest;
import com.campus.mall.dto.request.UpdateCartItemRequest;
import com.campus.mall.dto.response.CartItemResponse;
import com.campus.mall.entity.Product;
import com.campus.mall.product.ProductService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CartService {

    private static final String CART_KEY_PREFIX = "cart:";

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final ProductService productService;

    public List<CartItemResponse> list() {
        Long userId = requireUserId();
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(cartKey(userId));
        List<CartItemResponse> items = new ArrayList<>();
        for (Map.Entry<Object, Object> entry : entries.entrySet()) {
            Long productId = Long.valueOf(entry.getKey().toString());
            CartEntry cartEntry = parseEntry(entry.getValue().toString());
            Product product = productService.requireProduct(productId);
            String image = extractFirstImage(product.getImages());
            CartItemResponse item = new CartItemResponse();
            item.setProductId(productId);
            item.setTitle(product.getTitle());
            item.setPrice(product.getPrice());
            item.setImage(image);
            item.setQuantity(cartEntry.getQuantity());
            item.setSelected(cartEntry.getSelected());
            item.setStock(product.getStock());
            items.add(item);
        }
        return items;
    }

    public void addItem(CartItemRequest request) {
        Long userId = requireUserId();
        productService.requireOnSaleProduct(request.getProductId());
        String key = cartKey(userId);
        String field = String.valueOf(request.getProductId());
        String existing = (String) redisTemplate.opsForHash().get(key, field);
        CartEntry entry;
        if (existing != null) {
            entry = parseEntry(existing);
            entry.setQuantity(entry.getQuantity() + request.getQuantity());
            if (request.getSelected() != null) {
                entry.setSelected(request.getSelected());
            }
        } else {
            entry = new CartEntry();
            entry.setQuantity(request.getQuantity());
            entry.setSelected(request.getSelected() == null || request.getSelected());
        }
        redisTemplate.opsForHash().put(key, field, toJson(entry));
    }

    public void updateItem(Long productId, UpdateCartItemRequest request) {
        Long userId = requireUserId();
        String key = cartKey(userId);
        String field = String.valueOf(productId);
        String existing = (String) redisTemplate.opsForHash().get(key, field);
        if (existing == null) {
            throw new BusinessException(ErrorCode.PRODUCT_NOT_FOUND, "购物车中无该商品");
        }
        CartEntry entry = parseEntry(existing);
        entry.setQuantity(request.getQuantity());
        if (request.getSelected() != null) {
            entry.setSelected(request.getSelected());
        }
        redisTemplate.opsForHash().put(key, field, toJson(entry));
    }

    public void removeItem(Long productId) {
        redisTemplate.opsForHash().delete(cartKey(requireUserId()), String.valueOf(productId));
    }

    public void clear() {
        redisTemplate.delete(cartKey(requireUserId()));
    }

    public Map<Long, CartEntry> getSelectedItems() {
        Long userId = requireUserId();
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(cartKey(userId));
        java.util.HashMap<Long, CartEntry> selected = new java.util.HashMap<>();
        for (Map.Entry<Object, Object> entry : entries.entrySet()) {
            CartEntry cartEntry = parseEntry(entry.getValue().toString());
            if (Boolean.TRUE.equals(cartEntry.getSelected())) {
                selected.put(Long.valueOf(entry.getKey().toString()), cartEntry);
            }
        }
        return selected;
    }

    public CartEntry getItem(Long productId) {
        String value = (String) redisTemplate.opsForHash().get(cartKey(requireUserId()), String.valueOf(productId));
        return value == null ? null : parseEntry(value);
    }

    public void removeItems(List<Long> productIds) {
        if (productIds == null || productIds.isEmpty()) {
            return;
        }
        String key = cartKey(requireUserId());
        productIds.forEach(id -> redisTemplate.opsForHash().delete(key, String.valueOf(id)));
    }

    private String cartKey(Long userId) {
        return CART_KEY_PREFIX + userId;
    }

    private Long requireUserId() {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "请先登录");
        }
        return userId;
    }

    private CartEntry parseEntry(String json) {
        try {
            return objectMapper.readValue(json, CartEntry.class);
        } catch (JsonProcessingException e) {
            throw new BusinessException(ErrorCode.ORDER_STATUS_ERROR, "Error");
        }
    }

    private String toJson(CartEntry entry) {
        try {
            return objectMapper.writeValueAsString(entry);
        } catch (JsonProcessingException e) {
            throw new BusinessException(ErrorCode.ORDER_STATUS_ERROR, "Error");
        }
    }

    private String extractFirstImage(String images) {
        if (images == null || images.isBlank()) {
            return null;
        }
        if (images.startsWith("[")) {
            try {
                List<String> list = objectMapper.readValue(images, List.class);
                return list.isEmpty() ? null : list.get(0);
            } catch (JsonProcessingException e) {
                return images;
            }
        }
        return images;
    }

    @Data
    public static class CartEntry {
        private Integer quantity;
        private Boolean selected;
    }
}
