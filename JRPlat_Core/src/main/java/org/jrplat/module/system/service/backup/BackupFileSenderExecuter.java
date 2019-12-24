/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.system.service.backup;

import org.apache.commons.lang.StringUtils;
import org.jrplat.module.system.service.PropertyHolder;
import org.jrplat.platform.log.JRPlatLogger;
import org.jrplat.platform.log.JRPlatLoggerFactory;
import org.jrplat.platform.util.SpringContextUtils;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 *执行备份文件的发送服务，根据配置文件来判断使用哪些发送器，并按配置的前后顺序依次调用
 * @author 西安捷然
 */
@Service
public class BackupFileSenderExecuter implements BackupFileSender, ApplicationListener {
    private static final List<BackupFileSender> backupFileSenders = new LinkedList<>();
    protected final JRPlatLogger LOG = JRPlatLoggerFactory.getJRPlatLogger(getClass());

    @Override
    public void send(File file) {
        for (BackupFileSender sender : backupFileSenders) {
            sender.send(file);
        }
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            LOG.info("spring容器初始化完成,开始解析BackupFileSender");
            String senderstr = PropertyHolder.getProperty("log.backup.file.sender");
            if (StringUtils.isBlank(senderstr)) {
                LOG.info("未配置log.backup.file.sender");
                return;
            }
            LOG.info("log.backup.file.sender：" + senderstr);
            String[] senders = senderstr.trim().split(";");
            for (String sender : senders) {
                BackupFileSender backupFileSender = SpringContextUtils.getBean(sender.trim());
                if (backupFileSender != null) {
                    backupFileSenders.add(backupFileSender);
                    LOG.info("找到BackupFileSender：" + sender);
                } else {
                    LOG.info("未找到BackupFileSender：" + sender);
                }
            }
        }
    }
}