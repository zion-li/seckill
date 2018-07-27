package com.myself.seckill.config;

import com.myself.seckill.access.AccessFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author Created by zion
 * @Date 2018/7/27.
 */
@Configuration
public class FilterConfig {
    @Autowired
    private AccessFilter accessFilter;

    @Bean
    public FilterRegistrationBean accessFilterBean() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(accessFilter);
        registration.addUrlPatterns("/*");
        registration.setOrder(3);
        registration.setName("accessFilter");
        return registration;
    }
}
