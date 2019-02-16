package com.fly.search.service;

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
}
