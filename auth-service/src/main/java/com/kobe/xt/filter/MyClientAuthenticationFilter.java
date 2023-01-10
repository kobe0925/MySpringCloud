package com.kobe.xt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kobe.xt.response.ResponseDTO;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @author kobe_xt
 * @version 1.0
 * @description: 客户端信息过滤器，解决客户端信息异常时无法统一封装异常信息的问题
 * @date 2022/9/22 17:17
 */
@Component
@Slf4j
public class MyClientAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        this.handle(request,response,filterChain);
    }

    private void handle(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        ResponseDTO dto = new ResponseDTO();
        String clientSecret = null;
        String clientId = null;
        try {
            if (request.getRequestURI().equals("/oauth/token")) {
                Map<String, String> clientInfoMap = this.isHasClientDetails(request);
                if (clientInfoMap == null) {
                    dto.setRespCode(700000L);
                    dto.setMessage("请求中未包含客户端信息！");
                    dto.setResult(false);
                    response.setContentType("application/json,charset=utf-8");
                    response.setStatus(HttpStatus.OK.value());
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.writeValue(response.getOutputStream(), dto);
                    return;
                }
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication != null && authentication.isAuthenticated()) {
                    filterChain.doFilter(request, response);
                    return;
                }
                clientId = clientInfoMap.get("client_id");
                ClientDetails clientDetails = this.clientDetailsService.loadClientByClientId(clientId);
                clientSecret = clientDetails.getClientSecret();
                if (!bCryptPasswordEncoder.matches(clientInfoMap.get("client_secret"), clientSecret)) {
                    dto.setRespCode(700002L);
                    dto.setMessage("客户端秘钥错误！");
                    dto.setResult(false);
                    response.setContentType("application/json,charset=utf-8");
                    response.setStatus(HttpStatus.OK.value());
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.writeValue(response.getOutputStream(), dto);
                    return;
                }
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(clientDetails.getClientId(), clientDetails.getClientSecret(), clientDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(token);
            }
            filterChain.doFilter(request, response);
        }catch (NoSuchClientException e){
            log.info("------ 客户端id：{}，不存在",clientId);
            dto.setRespCode(700001L);
            dto.setMessage("客户端id不存在！");
            dto.setResult(false);
            response.setContentType("application/json,charset=utf-8");
            response.setStatus(HttpStatus.OK.value());
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(response.getOutputStream(), dto);
            return;
        }catch (Exception e){
            dto.setRespCode(700003L);
            dto.setMessage("客户端信息校验失败！");
            dto.setResult(false);
            response.setContentType("application/json,charset=utf-8");
            response.setStatus(HttpStatus.OK.value());
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(response.getOutputStream(), dto);
            return;
        }
    }

    /**
     * @description: 查询请求头中客户端信息，如果客户端信息为空，那么返回null
     * @author kobe_xt
     * @date: 2022/9/23 09:14
     * @param: request
     * @return: java.util.Map<java.lang.String,java.lang.String>
     */
    private Map<String,String> isHasClientDetails(HttpServletRequest request) {
        Map<String,String> map = null;
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(StringUtils.isNotBlank(header)){
            String clientInfo = new String(Base64.getDecoder().decode(header));
            String[] split = clientInfo.split(",");
            map = new HashMap<>();
            map.put("client_id",split[0]);
            map.put("client_secret",split[1]);
        }
        return map;
    }

    public ClientDetailsService getClientDetailsService() {
        return clientDetailsService;
    }

    public void setClientDetailsService(ClientDetailsService clientDetailsService) {
        this.clientDetailsService = clientDetailsService;
    }
}
