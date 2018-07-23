package com.myself.seckill.service;

import com.myself.seckill.SeckillApplication;
import com.myself.seckill.dto.SeckillExecution;
import com.myself.seckill.entity.SecKill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author Created by zion
 * @Date 2018/7/20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SeckillApplication.class)
public class SeckillServiceTest {

    @Autowired
    private SeckillService seckillService;

    @Test
    public void getSeckillList() throws Exception {
        List<SecKill> list = seckillService.getSeckillList();
        System.out.println(list.toString());
    }

    @Test
    public void getById() throws Exception {
        SecKill list = seckillService.getById(1000L);
        System.out.println(list.toString());
    }

    @Test
    public void exportSeckillUrl() throws Exception {
        long id = 1000;
        System.out.println(seckillService.exportSeckillUrl(id).toString());
    }

    @Test
    public void executeSeckill() throws Exception {
            long id=1000;
            long phone=123123123112L;
            String md5="6f770ede0870a49dd5d6e1261ee6573d";
        SeckillExecution execution=seckillService.executeSeckill(id,phone,md5);
        System.out.println(execution);
    }

}