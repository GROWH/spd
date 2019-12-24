/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.platform.filter;

import org.jrplat.module.dictionary.service.DicService;
import org.jrplat.platform.log.JRPlatLogger;
import org.jrplat.platform.log.JRPlatLoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Locale;

/**
 * 使用Filter来指定浏览器缓存或不缓存服务器数据
 * 此过滤器放在最前面，之后的过滤器可以覆盖该值
 * @author 西安捷然
 */
public class ResponseHeaderFilter implements javax.servlet.Filter {
    private static final JRPlatLogger LOG = JRPlatLoggerFactory.getJRPlatLogger(DicService.class);

    private String expiresSeconds;
    private FilterConfig filterConfig;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        expiresSeconds = filterConfig.getInitParameter("expiresSeconds");
        LOG.info("初始化过滤器：ResponseHeaderFilter");
        LOG.info("init filter：ResponseHeaderFilter", Locale.ENGLISH);
        LOG.info("过期秒数：" + expiresSeconds);
        LOG.info("expires seconds：" + expiresSeconds, Locale.ENGLISH);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        for (Enumeration e = filterConfig.getInitParameterNames(); e.hasMoreElements(); ) {
            String headerName = (String) e.nextElement();
            if ("expiresSeconds".equals(headerName)) {
                httpResponse.addHeader("Expires", System.currentTimeMillis() + Integer.parseInt(expiresSeconds) * 1000 + "");
                httpResponse.addHeader("Cache-Control", "public, max-age=" + expiresSeconds);
            } else {
                httpResponse.addHeader(headerName, filterConfig.getInitParameter(headerName));
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        LOG.info("销毁过滤器：ResponseHeaderFilter");
        LOG.info("destroy filter：ResponseHeaderFilter", Locale.ENGLISH);
    }
}