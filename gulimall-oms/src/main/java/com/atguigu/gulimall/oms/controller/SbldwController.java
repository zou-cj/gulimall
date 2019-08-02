package com.atguigu.gulimall.oms.controller;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableDiscoveryClient
public class SbldwController {
    @GetMapping("/hello")
    public String HAHA() {
        String msg = "傻逼来调我";
        return msg;
    }
}
