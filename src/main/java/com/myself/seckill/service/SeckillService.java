package com.myself.seckill.service;

import com.myself.seckill.dto.Exposer;
import com.myself.seckill.dto.SeckillExecution;
import com.myself.seckill.entity.SecKill;
import com.myself.seckill.exception.RepeatKillException;
import com.myself.seckill.exception.SeckillCloseException;
import com.myself.seckill.exception.SeckillException;

import java.util.List;

/**
 * 业务接口：站在使用者的角度设计接口
 * 方法定义粒度，参数，返回类型
 *
 * @author Created by zion
 * @Date 2018/7/19.
 */
public interface SeckillService {

    /**
     * 获取所有秒杀记录
     *
     * @return
     */
    List<SecKill> getSeckillList();

    /**
     * 查询单个秒杀记录
     *
     * @param seckillId
     * @return
     */
    SecKill getById(long seckillId);

    /**
     * 秒杀开启时输出秒杀的URL
     * 否者输出系统时间和秒杀时间
     *
     * @param seckillId
     */
    Exposer exportSeckillUrl(long seckillId);

    /**
     * 执行秒杀
     *
     * @param seckillId 产品ID
     * @param userPhone 用户身份
     * @param md5       规则校验，不正确就拒绝执行秒杀
     */
    SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws RepeatKillException, SeckillCloseException, SeckillException;
}
