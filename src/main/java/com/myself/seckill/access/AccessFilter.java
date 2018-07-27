package com.myself.seckill.access;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 前台跨域访问配置
 *
 * @author Created by zion
 * @Date 2018/7/27.
 */
@Configuration
@ConfigurationProperties(prefix = "response.header")
public class AccessFilter implements Filter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private String allowOrigin;
    private String allowCredentials;
    private String allowMethods;
    private String maxAge;
    private String allowHeaders;
    private String characterEncoding;

    public String getAllowOrigin() {
        return allowOrigin;
    }

    public void setAllowOrigin(String allowOrigin) {
        this.allowOrigin = allowOrigin;
    }

    public String getAllowCredentials() {
        return allowCredentials;
    }

    public void setAllowCredentials(String allowCredentials) {
        this.allowCredentials = allowCredentials;
    }

    public String getAllowMethods() {
        return allowMethods;
    }

    public void setAllowMethods(String allowMethods) {
        this.allowMethods = allowMethods;
    }

    public String getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(String maxAge) {
        this.maxAge = maxAge;
    }

    public String getAllowHeaders() {
        return allowHeaders;
    }

    public void setAllowHeaders(String allowHeaders) {
        this.allowHeaders = allowHeaders;
    }

    public String getCharacterEncoding() {
        return characterEncoding;
    }

    public void setCharacterEncoding(String characterEncoding) {
        this.characterEncoding = characterEncoding;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        request.setCharacterEncoding(characterEncoding);
        HttpServletResponse res = (HttpServletResponse) response;
        res.setHeader("Access-Control-Allow-Origin", allowOrigin);
        res.setHeader("Access-Control-Allow-Credentials", allowCredentials);
        res.setHeader("Access-Control-Allow-Methods", allowMethods);
        res.setHeader("Access-Control-Max-Age", maxAge);
        res.setHeader("Access-Control-Allow-Headers", allowHeaders);
        res.setCharacterEncoding(characterEncoding);
        chain.doFilter(request, res);
    }

    @Override
    public void destroy() {
    }
}
