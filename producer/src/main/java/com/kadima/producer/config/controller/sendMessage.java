package com.kadima.producer.config.controller;

import com.kadima.producer.config.RabbitmqConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author qianyong
 * @company finedo.cn
 * @date 2022/5/2
 * @title：
 */
@Controller
@RequestMapping("/rabbit/")
public class sendMessage {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @RequestMapping("/send")
    public void sendmes(){
        System.out.println("进入到发送消息控制层");
        rabbitTemplate.convertAndSend(RabbitmqConfig.Exchange_Bean,"5a751fab6abb5044e0d19ea1","hello world");
    }
}
