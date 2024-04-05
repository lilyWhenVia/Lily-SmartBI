package com.lilyVia.springbootinit.rabbitMq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.lilyVia.springbootinit.constant.MqConstant;
import lombok.SneakyThrows;

/**
 * 之启动一次用于生成项目消息队列
 * Created by lily via on 2024/3/29 10:22
 */
public class BiInitMain {

    @SneakyThrows
    public static void main(String[] args) {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // 工作队列与交换机绑定
        channel.exchangeDeclare(MqConstant.BI_WORK_EXCHANGE, "direct");
        channel.queueDeclare(MqConstant.BI_WORK_QUEUE, true, false, false, null);
        channel.queueBind(MqConstant.BI_WORK_QUEUE, MqConstant.BI_WORK_EXCHANGE, MqConstant.BI_WORK_ROUTING_KEY);

        // 死信队列与交换机绑定


    }

}
