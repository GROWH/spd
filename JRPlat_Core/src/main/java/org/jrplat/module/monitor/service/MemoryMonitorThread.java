/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.monitor.service;

import org.jrplat.module.monitor.model.MemoryState;
import org.jrplat.module.system.service.SystemListener;
import org.jrplat.platform.log.BufferLogCollector;
import org.jrplat.platform.log.JRPlatLogger;
import org.jrplat.platform.log.JRPlatLoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author 西安捷然
 */
public class MemoryMonitorThread extends Thread {
    private static final JRPlatLogger LOG = JRPlatLoggerFactory.getJRPlatLogger(MemoryMonitorThread.class);
    public volatile boolean running = true;
    private int circle = 10;

    public MemoryMonitorThread(int circle) {
        this.setDaemon(true);
        this.setName("内存监视线程(Memory monitor thread)");
        LOG.info("内存监视间隔为 " + circle + " 分钟");
        LOG.info("Memory monitor interval " + circle + " mins", Locale.ENGLISH);
        this.circle = circle;
    }

    @Override
    public void run() {
        LOG.info("内存监视线程启动");
        LOG.info("Launch memory monitor thread", Locale.ENGLISH);
        while (running) {
            log();
            try {
                Thread.sleep(circle * 60 * 1000);
            } catch (InterruptedException ex) {
                if (!running) {
                    LOG.info("内存监视线程退出");
                    LOG.info("Memory monitor thread abort", Locale.ENGLISH);
                } else {
                    LOG.error("内存监视线程出错", ex);
                    LOG.error("Error happens in memory monitor thread", ex, Locale.ENGLISH);
                }
            }
        }
    }

    private void log() {
        float max = (float) Runtime.getRuntime().maxMemory() / 1000000;
        float total = (float) Runtime.getRuntime().totalMemory() / 1000000;
        float free = (float) Runtime.getRuntime().freeMemory() / 1000000;

        MemoryState logger = new MemoryState();
        try {
            logger.setServerIP(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException ex) {
            LOG.error("获取服务器地址出错", ex);
            LOG.error("Can't get server's internet address", ex, Locale.ENGLISH);
        }
        logger.setAppName(SystemListener.getContextPath());
        logger.setRecordTime(new Date());
        logger.setMaxMemory(max);
        logger.setTotalMemory(total);
        logger.setFreeMemory(free);
        logger.setUsableMemory(logger.getMaxMemory() - logger.getTotalMemory() + logger.getFreeMemory());
        BufferLogCollector.collect(logger);
    }
}