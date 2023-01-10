package com.kobe.xt.exception;

/**
 * @author kobe_xt
 * @version 1.0
 * @description: 业务公共异常类
 * @date 2022/9/15 09:26
 */
public class ServiceException extends RuntimeException {

    private Long errCode;

    private Long errorCodeOffset;

    public ServiceException(Long errCode, String message, Throwable throwable){
        super(message,throwable);
        this.errCode = errCode;
        this.errorCodeOffset = 0l;
    }

    public ServiceException(Long errCode,  String message){
        super(message);
        this.errCode = errCode ;
        this.errorCodeOffset = 0l;
    }

    public ServiceException(Long errorCodeSeed,long errorCodeOffset,String message){
        super(message);
        this.errCode = errorCodeSeed + errorCodeOffset;
        this.errorCodeOffset = errorCodeOffset;
    }

    public ServiceException(Long errorCodeSeed,long errorCodeOffset,String message, Throwable throwable){
        super(message,throwable);
        this.errCode = errorCodeSeed + errorCodeOffset;
        this.errorCodeOffset = errorCodeOffset;
    }

    public ServiceException(String message){
        super(message);
    }

    public ServiceException(String message,Throwable throwable){
        super(message,throwable);
    }

    public Long getErrCode() {
        return errCode;
    }
}
