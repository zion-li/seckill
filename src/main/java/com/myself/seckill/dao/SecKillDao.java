package com.myself.seckill.dao;


import com.myself.seckill.entity.SecKill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author zion
 */
@Mapper
public interface SecKillDao {

    int deleteByPrimaryKey(@Param("seckillId") Long seckillId);

    int insert(SecKill record);

    int insertSelective(SecKill record);

    SecKill selectByPrimaryKey(@Param("seckillId") Long seckillId);

    int updateByPrimaryKeySelective(SecKill record);

    int updateByPrimaryKey(SecKill record);

    /**
     * 减库存
     * @param seckilled
     * @param killTime
     * @return
     */
    int reduceNumber(@Param("seckilled") long seckilled, @Param("killTime") Date killTime);

    /**
     * 查询所有秒杀记录
     * @return
     */
    List<SecKill> queryAll(@Param("offset") int offset,@Param("limit") int limit);
}