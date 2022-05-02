package com.kadima.producer.controller;

import com.kadima.producer.config.RabbitmqConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @author qianyong
 * @company finedo.cn
 * @date 2022/5/2
 * @title：
 */
@Controller
public class sendMes {

    @Autowired
    RabbitTemplate rabbitTemplate;

    public void sentMessage(){
        rabbitTemplate.convertAndSend(RabbitmqConfig.Exchange_Bean,"");
    }
}
