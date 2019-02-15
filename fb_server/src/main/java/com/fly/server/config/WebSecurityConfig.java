package com.fly.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author xiang.wei
 * 添加了权限的校验之后增加一个关闭csrf的配置，
 * 防止其他服务注册不上去。
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        关闭csrf
        http.csrf().ignoringAntMatchers("/eureka/**");
        super.configure(http);
    }
}
