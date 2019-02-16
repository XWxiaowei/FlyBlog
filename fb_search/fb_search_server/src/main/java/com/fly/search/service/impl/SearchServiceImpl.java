package com.fly.search.service.impl;

import cn.hutool.core.date.DateUtil;
import com.fly.common.resultVo.R;
import com.fly.search.client.FbBlogClient;
import com.fly.search.common.IndexKey;
import com.fly.search.dto.PostDTO;
import com.fly.search.dto.PostMqIndexMessage;
import com.fly.search.model.PostDocument;
import com.fly.search.repository.PostRepository;
import com.fly.search.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.Map;

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
    @Autowired
    FbBlogClient fbBlogClient;
    @Autowired
    ModelMapper modelMapper;


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

    @Override
    public void createOrUpdateIndex(PostMqIndexMessage message) {
        long postId = message.getPostId();


        R r = fbBlogClient.findPostDTOByPostId(postId);

        log.info("r------>{}", r);

        Map<String, Object> data = (Map<String, Object>) r.getData();
//        手动转换一下日期格式
        data.put("created", DateUtil.parseDateTime(String.valueOf(data.get("created"))));

        PostDTO postDTO = modelMapper.map(data, PostDTO.class);

        if (PostMqIndexMessage.CREATE.equals(message.getType())) {
            if (postRepository.existsById(postId)) {
                this.removeIndex(message);
            }
        }

        PostDocument postDocument = new PostDocument();
        modelMapper.map(postDTO, postDocument);

        PostDocument saveDoc = postRepository.save(postDocument);

        log.info("es 索引更新成功！----->{}", saveDoc.toString());

    }

    @Override
    public void removeIndex(PostMqIndexMessage message) {

    }
}
