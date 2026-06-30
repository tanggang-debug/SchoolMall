package com.campus.common.security;

import com.campus.common.core.constant.ErrorCode;
import com.campus.common.core.exception.BusinessException;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RoleAspect {

    @Before("@annotation(requireRole)")
    public void checkRole(RequireRole requireRole) {
        LoginUser user = UserContext.get();
        if (user == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "请先登录");
        }
        for (int role : requireRole.value()) {
            if (user.getRole() == role) {
                return;
            }
        }
        throw new BusinessException(ErrorCode.FORBIDDEN, "Error");
    }
}
