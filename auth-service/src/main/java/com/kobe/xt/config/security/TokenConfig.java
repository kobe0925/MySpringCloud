package com.kobe.xt.config.security;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * @author kobe_xt
 * @version 1.0
 * @description: token 令牌配置
 * @date 2022/9/7 10:28
 */
@Configuration
public class TokenConfig {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    private static final String SIGNING_KEY = "abc";

    /**
     * @description: 配置令牌存储方式为内存中存储
     * @author kobe_xt
     * @date: 2022/9/7 10:30
     * @param:
     * @return: org.springframework.security.oauth2.provider.token.TokenStore
     */
//    @Bean
//    public TokenStore tokenStore(){
//        return new InMemoryTokenStore();
//    }
    
    /** 
     * @description: 配置令牌存储方式为JWT方式
     * @author kobe_xt
     * @date: 2022/9/7 15:29 
     * @param:  
     * @return: org.springframework.security.oauth2.provider.token.TokenStore 
     */
//    @Bean
//    public TokenStore tokenStore(){
//        return new JwtTokenStore(accessTokenConverter());
//    }
    
    /** 
     * @description: 将生成的令牌存储在 Redis 中；
     * @author kobe_xt
     * @date: 2022/9/16 14:58 
     * @param:  
     * @return: org.springframework.security.oauth2.provider.token.TokenStore 
     */
    @Bean
    public  TokenStore tokenStore(){
        RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
        return redisTokenStore;
    }

    /** 
     * @description: 当需要使用 JWT 令牌的时候，需要配置该令牌转换器
     * @author kobe_xt
     * @date: 2022/9/7 15:42 
     * @param:  
     * @return: org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter 
     */
    @Bean
    public JwtAccessTokenConverter accessTokenConverter(){
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(SIGNING_KEY);// 设置对称加密的秘钥，用于 token 的验证
        return converter;
    }
    
}
