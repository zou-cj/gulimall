package com.atguigu.gulimall.pms.controller;

import com.atguigu.gulimall.pms.feign.MsgFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @Autowired
    MsgFeignService msgFeignService;

    @GetMapping("/sb")
    public String qdsb() {

        String haha = msgFeignService.HAHA();
        String hehe = "老子去调傻逼";
        return hehe +" " +haha;
    }
}
