package com.kobe.xt.component.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.Map;

/**
 * @author kobe_xt
 * @version 1.0
 * @description: 自定义的扫码登录授权模式
 * @date 2023/1/9 16:34
 */
@Slf4j
public class ScanQRCodeTokenGranter extends AbstractCustomizedTokenGranter {

    private static final String GRANT_TYPE = "scan_code";

    public ScanQRCodeTokenGranter(AuthenticationManager authenticationManager, AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory) {
        super(authenticationManager, tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
    }


    @Override
    protected void customizedProcess(Map<String, String> parameters) {

    }
}
