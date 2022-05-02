package com.kadima.consumer.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author qianyong
 * @company finedo.cn
 * @date 2022/5/1
 * @title：
 */
    @Configuration
    public class RabbitmqConfig {

        //队列bean的名称
        public static final String Queue_Bean = "queue_bean";

        //交换机bean的名称
        public static final String Exchange_Bean = "exchange_bean";

        //队列的名称
        @Value("${study.rabbitMQ.queue}")
        public String queue_name;

        //routingKey 即站点Id
        @Value("${study.rabbitMQ.routingKey}")
        public String routingKey;

        //声明交换机
        @Bean(Exchange_Bean)
        public Exchange getExchange(){
            return ExchangeBuilder.directExchange(Exchange_Bean).durable(true).build();
        }

        //声明队列
        @Bean(Queue_Bean)
        public Queue getQueue(){
            return new Queue(queue_name);
        }

        //绑定队列到交换机
        @Bean
        public Binding binding_Ee_queue(@Qualifier(Queue_Bean) Queue queue, @Qualifier(Exchange_Bean) Exchange exchange){
            return BindingBuilder.bind(queue).to(exchange).with(routingKey).noargs();

        }


    }

