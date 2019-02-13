package com.fly.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import cn.hutool.core.map.MapUtil;
import com.fly.shiro.AuthFilter;
import com.fly.shiro.OAuth2Realm;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by xiang.wei on 2018/12/1
 *
 * @author xiang.wei
 */
@Slf4j
@Configuration
public class ShiroConfig {

    /**
     * 将重写后的oAuth2Realm配置到SecurityManager中
     *
     * @param oAuth2Realm
     * @return
     */
    @Bean("securityManager")
    public SecurityManager securityManager(OAuth2Realm oAuth2Realm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        securityManager.setRealm(oAuth2Realm);
        log.info("----------------->SecurityManager注入完成");

        return securityManager;
    }

    /**
     * 配置了shiro的过滤器
     *
     * @param securityManager
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilter(@Qualifier("securityManager") SecurityManager securityManager) {
        ShiroFilterFactoryBean filterFactoryBean = new ShiroFilterFactoryBean();
        filterFactoryBean.setSecurityManager(securityManager);
//        配置登录的url和登录成功的url
        filterFactoryBean.setLoginUrl("/login");
        filterFactoryBean.setSuccessUrl("/user/center");
//        配置未授权跳转页面
        filterFactoryBean.setUnauthorizedUrl("/error/403");

//        配置异步请求登录过滤器
        filterFactoryBean.setFilters(MapUtil.of("user", authFilter()));

        HashMap<String, String> hashMap = new LinkedHashMap<>();
        hashMap.put("/login", "anon");
        hashMap.put("/user*", "user");
        hashMap.put("/user/**", "user");

        filterFactoryBean.setFilterChainDefinitionMap(hashMap);

        return filterFactoryBean;
    }

    /**
     *
     * 用于thymeleaf模板使用shiro标签,shiro方言标签
     * @return
     */
    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }

    @Bean
    public AuthFilter authFilter(){
        return new AuthFilter();
    }
}
