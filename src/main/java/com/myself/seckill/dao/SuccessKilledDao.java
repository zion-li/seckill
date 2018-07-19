package com.myself.seckill.dao;

import com.myself.seckill.entity.SuccessKilled;
import com.myself.seckill.entity.SuccessKilledKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 记录秒杀购买明细
 * @author zion
 */
@Repository
public interface SuccessKilledDao {


    int deleteByPrimaryKey(SuccessKilledKey key);

    /**
     * 插入购买明细，可以过滤重复，避免绕过前端控制
     * @param record
     * @return
     */
    int insert(SuccessKilledKey record);

    int insertSelective(SuccessKilledKey record);

    SuccessKilledKey selectByPrimaryKey(SuccessKilledKey key);

    int updateByPrimaryKeySelective(SuccessKilledKey record);

    int updateByPrimaryKey(SuccessKilledKey record);

    int insertSuccessKilled(SuccessKilledKey record);

    SuccessKilled queryByIdWithSeckill(@Param("seckillId") long seckillId,@Param("userPhone") long userPhone);
}