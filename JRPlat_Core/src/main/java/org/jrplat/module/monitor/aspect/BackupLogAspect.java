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
import org.jrplat.module.monitor.model.BackupLog;
import org.jrplat.module.monitor.model.BackupLogResult;
import org.jrplat.module.monitor.model.BackupLogType;
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
 * 备份恢复数据库日志Aspect
 * org.jrplat.module.system.service.backup.impl包下面有多个数据库的备份恢复实现
 * 他们实现了BackupService接口的backup方法（备份数据库）和restore（恢复数据库）方法
 * @author 西安捷然
 */
@Aspect
@Service
public class BackupLogAspect {
    private static final JRPlatLogger LOG = JRPlatLoggerFactory.getJRPlatLogger(BackupLogAspect.class);
    private static final boolean MONITOR_BACKUP = PropertyHolder.getBooleanProperty("monitor.backup");

    static {
        if (MONITOR_BACKUP) {
            LOG.info("启用备份恢复日志");
            LOG.info("Enable backup restore log", Locale.ENGLISH);
        } else {
            LOG.info("禁用备份恢复日志");
            LOG.info("Disable backup restore log", Locale.ENGLISH);
        }
    }

    private BackupLog backupLog = null;

    //拦截备份数据库操作    
    @Pointcut("execution( boolean org.jrplat.module.system.service.backup.impl.*.backup() )")
    public void backup() {
    }

    @Before("backup()")
    public void beforeBackup(JoinPoint jp) {
        if (MONITOR_BACKUP) {
            before(BackupLogType.BACKUP);
        }
    }

    @AfterReturning(value = "backup()", argNames = "result", returning = "result")
    public void afterBackup(JoinPoint jp, boolean result) {
        if (MONITOR_BACKUP) {
            after(result);
        }
    }

    //拦截恢复数据库操作    
    @Before(value = "execution( boolean org.jrplat.module.system.service.backup.impl.*.restore(java.lang.String) ) && args(date)",
            argNames = "date")
    public void beforeRestore(JoinPoint jp, String date) {
        if (MONITOR_BACKUP) {
            before(BackupLogType.RESTORE);
        }
    }

    @AfterReturning(pointcut = "execution( boolean org.jrplat.module.system.service.backup.impl.*.restore(java.lang.String) )",
            returning = "result")
    public void afterRestore(JoinPoint jp, boolean result) {
        if (MONITOR_BACKUP) {
            after(result);
        }
    }

    private void before(String type) {
        LOG.info("准备记录数据库" + type + "日志");
        User user = UserHolder.getCurrentLoginUser();
        String ip = UserHolder.getCurrentUserLoginIp();
        backupLog = new BackupLog();
        if (user != null) {
            backupLog.setUsername(user.getUsername());
        }
        backupLog.setLoginIP(ip);
        try {
            backupLog.setServerIP(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            LOG.error("无法获取服务器IP地址", e);
            LOG.error("Can't get server's ip address", e, Locale.ENGLISH);
        }
        backupLog.setAppName(SystemListener.getContextPath());
        backupLog.setStartTime(new Date());
        backupLog.setOperatingType(type);
    }

    private void after(boolean result) {
        if (result) {
            backupLog.setOperatingResult(BackupLogResult.SUCCESS);
        } else {
            backupLog.setOperatingResult(BackupLogResult.FAIL);
        }
        backupLog.setEndTime(new Date());
        backupLog.setProcessTime(backupLog.getEndTime().getTime() - backupLog.getStartTime().getTime());
        //将日志加入内存缓冲区
        BufferLogCollector.collect(backupLog);
        LOG.info("记录完毕");
    }
}