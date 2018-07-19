package com.myself.seckill.dao;

import com.myself.seckill.SeckillApplication;
import com.myself.seckill.entity.SuccessKilled;
import com.myself.seckill.entity.SuccessKilledKey;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Created by zion
 * @Date 2018/7/19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=SeckillApplication.class)
public class SuccessKilledDaoTest {

    @Autowired
    private SuccessKilledDao successKilledDao;

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

    }

    @Test
    public void updateByPrimaryKeySelective() throws Exception {

    }

    @Test
    public void updateByPrimaryKey() throws Exception {

    }

    @Test
    public void insertSuccessKilled() throws Exception {
        SuccessKilledKey successKilledKey=new SuccessKilledKey();
        successKilledKey.setSeckillId(123123123L);
        successKilledKey.setUserPhone(123123123112L);
        int count= successKilledDao.insertSuccessKilled(successKilledKey);
        System.out.println(count);
    }

    @Test
    public void queryByIdWithSeckill() throws Exception {
        SuccessKilled successKilled=  successKilledDao.queryByIdWithSeckill(123123123L,123123123112L);
        if(successKilled!=null){
            System.out.println(successKilled.getSeckillId());
            System.out.println(successKilled.getUserPhone());
        }

    }

}