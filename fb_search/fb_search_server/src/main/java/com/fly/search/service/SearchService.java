package com.fly.search.service;

import com.fly.search.dto.PostMqIndexMessage;
import com.fly.search.model.PostDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by xiang.wei on 2019/2/16
 *
 * @author xiang.wei
 */
public interface SearchService {

    Page<PostDocument> query(Pageable pageable, String keyword);

    /**
     * 创建更新索引
     * @param message
     */
    void createOrUpdateIndex(PostMqIndexMessage message);

    /**
     * 删除索引
     * @param message
     */
    void removeIndex(PostMqIndexMessage message);


}
