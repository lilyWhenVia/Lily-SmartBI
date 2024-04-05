package com.lilyVia.springbootinit.config;

import com.lilyVia.springbootinit.common.ErrorCode;
import com.lilyVia.springbootinit.exception.BusinessException;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * Created by lily via on 2024/3/24 16:14
 */
@Configuration
public class ThreadPoolExecuteConfig {

    @Bean
    public ThreadPoolExecutor threadPoolExecutor(){
        // 获得线程工厂
        ThreadFactory threadFactory = new ThreadFactory() {
            private int count = 1;

            @Override
            public Thread newThread(@NotNull Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("生成图表线程" + count++);
                return thread;
            }
        };
        /**
         * @param:
         * 核心线程数2
         * 最大线程数4
         * 非核心线程数存活时间30s
         */
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(2, 4,
                30, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(50), threadFactory, new RejectedExecutionHandler() {
            // todo 拒绝前不抛弃任务
            @Override
                    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                        throw new BusinessException(ErrorCode.OPERATION_ERROR);
                    }
                }
        );
        return poolExecutor;
    }

}
