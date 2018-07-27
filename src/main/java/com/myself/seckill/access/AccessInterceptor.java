package com.myself.seckill.access;

import com.myself.seckill.dao.cache.RedisDao;
import com.myself.seckill.dto.SeckillResult;
import com.myself.seckill.entity.SeckillUser;
import com.myself.seckill.service.UserService;
import com.myself.seckill.service.impl.UserServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

/**
 * 拦截器,用过注解限制接口访问次数
 *
 * @author Created by zion
 * @Date 2018/7/27.
 */
public class AccessInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisDao redisDao;

    public AccessInterceptor() {
        super();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (handler instanceof HandlerMethod) {
            SeckillUser user = getUser(request, response);
            UserContext.setUserHolder(user);
            AccessLimit accessLimit = ((HandlerMethod) handler).getMethodAnnotation(AccessLimit.class);
            if (accessLimit == null) {
                return true;
            }
            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();
            boolean needLogin = accessLimit.needLogin();
            String key = request.getRequestURI();
            if (needLogin) {
                if (user == null) {
                    render(response, "Session不存在或者已经失效");
                    return false;
                }
                key += "_" + user.getId();
            }
            Integer count = redisDao.getUrlAccessCount(key);
            if (count == null) {
                redisDao.setUrlAccessCount(key, 1);
            } else if (count < maxCount) {
                redisDao.incrUrlAccessCount(key);
            } else {
                render(response, "Session不存在或者已经失效");
            }
        }
        return true;
    }

    private void render(HttpServletResponse response, String message) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        SeckillResult seckillResult = new SeckillResult(false, message);
        out.write(seckillResult.toString().getBytes("UTF-8"));
        out.flush();
        out.close();
    }


    private SeckillUser getUser(HttpServletRequest request, HttpServletResponse response) {
        String paramToken = request.getParameter(UserServiceImpl.COOKI_NAME_TOKEN);
        String cookieToken = getCookieValue(request, UserServiceImpl.COOKI_NAME_TOKEN);
        if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
            return null;
        }
        String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;

        return userService.getByToken(response, token);
    }

    private String getCookieValue(HttpServletRequest request, String cookiName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length <= 0) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookiName)) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
