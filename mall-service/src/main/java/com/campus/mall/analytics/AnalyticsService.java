package com.campus.mall.analytics;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.common.core.enums.OrderStatus;
import com.campus.common.core.enums.ProductStatus;
import com.campus.mall.dto.response.HotProductResponse;
import com.campus.mall.dto.response.MerchantAnalyticsResponse;
import com.campus.mall.dto.response.SalesReportResponse;
import com.campus.mall.dto.response.UserActivityResponse;
import com.campus.mall.entity.*;
import com.campus.mall.mapper.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final SalesReportMapper salesReportMapper;
    private final HotProductMapper hotProductMapper;
    private final LoginLogMapper loginLogMapper;
    private final UserMapper userMapper;
    private final OrderMapper orderMapper;
    private final ProductMapper productMapper;

    public List<SalesReportResponse> sales(LocalDate start, LocalDate end) {
        LambdaQueryWrapper<SalesReport> wrapper = new LambdaQueryWrapper<>();
        if (start != null) {
            wrapper.ge(SalesReport::getReportDate, start);
        }
        if (end != null) {
            wrapper.le(SalesReport::getReportDate, end);
        }
        wrapper.orderByDesc(SalesReport::getReportDate);
        return salesReportMapper.selectList(wrapper).stream().map(r -> {
            SalesReportResponse resp = new SalesReportResponse();
            resp.setDate(r.getReportDate() == null ? null : r.getReportDate().toString());
            resp.setTotalAmount(r.getTotalAmount());
            resp.setOrderCount(r.getOrderCount());
            return resp;
        }).collect(Collectors.toList());
    }

    public List<HotProductResponse> hotProducts(LocalDate date, int limit) {
        LocalDate reportDate = date != null ? date : LocalDate.now();
        List<HotProduct> hotList = hotProductMapper.selectList(new LambdaQueryWrapper<HotProduct>()
                .eq(HotProduct::getReportDate, reportDate)
                .orderByDesc(HotProduct::getSalesCount)
                .last("LIMIT " + limit));
        if (hotList.isEmpty()) {
            return productMapper.selectList(new LambdaQueryWrapper<Product>()
                    .eq(Product::getStatus, ProductStatus.ON_SALE.getCode())
                    .orderByDesc(Product::getSalesCount)
                    .last("LIMIT " + limit)).stream().map(p -> {
                HotProductResponse resp = new HotProductResponse();
                resp.setProductId(p.getId());
                resp.setTitle(p.getTitle());
                resp.setSalesCount(p.getSalesCount());
                return resp;
            }).collect(Collectors.toList());
        }
        List<HotProductResponse> result = new ArrayList<>();
        for (HotProduct hot : hotList) {
            Product product = productMapper.selectById(hot.getProductId());
            if (product != null) {
                HotProductResponse resp = new HotProductResponse();
                resp.setProductId(product.getId());
                resp.setTitle(product.getTitle());
                resp.setSalesCount(hot.getSalesCount());
                result.add(resp);
            }
        }
        return result;
    }

    public UserActivityResponse userActivity() {
        long activeUsers = loginLogMapper.selectCount(new LambdaQueryWrapper<LoginLog>().eq(LoginLog::getResult, 1));
        long newUsers = userMapper.selectCount(new LambdaQueryWrapper<User>()
                .ge(User::getCreateTime, LocalDate.now().atStartOfDay()));
        long orderCount = orderMapper.selectCount(new LambdaQueryWrapper<Order>()
                .ge(Order::getCreateTime, LocalDate.now().atStartOfDay()));
        UserActivityResponse resp = new UserActivityResponse();
        resp.setActiveUsers((int) activeUsers);
        resp.setNewUsers((int) newUsers);
        resp.setOrderCount((int) orderCount);
        return resp;
    }

    public MerchantAnalyticsResponse merchantAnalytics(Long merchantId) {
        int productCount = productMapper.selectCount(new LambdaQueryWrapper<Product>()
                .eq(Product::getMerchantId, merchantId)).intValue();
        List<Order> orders = orderMapper.selectList(new LambdaQueryWrapper<Order>()
                .eq(Order::getMerchantId, merchantId)
                .in(Order::getStatus,
                        OrderStatus.PAID.getCode(),
                        OrderStatus.TO_SHIP.getCode(),
                        OrderStatus.SHIPPED.getCode(),
                        OrderStatus.COMPLETED.getCode()));
        BigDecimal totalSales = orders.stream()
                .map(Order::getPayAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        MerchantAnalyticsResponse resp = new MerchantAnalyticsResponse();
        resp.setProductCount(productCount);
        resp.setOrderCount(orders.size());
        resp.setTotalSales(totalSales);
        return resp;
    }
}
