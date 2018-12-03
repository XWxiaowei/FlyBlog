package com.fly;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
@MapperScan(value = "com.fly.dao")
public class FlyBlogApplication {


	public static void main(String[] args) {

		SpringApplication.run(FlyBlogApplication.class, args);
		log.info("--------------->系统启动成功");
	}
}
