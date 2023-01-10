package com.kobe.xt.component.security;

import com.kobe.xt.exception.SmsCodeGrantException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author kobe_xt
 * @version 1.0
 * @description: 定制化 OAuth2.0 授权模式抽象类
 *               作用：当定制化的授权模式需要使用 OAuth2.0 提供的密码模式时，可以继承该类；
 * @date 2023/1/9 16:08
 */
@Slf4j
public abstract class AbstractCustomizedTokenGranter extends AbstractTokenGranter {

    private final AuthenticationManager authenticationManager;

    protected AbstractCustomizedTokenGranter(AuthenticationManager authenticationManager, AuthorizationServerTokenServices tokenServices,
                                  ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory, String grantType) {
        super(tokenServices, clientDetailsService, requestFactory, grantType);
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        Map<String, String> parameters = new LinkedHashMap<String, String>(tokenRequest.getRequestParameters());

        customizedProcess(parameters);

        String username = parameters.get("username");
        String password = parameters.get("password");
        // Protect from downstream leaks of password
        parameters.remove("password");

        Authentication userAuth = new UsernamePasswordAuthenticationToken(username, password);
        ((AbstractAuthenticationToken) userAuth).setDetails(parameters);
        // 将  UsernamePasswordAuthenticationToken 放到 SecurityContext 中，否则 MyUserDetailsServiceImpl 中无法获取到 grant_type；
        SecurityContextHolder.getContext().setAuthentication(userAuth);
        try {
            userAuth = authenticationManager.authenticate(userAuth);
        }
        catch (AccountStatusException ase) {
            //covers expired, locked, disabled cases (mentioned in section 5.2, draft 31)
            throw new SmsCodeGrantException(400003L,"账号状态异常",ase);
        }
        catch (BadCredentialsException e) {
            // If the username/password are wrong the spec says we should send 400/invalid grant
            throw new SmsCodeGrantException(400004L,"账号信息异常",e);
        }
        if (userAuth == null || !userAuth.isAuthenticated()) {
            throw new SmsCodeGrantException(400005L,"登录失败！");
        }

        OAuth2Request storedOAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);
        return new OAuth2Authentication(storedOAuth2Request, userAuth);
    }

    protected abstract void customizedProcess(Map<String, String> parameters);

}
