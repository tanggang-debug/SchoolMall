package com.campus.mall.payment;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.common.core.constant.ErrorCode;
import com.campus.common.core.enums.OrderStatus;
import com.campus.common.core.enums.PaymentStatus;
import com.campus.common.core.exception.BusinessException;
import com.campus.common.security.UserContext;
import com.campus.mall.dto.request.CreatePaymentRequest;
import com.campus.mall.dto.request.PaymentCallbackRequest;
import com.campus.mall.dto.response.PaymentResponse;
import com.campus.mall.entity.Order;
import com.campus.mall.entity.PaymentRecord;
import com.campus.mall.mapper.PaymentRecordMapper;
import com.campus.mall.order.OrderService;
import com.campus.mall.util.BizNoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRecordMapper paymentRecordMapper;
    private final OrderService orderService;

    @Transactional
    public PaymentResponse create(CreatePaymentRequest request) {
        Order order = orderService.requireByOrderNo(request.getOrderNo());
        if (!order.getUserId().equals(UserContext.getUserId())) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "Error");
        }
        if (order.getStatus() != OrderStatus.UNPAID.getCode()) {
            throw new BusinessException(ErrorCode.ORDER_STATUS_ERROR, "Error");
        }
        PaymentRecord existing = paymentRecordMapper.selectOne(new LambdaQueryWrapper<PaymentRecord>()
                .eq(PaymentRecord::getOrderNo, request.getOrderNo()));
        if (existing != null) {
            return toResponse(existing);
        }
        PaymentRecord record = new PaymentRecord();
        record.setOrderNo(order.getOrderNo());
        record.setPaymentNo(BizNoUtil.paymentNo());
        record.setAmount(order.getPayAmount());
        record.setChannel(request.getChannel());
        record.setStatus(PaymentStatus.INIT.getCode());
        record.setCreateTime(LocalDateTime.now());
        record.setUpdateTime(LocalDateTime.now());
        paymentRecordMapper.insert(record);
        return toResponse(record);
    }

    public PaymentResponse getById(Long id) {
        PaymentRecord record = requirePayment(id);
        checkAccess(record);
        return toResponse(record);
    }

    @Transactional
    public PaymentResponse callback(PaymentCallbackRequest request) {
        PaymentRecord record = paymentRecordMapper.selectOne(new LambdaQueryWrapper<PaymentRecord>()
                .eq(PaymentRecord::getPaymentNo, request.getPaymentNo()));
        if (record == null) {
            throw new BusinessException(ErrorCode.PAYMENT_FAILED, "Error");
        }
        if (StringUtils.hasText(record.getCallbackNo())
                && record.getCallbackNo().equals(request.getCallbackNo())
                && record.getStatus() == PaymentStatus.SUCCESS.getCode()) {
            return toResponse(record);
        }
        if (record.getStatus() == PaymentStatus.SUCCESS.getCode()) {
            return toResponse(record);
        }
        record.setCallbackNo(request.getCallbackNo());
        record.setTradeNo(request.getTradeNo());
        record.setCallbackData(request.getCallbackData());
        record.setStatus(PaymentStatus.SUCCESS.getCode());
        record.setUpdateTime(LocalDateTime.now());
        paymentRecordMapper.updateById(record);
        orderService.onPaid(record.getOrderNo());
        return toResponse(record);
    }

    @Transactional
    public PaymentResponse mockPay(Long id) {
        PaymentRecord record = requirePayment(id);
        checkAccess(record);
        if (record.getStatus() == PaymentStatus.SUCCESS.getCode()) {
            return toResponse(record);
        }
        String callbackNo = "MOCK-" + UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        PaymentCallbackRequest callback = new PaymentCallbackRequest();
        callback.setPaymentNo(record.getPaymentNo());
        callback.setCallbackNo(callbackNo);
        callback.setTradeNo("MOCK_TRADE_" + record.getPaymentNo());
        callback.setCallbackData("{\"mock\":true}");
        callback.setSuccess(true);
        return callback(callback);
    }

    private PaymentRecord requirePayment(Long id) {
        PaymentRecord record = paymentRecordMapper.selectById(id);
        if (record == null) {
            throw new BusinessException(ErrorCode.PAYMENT_FAILED, "Error");
        }
        return record;
    }

    private void checkAccess(PaymentRecord record) {
        Order order = orderService.requireByOrderNo(record.getOrderNo());
        if (!order.getUserId().equals(UserContext.getUserId())
                && UserContext.get().getRole() != 3) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "无权查看支付记录");
        }
    }

    private PaymentResponse toResponse(PaymentRecord record) {
        PaymentResponse resp = new PaymentResponse();
        resp.setId(record.getId());
        resp.setOrderNo(record.getOrderNo());
        resp.setPaymentNo(record.getPaymentNo());
        resp.setAmount(record.getAmount());
        resp.setStatus(record.getStatus());
        resp.setQrCode("MOCK-QR-" + record.getPaymentNo());
        resp.setExpireTime(record.getCreateTime() == null ? null : record.getCreateTime().plusMinutes(15).toString());
        return resp;
    }
}
