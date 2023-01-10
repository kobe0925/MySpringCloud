package com.kobe.xt.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @author: kobe_xt
 * @Date: 2021/6/30 17:34
 * @Version:1.0
 */
@RestController
@Slf4j
@RequestMapping("/api")
public class TradeController {

    @PostMapping("/info")
    public String info(){
        return "tradeInfo success .........";
    }
}
