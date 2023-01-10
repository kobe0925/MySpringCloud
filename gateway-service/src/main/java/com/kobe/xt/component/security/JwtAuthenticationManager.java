package com.kobe.xt.component.security;

import com.kobe.xt.exception.TokenExpireException;
import com.kobe.xt.exception.TokenInvalidException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @author kobe_xt
 * @version 1.0
 * @description: 基于 jwt 的认证管理器，在这个认证管理器自定义认证逻辑，主要的作用就是：
 *                  （1）对请求中携带的 token 进行校验，比如过期时间，加密方式等；
 *                  （2）一旦 token 校验通过之后，则将请求交给鉴权管理器（AuthorizationManager）进行鉴权
 * @date 2022/9/14 14:18
 */
@Component
@Slf4j
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {

    @Autowired
    private TokenStore tokenStore;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        log.info("---------- 进入了认证管理器 ------------");
        return Mono.justOrEmpty(authentication).filter(a -> a instanceof BearerTokenAuthenticationToken)
                .cast(BearerTokenAuthenticationToken.class)
                .map(BearerTokenAuthenticationToken::getToken)
                .flatMap(accessToken -> {
                    OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(accessToken);
                    if(oAuth2AccessToken == null){
                        return Mono.error(new TokenExpireException("无效的token！"));
                    } else if(oAuth2AccessToken.isExpired()){
                        return Mono.error(new TokenExpireException("token已过期！"));
                    }
                    OAuth2Authentication oAuth2Authentication = tokenStore.readAuthentication(accessToken);
                    if(oAuth2Authentication == null){
                        return Mono.error(new TokenInvalidException("无效的token！"));
                    }else{
                        return Mono.just(oAuth2Authentication);
                    }
                }).cast(Authentication.class);
    }
}
