package com.fly;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(value = "com.fly.dao")
public class FlyBlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlyBlogApplication.class, args);
	}
}
