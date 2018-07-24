package com.myself.seckill.dao.cache;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.myself.seckill.entity.SecKill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author Created by zion
 * @Date 2018/7/23.
 */
@Component
public class RedisDao {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 自定义序列化工具
     */
    private RuntimeSchema<SecKill> schema = RuntimeSchema.createFrom(SecKill.class);

    /**
     * 查询缓存
     *
     * @param seckillId
     * @return
     */
    public SecKill getSeckill(Long seckillId) {
        try {
            String key = "seckill:" + seckillId;
            //空间压缩1/10，速度差两个数量级
            //并没有实现内部的序列化操作,get的是byte[]，序列化反序化拿到对象,使用开源社区自定义序列化方式
            Object bytes = redisTemplate.opsForValue().get(key);
            if (null != bytes) {
                SecKill secKill = new SecKill();
                ProtostuffIOUtil.mergeFrom((byte[]) bytes, secKill, schema);
                return secKill;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 插入缓存
     *
     * @param secKill
     * @return
     */
    public void putSeckill(SecKill secKill) {
        //set object ->bytes(序列化的过程）
        try {
            String key = "seckill:" + secKill.getSeckillId();
            byte[] bytes = ProtostuffIOUtil.toByteArray(secKill, schema,
                    LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
            redisTemplate.opsForValue().set(key, bytes, 1L, TimeUnit.HOURS);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

}
