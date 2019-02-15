package com.fly.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class FbSearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(FbSearchApplication.class, args);
    }

}

