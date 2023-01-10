package com.kobe.xt.controller;

import com.kobe.xt.exception.SmsCodeGrantException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author kobe_xt
 * @version 1.0
 * @description: TODO
 * @date 2022/9/9 13:55
 */
@RestController
@RequestMapping("/api")
public class LoginController {

    @GetMapping("/login")
    public String login(){
        return "success";
    }
}
