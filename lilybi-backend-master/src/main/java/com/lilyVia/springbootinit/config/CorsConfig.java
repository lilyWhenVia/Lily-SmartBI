package com.lilyVia.springbootinit.config;

import com.alibaba.excel.event.Order;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
/**
 * 全局跨域配置
 *
* @author lily <a href="https://github.com/lilyWhenVia">come to find lily</a>
 */
@Configuration
public class CorsConfig implements Order {
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        // 设置运行跨域的地址，也可以使用 * 代替允许所有地址
        config.addAllowedOrigin("http://localhost:8000");
        // 是否运行携带 cookie 相关信息
        // 在前端也有的框架可以配置这一样，前端是否允许跨域的时候携带：axios.defaults.withCredentials = true;
        config.setAllowCredentials(true);
        // 允许访问所有的 http 请求方式，如 get、post
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");

        // 为 url 添加映射路径
        // 允许所有路径使用该配置
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }

    @Override
    public int order() {
        return 0;
    }
}

