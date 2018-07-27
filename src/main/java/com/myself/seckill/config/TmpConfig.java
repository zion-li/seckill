package com.myself.seckill.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;
import java.io.File;
import java.net.URL;

/**
 * 配置SpringBoot项目上上传文件的临时目录路径
 * @author Created by zion
 * @Date 2018/6/8.
 */
@Configuration
public class TmpConfig {

    private static final Logger logger = LoggerFactory.getLogger(TmpConfig.class);
    /**
     * 文件上传临时路径
     */
    @Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        String relativePath="/tmp";
        URL url=Thread.currentThread().getContextClassLoader().getResource("");
        if (url != null) {
            relativePath = url.getPath()+"tmp";
        }
        File dir = new File(relativePath);
        if (!dir.exists()) {
            logger.info(">>>>>>>>>>>>>>>>>>>>Spring Boot upload file tmp dir is:"+relativePath+",But it's not exists");
            logger.info(">>>>>>>>>>>>>>>>>>>>Start creating dir");
            dir.mkdirs();
            logger.info(">>>>>>>>>>>>>>>>>>>> creating finish");
        }
        factory.setLocation(relativePath);
        logger.info("Spring Boot upload file tmp dir is:"+relativePath);
        return factory.createMultipartConfig();
    }

}