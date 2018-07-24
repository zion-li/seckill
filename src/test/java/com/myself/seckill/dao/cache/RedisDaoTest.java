package com.myself.seckill.dao.cache;

import com.myself.seckill.SeckillApplication;
import com.myself.seckill.entity.SecKill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Created by zion
 * @Date 2018/7/23.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SeckillApplication.class)
public class RedisDaoTest {
    @Autowired
    private RedisDao redisDao;
    @Test
    public void getSeckill1() throws Exception {
        SecKill secKill=new SecKill();
        secKill.setSeckillId(1L);
        redisDao.putSeckill(secKill);
    }

    @Test
    public void putSeckill1() throws Exception {

    }

}