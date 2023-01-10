package com.kobe.xt.exception.handler;

import com.kobe.xt.exception.TokenExpireException;
import com.kobe.xt.exception.TokenInvalidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

/**
 * @author kobe_xt
 * @version 1.0
 * @description: 网关全局异常处理类：
 *                  1、getRoutingFunction 方法捕获到 gateway 自己抛出的所有异常；
 *                  2、然后在 getRoutingFunction 方法中调用 renderErrorResponse 方法；
 *                  3、接着在 renderErrorResponse 中根据异常类型的不同，自定义返回给前端的异常信息；
 *                  4、最后在 GlobalExceptionHandlerAdvice 中统一定义各种异常的返回信息；
 * @date 2022/9/15 09:37
 */

public class GlobalExceptionHandler extends DefaultErrorWebExceptionHandler {

    @Autowired
    private GlobalExceptionHandlerAdvice globalExceptionHandlerAdvice;

    public GlobalExceptionHandler(ErrorAttributes errorAttributes, ResourceProperties resourceProperties, ErrorProperties errorProperties, ApplicationContext applicationContext) {
        super(errorAttributes, resourceProperties, errorProperties, applicationContext);
    }


    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    @Override
    protected Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        Throwable throwable = getError(request);
        if(throwable instanceof TokenInvalidException){
            return ServerResponse.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .body(BodyInserters.fromObject(globalExceptionHandlerAdvice.tokenInvalidHandle(throwable)));
        }else if(throwable instanceof TokenExpireException){
            return ServerResponse.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .body(BodyInserters.fromObject(globalExceptionHandlerAdvice.tokenExpireHandle(throwable)));
        }else{
            return ServerResponse.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .body(BodyInserters.fromObject(globalExceptionHandlerAdvice.handle(throwable)));
        }
    }
}
