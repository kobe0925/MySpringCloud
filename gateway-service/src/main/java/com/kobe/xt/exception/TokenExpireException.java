package com.kobe.xt.exception;

/**
 * @author kobe_xt
 * @version 1.0
 * @description: token非法异常
 * @date 2022/9/15 09:51
 */
public class TokenExpireException extends ServiceException {

    public TokenExpireException(Long errCode, String message, Throwable throwable) {
        super(errCode, message, throwable);
    }

    public TokenExpireException(Long errCode, String message) {
        super(errCode, message);
    }

    public TokenExpireException(Long errorCodeSeed, long errorCodeOffset, String message) {
        super(errorCodeSeed, errorCodeOffset, message);
    }

    public TokenExpireException(Long errorCodeSeed, long errorCodeOffset, String message, Throwable throwable) {
        super(errorCodeSeed, errorCodeOffset, message, throwable);
    }

    public TokenExpireException(String message) {
        super(message);
    }

    public TokenExpireException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
