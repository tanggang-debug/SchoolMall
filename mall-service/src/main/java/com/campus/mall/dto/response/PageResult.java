package com.campus.mall.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class PageResult<T> {
    private List<T> records;
    private Long total;
    private Long page;
    private Long size;

    public static <T> PageResult<T> of(long total, long page, long size, List<T> records) {
        PageResult<T> p = new PageResult<>();
        p.setTotal(total);
        p.setPage(page);
        p.setSize(size);
        p.setRecords(records);
        return p;
    }
}
