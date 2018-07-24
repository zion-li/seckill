package com.myself.seckill.service.impl;

import com.myself.seckill.dao.SecKillDao;
import com.myself.seckill.dao.SuccessKilledDao;
import com.myself.seckill.dto.Exposer;
import com.myself.seckill.dto.SeckillExecution;
import com.myself.seckill.entity.SecKill;
import com.myself.seckill.entity.SuccessKilled;
import com.myself.seckill.entity.SuccessKilledKey;
import com.myself.seckill.enums.SeckillStatEnum;
import com.myself.seckill.exception.RepeatKillException;
import com.myself.seckill.exception.SeckillCloseException;
import com.myself.seckill.exception.SeckillException;
import com.myself.seckill.service.SeckillService;
import com.myself.seckill.utils.MD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author Created by zion
 * @Date 2018/7/19.
 */
@Service
public class SeckillServiceImpl implements SeckillService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String salt = "#5&drfadsfasdLDTadf12353*&^23%52Gd==";

    @Autowired
    private SecKillDao secKillDao;

    @Autowired
    private SuccessKilledDao successKilledDao;

    @Override
    public List<SecKill> getSeckillList() {
        return secKillDao.queryAll();
    }

    @Override
    public SecKill getById(long seckillId) {
        return secKillDao.selectByPrimaryKey(seckillId);
    }

    @Override
    public Exposer exportSeckillUrl(long seckillId) {


        //缓存
        SecKill secKill = secKillDao.selectByPrimaryKey(seckillId);


        if (secKill == null) {
            return new Exposer(false, seckillId);
        }

        Date startTime = secKill.getStartTime();
        Date endTime = secKill.getEndTime();
        Date nowTime = new Date();
        if (nowTime.getTime() < startTime.getTime()
                || nowTime.getTime() > endTime.getTime()) {
            return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
        }
        String md5 = getMd5(seckillId);
        return new Exposer(true, md5, seckillId);
    }

    private String getMd5(long seckillId) {
        String base = seckillId + "/" + salt;
        return MD5Util.MD5(base);
    }

    /**
     * 使用注解控制事务。
     * 保证事务尽可能短的执行，不要穿插其他网络操作 prc/http或者玻璃到事务的方法外部。
     * 不是所有的方法都需要事务，
     *
     * @param seckillId 产品ID
     * @param userPhone 用户身份
     * @param md5       规则校验，不正确就拒绝执行秒杀
     * @return
     * @throws RepeatKillException
     * @throws SeckillCloseException
     * @throws SeckillException
     */
    @Override
    @Transactional
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws RepeatKillException, SeckillCloseException, SeckillException {
        //MD5判断
        if (md5 == null || !md5.equals(getMd5(seckillId))) {
            throw new SeckillException("seckill data rewrite");
        }
        //执行秒杀逻辑(减库存+记录购买记录)
        Date nowTime = new Date();
        try {
            int updateCount = secKillDao.reduceNumber(seckillId, nowTime);
            if (updateCount <= 0) {
                //没有更新记录,秒杀结束
                throw new SeckillCloseException("seckill is closed");
            } else {
                //减库存成功，记录购买行为
                int insertCount = successKilledDao.insertSuccessKilled(new SuccessKilledKey(seckillId, userPhone));
                if (insertCount <= 0) {
                    //重复秒杀
                    throw new RepeatKillException("seckill repeated");
                } else {
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilled);
                }
            }
        } catch (SeckillCloseException | RepeatKillException e1) {
            throw e1;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            //所有编译器异常转化为运行期异常
            throw new SeckillException("seckill inner error：" + e.getMessage());
        }
    }
}
