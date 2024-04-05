package com.lilyVia.springbootinit.rabbitMq;

import javax.annotation.Resource;

/**
 * Created by lily via on 2024/3/29 16:24
 */
//@SpringBootTest
class BiInitMainTest {


    @Resource
    BiMqProducer producer;

//    @Test
    public void messageTest(){
            producer.sendMessage("message");
    }
}