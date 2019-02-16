package com.fly.search.controller;

import com.fly.common.resultVo.R;
import com.fly.search.client.FbBlogClient;
import com.fly.search.dto.SearchResultDTO;
import com.fly.search.model.PostDocument;
import com.fly.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by xiang.wei on 2019/2/16
 *
 * @author xiang.wei
 */
@RestController
@RequestMapping("/fb-search")
public class SearchController{
    @Autowired
    SearchService searchService;

    @GetMapping("/search/{page}/{size}")
    public R<SearchResultDTO> search(@PathVariable(name = "page") Integer current,
                                     @PathVariable  Integer size, String keyword) {
//        分页参数
        PageRequest pageable = PageRequest.of(current - 1, size);
        Page<PostDocument> results = searchService.query(pageable, keyword);

        return null;
    }

    public R initEsIndex() {
        return null;
    }
}
