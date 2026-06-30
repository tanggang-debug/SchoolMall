package com.campus.mall.review;

import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.common.core.constant.ErrorCode;
import com.campus.common.core.enums.OrderStatus;
import com.campus.common.core.enums.UserRole;
import com.campus.common.core.exception.BusinessException;
import com.campus.common.security.UserContext;
import com.campus.mall.dto.request.CreateReviewRequest;
import com.campus.mall.dto.request.ReviewReplyRequest;
import com.campus.mall.dto.response.PageResult;
import com.campus.mall.dto.response.ReviewReplyResponse;
import com.campus.mall.dto.response.ReviewResponse;
import com.campus.mall.entity.Order;
import com.campus.mall.entity.Review;
import com.campus.mall.entity.ReviewReply;
import com.campus.mall.mapper.ReviewMapper;
import com.campus.mall.mapper.ReviewReplyMapper;
import com.campus.mall.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewMapper reviewMapper;
    private final ReviewReplyMapper reviewReplyMapper;
    private final OrderService orderService;

    @Transactional
    public ReviewResponse create(CreateReviewRequest request) {
        Long userId = UserContext.getUserId();
        Order order = orderService.requireOrder(request.getOrderId());
        if (!order.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "Error");
        }
        if (order.getStatus() != OrderStatus.COMPLETED.getCode()) {
            throw new BusinessException(ErrorCode.ORDER_STATUS_ERROR, "Error");
        }
        Long exists = reviewMapper.selectCount(new LambdaQueryWrapper<Review>()
                .eq(Review::getOrderId, request.getOrderId()));
        if (exists > 0) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "该订单已评价");
        }
        Review review = new Review();
        review.setOrderId(request.getOrderId());
        review.setProductId(request.getProductId());
        review.setUserId(userId);
        review.setRating(request.getRating());
        review.setContent(request.getContent());
        review.setImages(request.getImages());
        review.setCreateTime(LocalDateTime.now());
        reviewMapper.insert(review);
        return toResponse(review, null);
    }

    public PageResult<ReviewResponse> listByProduct(Long productId, int page, int size) {
        Page<Review> result = reviewMapper.selectPage(new Page<>(page, size),
                new LambdaQueryWrapper<Review>()
                        .eq(Review::getProductId, productId)
                        .orderByDesc(Review::getCreateTime));
        return PageResult.of(result.getTotal(), result.getCurrent(), result.getSize(),
                result.getRecords().stream()
                        .map(r -> toResponse(r, loadReply(r.getId())))
                        .collect(java.util.stream.Collectors.toList()));
    }

    @Transactional
    public ReviewReplyResponse reply(Long reviewId, ReviewReplyRequest request) {
        Review review = reviewMapper.selectById(reviewId);
        if (review == null) {
            throw new BusinessException(ErrorCode.PRODUCT_NOT_FOUND, "Error");
        }
        if (UserContext.get().getRole() != UserRole.MERCHANT.getCode()
                && UserContext.get().getRole() != UserRole.ADMIN.getCode()) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "仅商户可回复");
        }
        ReviewReply existing = reviewReplyMapper.selectOne(new LambdaQueryWrapper<ReviewReply>()
                .eq(ReviewReply::getReviewId, reviewId));
        if (existing != null) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "Error");
        }
        ReviewReply reply = new ReviewReply();
        reply.setReviewId(reviewId);
        reply.setMerchantId(UserContext.getUserId());
        reply.setContent(request.getContent());
        reply.setCreateTime(LocalDateTime.now());
        reviewReplyMapper.insert(reply);
        ReviewReplyResponse replyResp = new ReviewReplyResponse();
        replyResp.setId(reply.getId());
        replyResp.setContent(reply.getContent());
        replyResp.setCreateTime(reply.getCreateTime() == null ? null : reply.getCreateTime().toString());
        return replyResp;
    }

    private ReviewReply loadReply(Long reviewId) {
        return reviewReplyMapper.selectOne(new LambdaQueryWrapper<ReviewReply>()
                .eq(ReviewReply::getReviewId, reviewId));
    }

    private ReviewResponse toResponse(Review review, ReviewReply reply) {
        ReviewReplyResponse replyResponse = null;
        if (reply != null) {
            replyResponse = new ReviewReplyResponse();
            replyResponse.setId(reply.getId());
            replyResponse.setContent(reply.getContent());
            replyResponse.setCreateTime(reply.getCreateTime() == null ? null : reply.getCreateTime().toString());
        }
        ReviewResponse resp = new ReviewResponse();
        resp.setId(review.getId());
        resp.setOrderId(review.getOrderId());
        resp.setProductId(review.getProductId());
        resp.setUserId(review.getUserId());
        resp.setRating(review.getRating());
        resp.setContent(review.getContent());
        resp.setImages(review.getImages());
        resp.setCreateTime(review.getCreateTime() == null ? null : review.getCreateTime().toString());
        resp.setReply(replyResponse);
        return resp;
    }
}
