package com.myself.seckill.access;

import com.myself.seckill.entity.SeckillUser;

/**
 * 登录用户信息
 * @author Created by zion
 * @Date 2018/7/26.
 */
public class UserContext {
    private static ThreadLocal<SeckillUser> userHolder=new ThreadLocal<>();

    public static SeckillUser getUserHolder() {
        return userHolder.get();
    }

    public static void setUserHolder(SeckillUser user) {
        userHolder.set(user);
    }
}
