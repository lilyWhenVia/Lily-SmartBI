package com.lilyVia.springbootinit.manager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * Created by lily via on 2024/3/23 12:02
 */
@SpringBootTest
class RedissonManagerTest {

    @Resource
    RedissonManager redissonManager;

    @Test
    public void rateLimiterTest(){

        for (int i = 0; i < 8; i++) {
            redissonManager.doReteLimiter("lock1");
            System.out.println(i+","+Thread.currentThread().getName());
        }
    }
}