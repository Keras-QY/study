package com.kadima.aop.controller;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.kadima.aop.annnotation.aopTest;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author qianyong
 * @company finedo.cn
 * @date 2022/5/6
 * @title：
 */

@RestController
@RequestMapping("/AOP/")
public class aopController {

    @RequestMapping("/test")
    @aopTest(value = "hello")
    public String aopTest(){
        System.out.println("目标方法执行中");
        //throw new RuntimeException();
        return "hello world";
    }
}
