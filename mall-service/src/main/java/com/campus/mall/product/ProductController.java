package com.campus.mall.product;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.common.core.result.Result;
import com.campus.common.security.RequireLogin;
import com.campus.common.security.RequireRole;
import com.campus.mall.dto.request.ProductAuditRequest;
import com.campus.mall.dto.request.ProductRequest;
import com.campus.mall.dto.response.PageResult;
import com.campus.mall.dto.response.ProductResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public Result<PageResult<ProductResponse>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Integer status) {
        return Result.ok(productService.list(page, size, keyword, categoryId, status));
    }

    @GetMapping("/{id}")
    public Result<ProductResponse> getById(@PathVariable Long id) {
        return Result.ok(productService.getById(id));
    }

    @PostMapping
    @RequireLogin
    public Result<ProductResponse> create(@Valid @RequestBody ProductRequest request) {
        return Result.ok(productService.create(request));
    }

    @PutMapping("/{id}")
    @RequireLogin
    public Result<ProductResponse> update(@PathVariable Long id, @Valid @RequestBody ProductRequest request) {
        return Result.ok(productService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @RequireLogin
    public Result<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return Result.ok();
    }

    @PutMapping("/{id}/audit")
    @RequireRole(3)
    public Result<ProductResponse> audit(@PathVariable Long id, @Valid @RequestBody ProductAuditRequest request) {
        return Result.ok(productService.audit(id, request));
    }

    @PutMapping("/{id}/off-shelf")
    @RequireLogin
    public Result<ProductResponse> offShelf(@PathVariable Long id) {
        return Result.ok(productService.offShelf(id));
    }

    @PutMapping("/{id}/on-shelf")
    @RequireLogin
    public Result<ProductResponse> onShelf(@PathVariable Long id) {
        return Result.ok(productService.onShelf(id));
    }
}
