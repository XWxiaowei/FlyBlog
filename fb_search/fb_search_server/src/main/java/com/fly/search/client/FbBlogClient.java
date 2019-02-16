package com.fly.search.client;

import com.fly.common.resultVo.R;
import com.fly.search.dto.SearchResultDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by xiang.wei on 2019/2/16
 *
 * @author xiang.wei
 */
@Component
@FeignClient(name = "fb-blog")
public interface FbBlogClient {

    @RequestMapping("/fb-blog/search/{current}/{size}")
    R<SearchResultDTO> search(@PathVariable("current") Integer current, @PathVariable("size") Integer size, @RequestParam("keyword") String keyword);

    /**
     * 注意需要admin权限
     * @return
     */
    @RequestMapping("/fb-blog/admin/initEsIndex")
    R initEsIndex();
}
