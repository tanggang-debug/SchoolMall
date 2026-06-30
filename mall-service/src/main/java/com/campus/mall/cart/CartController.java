package com.campus.mall.cart;

import com.campus.common.core.result.Result;
import com.campus.common.security.RequireLogin;
import com.campus.mall.dto.request.CartItemRequest;
import com.campus.mall.dto.request.UpdateCartItemRequest;
import com.campus.mall.dto.response.CartItemResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    @RequireLogin
    public Result<List<CartItemResponse>> list() {
        return Result.ok(cartService.list());
    }

    @PostMapping("/items")
    @RequireLogin
    public Result<Void> addItem(@Valid @RequestBody CartItemRequest request) {
        cartService.addItem(request);
        return Result.ok();
    }

    @PutMapping("/items/{productId}")
    @RequireLogin
    public Result<Void> updateItem(@PathVariable Long productId,
                                   @Valid @RequestBody UpdateCartItemRequest request) {
        cartService.updateItem(productId, request);
        return Result.ok();
    }

    @DeleteMapping("/items/{productId}")
    @RequireLogin
    public Result<Void> removeItem(@PathVariable Long productId) {
        cartService.removeItem(productId);
        return Result.ok();
    }

    @DeleteMapping("/clear")
    @RequireLogin
    public Result<Void> clear() {
        cartService.clear();
        return Result.ok();
    }
}
