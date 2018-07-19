package com.myself.seckill.exception;

/**
 * 重复秒杀异常
 * @author Created by zion
 * @Date 2018/7/19.
 */
public class RepeatKillException extends SeckillException {

    public RepeatKillException(String message) {
        super(message);
    }

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }


}
