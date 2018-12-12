package com.fly.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.entity.UserCollection;
import com.fly.service.UserCollectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * Created by xiang.wei on 2018/12/12
 *
 * @author xiang.wei
 */
@Slf4j
@Controller
public class CenterController extends BaseController {

    /**
     * 个人收藏分页查询
     * @param current
     * @param size
     * @return
     */
    @GetMapping("/collection")
    public String collection(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size) {

        IPage<UserCollection> page = new Page<>();
        page.setCurrent(current);
        page.setSize(size);

        IPage<Map<String, Object>> pageData = userCollectionService.
                pageMaps(page,new QueryWrapper<UserCollection>()
                .eq("user_id",getProfileId()).orderByDesc("created"));

        postService.join(pageData, "post_id");
        request.setAttribute("pageData",pageData);

        return "user/collection";
    }

}
