package com.campus.mall.analytics;

import com.campus.common.core.result.Result;
import com.campus.common.security.RequireLogin;
import com.campus.mall.dto.response.HotProductResponse;
import com.campus.mall.dto.response.MerchantAnalyticsResponse;
import com.campus.mall.dto.response.SalesReportResponse;
import com.campus.mall.dto.response.UserActivityResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/sales")
    @RequireLogin
    public Result<List<SalesReportResponse>> sales(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return Result.ok(analyticsService.sales(start, end));
    }

    @GetMapping("/hot-products")
    public Result<List<HotProductResponse>> hotProducts(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(defaultValue = "10") int limit) {
        return Result.ok(analyticsService.hotProducts(date, limit));
    }

    @GetMapping("/user-activity")
    @RequireLogin
    public Result<UserActivityResponse> userActivity() {
        return Result.ok(analyticsService.userActivity());
    }

    @GetMapping("/merchant/{id}")
    @RequireLogin
    public Result<MerchantAnalyticsResponse> merchantAnalytics(@PathVariable Long id) {
        return Result.ok(analyticsService.merchantAnalytics(id));
    }
}
