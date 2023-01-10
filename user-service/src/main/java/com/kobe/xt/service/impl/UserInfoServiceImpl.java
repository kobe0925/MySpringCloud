package com.kobe.xt.service.impl;

import com.kobe.xt.service.UserInfoService;
import com.kobe.xt.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author kobe_xt
 * @version 1.0
 * @description: TODO
 * @date 2022/12/23 11:48
 */
@Slf4j
@Service
public class UserInfoServiceImpl implements UserInfoService {

    private UserService userService;

    @Override
    public void userInfo() {
        userService.updateUserInfo();
    }
}
