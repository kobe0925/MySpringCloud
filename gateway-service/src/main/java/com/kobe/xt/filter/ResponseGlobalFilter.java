package com.kobe.xt.filter;

import com.kobe.xt.component.security.HttpResponseCookieDecorator;
import com.kobe.xt.component.security.HttpResponseDecorator;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.NettyWriteResponseFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author kobe_xt
 * @version 1.0
 * @description: 处理响应的全局过滤器；
 *               作用：
 *                  解决使用 OAuth2 授权码模式时，获取授权码的请求通过网关时跳转
 *                  到登录页面进行登录之后无法跳转到授权页面的问题；
 * @date 2023/1/5 17:35
 */
@Component
public class ResponseGlobalFilter implements GlobalFilter, Ordered {

    @Value("${cors.crossOriginPath}")
    private String crossOriginPath;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
                             GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        if(path.contains("/auth/oauth/authorize")){
            //构建响应包装类
            HttpResponseDecorator responseDecorator = new HttpResponseDecorator(exchange.getRequest(),exchange.getResponse(), crossOriginPath);
            return chain
                    .filter(exchange.mutate().response(responseDecorator).build());
        }
//        if(path.contains("/auth/login")){
//            HttpResponseCookieDecorator responseCookieDecorator = new HttpResponseCookieDecorator(exchange.getResponse());
//            return chain
//                    .filter(exchange.mutate().response(responseCookieDecorator).build());
//        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        //WRITE_RESPONSE_FILTER 之前执行
        return NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER - 1;
    }

}
