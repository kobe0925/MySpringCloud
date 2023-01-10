package com.kobe.xt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @author kobe_xt
 * @version 1.0
 * @description: TODO
 * @date 2022/9/7 09:56
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableRedisHttpSession
public class AuthServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class,args);
    }
}
