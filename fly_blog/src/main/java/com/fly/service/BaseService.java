package com.fly.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * Created by xiang.wei on 2018/12/3
 *
 * @author xiang.wei
 */
public interface BaseService<T> extends IService<T> {

    /**
     * 添加关联表信息
     * @param stringObjectMap
     * @param field
     */
    void join(Map<String,Object> stringObjectMap,String field);

    /**
     * @param datas
     * @param field
     */
    void join(List<Map<String, Object>> datas, String field);

    /**
     * @param pageData
     * @param field
     */
    void join(IPage<Map<String,Object>> pageData,String field);
}
