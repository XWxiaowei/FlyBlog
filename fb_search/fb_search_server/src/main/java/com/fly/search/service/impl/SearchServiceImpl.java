package com.fly.search.service.impl;

import com.fly.search.common.IndexKey;
import com.fly.search.model.PostDocument;
import com.fly.search.repository.PostRepository;
import com.fly.search.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

/**
 * Created by xiang.wei on 2019/2/16
 *
 * @author xiang.wei
 */
@Slf4j
@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    PostRepository postRepository;



    @Override
    public Page<PostDocument> query(Pageable pageable, String keyword) {
//       多个字段匹配，只要满足一个即可返回结果
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(keyword,
                IndexKey.POST_TITLE,
                IndexKey.POST_DESCRIPTION,
                IndexKey.POST_AUTHOR,
                IndexKey.POST_CATEGORY,
                IndexKey.POST_TAGS);

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(multiMatchQueryBuilder)
                .withPageable(pageable)
                .build();

        Page<PostDocument> page = postRepository.search(searchQuery);
        log.info("查询--{}-的得到结果如下--------->{}个查询结果，一共{}页",
                keyword, page.getTotalElements(), page.getTotalPages());

        return page;
    }
}
