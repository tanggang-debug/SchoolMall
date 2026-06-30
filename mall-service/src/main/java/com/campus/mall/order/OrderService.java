package com.campus.mall.order;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.common.core.constant.ErrorCode;
import com.campus.common.core.enums.OrderStatus;
import com.campus.common.core.enums.UserRole;
import com.campus.common.core.exception.BusinessException;
import com.campus.common.security.UserContext;
import com.campus.mall.cart.CartService;
import com.campus.mall.dto.request.CreateOrderRequest;
import com.campus.mall.dto.request.OrderItemRequest;
import com.campus.mall.dto.response.OrderItemResponse;
import com.campus.mall.dto.response.OrderResponse;
import com.campus.mall.dto.response.PageResult;
import com.campus.mall.entity.*;
import com.campus.mall.mapper.OrderEventMapper;
import com.campus.mall.mapper.OrderItemMapper;
import com.campus.mall.mapper.OrderMapper;
import com.campus.mall.notify.NotificationService;
import com.campus.mall.product.ProductService;
import com.campus.mall.user.AddressService;
import com.campus.mall.util.BizNoUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final OrderEventMapper orderEventMapper;
    private final ProductService productService;
    private final AddressService addressService;
    private final CartService cartService;
    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    @Transactional
    public List<OrderResponse> create(CreateOrderRequest request) {
        Long userId = UserContext.getUserId();
        Address address = addressService.getByIdAndUserId(request.getAddressId(), userId);
        String addressSnapshot = toAddressJson(address);
        Map<Long, Integer> itemMap = resolveItems(request);

        Map<Long, List<Map.Entry<Long, Integer>>> byMerchant = new HashMap<>();
        for (Map.Entry<Long, Integer> entry : itemMap.entrySet()) {
            Product product = productService.requireOnSaleProduct(entry.getKey());
            byMerchant.computeIfAbsent(product.getMerchantId(), k -> new ArrayList<>()).add(entry);
        }

        List<OrderResponse> orders = new ArrayList<>();
        List<Long> purchasedProductIds = new ArrayList<>();
        for (Map.Entry<Long, List<Map.Entry<Long, Integer>>> merchantEntry : byMerchant.entrySet()) {
            Order order = new Order();
            order.setOrderNo(BizNoUtil.orderNo());
            order.setUserId(userId);
            order.setMerchantId(merchantEntry.getKey());
            order.setAddressSnapshot(addressSnapshot);
            order.setRemark(request.getRemark());
            order.setStatus(OrderStatus.UNPAID.getCode());
            order.setVersion(0);
            order.setCreateTime(LocalDateTime.now());
            order.setUpdateTime(LocalDateTime.now());

            BigDecimal total = BigDecimal.ZERO;
            List<OrderItem> orderItems = new ArrayList<>();
            for (Map.Entry<Long, Integer> item : merchantEntry.getValue()) {
                Product product = productService.requireOnSaleProduct(item.getKey());
                productService.deductStock(product.getId(), item.getValue());
                purchasedProductIds.add(product.getId());
                BigDecimal lineAmount = product.getPrice().multiply(BigDecimal.valueOf(item.getValue()));
                total = total.add(lineAmount);
                OrderItem orderItem = new OrderItem();
                orderItem.setProductId(product.getId());
                orderItem.setProductTitle(product.getTitle());
                orderItem.setProductImage(extractFirstImage(product.getImages()));
                orderItem.setPrice(product.getPrice());
                orderItem.setQuantity(item.getValue());
                orderItems.add(orderItem);
            }
            order.setTotalAmount(total);
            order.setPayAmount(total);
            orderMapper.insert(order);
            for (OrderItem orderItem : orderItems) {
                orderItem.setOrderId(order.getId());
                orderItemMapper.insert(orderItem);
            }
            recordEvent(order.getId(), null, OrderStatus.UNPAID, "创建订单");
            notificationService.send(userId, "Order Created",
                    "Order " + order.getOrderNo() + " created, please pay", "ORDER_CREATED", order.getOrderNo());
            orders.add(toResponse(order, orderItems));
        }

        if (CollectionUtils.isEmpty(request.getItems())) {
            cartService.removeItems(purchasedProductIds);
        }
        return orders;
    }

    public PageResult<OrderResponse> list(int page, int size, Integer status) {
        Long userId = UserContext.getUserId();
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        if (UserContext.get().getRole() == UserRole.MERCHANT.getCode()) {
            wrapper.eq(Order::getMerchantId, userId);
        } else if (UserContext.get().getRole() != UserRole.ADMIN.getCode()) {
            wrapper.eq(Order::getUserId, userId);
        }
        if (status != null) {
            wrapper.eq(Order::getStatus, status);
        }
        wrapper.orderByDesc(Order::getCreateTime);
        Page<Order> result = orderMapper.selectPage(new Page<>(page, size), wrapper);
        return PageResult.of(result.getTotal(), result.getCurrent(), result.getSize(),
                result.getRecords().stream().map(o -> toResponse(o, loadItems(o.getId()))).collect(java.util.stream.Collectors.toList()));
    }

    public OrderResponse getById(Long id) {
        Order order = requireOrder(id);
        checkOrderAccess(order);
        return toResponse(order, loadItems(order.getId()));
    }

    @Transactional
    public OrderResponse cancel(Long id) {
        Order order = requireOrder(id);
        checkBuyer(order);
        OrderStatus current = OrderStateMachine.of(order.getStatus());
        if (!OrderStateMachine.canCancel(current)) {
            throw new BusinessException(ErrorCode.ORDER_STATUS_ERROR, "Error");
        }
        transition(order, current, OrderStatus.CANCELLED, "用户取消订单");
        order.setCancelTime(LocalDateTime.now());
        orderMapper.updateById(order);
        restoreOrderStock(order.getId());
        notificationService.send(order.getUserId(), "Order Cancelled",
                "Order " + order.getOrderNo() + " cancelled", "ORDER_CANCELLED", order.getOrderNo());
        return toResponse(order, loadItems(order.getId()));
    }

    @Transactional
    public OrderResponse confirm(Long id) {
        Order order = requireOrder(id);
        checkBuyer(order);
        OrderStatus current = OrderStateMachine.of(order.getStatus());
        if (!OrderStateMachine.canConfirm(current)) {
            throw new BusinessException(ErrorCode.ORDER_STATUS_ERROR, "Error");
        }
        transition(order, current, OrderStatus.COMPLETED, "确认收货");
        order.setConfirmTime(LocalDateTime.now());
        orderMapper.updateById(order);
        List<OrderItem> items = loadItems(order.getId());
        for (OrderItem item : items) {
            productService.increaseSales(item.getProductId(), item.getQuantity());
        }
        notificationService.send(order.getUserId(), "Order Completed",
                "Order " + order.getOrderNo() + " completed", "ORDER_COMPLETED", order.getOrderNo());
        return toResponse(order, items);
    }

    @Transactional
    public OrderResponse ship(Long id, String logisticsNo) {
        Order order = requireOrder(id);
        checkMerchant(order);
        OrderStatus current = OrderStateMachine.of(order.getStatus());
        if (!OrderStateMachine.canShip(current)) {
            throw new BusinessException(ErrorCode.ORDER_STATUS_ERROR, "Error");
        }
        transition(order, current, OrderStatus.SHIPPED, "商户发货: " + logisticsNo);
        order.setLogisticsNo(logisticsNo);
        order.setShipTime(LocalDateTime.now());
        orderMapper.updateById(order);
        notificationService.send(order.getUserId(), "Order Shipped",
                "Order " + order.getOrderNo() + " shipped, logistics: " + logisticsNo,
                "ORDER_SHIPPED", order.getOrderNo());
        return toResponse(order, loadItems(order.getId()));
    }

    @Transactional
    public OrderResponse refund(Long id, String reason) {
        Order order = requireOrder(id);
        checkBuyer(order);
        OrderStatus current = OrderStateMachine.of(order.getStatus());
        if (!OrderStateMachine.canRefund(current)) {
            throw new BusinessException(ErrorCode.REFUND_FAILED, "Error");
        }

        transition(order, current, OrderStatus.REFUNDING, "Refund: " + (reason == null ? "" : reason));

        // 【核心改动】不传实体，直接通过 wrapper 指定列更新
        orderMapper.update(null, new LambdaUpdateWrapper<Order>()
                .set(Order::getStatus, order.getStatus())
                .eq(Order::getId, order.getId())
                .eq(Order::getVersion, order.getVersion()) // 手动附加乐观锁条件
        );

        notificationService.send(order.getMerchantId(), "Refund Request",
                "Order " + order.getOrderNo() + " refund requested", "ORDER_REFUND", order.getOrderNo());

        return toResponse(order, loadItems(order.getId()));
    }

    @Transactional
    public void onPaid(String orderNo) {
        // 1. 查询出最新的订单数据
        Order order = orderMapper.selectOne(new LambdaQueryWrapper<Order>().eq(Order::getOrderNo, orderNo));
        if (order == null) {
            throw new BusinessException(ErrorCode.ORDER_NOT_FOUND, "Error");
        }

        OrderStatus current = OrderStateMachine.of(order.getStatus());
        if (current != OrderStatus.UNPAID) {
            return;
        }

        // 2. 纯内存操作：执行状态流转和字段赋值
        transition(order, current, OrderStatus.PAID, "支付成功");
        order.setPayTime(LocalDateTime.now());
        order.setStatus(1);

        OrderStatus paid = OrderStateMachine.of(order.getStatus());
        transition(order, paid, OrderStatus.TO_SHIP, "To ship");

        // 3. 核心改动：使用 LambdaUpdateWrapper 显式指定更新字段，避开直接传 entity 的坑
        boolean success = orderMapper.update(null, new LambdaUpdateWrapper<Order>()
                .set(Order::getStatus, order.getStatus())
                .set(Order::getPayTime, order.getPayTime())
                // 如果 transition 方法里还修改了其他字段，请在这里一条条 .set() 加上
                // .set(Order::getSomeOtherField, order.getSomeOtherField())
                .eq(Order::getId, order.getId())
                .eq(Order::getVersion, order.getVersion()) // 手动带上乐观锁版本号
        ) > 0;

        if (!success) {
            throw new BusinessException(500, "订单状态已被修改，请稍后重试（乐观锁冲突）");
        }

        // 4. 发送通知
        notificationService.send(order.getMerchantId(), "New Order",
                "Order " + order.getOrderNo() + " paid, please ship", "ORDER_PAID", order.getOrderNo());
    }

    public Order requireOrder(Long id) {
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new BusinessException(ErrorCode.ORDER_NOT_FOUND, "Error");
        }
        return order;
    }

    public Order requireByOrderNo(String orderNo) {
        Order order = orderMapper.selectOne(new LambdaQueryWrapper<Order>().eq(Order::getOrderNo, orderNo));
        if (order == null) {
            throw new BusinessException(ErrorCode.ORDER_NOT_FOUND, "Error");
        }
        return order;
    }

    private Map<Long, Integer> resolveItems(CreateOrderRequest request) {
        Map<Long, Integer> itemMap = new LinkedHashMap<>();
        if (!CollectionUtils.isEmpty(request.getItems())) {
            for (OrderItemRequest item : request.getItems()) {
                itemMap.merge(item.getProductId(), item.getQuantity(), Integer::sum);
            }
        } else if (!CollectionUtils.isEmpty(request.getProductIds())) {
            for (Long productId : request.getProductIds()) {
                CartService.CartEntry entry = cartService.getItem(productId);
                if (entry == null) {
                    throw new BusinessException(ErrorCode.PRODUCT_NOT_FOUND, "Cart item not found: " + productId);
                }
                itemMap.put(productId, entry.getQuantity());
            }
        } else {
            Map<Long, CartService.CartEntry> selected = cartService.getSelectedItems();
            if (selected.isEmpty()) {
                throw new BusinessException(ErrorCode.ORDER_STATUS_ERROR, "请选择商品");
            }
            selected.forEach((pid, entry) -> itemMap.put(pid, entry.getQuantity()));
        }
        return itemMap;
    }

    private void restoreOrderStock(Long orderId) {
        for (OrderItem item : loadItems(orderId)) {
            productService.restoreStock(item.getProductId(), item.getQuantity());
        }
    }

    private List<OrderItem> loadItems(Long orderId) {
        return orderItemMapper.selectList(new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, orderId));
    }

    private void transition(Order order, OrderStatus from, OrderStatus to, String remark) {
        OrderStateMachine.validateTransition(from, to);
        order.setStatus(to.getCode());
        order.setUpdateTime(LocalDateTime.now());
        recordEvent(order.getId(), from, to, remark);
    }

    private void recordEvent(Long orderId, OrderStatus from, OrderStatus to, String remark) {
        OrderEvent event = new OrderEvent();
        event.setOrderId(orderId);
        event.setFromStatus(from == null ? null : from.getCode());
        event.setToStatus(to.getCode());
        event.setOperatorId(UserContext.getUserId());
        event.setOperatorRole(UserContext.get() == null ? null : UserContext.get().getRole());
        event.setRemark(remark);
        event.setEventTime(LocalDateTime.now());
        orderEventMapper.insert(event);
    }

    private void checkOrderAccess(Order order) {
        if (UserContext.get().getRole() == UserRole.ADMIN.getCode()) {
            return;
        }
        if (UserContext.get().getRole() == UserRole.MERCHANT.getCode()
                && order.getMerchantId().equals(UserContext.getUserId())) {
            return;
        }
        if (order.getUserId().equals(UserContext.getUserId())) {
            return;
        }
        throw new BusinessException(ErrorCode.FORBIDDEN, "无权查看订单");
    }

    private void checkBuyer(Order order) {
        if (!order.getUserId().equals(UserContext.getUserId())) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "无权操作订单");
        }
    }

    private void checkMerchant(Order order) {
        if (UserContext.get().getRole() != UserRole.MERCHANT.getCode()
                && UserContext.get().getRole() != UserRole.ADMIN.getCode()) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "仅商户可发货");
        }
        if (UserContext.get().getRole() == UserRole.MERCHANT.getCode()
                && !order.getMerchantId().equals(UserContext.getUserId())) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "Error");
        }
    }

    private String toAddressJson(Address address) {
        try {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("receiverName", address.getReceiverName());
            map.put("phone", address.getPhone());
            map.put("province", address.getProvince());
            map.put("city", address.getCity());
            map.put("district", address.getDistrict());
            map.put("detail", address.getDetail());
            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new BusinessException(ErrorCode.ADDRESS_INVALID, "Error");
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

    private OrderResponse toResponse(Order order, List<OrderItem> items) {
        List<OrderItemResponse> itemResponses = items.stream().map(i -> {
            OrderItemResponse r = new OrderItemResponse();
            r.setProductId(i.getProductId());
            r.setProductTitle(i.getProductTitle());
            r.setProductImage(i.getProductImage());
            r.setPrice(i.getPrice());
            r.setQuantity(i.getQuantity());
            return r;
        }).collect(Collectors.toList());
        OrderResponse resp = new OrderResponse();
        resp.setId(order.getId());
        resp.setOrderNo(order.getOrderNo());
        resp.setUserId(order.getUserId());
        resp.setMerchantId(order.getMerchantId());
        resp.setTotalAmount(order.getTotalAmount());
        resp.setPayAmount(order.getPayAmount());
        resp.setStatus(order.getStatus());
        resp.setAddressSnapshot(order.getAddressSnapshot());
        resp.setRemark(order.getRemark());
        resp.setLogisticsNo(order.getLogisticsNo());
        resp.setCreateTime(order.getCreateTime() == null ? null : order.getCreateTime().toString());
        resp.setItems(itemResponses);
        return resp;
    }
}
