package com.fly.interceptor;

import com.fly.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by xiang.wei on 2019/3/3
 *
 * @author xiang.wei
 */
@Slf4j
public class PostClickInterceptor implements HandlerInterceptor {
    @Autowired
    RedisUtil redisUtil;


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {

    }
}
