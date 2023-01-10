package com.kobe.xt.constant.security;

/**
 * @author kobe_xt
 * @version 1.0
 * @description: Spring Security OAuth2 异常描述信息枚举
 * @date 2022/9/23 14:28
 */
public enum ExceptionDetailEnum {
    ERROR("error","业务异常"),
    ERROR_DESCRIPTION("error_description","业务异常"),
    ERROR_URI("error_uri","请求地址错误"),
    INVALID_REQUEST("invalid_request","无效的请求"),
    INVALID_CLIENT("invalid_client","无效的客户端"),
    INVALID_GRANT("invalid_grant","无效的授权"),
    UNAUTHORIZED_CLIENT("unauthorized_client","未授权客户端"),
    UNSUPPORTED_GRANT_TYPE("unsupported_grant_type","不支持的授权模式"),
    INVALID_SCOPE("invalid_scope","无效的权限"),
    INSUFFICIENT_SCOPE("insufficient_scope","权限不足"),
    INVALID_TOKEN("invalid_token","无效的token"),
    REDIRECT_URI_MISMATCH("redirect_uri_mismatch","业务异常"),
    UNSUPPORTED_RESPONSE_TYPE("unsupported_response_type","业务异常"),
    ACCESS_DENIED("access_denied","无权访问"),
    METHOD_NOT_ALLOWED("method_not_allowed","方法不允许访问"),
    SERVER_ERROR("server_error","服务器异常"),
    UNAUTHORIZED("unauthorized","未授权")
    ;
    private String code;
    private String message;

    ExceptionDetailEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }


    public String getMessage() {
        return message;
    }

    public static String getMessageByCode(String code){
        ExceptionDetailEnum[] values = values();
        for(ExceptionDetailEnum detailEnum:values){
            if(detailEnum.code.equals(code)){
                return detailEnum.getMessage();
            }
        }
        return null;
    }

}
