package com.kobe.xt.component.security;

import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import reactor.core.publisher.Mono;

/**
 * @author kobe_xt
 * @version 1.0
 * @description: TODO
 * @date 2023/1/11 10:24
 */
public class HttpResponseCookieDecorator extends ServerHttpResponseDecorator {

    public HttpResponseCookieDecorator(ServerHttpResponse delegate) {
        super(delegate);
    }

    @Override
    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
        getHeaders().remove(HttpHeaders.SET_COOKIE);
        return super.writeWith(body);
    }
}
