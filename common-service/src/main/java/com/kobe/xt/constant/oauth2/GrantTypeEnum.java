package com.kobe.xt.constant.oauth2;

/**
 * @author kobe_xt
 * @version 1.0
 * @description: 统一认证中心授权模式枚举类
 * @date 2023/1/13 13:55
 */
public enum GrantTypeEnum {
    AUTHORIZATION_CODE("authorization_code","授权码模式"),
    PASSWORD("password","密码模式"),
    CLIENT_CREDENTIALS("client_credentials","客户端凭证模式"),
    IMPLICIT("implicit","隐藏模式"),
    SMS("sms","短信验证码模式"),
    REFRESH_TOKEN("refresh_token","刷新token模式")
    ;

    private String type;
    private String msg;

    GrantTypeEnum(String type, String msg) {
        this.type = type;
        this.msg = msg;
    }

    public String getType() {
        return type;
    }

    public String getMsg() {
        return msg;
    }
}
