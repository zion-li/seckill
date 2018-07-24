package com.myself.seckill.controller;

import com.myself.seckill.dto.Exposer;
import com.myself.seckill.dto.SeckillExecution;
import com.myself.seckill.dto.SeckillResult;
import com.myself.seckill.entity.SecKill;
import com.myself.seckill.enums.SeckillStatEnum;
import com.myself.seckill.exception.RepeatKillException;
import com.myself.seckill.exception.SeckillCloseException;
import com.myself.seckill.exception.SeckillException;
import com.myself.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * /模块/资源/{id}/细分
 *
 * @author Created by zion
 * @Date 2018/7/20.
 */
@Controller
@RequestMapping("/seckill")
public class SeckillController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    @RequestMapping(name = "/list", method = RequestMethod.GET)
    public String list(Model model) {
        //获取列表页
        List<SecKill> list = seckillService.getSeckillList();
        model.addAttribute("list", list);
        return "list";
    }

    @RequestMapping(name = "/{seckillId}/detail", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public SeckillResult<SecKill> detail(@PathVariable("seckillId") Long seckillId) {
        if (seckillId == null) {
            throw new SeckillException("参数不合法");
        }
        SecKill secKill = seckillService.getById(seckillId);
        if (secKill == null) {
            throw new SeckillException("参数不合法");
        }
        return new SeckillResult<>(true, secKill);
    }

    /**
     * 无法使用CND缓存，但是可以用redis中，集群一秒百万QPS，一致性维护成本低
     *
     * @param seckillId
     * @return
     */
    @RequestMapping(value = "/{seckillId}/exposer", produces = "application/json", method = RequestMethod.POST)
    @ResponseBody
    public SeckillResult<Exposer> exposer(@PathVariable("seckillId") Long seckillId) {
        SeckillResult<Exposer> result;
        try {
            Exposer exposer = seckillService.exportSeckillUrl(seckillId);
            result = new SeckillResult<>(true, exposer);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result = new SeckillResult<>(false, e.getMessage());
        }
        return result;
    }

    /**
     * 架构：redis原子计数器->记录行为消息（MQ）->消息行为落地（mysql）
     * 强大的运维团队，系统不太稳定。
     * 无法保证幂等性
     * 瓶颈：事务（通信网络延迟、GC） 2ms左右---1秒500次的减库存操作。性能指数级别下降
     * 优化：如何减少行级锁的时间
     * 一次GC50ms+机房（1300*2）/300000*2/3=20ms
     * 总时间 1次sql=20ms
     * 思路：把客户端的逻辑放在mysql服务器，避免网络延迟和Gc影响：
     * 定制化mysql优化方法   源码级别更改  update \/*auto_commit*\/
     * 使用存储过程完成
     *
     * @param seckillId
     * @param md5
     * @param userPhone
     * @return
     */
    @RequestMapping(name = "/{seckillId}/{md5}/execution",
            method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    @ResponseBody
    public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") Long seckillId,
                                                   @PathVariable("md5") String md5,
                                                   @CookieValue(value = "userPhone", required = false) Long userPhone) {
        if (userPhone == null) {
            return new SeckillResult<>(false, "未注册");
        }
        try {
            SeckillExecution execution = seckillService.executeSeckill(seckillId, userPhone, md5);
            return new SeckillResult<>(true, execution);
        } catch (RepeatKillException e1) {
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.PEPEAT_KILL);
            return new SeckillResult<>(true, execution);
        } catch (SeckillCloseException e2) {
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.END);
            return new SeckillResult<>(true, execution);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);
            return new SeckillResult<>(true, execution);
        }
    }

    /**
     * 访问一次内存10NS，一秒可以访问一亿次，不需要优化
     *
     * @return
     */
    @RequestMapping(value = "/time/now", method = RequestMethod.GET)
    @ResponseBody
    public SeckillResult<Long> getcurrentTime() {
        Date date = new Date();
        return new SeckillResult(true, date.getTime());
    }


}
