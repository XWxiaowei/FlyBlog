package com.fly.service.impl;

import com.fly.entity.Category;
import com.fly.dao.CategoryMapper;
import com.fly.service.CategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author fly.xiang
 * @since 2018-10-29
 */
@Service
public class CategoryServiceImpl extends BaseServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Override
    public void join(Map<String, Object> map, String field) {
        Map<Object, Object> joinColumns = new HashMap<>();
        String linkfieldValue = map.get(field).toString();

        Category category = this.getById(linkfieldValue);
        if (category != null) {
            joinColumns.put("id", category.getId());
            joinColumns.put("name", category.getName());
            joinColumns.put("icon", category.getIcon());
            map.put("category", joinColumns);
        } else {
            map.put("category",null);
        }

    }
}
