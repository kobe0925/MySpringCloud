package com.kobe.xt.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author kobe_xt
 * @version 1.0
 * @description: TODO
 * @date 2022/9/13 13:25
 */
@RestController
@Slf4j
@RequestMapping("/test")
public class TestController {

    @PostMapping("/test01")
    public String test01(){
        return "test01 success";
    }

    @PostMapping("/test02")
    public String test02(){
        return "test02 success";
    }

}
