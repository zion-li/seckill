package com.myself.seckill.exception;

/**
 * 秒杀业务异常
 * @author Created by zion
 * @Date 2018/7/19.
 */
public class SeckillException  extends RuntimeException{
    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}
