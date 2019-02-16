package com.fly.feign;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.resultVo.R;
import com.fly.search.dto.PostDTO;
import com.fly.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by xiang.wei on 2019/2/16
 *
 * @author xiang.wei
 */
@RestController
@RequestMapping("/fb-blog")
public class FbBlogFeign {

    @Autowired
    PostService postService;


    @GetMapping("/findPostDTOByPostId")
    public R<PostDTO> findPostDTOByPostId(long postId) {
        PostDTO postDTO = postService.findPostDTOById(postId);
        return R.ok(postDTO);
    }

    @PostMapping("/findPostDTOByPage")
    R<List<PostDTO>> findPostDTOByPage(@RequestParam("current") int current, @RequestParam("size") int size){

        Page<PostDTO> page = new Page<>();
        page.setCurrent(current);
        page.setSize(size);

        IPage<PostDTO> pageData = postService.findPostDTOByPage(page, null);

        return R.ok(pageData.getRecords());
    }
}
