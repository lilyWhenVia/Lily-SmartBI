package com.lilyVia.springbootinit.rabbitMq;

import com.lilyVia.springbootinit.constant.MqConstant;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by lily via on 2024/3/29 10:22
 */
@Component
public class BiMqProducer {

    @Resource
    private RabbitTemplate rabbitTemplate;

    public void sendMessage(String message){
        rabbitTemplate.convertAndSend(MqConstant.BI_WORK_EXCHANGE, MqConstant.BI_WORK_ROUTING_KEY, message);
    }

}
