package com.kobe.xt.service.impl;

import com.kobe.xt.dto.MyUserDetails;
import com.kobe.xt.entity.UserEntity;
import com.kobe.xt.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

/**
 * @author kobe_xt
 * @version 1.0
 * @description: 自定义的 UserDetailsService
 * @date 2022/9/7 13:45
 */
@Service
public class MyUserDetailsServiceImpl implements UserDetailsService {

    private static final String SMS_GRANT_TYPE = "sms";

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> userEntityOptional = userEntityRepository.findByUsername(username);
        if(!userEntityOptional.isPresent()){
           throw new UsernameNotFoundException("当前用户不存在！");
        }
        UserEntity userEntity = userEntityOptional.get();
        String password = userEntity.getPassword();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null){
            Map<String, String> details = (Map<String, String>) authentication.getDetails();
            if(details != null){
                String grantType = details.get("grant_type");
                if(SMS_GRANT_TYPE.equals(grantType)){
                    password = passwordEncoder.encode(password);
                }
            }
        }
        return MyUserDetails.builder().username(username).password(password).accountNonExpired(true)
                .accountNonLocked(true).credentialsNonExpired(true).enabled(true).authorities(AuthorityUtils.commaSeparatedStringToAuthorityList(userEntity.getRoleIds())).build();
    }
}
