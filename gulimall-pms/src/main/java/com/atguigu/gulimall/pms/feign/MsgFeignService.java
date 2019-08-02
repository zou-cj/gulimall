package com.atguigu.gulimall.pms.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

@Service
@FeignClient("gulimall-oms")
public interface MsgFeignService {

    @GetMapping("/hello")
    public String HAHA();
}
