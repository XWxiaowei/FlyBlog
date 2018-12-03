package com.fly.shiro;

import java.io.Serializable;
import java.util.Date;

public class AccountProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private String email;
    private Integer point;
    private String gender;
    private String avatar;
    private Integer postCount;
    private Integer commentCount;
    private Date lasted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getPostCount() {
        return postCount;
    }

    public void setPostCount(Integer postCount) {
        this.postCount = postCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Date getLasted() {
        return lasted;
    }

    public void setLasted(Date lasted) {
        this.lasted = lasted;
    }

    @Override
    public String toString() {
        return "AccountProfile{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", point=" + point +
                ", gender='" + gender + '\'' +
                ", avatar='" + avatar + '\'' +
                ", postCount=" + postCount +
                ", commentCount=" + commentCount +
                ", lasted=" + lasted +
                '}';
    }
}
