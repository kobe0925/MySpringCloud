package com.kobe.xt.config.security;

import cn.hutool.core.collection.CollectionUtil;
import com.kobe.xt.component.security.MyWebResponseExceptionTranslator;
import com.kobe.xt.component.security.ScanQRCodeTokenGranter;
import com.kobe.xt.component.security.SmsCodeTokenGranter;
import com.kobe.xt.filter.MyClientAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenGranter;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeTokenGranter;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.implicit.ImplicitTokenGranter;
import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author kobe_xt
 * @version 1.0
 * @description: 授权服务器配置类，主要配置如下几项：
 *              1、ClientDetailsServiceConfigurer：用来配置客户端详情服务（ClientDetailsService），客户端详情信息在这个配置中初始化；
 *              2、AuthorizationServerEndpointsConfigurer：用来配置令牌的访问端点
 *                  2.1 配置令牌访问端点前，还需要配置令牌服务 AuthorizationServerTokenServices
 *              3、AuthorizationServerSecurityConfigurer：用来配置令牌端点的安全约束；
 * @date 2022/9/7 09:58
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private AuthorizationCodeServices authorizationCodeServices;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    @Autowired
    private MyWebResponseExceptionTranslator myWebResponseExceptionTranslator;

    @Autowired
    private MyClientAuthenticationFilter myClientAuthenticationFilter;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;


    /**
     * @description: 1、配置客户端信息
     * @author kobe_xt
     * @date: 2022/9/7 10:27
     * @param: clients
     * @return: void
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        clients.inMemory() //使用内存方式存储用户信息
//                .withClient("client01") //客户端id
//                .secret(bCryptPasswordEncoder.encode("123")) //客户端秘钥
//                .resourceIds("user-service")//资源列表
//                .authorizedGrantTypes("authorization_code","password","client_credentials","implicit","refresh_token")//授权类型：四种授权类型加refresh_token
//                .scopes("all")//允许的授权范围
//                .autoApprove(false)//是否自动授权
//                .redirectUris("http://www.baidu.com");//验证回调地址

        clients.withClientDetails(clientDetailsService(dataSource));

    }

    @Bean
    public ClientDetailsService clientDetailsService(DataSource dataSource){
        ClientDetailsService detailsService = new JdbcClientDetailsService(dataSource);
        ((JdbcClientDetailsService)detailsService).setPasswordEncoder(bCryptPasswordEncoder);
        return detailsService;
    }



    /**
     * @description: 2、配置令牌访问端点
     * @author kobe_xt
     * @date: 2022/9/7 10:44
     * @param: endpoints
     * @return: void
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .authenticationManager(authenticationManager) // 当授权模式为密码模式的时候必须配置 authenticationManager，并且 authenticationManager 需要在 SecurityConfig 中注入
                .authorizationCodeServices(authorizationCodeServices) // 当授权模式为授权码模式的时候可以通过配置 authorizationCodeServices 来选择存储授权码的方式，比如说可以存储在内存中，数据库中或者redis中
//                .accessTokenConverter(jwtAccessTokenConverter)
                .tokenServices(tokenServices()) // 配置令牌服务
                .allowedTokenEndpointRequestMethods(HttpMethod.POST)// 设置获取令牌端点允许的请求方法
                .exceptionTranslator(myWebResponseExceptionTranslator);// 设置自定义异常解析器

        /**
         * 添加自定义的授权模式配置
         */
        TokenGranter tokenGranter = endpoints.getTokenGranter();//OAuth2 自带的授权模式；
        List<TokenGranter> tokenGranters = getTokenGranters(authenticationManager, tokenServices(),
                endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory());// 获取自定义的 TokenGranter 集合
        tokenGranters.add(tokenGranter);//将 OAuth2 自带的授权模式添加到集合中
        endpoints.tokenGranter(new CompositeTokenGranter(tokenGranters));// 最后将授权模式集合封装成 CompositeTokenGranter ，然后添加到 endpoint 配置中；
    }

    /**
     * @description: 2.1、AuthorizationServerTokenServices
     * @author kobe_xt
     * @date: 2022/9/7 10:35
     * @param:
     * @return: org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices
     */
    @Bean
    public DefaultTokenServices tokenServices(){
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setClientDetailsService(clientDetailsService(dataSource));// 设置客户端信息服务
        tokenServices.setSupportRefreshToken(true);// 设置是否支持刷新令牌
        tokenServices.setTokenStore(tokenStore);// 设置令牌存储策略
        //当使用 JdbcClientDetailsService 时，此处设置令牌的有效期无效，而是以数据库中 oauth_client_details 表中配置的为准
//        tokenServices.setAccessTokenValiditySeconds(10);// 设置令牌有效期
//        tokenServices.setRefreshTokenValiditySeconds(10);// 设置 refresh_token 有效期（3天）
        // 使用 JWT 的时候，需要配置令牌增强器
        TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
        enhancerChain.setTokenEnhancers(Collections.singletonList(jwtAccessTokenConverter));
        tokenServices.setTokenEnhancer(enhancerChain);

        return tokenServices;
    }

    /**
     * @description: 3、配置令牌访问端点的安全策略
     * @author kobe_xt
     * @date: 2022/9/7 10:49
     * @param: security
     * @return: void
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
//        security.tokenKeyAccess("permitAll()") // /oauth/token_key 端点完全公开
//                .checkTokenAccess("permitAll()") // /oauth/check_token 端点完全公开
//                .allowFormAuthenticationForClients(); // 允许通过表单提交的方式来申请令牌
        security.tokenKeyAccess("permitAll()") // /oauth/token_key 端点完全公开
                .checkTokenAccess("permitAll()")// /oauth/check_token 端点完全公开
                .addTokenEndpointAuthenticationFilter(myClientAuthenticationFilter);// 添加客户端信息校验过滤器

    }

    /**
     * @description: 配置授权码服务，指定授权码的存储方式
     * @author kobe_xt
     * @date: 2022/9/7 14:39
     * @param:
     * @return: org.springframework.security.oauth2.provider.code.AuthorizationCodeServices
     */
    @Bean
    public AuthorizationCodeServices authorizationCodeServices(DataSource dataSource){
//        return new InMemoryAuthorizationCodeServices();//将授权码存储在内存中
        return new JdbcAuthorizationCodeServices(dataSource);//将授权码存储在数据库中
    }

    /**
     * @description: 获取自定义 TokenGranter 集合
     * @author kobe_xt
     * @date: 2022/9/21 14:56
     * @param: authenticationManager,tokenServices,clientDetailsService,requestFactory
     * @return: java.util.List<org.springframework.security.oauth2.provider.TokenGranter>
     */
    private List<TokenGranter> getTokenGranters(AuthenticationManager authenticationManager,AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory) {
        return CollectionUtil.toList(
                new SmsCodeTokenGranter(authenticationManager, tokenServices, clientDetailsService, requestFactory),
                new ScanQRCodeTokenGranter(authenticationManager, tokenServices, clientDetailsService, requestFactory));
    }
}
