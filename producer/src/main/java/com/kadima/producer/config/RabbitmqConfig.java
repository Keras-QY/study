package com.kadima.producer.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author qianyong
 * @company finedo.cn
 * @date 2022/5/1
 * @title：springBoot集成了RabbitMQ,声明队列、交换机作为bean放到容器里
 */
@Configuration
public class RabbitmqConfig {

    //交换机bean的名称
    public static final String Exchange_Bean = "exchange_bean";


    //声明交换机
    @Bean(Exchange_Bean)
    public Exchange getExchange(){
        return ExchangeBuilder.directExchange(Exchange_Bean).durable(true).build();
    }


}
