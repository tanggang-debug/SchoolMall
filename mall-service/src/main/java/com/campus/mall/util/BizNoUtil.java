package com.campus.mall.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

public final class BizNoUtil {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private BizNoUtil() {}

    public static String orderNo() {
        return "ORD" + FMT.format(LocalDateTime.now()) + random4();
    }

    public static String paymentNo() {
        return "PAY" + FMT.format(LocalDateTime.now()) + random4();
    }

    private static String random4() {
        return String.format("%04d", ThreadLocalRandom.current().nextInt(10000));
    }
}
