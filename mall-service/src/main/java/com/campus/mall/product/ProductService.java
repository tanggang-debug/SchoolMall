package com.campus.mall.product;

import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.common.core.constant.ErrorCode;
import com.campus.common.core.enums.ProductStatus;
import com.campus.common.core.enums.UserRole;
import com.campus.common.core.exception.BusinessException;
import com.campus.common.security.UserContext;
import com.campus.mall.dto.request.ProductAuditRequest;
import com.campus.mall.dto.request.ProductRequest;
import com.campus.mall.dto.response.PageResult;
import com.campus.mall.dto.response.ProductResponse;
import com.campus.mall.entity.Product;
import com.campus.mall.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductMapper productMapper;

    public ProductResponse getById(Long id) {
        Product product = requireProduct(id);
        product.setViewCount(product.getViewCount() == null ? 1 : product.getViewCount() + 1);
        productMapper.updateById(product);
        return toResponse(product);
    }

    public PageResult<ProductResponse> list(int page, int size, String keyword, Long categoryId, Integer status) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like(Product::getTitle, keyword).or().like(Product::getDescription, keyword));
        }
        if (categoryId != null) {
            wrapper.eq(Product::getCategoryId, categoryId);
        }
        if (status != null) {
            wrapper.eq(Product::getStatus, status);
        } else {
            wrapper.eq(Product::getStatus, ProductStatus.ON_SALE.getCode());
        }
        wrapper.orderByDesc(Product::getCreateTime);
        Page<Product> result = productMapper.selectPage(new Page<>(page, size), wrapper);
        return PageResult.of(result.getTotal(), result.getCurrent(), result.getSize(),
                result.getRecords().stream().map(this::toResponse).collect(java.util.stream.Collectors.toList()));
    }

    @Transactional
    public ProductResponse create(ProductRequest request) {
        LoginUserCheck.merchant();
        Product product = new Product();
        product.setMerchantId(UserContext.getUserId());
        product.setCategoryId(request.getCategoryId());
        product.setTitle(request.getTitle());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setOriginalPrice(request.getOriginalPrice());
        product.setStock(request.getStock());
        product.setImages(request.getImages());
        product.setStatus(ProductStatus.PENDING.getCode());
        product.setViewCount(0);
        product.setSalesCount(0);
        product.setCreateTime(LocalDateTime.now());
        product.setUpdateTime(LocalDateTime.now());
        productMapper.insert(product);
        return toResponse(product);
    }

    @Transactional
    public ProductResponse update(Long id, ProductRequest request) {
        Product product = requireProduct(id);
        checkMerchantOwner(product);
        product.setCategoryId(request.getCategoryId());
        product.setTitle(request.getTitle());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setOriginalPrice(request.getOriginalPrice());
        product.setStock(request.getStock());
        product.setImages(request.getImages());
        product.setUpdateTime(LocalDateTime.now());
        productMapper.updateById(product);
        return toResponse(product);
    }

    @Transactional
    public void delete(Long id) {
        Product product = requireProduct(id);
        checkMerchantOwner(product);
        ProductStatus current = ProductStateMachine.of(product.getStatus());
        if (current == ProductStatus.ON_SALE) {
            changeStatus(product, ProductStatus.OFF_SHELF);
        } else {
            productMapper.deleteById(id);
        }
    }

    @Transactional
    public ProductResponse audit(Long id, ProductAuditRequest request) {
        Product product = requireProduct(id);
        ProductStatus current = ProductStateMachine.of(product.getStatus());
        if (current != ProductStatus.PENDING) {
            throw new BusinessException(ErrorCode.ORDER_STATUS_ERROR, "Error");
        }
        if (Boolean.TRUE.equals(request.getApproved())) {
            changeStatus(product, ProductStatus.ON_SALE);
            product.setRejectReason(null);
        } else {
            if (!StringUtils.hasText(request.getRejectReason())) {
                throw new BusinessException(ErrorCode.FORBIDDEN, "拒绝原因不能为空");
            }
            changeStatus(product, ProductStatus.REJECTED);
            product.setRejectReason(request.getRejectReason());
        }
        product.setUpdateTime(LocalDateTime.now());
        productMapper.updateById(product);
        return toResponse(product);
    }

    @Transactional
    public ProductResponse offShelf(Long id) {
        Product product = requireProduct(id);
        checkMerchantOwner(product);
        changeStatus(product, ProductStatus.OFF_SHELF);
        product.setUpdateTime(LocalDateTime.now());
        productMapper.updateById(product);
        return toResponse(product);
    }

    @Transactional
    public ProductResponse onShelf(Long id) {
        Product product = requireProduct(id);
        checkMerchantOwner(product);
        ProductStatus current = ProductStateMachine.of(product.getStatus());
        ProductStatus target = current == ProductStatus.REJECTED ? ProductStatus.PENDING : ProductStatus.ON_SALE;
        changeStatus(product, target);
        product.setUpdateTime(LocalDateTime.now());
        productMapper.updateById(product);
        return toResponse(product);
    }

    public Product requireProduct(Long id) {
        Product product = productMapper.selectById(id);
        if (product == null) {
            throw new BusinessException(ErrorCode.PRODUCT_NOT_FOUND, "Error");
        }
        return product;
    }

    public Product requireOnSaleProduct(Long id) {
        Product product = requireProduct(id);
        if (product.getStatus() != ProductStatus.ON_SALE.getCode()) {
            throw new BusinessException(ErrorCode.PRODUCT_NOT_FOUND, "Error");
        }
        return product;
    }

    @Transactional
    public void deductStock(Long productId, int quantity) {
        Product product = requireOnSaleProduct(productId);
        if (product.getStock() < quantity) {
            throw new BusinessException(ErrorCode.STOCK_NOT_ENOUGH, "库存不足: " + product.getTitle());
        }
        product.setStock(product.getStock() - quantity);
        productMapper.updateById(product);
    }

    @Transactional
    public void restoreStock(Long productId, int quantity) {
        Product product = requireProduct(productId);
        product.setStock(product.getStock() + quantity);
        productMapper.updateById(product);
    }

    @Transactional
    public void increaseSales(Long productId, int quantity) {
        Product product = requireProduct(productId);
        product.setSalesCount((product.getSalesCount() == null ? 0 : product.getSalesCount()) + quantity);
        productMapper.updateById(product);
    }

    private void changeStatus(Product product, ProductStatus target) {
        ProductStatus current = ProductStateMachine.of(product.getStatus());
        ProductStateMachine.validateTransition(current, target);
        product.setStatus(target.getCode());
    }

    private void checkMerchantOwner(Product product) {
        if (UserContext.get().getRole() == UserRole.ADMIN.getCode()) {
            return;
        }
        if (UserContext.get().getRole() != UserRole.MERCHANT.getCode()
                || !product.getMerchantId().equals(UserContext.getUserId())) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "Error");
        }
    }

    ProductResponse toResponse(Product product) {
        ProductResponse resp = new ProductResponse();
        resp.setId(product.getId());
        resp.setMerchantId(product.getMerchantId());
        resp.setCategoryId(product.getCategoryId());
        resp.setTitle(product.getTitle());
        resp.setDescription(product.getDescription());
        resp.setPrice(product.getPrice());
        resp.setOriginalPrice(product.getOriginalPrice());
        resp.setStock(product.getStock());
        resp.setImages(product.getImages());
        resp.setStatus(product.getStatus());
        resp.setViewCount(product.getViewCount());
        resp.setSalesCount(product.getSalesCount());
        resp.setCreateTime(product.getCreateTime() == null ? null : product.getCreateTime().toString());
        return resp;
    }

    private static final class LoginUserCheck {
        static void merchant() {
            if (UserContext.get() == null) {
                throw new BusinessException(ErrorCode.UNAUTHORIZED, "请先登录");
            }
            if (UserContext.get().getRole() != UserRole.MERCHANT.getCode()
                    && UserContext.get().getRole() != UserRole.ADMIN.getCode()) {
                throw new BusinessException(ErrorCode.FORBIDDEN, "仅商户可发布商品");
            }
        }
    }
}
