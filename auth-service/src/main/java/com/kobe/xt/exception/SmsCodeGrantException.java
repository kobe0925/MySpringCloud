package com.kobe.xt.exception;

/**
 * @author kobe_xt
 * @version 1.0
 * @description: TODO
 * @date 2022/9/21 15:57
 */
public class SmsCodeGrantException extends ServiceException {
    public SmsCodeGrantException(Long errCode, String message, Throwable throwable) {
        super(errCode, message, throwable);
    }

    public SmsCodeGrantException(Long errCode, String message) {
        super(errCode, message);
    }

    public SmsCodeGrantException(Long errorCodeSeed, long errorCodeOffset, String message) {
        super(errorCodeSeed, errorCodeOffset, message);
    }

    public SmsCodeGrantException(Long errorCodeSeed, long errorCodeOffset, String message, Throwable throwable) {
        super(errorCodeSeed, errorCodeOffset, message, throwable);
    }

    public SmsCodeGrantException(String message) {
        super(message);
    }

    public SmsCodeGrantException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
