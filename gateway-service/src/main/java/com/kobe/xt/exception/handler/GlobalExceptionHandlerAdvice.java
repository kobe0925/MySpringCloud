package com.kobe.xt.exception.handler;

import com.kobe.xt.response.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author kobe_xt
 * @version 1.0
 * @description:
 * @date 2022/9/15 09:54
 */
@Component
@Slf4j
public class GlobalExceptionHandlerAdvice {

    public ResponseDTO tokenInvalidHandle(Throwable throwable) {
        log.info("tokenInvalidError--",throwable);
        return new ResponseDTO("请先登录",400001,false,null);
    }

    public ResponseDTO tokenExpireHandle(Throwable throwable) {
        log.info("tokenInvalidError--",throwable);
        return new ResponseDTO("登陆超时,请重新登陆",400002,false,null);
    }

    public ResponseDTO handle(Throwable throwable) {
        log.info("gatewayError--",throwable);
        return new ResponseDTO(throwable.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value(),false,null);
    }

}
