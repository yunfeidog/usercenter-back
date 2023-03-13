package com.cxk.usercenter.exception;


import com.cxk.usercenter.common.ErrorCode;
import com.cxk.usercenter.common.R;
import com.cxk.usercenter.common.RUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 * Spring AOP
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 只去处理 BusinessException 异常
     *
     * @param e 异常
     * @return R
     */
    @ExceptionHandler(value = BusinessException.class)
    public R businessExceptionHandler(BusinessException e) {
        log.info("业务异常：{}", e.getMessage());
        return RUtils.error(e.getCode(),e.getMessage(),e.getDescription());
    }

    @ExceptionHandler(value = RuntimeException.class)
    public R runtimeExceptionHandler(RuntimeException e) {
        log.info("运行时异常：{}", e.getMessage());
        return RUtils.error(ErrorCode.SYSTEM_ERROR,e.getMessage(),"");
    }
}
