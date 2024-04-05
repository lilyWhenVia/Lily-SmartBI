package com.lilyVia.springbootinit.config;

import io.github.briqt.spark4j.SparkClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * Created by lily via on 2024/3/20 23:25
 */
@Configuration
@ConfigurationProperties(prefix = "xunfei.client")
@Data
public class AiConfig {
        private String appid;
        private String apiSecret;
        private String apiKey;

        @Bean
        public SparkClient sparkClient() {
            SparkClient sparkClient = new SparkClient();
            sparkClient.apiKey = apiKey;
            sparkClient.apiSecret = apiSecret;
            sparkClient.appid = appid;
            return sparkClient;
        }

}
