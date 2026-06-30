package com.campus.mall.product;

import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.common.core.result.Result;
import com.campus.mall.dto.response.CategoryResponse;
import com.campus.mall.entity.Category;
import com.campus.mall.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryMapper categoryMapper;

    @GetMapping
    public Result<List<CategoryResponse>> list() {
        List<CategoryResponse> list = categoryMapper.selectList(new LambdaQueryWrapper<Category>()
                        .orderByAsc(Category::getSortOrder))
                .stream()
                .map(c -> {
                    CategoryResponse resp = new CategoryResponse();
                    resp.setId(c.getId());
                    resp.setName(c.getName());
                    resp.setSortOrder(c.getSortOrder());
                    return resp;
                })
                .collect(Collectors.toList());
        return Result.ok(list);
    }
}
