package com.myself.seckill.dto;

import com.myself.seckill.entity.SuccessKilled;
import com.myself.seckill.enums.SeckillStatEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 秒杀执行后的结果
 *
 * @author Created by zion
 * @Date 2018/7/19.
 */
@Builder
@Getter
@Setter
public class SeckillExecution {

    private long seckillId;

    /**
     * 状态
     */
    private int state;

    /**
     * 状态标识
     */
    private String stateInfo;

    /**
     * 秒杀成功对象
     */
    private SuccessKilled successKilled;

    public SeckillExecution(long seckillId, SeckillStatEnum statEnum, SuccessKilled successKilled) {
        this.seckillId = seckillId;
        this.state = statEnum.getKey();
        this.stateInfo = statEnum.getDesc();
        this.successKilled = successKilled;
    }

    public SeckillExecution(long seckillId, SeckillStatEnum statEnum) {
        this.seckillId = seckillId;
        this.state = statEnum.getKey();
        this.stateInfo = statEnum.getDesc();
    }

    public SeckillExecution() {
    }
}
