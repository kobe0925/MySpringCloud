package com.kobe.xt.service.impl;

import com.alibaba.fastjson.JSONArray;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author kobe_xt
 * @version 1.0
 * @description: TODO
 * @date 2022/9/13 17:15
 */
@Service
@Slf4j
public class InitAuthDataService implements ApplicationRunner {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    private final static String PERMISSION = "ROLE:PERMISSION";

    @Override
    public void run(ApplicationArguments args) throws Exception {
        this.initData();
    }

    /**
     * @description: 模拟初始化角色和权限对应关系
     * @author kobe_xt
     * @date: 2022/9/13 17:16
     * @param:
     * @return: void
     */
    private void initData(){
        Map<String,Object> map = new HashMap<>();
        Set<String> set = new HashSet<>();
        set.add("role_1");
        map.put("/trade/api/info", JSONArray.toJSONString(set));
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        hashOperations.putAll(PERMISSION,map);
        log.info("====== 角色权限对应关系初始化完成");
    }
}
