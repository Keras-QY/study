package com.kadima.aop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.kadima.aop.annnotation.aopTest;


/**
 * @author qianyong
 * @company finedo.cn
 * @date 2022/5/6
 * @title：
 */
@Controller
@RequestMapping("/AOP/")
public class aopController {

    @RequestMapping("/test")
    @aopTest(value = "hello")
    public void aopTest(){
        System.out.println("目标方法执行中");

    }
}
