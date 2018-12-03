package com.fly.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * Created by xiang.wei on 2018/12/1
 *
 * @author xiang.wei
 */
@Configuration
public class KatchaConfig {

    @Bean
    public DefaultKaptcha producer() {
        Properties properties = new Properties();
        properties.put("kaptcha.border", "no");
        properties.put("kaptcha.image.height", "38");
        properties.put("kaptcha.image.width", "150");
        properties.put("kaptcha.textproducer.font.color", "black");
        properties.put("kaptcha.textproducer.font.size", "32");

        Config config = new Config(properties);
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}
