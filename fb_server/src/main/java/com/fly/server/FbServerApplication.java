package com.fly.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class FbServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FbServerApplication.class, args);
    }

}

