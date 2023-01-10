package com.kobe.xt.component.security;

import com.alibaba.fastjson.JSONObject;
import com.kobe.xt.bo.PermissionBo;
import com.kobe.xt.constant.AuthConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

/**
 * @author kobe_xt
 * @version 1.0
 * @description: 鉴权管理器，用于判断请求是由有权限访问资源
 * @date 2022/9/9 16:59
 */
@Slf4j
@Component
public class AuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    private final static String PERMISSION = "ROLE:PERMISSION";


    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> mono, AuthorizationContext authorizationContext) {
        // 获取当前请求的URI
        URI uri = authorizationContext.getExchange().getRequest().getURI();
        String requestURI  = uri.getPath();
        log.info("------ 当前请求地址:{}",requestURI);
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        List<String> roleIdList = JSONObject.parseArray((String) hashOperations.get(PERMISSION, requestURI), String.class);
        // 认证通过并且权限匹配一致的请求放行，否则拦截
        return mono
                .filter(Authentication::isAuthenticated)
                .flatMapIterable(Authentication::getAuthorities)
                .map(GrantedAuthority::getAuthority)
                .any(role ->{
                    role = role.replace(PERMISSION, "");
                    for (String roleId:roleIdList){
                        if(roleId.equals(role)){
                            return true;
                        }
                    }
                    return false;
                })
                .map(AuthorizationDecision::new)
                .defaultIfEmpty(new AuthorizationDecision(false));
    }
}
