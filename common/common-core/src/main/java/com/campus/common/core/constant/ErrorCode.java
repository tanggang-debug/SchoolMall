package com.campus.common.core.constant;

public final class ErrorCode {
    private ErrorCode() {}

    public static final int USER_NOT_FOUND = 1001;
    public static final int PASSWORD_ERROR = 1002;
    public static final int UNAUTHORIZED = 1003;
    public static final int FORBIDDEN = 1004;

    public static final int PRODUCT_NOT_FOUND = 2001;
    public static final int STOCK_NOT_ENOUGH = 2002;

    public static final int ORDER_NOT_FOUND = 3001;
    public static final int ORDER_STATUS_ERROR = 3002;
    public static final int ADDRESS_INVALID = 3003;

    public static final int PAYMENT_FAILED = 4001;
    public static final int REFUND_FAILED = 4002;
}
