/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.platform.log;

import org.apache.commons.lang.StringUtils;
import org.jrplat.module.system.service.PropertyHolder;
import org.jrplat.platform.log.handler.LogHandler;
import org.jrplat.platform.model.Model;
import org.jrplat.platform.util.SpringContextUtils;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 批量收集日志
 * 缓冲区满了之后调用LogHandler进行处理
 *
 * @author 西安捷然
 */
@Service
public class BufferLogCollector implements ApplicationListener {
    private static final JRPlatLogger LOG = JRPlatLoggerFactory.getJRPlatLogger(BufferLogCollector.class);

    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private static final HandleLogRunnable handleLogRunnable = new HandleLogRunnable();
    private static final List<LogHandler> logHandlers = new ArrayList<>();
    private static final ConcurrentLinkedQueue<Model> buffers = new ConcurrentLinkedQueue<>();
    private static final int logBufferMax = PropertyHolder.getIntProperty("log.buffer.max");
    private static final LogSaver LOG_SAVER = new LogSaver();
    private static final AtomicLong count = new AtomicLong();

    /**
     * 等待任务处理完毕
     * 然后关闭线程池
     */
    public static void close() {
        LOG.info("等待任务处理完毕");
        if (shoudHandle()) {
            LOG_SAVER.save();
        }
        LOG.info("关闭日志处理线程池");
        executorService.shutdown();
    }

    /**
     * 将日志加入缓冲区
     * @param <T>
     * @param t
     */
    public static <T extends Model> void collect(T t) {
        LOG.debug(count.incrementAndGet() + "、将日志加入缓冲区：\n" + t.toString());
        buffers.add(t);
        //判断缓冲区是否达到限制
        if (buffers.size() > logBufferMax) {
            LOG.info("缓冲区已达到限制数：" + logBufferMax + " ，处理日志");
            handleLog();
        }
    }

    /**
     * 提交日志之后立即返回，不会阻塞调用线程
     */
    public static void handleLog() {
        LOG.info("处理缓冲区中的日志");
        if (shoudHandle()) {
            executorService.submit(handleLogRunnable);
        }
    }

    private static boolean shoudHandle() {
        if (logHandlers.isEmpty()) {
            LOG.error("未找到任何LogHandler");
            return false;
        }
        int len = buffers.size();
        if (len == 0) {
            LOG.info("没有日志需要保存");
            LOG.info("No logs need to save：" + len, Locale.ENGLISH);
            return false;
        }
        LOG.info("需要处理缓冲区中的日志");
        return true;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            LOG.info("spring容器初始化完成,开始解析LogHandler");
            String handlerstr = PropertyHolder.getProperty("log.handlers");
            if (StringUtils.isBlank(handlerstr)) {
                LOG.info("未配置log.handlers");
                return;
            }
            LOG.info("handlerstr：" + handlerstr);
            String[] handlers = handlerstr.trim().split(";");
            for (String handler : handlers) {
                LogHandler logHandler = SpringContextUtils.getBean(handler.trim());
                if (logHandler != null) {
                    logHandlers.add(logHandler);
                    LOG.info("找到LogHandler：" + handler);
                } else {
                    LOG.info("未找到LogHandler：" + handler);
                }
            }
        }
    }

    /**
     * 处理日志线程，异步使用
     */
    private static class HandleLogRunnable implements Runnable {
        @Override
        public void run() {
            LOG_SAVER.save();
        }
    }

    /**
     * 处理日志类，同步使用
     */
    private static class LogSaver {
        public void save() {
            int len = buffers.size();
            LOG.info("开始处理缓冲区中的" + len + " 个日志对象");
            List<Model> list = new ArrayList<>(len);
            for (int i = 0; i < len; i++) {
                list.add(buffers.remove());
            }
            //把日志交给LogHandler处理
            for (LogHandler logHandler : logHandlers) {
                logHandler.handle(list);
            }
            //加速垃圾回收
            list.clear();
            LOG.info("缓冲区中的日志处理完毕");
        }
    }
}
