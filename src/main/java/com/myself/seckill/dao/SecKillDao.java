package com.myself.seckill.dao;


import com.myself.seckill.entity.SecKill;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
@Mapper
public interface SecKillDao {

    int deleteByPrimaryKey(Long seckillId);

    int insert(SecKill record);

    int insertSelective(SecKill record);

    SecKill selectByPrimaryKey(Long seckillId);

    int updateByPrimaryKeySelective(SecKill record);

    int updateByPrimaryKey(SecKill record);

    /**
     * 减库存
     * @param seckilled
     * @param killTime
     * @return
     */
    int reduceNumber(long seckilled, Date killTime);
}