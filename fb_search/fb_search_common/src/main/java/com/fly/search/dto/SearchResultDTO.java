package com.fly.search.dto;

import lombok.Data;

/**
 * Created by xiang.wei on 2019/2/16
 *
 * @author xiang.wei
 */
@Data
public class SearchResultDTO {
    private long current;
    private long size;
    private long total;
    private Object records;

    public SearchResultDTO() {
    }

    public SearchResultDTO(long current, long size, long total, Object records) {
        this.current = current;
        this.size = size;
        this.total = total;
        this.records = records;
    }
}
