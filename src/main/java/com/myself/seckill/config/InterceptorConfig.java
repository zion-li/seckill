package com.myself.seckill.config;

import com.myself.seckill.access.AccessInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Created by zion
 * @Date 2018/7/27.
 */
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private AccessInterceptor accessInterceptor;
    /**
     * 添加拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(accessInterceptor).addPathPatterns("/");
    }
}
