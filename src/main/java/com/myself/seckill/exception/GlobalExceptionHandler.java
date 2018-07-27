package com.myself.seckill.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import com.myself.seckill.dto.SeckillResult;

/**
 * @author Created by zion
 * @ControllerAdvice + @ExceptionHandler 实现全局的 Controller 层的异常处理
 * @Date 2018/7/25.
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理所有不可知的异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    SeckillResult handleException(Exception e) {
        LOGGER.error(e.getMessage(), e);
        LOGGER.error(e.getMessage(), e);
        SeckillResult result = new SeckillResult(false, "操作失败");
        return result;
    }


    /**
     * SeckillException
     *
     * @param e
     * @return
     */
    @ExceptionHandler(SeckillException.class)
    @ResponseBody
    SeckillResult handleSeckillException(SeckillException e) {
        LOGGER.error(e.getMessage(), e);
        SeckillResult result = new SeckillResult(false, e.getMessage());
        return result;
    }

    /**
     * 重复秒杀异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(RepeatKillException.class)
    @ResponseBody
    SeckillResult handleRepeatKillException(RepeatKillException e) {
        LOGGER.error(e.getMessage(), e);
        SeckillResult result = new SeckillResult(false, e.getMessage());
        return result;
    }

    /**
     * 秒杀关闭异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(SeckillCloseException.class)
    @ResponseBody
    SeckillResult handleSeckillCloseException(SeckillCloseException e) {
        LOGGER.error(e.getMessage(), e);
        SeckillResult result = new SeckillResult(false, e.getMessage());
        return result;
    }

    /**
     * 处理所有接口数据验证异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    SeckillResult handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        LOGGER.error(e.getMessage(), e);
        SeckillResult result = new SeckillResult(false, e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return result;
    }
}
