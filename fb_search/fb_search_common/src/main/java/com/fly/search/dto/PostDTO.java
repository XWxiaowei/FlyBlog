package com.fly.search.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class PostDTO implements Serializable {

    private Long id;
    private String title;

    private Long authorId;
    private String author;
    private String authorVip;
    private String avatar;

    private Long categoryId;
    private String category;

    private Boolean recommend;
    private Integer level;


    private Integer commentCount;
    private Integer viewCount;


    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date created;

    private Integer status;
}
