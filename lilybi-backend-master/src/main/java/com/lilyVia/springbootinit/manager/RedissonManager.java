package com.lilyVia.springbootinit.manager;

import com.lilyVia.springbootinit.common.ErrorCode;
import com.lilyVia.springbootinit.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by lily via on 2024/3/23 11:20
 */
@Component
@Slf4j
public class RedissonManager {

    @Autowired
    private RedissonClient redissonClient;


    public void doReteLimiter(String keyName){
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(keyName);
        /**
         * 已经设置了rate，则trySetRate返回为True，否则为false
         */
        log.info(Thread.currentThread().getName());
        rateLimiter.trySetRate(RateType.OVERALL, 2, 1, RateIntervalUnit.SECONDS);
        // todo 验证令牌延迟发放问题是否与try acquire有关
        boolean canOp = rateLimiter.tryAcquire(1);
        if (!canOp){
            throw new BusinessException(ErrorCode.TOO_MANY_REQUEST, ErrorCode.TOO_MANY_REQUEST.getMessage());
        }
    }

}
