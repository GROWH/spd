/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.monitor.filter;

import org.jrplat.module.monitor.model.ProcessTime;
import org.jrplat.module.security.model.User;
import org.jrplat.module.security.service.UserHolder;
import org.jrplat.module.system.service.PropertyHolder;
import org.jrplat.module.system.service.SystemListener;
import org.jrplat.platform.log.BufferLogCollector;
import org.jrplat.platform.log.JRPlatLogger;
import org.jrplat.platform.log.JRPlatLoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Locale;

/**
 * 性能过滤器
 *
 * @author 西安捷然
 */
public class PerformanceFilter implements Filter {
    private static final JRPlatLogger LOG = JRPlatLoggerFactory.getJRPlatLogger(PerformanceFilter.class);
    private boolean enabled = false;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        HttpServletRequest req = (HttpServletRequest) request;

        long start = 0;
        if (enabled && filter(req)) {
            start = System.currentTimeMillis();
        }
        chain.doFilter(request, response);
        if (enabled && filter(req)) {
            long end = System.currentTimeMillis();

            User user = UserHolder.getCurrentLoginUser();
            String userName = "";
            if (user != null) {
                userName = user.getUsername();
            }
            ProcessTime logger = new ProcessTime();
            logger.setUsername(userName);
            logger.setUserIP(req.getRemoteAddr());
            try {
                logger.setServerIP(InetAddress.getLocalHost().getHostAddress());
            } catch (UnknownHostException e) {
                LOG.error("无法获取服务器IP地址", e);
                LOG.error("Can't get server's ip address", e, Locale.ENGLISH);
            }
            logger.setAppName(SystemListener.getContextPath());
            String resource = req.getRequestURI().replace(logger.getAppName(), "");
            logger.setResource(resource);
            logger.setStartTime(new Date(start));
            logger.setEndTime(new Date(end));
            logger.setProcessTime(end - start);
            BufferLogCollector.collect(logger);
        }
    }

    @Override
    public void init(FilterConfig fc) throws ServletException {
        LOG.info("初始化性能过滤器");
        LOG.info("Initialize the performance filter", Locale.ENGLISH);
        enabled = PropertyHolder.getBooleanProperty("monitor.performance");
        if (enabled) {
            LOG.info("启用性能分析日志");
            LOG.info("Enable performance analyzing log", Locale.ENGLISH);
        } else {
            LOG.info("禁用性能分析日志");
            LOG.info("Disable performance analyzing log", Locale.ENGLISH);
        }
    }

    @Override
    public void destroy() {
        LOG.info("销毁性能过滤器");
        LOG.info("Destroy the performance filter", Locale.ENGLISH);
    }

    private boolean filter(HttpServletRequest req) {
        String path = req.getRequestURI();
        if (path.contains("/log/")) {
            LOG.info("路径包含/log/,不执行性能分析" + path);
            LOG.info("/log/ in path, not execute performance analysis", Locale.ENGLISH);
            return false;
        }
        if (path.contains("/monitor/")) {
            LOG.info("路径包含/monitor/,不执行性能分析" + path);
            LOG.info("/monitor/ in path, not execute performance analysis", Locale.ENGLISH);
            return false;
        }
        return true;
    }
}