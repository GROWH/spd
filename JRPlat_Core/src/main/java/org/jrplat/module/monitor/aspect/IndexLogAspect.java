/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.monitor.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.jrplat.module.monitor.model.IndexLog;
import org.jrplat.module.monitor.model.IndexLogResult;
import org.jrplat.module.security.model.User;
import org.jrplat.module.security.service.UserHolder;
import org.jrplat.module.system.service.PropertyHolder;
import org.jrplat.module.system.service.SystemListener;
import org.jrplat.platform.log.BufferLogCollector;
import org.jrplat.platform.log.JRPlatLogger;
import org.jrplat.platform.log.JRPlatLoggerFactory;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Locale;

/**
 * 重建索引日志Aspect
 * org.jrplat.platform.search.IndexManager类的
 * rebuidIndex方法负责重建索引
 * @author 西安捷然
 */
@Aspect
@Service
public class IndexLogAspect {
    private static final JRPlatLogger LOG = JRPlatLoggerFactory.getJRPlatLogger(IndexLogAspect.class);
    private static final boolean MONITOR_INDEX = PropertyHolder.getBooleanProperty("monitor.index");

    static {
        if (MONITOR_INDEX) {
            LOG.info("启用重建索引日志");
            LOG.info("Enable rebuild index log", Locale.ENGLISH);
        } else {
            LOG.info("禁用重建索引日志");
            LOG.info("Disable rebuild index log", Locale.ENGLISH);
        }
    }

    private IndexLog indexLog = null;

    //拦截重建索引操作    
    @Pointcut("execution( boolean org.jrplat.platform.search.IndexRebuilder.build() )")
    public void rebuidIndex() {
    }

    @Before("rebuidIndex()")
    public void beforeRebuidIndex(JoinPoint jp) {
        if (MONITOR_INDEX) {
            before();
        }
    }

    @AfterReturning(value = "rebuidIndex()", argNames = "result", returning = "result")
    public void afterRebuidIndex(JoinPoint jp, boolean result) {
        if (MONITOR_INDEX) {
            after(result);
        }
    }

    private void before() {
        LOG.info("准备记录重建索引日志");
        User user = UserHolder.getCurrentLoginUser();
        String ip = UserHolder.getCurrentUserLoginIp();
        indexLog = new IndexLog();
        if (user != null) {
            indexLog.setUsername(user.getUsername());
        }
        indexLog.setLoginIP(ip);
        try {
            indexLog.setServerIP(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            LOG.error("无法获取服务器IP地址", e);
            LOG.error("Can't get server's ip address", e, Locale.ENGLISH);
        }
        indexLog.setAppName(SystemListener.getContextPath());
        indexLog.setStartTime(new Date());
    }

    private void after(boolean result) {
        if (result) {
            indexLog.setOperatingResult(IndexLogResult.SUCCESS);
        } else {
            indexLog.setOperatingResult(IndexLogResult.FAIL);
        }
        indexLog.setEndTime(new Date());
        indexLog.setProcessTime(indexLog.getEndTime().getTime() - indexLog.getStartTime().getTime());
        //将日志加入内存缓冲区
        BufferLogCollector.collect(indexLog);
        LOG.info("记录完毕");
    }
}