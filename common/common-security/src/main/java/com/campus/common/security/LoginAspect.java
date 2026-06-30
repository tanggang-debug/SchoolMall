package com.campus.common.security;

import com.campus.common.core.constant.ErrorCode;
import com.campus.common.core.exception.BusinessException;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoginAspect {

    @Before("@annotation(com.campus.common.security.RequireLogin)")
    public void checkLogin() {
        if (UserContext.get() == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "请先登录");
        }
    }
}
