package com.kobe.xt.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * @author kobe_xt
 * @version 1.0
 * @description: 令牌配置类
 * @date 2022/9/13 16:25
 */
@Configuration
public class TokenConfig {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    private static final String SIGNING_KEY = "abc";


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
    @Bean
    public  TokenStore tokenStore(){
        RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
        return redisTokenStore;
    }

    /**
     * @description: 使用 JWT 令牌的时候，配置令牌转换器
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
