package com.fly.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fly.service.BaseService;
import com.fly.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Created by xiang.wei on 2018/12/3
 *
 * @author xiang.wei
 */
public class BaseServiceImpl<M extends BaseMapper<T>,T> extends ServiceImpl<M,T> implements BaseService<T> {
    @Autowired
    RedisUtil redisUtil;

    @Override
    public void join(Map<String, Object> map, String field) {

    }

    @Override
    public void join(List<Map<String, Object>> datas, String field) {
        datas.forEach(map->{
            this.join(map,field);
        });
    }

    @Override
    public void join(IPage<Map<String, Object>> pageData, String field) {
        List<Map<String, Object>> records = pageData.getRecords();
        this.join(records, field);
    }
}
