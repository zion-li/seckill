package com.myself.seckill.dao;

import com.myself.seckill.SeckillApplication;
import com.myself.seckill.entity.SecKill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

/**
 * 测试类
 * @author Created by zion
 * @Date 2018/7/19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=SeckillApplication.class)
public class SecKillDaoTest {

    @Autowired
    private SecKillDao secKillDao;
    @Test
    public void deleteByPrimaryKey() throws Exception {

    }

    @Test
    public void insert() throws Exception {

    }

    @Test
    public void insertSelective() throws Exception {

    }

    @Test
    public void selectByPrimaryKey() throws Exception {
        SecKill secKill= secKillDao.selectByPrimaryKey(1000L);
        System.out.println(secKill.getName());
    }

    @Test
    public void updateByPrimaryKeySelective() throws Exception {

    }

    @Test
    public void updateByPrimaryKey() throws Exception {

    }

    @Test
    public void reduceNumber() throws Exception {
       int i= secKillDao.reduceNumber(1000,new Date());
        System.out.println(i);
    }

}