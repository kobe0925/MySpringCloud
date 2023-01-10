package com.kobe.xt.controller;

import com.kobe.xt.response.ResponseDTO;
import com.kobe.xt.service.UserInfoService;
import com.kobe.xt.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author kobe_xt
 * @version 1.0
 * @description: TODO
 * @date 2022/12/23 11:18
 */
@RestController
@Slf4j
@RequestMapping("/test")
public class TestController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserInfoService userInfoService;

    @PostMapping("/transactionTest")
    public String test(){
//        userService.updateUserInfo();
//        userService.test();
        userInfoService.userInfo();
        return "success";
    }
}
