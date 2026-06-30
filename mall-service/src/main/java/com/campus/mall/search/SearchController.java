package com.campus.mall.search;

import com.campus.common.core.result.Result;
import com.campus.mall.dto.response.PageResult;
import com.campus.mall.dto.response.ProductResponse;
import com.campus.mall.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final ProductService productService;

    @GetMapping
    public Result<PageResult<ProductResponse>> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.ok(productService.list(page, size, keyword, categoryId,
                com.campus.common.core.enums.ProductStatus.ON_SALE.getCode()));
    }
}
