package com.myself.seckill.dao;

import com.myself.seckill.entity.SeckillUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SeckillUserDao {
    int deleteByPrimaryKey(Long id);

    int insert(SeckillUser record);

    int insertSelective(SeckillUser record);

    SeckillUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SeckillUser record);

    int updateByPrimaryKey(SeckillUser record);
}