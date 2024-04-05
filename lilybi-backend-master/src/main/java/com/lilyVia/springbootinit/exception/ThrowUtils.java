package com.lilyVia.springbootinit.exception;

import com.lilyVia.springbootinit.common.ErrorCode;
import lombok.extern.slf4j.Slf4j;

/**
 * 抛异常工具类
 *
 */
@Slf4j
public class ThrowUtils {

    /**
     * 条件成立则抛异常
     *
     * @param condition
     * @param runtimeException
     */
    public static void throwIf(boolean condition, RuntimeException runtimeException) {
        if (condition) {
            throw new RuntimeException();
        }
    }

    /**
     * 条件成立则抛异常
     *
     * @param condition
     * @param errorCode
     */
    public static void throwIf(boolean condition, ErrorCode errorCode) {
        log.error(errorCode.getMessage());
        throwIf(condition, new BusinessException(errorCode));
    }

    /**
     * 条件成立则抛异常
     *
     * @param condition
     * @param errorCode
     * @param message
     */
    public static void throwIf(boolean condition, ErrorCode errorCode, String message) {
        log.error(errorCode.getMessage()+","+message);
        throwIf(condition, new BusinessException(errorCode, message));
    }
}
