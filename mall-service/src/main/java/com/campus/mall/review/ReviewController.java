package com.campus.mall.review;

import com.campus.common.core.result.Result;
import com.campus.common.security.RequireLogin;
import com.campus.common.security.RequireRole;
import com.campus.mall.dto.request.CreateReviewRequest;
import com.campus.mall.dto.request.ReviewReplyRequest;
import com.campus.mall.dto.response.PageResult;
import com.campus.mall.dto.response.ReviewReplyResponse;
import com.campus.mall.dto.response.ReviewResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    @RequireLogin
    public Result<ReviewResponse> create(@Valid @RequestBody CreateReviewRequest request) {
        return Result.ok(reviewService.create(request));
    }

    @GetMapping("/products/{productId}/reviews")
    public Result<PageResult<ReviewResponse>> listByProduct(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.ok(reviewService.listByProduct(productId, page, size));
    }

    @PostMapping("/{id}/reply")
    @RequireRole(2)
    public Result<ReviewReplyResponse> reply(@PathVariable Long id,
                                             @Valid @RequestBody ReviewReplyRequest request) {
        return Result.ok(reviewService.reply(id, request));
    }
}
