package com.fly.shiro;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.api.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.UserFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 异步请求登录过滤器
 *
 * @author xiang.wei
 * @create 2018/12/20 16:12
 */
@Slf4j
public class AuthFilter extends UserFilter {

    @Override
    protected void redirectToLogin(ServletRequest servletRequest, ServletResponse response) throws IOException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        log.info("--------------------->请求接口地址={}", ((HttpServletRequest) servletRequest).getRequestURI());
//        异步请求要先登录
        String header = request.getHeader("X-Requested-With");
        if (header != null && "XMLHttpRequest".equals(header)) {
            Subject subject = SecurityUtils.getSubject();
            if (!subject.isAuthenticated()) {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().print(JSONUtil.toJsonStr(R.failed("请先登录!")));
            }
        } else {
            super.redirectToLogin(servletRequest, response);

        }

    }
}
