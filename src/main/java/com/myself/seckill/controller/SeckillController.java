package com.myself.seckill.controller;

import com.myself.seckill.dto.Exposer;
import com.myself.seckill.dto.SeckillExecution;
import com.myself.seckill.dto.SeckillResult;
import com.myself.seckill.entity.SecKill;
import com.myself.seckill.enums.SeckillStatEnum;
import com.myself.seckill.exception.RepeatKillException;
import com.myself.seckill.exception.SeckillCloseException;
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

    @RequestMapping(name = "/{seckillId}/detail", method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long seckillId, Model model) {
        if (seckillId == null) {
            return "redirect:/seckill/list";
        }
        SecKill secKill = seckillService.getById(seckillId);
        if (secKill == null) {
            return "forward:/seckill/list";
        }
        model.addAttribute("seckill", secKill);
        return "detail";
    }

    @RequestMapping(name = "/{seckillId}/exposer",
            method = RequestMethod.POST,
            produces = "{application/json;charset=UTF-8}")
    @ResponseBody
    public SeckillResult<Exposer> exposer(Long seckillId) {
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

    @RequestMapping(name = "/{seckillId}/{md5}/execution",
            method = RequestMethod.POST,
            produces = "{application/json;charset=UTF-8}")
    @ResponseBody
    public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") Long seckillId,
                                                   @PathVariable("md5") String md5,
                                                   @CookieValue(value = "userPhone", required = false) Long userPhone) {
        SeckillResult<SeckillExecution> result;
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

    @RequestMapping(name = "/time/now",
            method = RequestMethod.GET,
            produces = "{application/json;charset=UTF-8}")
    @ResponseBody
    public SeckillResult<Long> time() {
        Date date = new Date();
        return new SeckillResult(true,date.getTime());
    }

}
