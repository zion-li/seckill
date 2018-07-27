package com.myself.seckill.config;

import com.alibaba.druid.support.http.StatViewServlet;
import com.google.common.collect.Maps;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @author Created by zion
 * @Date 2018/7/27.
 */
@Configuration
public class ServletConfig {
    @Bean
    public ServletRegistrationBean druidStatViewServletBean() {
        //后台的路径
        ServletRegistrationBean statViewServletRegistrationBean =
                new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        Map<String, String> params = Maps.newHashMap();
        //账号密码，是否允许重置数据
        params.put("loginUsername", "root");
        params.put("loginPassword", "root");
        params.put("resetEnable", "true");
        statViewServletRegistrationBean.setInitParameters(params);
        return statViewServletRegistrationBean;
    }
}
