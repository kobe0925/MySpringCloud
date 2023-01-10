package com.kobe.xt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Description:
 * @author: kobe_xt
 * @Date: 2021/6/30 16:21
 * @Version:1.0
 */
@SpringBootApplication
@EnableDiscoveryClient
public class TradeServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(TradeServiceApplication.class, args);
    }
}
