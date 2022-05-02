package com.kadima.consumer.mq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author qianyong
 * @company finedo.cn
 * @date 2022/5/2
 * @title：
 */
@Component
public class dealMessage {

    @RabbitListener(queues = "${study.rabbitMQ.queue}")
    public void delMes(String mes){
        System.out.println("接收到消息："+mes);
    }
}
