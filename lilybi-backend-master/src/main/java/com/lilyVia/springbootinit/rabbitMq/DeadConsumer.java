package com.lilyVia.springbootinit.rabbitMq;

import com.lilyVia.springbootinit.constant.ChartConstant;
import com.lilyVia.springbootinit.model.entity.Chart;
import com.lilyVia.springbootinit.service.ChartService;
import com.rabbitmq.client.Channel;
import com.lilyVia.springbootinit.constant.MqConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.IOException;

/**
 * @description: 死信队列数据库记录失败状态
 * @author Lily Via
 * @date 2024/4/5 11:03
 * @version 1.0
 */
@Component
@Slf4j
public class DeadConsumer {

    @Resource
    private ChartService ChartService;

    @RabbitListener(queues = {MqConstant.DEAD_LETTER_QUEUE})
    public void process(@NotEmpty String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        // 打日志，报警
        log.info("死信队列收到来自 {} 的消息 : {}" , deliveryTag,message);

        /**
         * 更新图表生成状态
         */
        long chartId = Long.parseLong(message);
        Chart chartStatus = new Chart();
        chartStatus.setId(chartId);
        chartStatus.setChartStatus(ChartConstant.FAILED);
        boolean b = ChartService.updateById(chartStatus);
        if (b){
            channel.basicAck(deliveryTag, false);
        }else{
            channel.basicNack(deliveryTag, false, true);
            ChartService.handleGenChartError(chartId, "图表更新失败状态失败");
        }

    }
}
