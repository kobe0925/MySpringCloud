package com.kobe.xt.config.security;

import cn.hutool.core.util.ArrayUtil;
import com.kobe.xt.component.security.AuthorizationManager;
import com.kobe.xt.component.security.JwtAuthenticationManager;
import com.kobe.xt.component.security.RequestAccessDeniedHandler;
import com.kobe.xt.component.security.RequestAuthenticationEntryPoint;
import com.kobe.xt.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;

import org.springframework.security.oauth2.server.resource.web.server.ServerBearerTokenAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author kobe_xt
 * @version 1.0
 * @description: 资源服务器配置类
 * @date 2022/9/9 16:37
 */
@Configuration
@EnableWebFluxSecurity
public class ResourceServerConfig {

    @Autowired
    private AuthorizationManager authorizationManager;

    @Autowired
    private JwtAuthenticationManager jwtAuthenticationManager;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private RequestAuthenticationEntryPoint requestAuthenticationEntryPoint;

    @Autowired
    private RequestAccessDeniedHandler requestAccessDeniedHandler;


    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http){
        // 1、设置认证过滤器，并将认证管理器配置到认证过滤器中
        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(jwtAuthenticationManager);
        authenticationWebFilter.setServerAuthenticationConverter(new ServerBearerTokenAuthenticationConverter());

        // 2、白名单中的请求地址全部放行
        List<String> whiteList = securityProperties.getWhiteList();
        if (!CollectionUtils.isEmpty(whiteList)) {
            http.authorizeExchange().pathMatchers(ArrayUtil.toArray(whiteList, String.class)).permitAll();
        }

        // 3、将需要权限的请求地址、认证管理器、鉴权管理器配置到后续的过滤流程中
        List<String> needCheck = securityProperties.getNeedCheck();
        http.   csrf().disable()
                .httpBasic().disable()
                .authorizeExchange()
                .pathMatchers(ArrayUtil.toArray(needCheck, String.class))
                .access(authorizationManager)//鉴权管理器配置
                .and().exceptionHandling()
                .authenticationEntryPoint(requestAuthenticationEntryPoint)//配置未认证时自定义处理流程
                .accessDeniedHandler(requestAccessDeniedHandler)//配置未授权时自定义处理流程
                .and().addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION);//将认证管理器添加到过滤流程中

        return http.build();


    }

//    @Bean
//    public Converter<Jwt, ? extends Mono<? extends AbstractAuthenticationToken>> jwtAuthenticationConverter() {
//        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
//        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
//    }
}
