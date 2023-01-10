package com.kobe.xt.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kobe_xt
 * @version 1.0
 * @description: 网关安全相关配置
 * @date 2022/9/13 10:32
 */
@Data
@Component
@ConfigurationProperties(prefix = "secure")
public class SecurityProperties {

    // 网关白名单地址集合
    private List<String> whiteList = new ArrayList<>();

    // 需要权限的地址集合
    private List<String> needCheck = new ArrayList<>();
}
