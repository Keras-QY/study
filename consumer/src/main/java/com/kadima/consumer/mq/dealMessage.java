package com.kadima.consumer.mq;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author qianyong
 * @company finedo.cn
 * @date 2022/5/2
 * @title：
 */
@Component
public class dealMessage {

    @RabbitListener(queues = "${study.rabbitMQ.queue}")
    public void delMes(String mes, Message message, Channel channel) throws IOException {
        System.out.println("接收到消息："+mes);
        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        /**
         * 消息被正确处理,手动ack机制
         * 消费异常，把消息重新放回队列给其他消费者或者下次使用：
         * channel.basicNack(deliveryTag, false, true)，或者 channel.basicReject(deliveryTag:, true);
         * 消费异常，不想把消息重新放回队列
         * channel.basicNack(deliveryTag, false, false）, channel.basicReject(deliveryTag:, false);
         */
        channel.basicAck(deliveryTag,false);

    }
}
