package com.lilyVia.springbootinit.exception;

import com.lilyVia.springbootinit.common.BaseResponse;
import com.lilyVia.springbootinit.common.ErrorCode;
import com.lilyVia.springbootinit.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({BusinessException.class, Exception.class})
    public BaseResponse<?> businessExceptionHandler(BusinessException e) {
        log.error("BusinessException", e);
        return ResultUtils.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeExceptionHandler(RuntimeException e) {
        log.error("RuntimeException", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "系统错误");
    }

    // 如果异常是BindException
    @ExceptionHandler(BindException.class)
    public BaseResponse<?> exceptionHandler(BindException e) {
        log.error("参数校验异常：" + e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return ResultUtils.error(ErrorCode.PARAMS_ERROR, "参数校验异常：" + e.getBindingResult().getAllErrors().get(0).getDefaultMessage());

    }
    // 捕获所有Exception
}