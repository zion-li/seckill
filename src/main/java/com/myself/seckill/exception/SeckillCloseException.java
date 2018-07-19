package com.myself.seckill.exception;

/**
 * 秒杀关闭异常
 * @author Created by zion
 * @Date 2018/7/19.
 */
public class SeckillCloseException  extends SeckillException{
    public SeckillCloseException(String message) {
        super(message);
    }

    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }
}
