package com.kobe.xt.component.security;

import com.kobe.xt.dto.UserDTO;
import com.kobe.xt.exception.SmsCodeGrantException;
import com.kobe.xt.service.UserService;
import com.kobe.xt.utils.ApplicationContextAwareUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author kobe_xt
 * @version 1.0
 * @description: 自定义的短信验证码授权模式
 * @date 2022/9/21 11:07
 */
@Slf4j
public class SmsCodeTokenGranter extends AbstractCustomizedTokenGranter {

    private static final String GRANT_TYPE = "sms";

    public SmsCodeTokenGranter(AuthenticationManager authenticationManager, AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory) {
        super(authenticationManager, tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
    }

    /**
     * @description: 1、短信验证码授权模式和 OAuth2.0 自带的密码授权模式的区别主要就是短信验证码授权模式需要验证短信随机码，
     *                  在验证通过之后的授权流程和密码授权模式一样，因此只用重写 customizedProcess() 方法，并在该方法中
     *                  实现短信验证码的验证流程；
     * @author kobe_xt
     * @date: 2022/9/21 15:39
     * @param: client,tokenRequest
     * @return: org.springframework.security.oauth2.provider.OAuth2Authentication
     */
    @Override
    protected void customizedProcess(Map<String, String> parameters) {
        String phone = parameters.get("phone");
        String smsCode = parameters.get("smsCode");
        if(StringUtils.isBlank(phone)){
            throw new SmsCodeGrantException("手机号不能为空！");
        }
        if(StringUtils.isBlank(smsCode)){
            throw new SmsCodeGrantException("短信随机码不能为空！");
        }
        log.info("------ 获取到的短信验证码：{}",smsCode);
        /**
         * 此处省略短信验证码校验流程
         */
        UserService userServiceImpl = (UserService)ApplicationContextAwareUtil.getBean("userServiceImpl");
        UserDTO userDTO = userServiceImpl.getUserInfoByPhoneNumber(phone);
        parameters.put("username",userDTO.getUsername());
        parameters.put("password",userDTO.getPassword());
    }

}
