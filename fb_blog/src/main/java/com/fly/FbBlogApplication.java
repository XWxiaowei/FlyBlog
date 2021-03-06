package com.fly;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@Slf4j
@SpringBootApplication
@MapperScan(value = "com.fly.dao")
@EnableEurekaClient
public class FbBlogApplication {


	public static void main(String[] args) {

		SpringApplication.run(FbBlogApplication.class, args);
		log.info("------>系统启动成功");
	}
}
