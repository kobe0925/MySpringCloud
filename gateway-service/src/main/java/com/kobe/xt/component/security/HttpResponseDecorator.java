package com.kobe.xt.component.security;

import org.apache.commons.lang3.StringUtils;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import reactor.core.publisher.Mono;

/**
 * @author kobe_xt
 * @version 1.0
 * @description: Response 包装类；
 *               作用：
 *                   解决使用 OAuth2 授权码模式时，获取授权码的请求通过网关时跳转
 *                   到登录页面进行登录之后无法跳转到授权页面的问题；
 * @date 2023/1/5 17:28
 */
public class HttpResponseDecorator extends ServerHttpResponseDecorator {

    private String proxyUrl;

    private ServerHttpRequest request;


    public HttpResponseDecorator(ServerHttpRequest request, ServerHttpResponse delegate, String proxyUrl) {
        super(delegate);
        this.request = request;
        this.proxyUrl = proxyUrl;
    }

    @Override
    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
        HttpStatus status = this.getStatusCode();
        if (status.equals(HttpStatus.FOUND)) {
            String domain = "";
            if(StringUtils.isBlank(proxyUrl)) {
                domain = request.getURI().getScheme()+"://"+request.getURI().getAuthority()+"/auth";
            } else {
                domain = proxyUrl + "/auth";
            }
            String location = getHeaders().getFirst("Location");
            String replaceLocation = location.replaceAll("^((ht|f)tps?):\\/\\/(\\d{1,3}.){3}\\d{1,3}(:\\d+)?", domain);
            getHeaders().set("Location", replaceLocation);
        }
        this.getStatusCode();
        return super.writeWith(body);
    }
}
