package com.lilyVia.springbootinit.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by lily via on 2024/3/23 9:05
 */
@Configuration
@ConfigurationProperties("spring.redis")
@Data
@Slf4j
public class RedissonConfig {

    private String host;

    private String port;

    private String password;

    private int timeout;

    private int database;


    @Bean
    public RedissonClient redissonClient(){
        Config config = new Config();
        SingleServerConfig singleServerConfig = config.useSingleServer();
        singleServerConfig.setAddress("redis://"+host+":"+port)
                .setPassword(password)
                .setDatabase(database)
                .setTimeout(timeout);
        RedissonClient redissonClient = Redisson.create(config);
        return redissonClient;
    }
}
