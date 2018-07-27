package com.myself.seckill.dao.cache;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.myself.seckill.entity.SecKill;
import com.myself.seckill.entity.SeckillUser;
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
    private RuntimeSchema<SecKill> secKillRuntimeSchema = RuntimeSchema.createFrom(SecKill.class);

    /**
     * 自定义序列化工具
     */
    private RuntimeSchema<SeckillUser> seckillUserRuntimeSchema = RuntimeSchema.createFrom(SeckillUser.class);

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
            SecKill secKill = new SecKill();
            ProtostuffIOUtil.mergeFrom((byte[]) bytes, secKill, secKillRuntimeSchema);
            return secKill;
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
            byte[] bytes = ProtostuffIOUtil.toByteArray(secKill, secKillRuntimeSchema,
                    LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
            redisTemplate.opsForValue().set(key, bytes, 1L, TimeUnit.HOURS);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }


    /**
     * 查询缓存
     *
     * @param seckillUserId
     * @return
     */
    public SeckillUser getSeckillUser(Long seckillUserId) {
        try {
            String key = "seckill:" + seckillUserId;
            //空间压缩1/10，速度差两个数量级
            //并没有实现内部的序列化操作,get的是byte[]，序列化反序化拿到对象,使用开源社区自定义序列化方式
            Object bytes = redisTemplate.opsForValue().get(key);
            SeckillUser seckillUser = new SeckillUser();
            ProtostuffIOUtil.mergeFrom((byte[]) bytes, seckillUser, seckillUserRuntimeSchema);
            return seckillUser;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 插入缓存
     *
     * @param seckillUser
     * @return
     */
    public String putSeckillUser(SeckillUser seckillUser) {
        //set object ->bytes(序列化的过程）
        String key = "seckill:" + seckillUser.getId();
        try {

            byte[] bytes = ProtostuffIOUtil.toByteArray(seckillUser, seckillUserRuntimeSchema,
                    LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
            redisTemplate.opsForValue().set(key, bytes, 1L, TimeUnit.HOURS);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return key;
    }

    /**
     * 添加路径访问次数
     */
    public boolean incrUrlAccessCount(String key) {
        return true;
    }

    public boolean setUrlAccessCount(String key, int value) {
        return true;
    }

    public Integer getUrlAccessCount(String key) {
        return 0;
    }

}
