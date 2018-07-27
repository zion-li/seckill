package com.myself.seckill.controller;

import com.myself.seckill.dto.SeckillResult;
import com.myself.seckill.service.UserService;
import com.myself.seckill.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * 登录认证
 *
 * @author Created by zion
 * @Date 2018/7/25.
 */
@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UserService userService;

    @RequestMapping(name = "/do_login",
            method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    @ResponseBody
    public SeckillResult<Boolean> doLogin(HttpServletResponse response, @Validated @RequestBody LoginVo loginVo) {
        return new SeckillResult(true, userService.login(response, loginVo));
    }
}
