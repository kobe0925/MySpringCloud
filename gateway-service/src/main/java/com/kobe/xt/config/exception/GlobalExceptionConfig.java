package com.kobe.xt.config.exception;

import com.kobe.xt.exception.handler.GlobalExceptionHandler;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.result.view.ViewResolver;

import java.util.Collections;
import java.util.List;

/**
 * @author kobe_xt
 * @version 1.0
 * @description: 网关全局异常处理配置类
 * @date 2022/9/15 10:12
 */
@Configuration
@EnableConfigurationProperties({ServerProperties.class, ResourceProperties.class})
public class GlobalExceptionConfig {

    private ServerProperties serverProperties;

    private ApplicationContext applicationContext;

    private ResourceProperties resourceProperties;

    private List<ViewResolver> viewResolvers;

    private ServerCodecConfigurer serverCodecConfigurer;

    public GlobalExceptionConfig(ServerProperties serverProperties,
                                      ResourceProperties resourceProperties,
                                      ObjectProvider<List<ViewResolver>> viewResolversProvider,
                                      ServerCodecConfigurer serverCodecConfigurer,
                                      ApplicationContext applicationContext) {
        this.serverProperties = serverProperties;
        this.applicationContext = applicationContext;
        this.resourceProperties = resourceProperties;
        this.viewResolvers = viewResolversProvider
                .getIfAvailable(() -> Collections.emptyList());
        this.serverCodecConfigurer = serverCodecConfigurer;
    }

    @Bean
    public ErrorWebExceptionHandler globalExceptionHandler(ErrorAttributes errorAttributes){
        DefaultErrorWebExceptionHandler exceptionHandler = new GlobalExceptionHandler(
                errorAttributes, this.resourceProperties,
                this.serverProperties.getError(), this.applicationContext);
        exceptionHandler.setViewResolvers(this.viewResolvers);
        exceptionHandler.setMessageWriters(this.serverCodecConfigurer.getWriters());
        exceptionHandler.setMessageReaders(this.serverCodecConfigurer.getReaders());
        return exceptionHandler;
    }

}
