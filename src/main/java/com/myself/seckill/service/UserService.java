package com.myself.seckill.service;

import com.myself.seckill.entity.SeckillUser;
import com.myself.seckill.vo.LoginVo;

import javax.servlet.http.HttpServletResponse;

/**
 * 用户相关功能
 *
 * @author Created by zion
 * @Date 2018/7/25.
 */
public interface UserService {
    /**
     * 登录校验方法验证
     *
     * @param response
     * @param loginVo
     * @return
     */
    String login(HttpServletResponse response, LoginVo loginVo);

    /**
     * 获取redis token
     * @param response
     * @param token
     * @return
     */
    SeckillUser getByToken(HttpServletResponse response, String token);
}
