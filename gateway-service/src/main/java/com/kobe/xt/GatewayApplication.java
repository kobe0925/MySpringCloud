package com.kobe.xt;

import com.kobe.xt.properties.SecurityProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author kobe_xt
 * @version 1.0
 * @description: TODO
 * @date 2022/9/9 11:22
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigurationProperties(SecurityProperties.class)
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class,args);
    }
}
