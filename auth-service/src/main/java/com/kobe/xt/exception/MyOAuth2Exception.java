package com.kobe.xt.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.kobe.xt.component.security.MyOAuth2ExceptionSerializer;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
 * @author kobe_xt
 * @version 1.0
 * @description: 自定义异常
 * @date 2022/9/22 13:54
 */
@JsonSerialize(using = MyOAuth2ExceptionSerializer.class)
public class MyOAuth2Exception extends OAuth2Exception {

    private Integer status;

    public MyOAuth2Exception(String msg,Integer status,Throwable t) {
        super(msg, t);
        this.status = status;
    }

    public MyOAuth2Exception(String msg) {
        super(msg);
    }

    @Override
    public int getHttpErrorCode() {
        return status;
    }
}
