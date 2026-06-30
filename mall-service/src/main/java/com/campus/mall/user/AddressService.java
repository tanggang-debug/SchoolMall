package com.campus.mall.user;

import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.campus.common.core.constant.ErrorCode;
import com.campus.common.core.exception.BusinessException;
import com.campus.common.security.UserContext;
import com.campus.mall.dto.request.AddressRequest;
import com.campus.mall.dto.request.UpdateUserRequest;
import com.campus.mall.dto.response.AddressResponse;
import com.campus.mall.dto.response.UserResponse;
import com.campus.mall.entity.Address;
import com.campus.mall.entity.User;
import com.campus.mall.mapper.AddressMapper;
import com.campus.mall.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressMapper addressMapper;

    public List<AddressResponse> listByUserId(Long userId) {
        checkUserAccess(userId);
        return addressMapper.selectList(new LambdaQueryWrapper<Address>()
                        .eq(Address::getUserId, userId)
                        .orderByDesc(Address::getIsDefault)
                        .orderByDesc(Address::getCreateTime))
                .stream()
                .map(this::toResponse)
                .collect(java.util.stream.Collectors.toList());
    }

    @Transactional
    public AddressResponse create(Long userId, AddressRequest request) {
        checkUserAccess(userId);
        if (Boolean.TRUE.equals(request.getIsDefault())) {
            clearDefault(userId);
        }
        Address address = new Address();
        address.setUserId(userId);
        address.setReceiverName(request.getReceiverName());
        address.setPhone(request.getPhone());
        address.setProvince(request.getProvince());
        address.setCity(request.getCity());
        address.setDistrict(request.getDistrict());
        address.setDetail(request.getDetail());
        address.setIsDefault(Boolean.TRUE.equals(request.getIsDefault()) ? 1 : 0);
        address.setCreateTime(LocalDateTime.now());
        addressMapper.insert(address);
        if (address.getIsDefault() == 0) {
            Long defaultCount = addressMapper.selectCount(new LambdaQueryWrapper<Address>()
                    .eq(Address::getUserId, userId)
                    .eq(Address::getIsDefault, 1));
            if (defaultCount == 0) {
                address.setIsDefault(1);
                addressMapper.updateById(address);
            }
        }
        return toResponse(address);
    }

    @Transactional
    public void setDefault(Long userId, Long addrId) {
        checkUserAccess(userId);
        Address address = addressMapper.selectById(addrId);
        if (address == null || !address.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.ADDRESS_INVALID, "Error");
        }
        clearDefault(userId);
        address.setIsDefault(1);
        addressMapper.updateById(address);
    }

    public Address getByIdAndUserId(Long addrId, Long userId) {
        Address address = addressMapper.selectById(addrId);
        if (address == null || !address.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.ADDRESS_INVALID, "Error");
        }
        return address;
    }

    private void clearDefault(Long userId) {
        addressMapper.update(null, new LambdaUpdateWrapper<Address>()
                .eq(Address::getUserId, userId)
                .set(Address::getIsDefault, 0));
    }

    private void checkUserAccess(Long userId) {
        Long current = UserContext.getUserId();
        if (current == null || !current.equals(userId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "无权操作该用户地址");
        }
    }

    private AddressResponse toResponse(Address address) {
        AddressResponse resp = new AddressResponse();
        resp.setId(address.getId());
        resp.setReceiverName(address.getReceiverName());
        resp.setPhone(address.getPhone());
        resp.setProvince(address.getProvince());
        resp.setCity(address.getCity());
        resp.setDistrict(address.getDistrict());
        resp.setDetail(address.getDetail());
        resp.setIsDefault(address.getIsDefault() != null && address.getIsDefault() == 1);
        return resp;
    }
}
