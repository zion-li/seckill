package com.myself.seckill.service.impl;

import com.myself.seckill.dao.SeckillUserDao;
import com.myself.seckill.dao.cache.RedisDao;
import com.myself.seckill.entity.SeckillUser;
import com.myself.seckill.exception.SeckillException;
import com.myself.seckill.service.UserService;
import com.myself.seckill.utils.MD5Util;
import com.myself.seckill.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Created by zion
 * @Date 2018/7/25.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private SeckillUserDao userDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisDao redisDao;


    public static final String COOKI_NAME_TOKEN = "token";

    private int defaultSecond = 30;

    @Override
    public String login(HttpServletResponse response, LoginVo loginVo) {
        if (loginVo == null) {
            throw new SeckillException("不合法");
        }
        String mobile = loginVo.getPhone();
        String formPass = loginVo.getPassword();
        //判断手机号是否存在
        SeckillUser user = userDao.selectByPrimaryKey(Long.parseLong(mobile));
        if (user == null) {
            throw new SeckillException("不合法");
        }
        //验证密码
        String dbPass = user.getPassword();
        if (MD5Util.verify(formPass, dbPass)) {
            throw new SeckillException("不合法");
        }
        //生成cookie
        return addCookie(response, user);
    }


    @Override
    public SeckillUser getByToken(HttpServletResponse response, String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        SeckillUser user = (SeckillUser) redisTemplate.opsForValue().get(token);
        //延长有效期
        addCookie(response, user);
        return user;
    }


    private String addCookie(HttpServletResponse response, SeckillUser user) {
        String token = redisDao.putSeckillUser(user);
        Cookie cookie = new Cookie(COOKI_NAME_TOKEN, token);
        cookie.setMaxAge(defaultSecond);
        cookie.setPath("/");
        response.addCookie(cookie);
        return token;
    }
}
