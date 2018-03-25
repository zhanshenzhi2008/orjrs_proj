package com.orjrs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
@EnableCaching
@EnableRedisRepositories
public class OrjrsApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrjrsApplication.class, args);
    }
}
