package com.myself.seckill.dto;

import lombok.*;

/**
 * 暴露秒杀接口
 *
 * @author Created by zion
 * @Date 2018/7/19.
 */
@Builder
@Getter
@Setter
public class Exposer {

    /**
     * 是否开启秒杀
     */
    private boolean exposed;

    /**
     * 一种加密措施
     */
    private String md5;

    /**
     * 商品ID
     */
    private long seckillId;

    /**
     * 系统时间
     */
    private long now;

    /**
     * 秒杀开启时间
     */
    private long start;

    /**
     * 秒杀结束时间
     */
    private long end;

    public Exposer(boolean exposed, String md5, long seckillId) {
        this.exposed = exposed;
        this.md5 = md5;
        this.seckillId = seckillId;
    }

    public Exposer(boolean exposed, long seckillId,long now, long start, long end) {
        this.exposed = exposed;
        this.seckillId = seckillId;
        this.now = now;
        this.start = start;
        this.end = end;
    }

    public Exposer(boolean exposed, long seckillId) {
        this.exposed = exposed;
        this.seckillId = seckillId;
    }

    public Exposer() {
    }
}
