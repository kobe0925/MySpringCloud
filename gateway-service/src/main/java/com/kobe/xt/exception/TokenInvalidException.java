package com.kobe.xt.exception;

/**
 * @author kobe_xt
 * @version 1.0
 * @description: token非法异常
 * @date 2022/9/15 09:34
 */
public class TokenInvalidException extends ServiceException {
    public TokenInvalidException(Long errCode, String message, Throwable throwable) {
        super(errCode, message, throwable);
    }

    public TokenInvalidException(Long errCode, String message) {
        super(errCode, message);
    }

    public TokenInvalidException(Long errorCodeSeed, long errorCodeOffset, String message) {
        super(errorCodeSeed, errorCodeOffset, message);
    }

    public TokenInvalidException(Long errorCodeSeed, long errorCodeOffset, String message, Throwable throwable) {
        super(errorCodeSeed, errorCodeOffset, message, throwable);
    }

    public TokenInvalidException(String message) {
        super(message);
    }

    public TokenInvalidException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
