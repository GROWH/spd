/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.system.service.backup.impl;

import org.apache.commons.io.FileUtils;
import org.jrplat.module.system.service.PropertyHolder;
import org.jrplat.module.system.service.backup.BackupFileSender;
import org.jrplat.platform.log.JRPlatLogger;
import org.jrplat.platform.log.JRPlatLoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

/**
 * 将备份文件从本地一个目录复制到另一个目录
 * @author 西安捷然
 */
@Service
public class LocalBackupFileSender implements BackupFileSender {
    protected final JRPlatLogger LOG = JRPlatLoggerFactory.getJRPlatLogger(getClass());

    @Override
    public void send(File file) {
        try {
            String dist = PropertyHolder.getProperty("log.backup.file.local.dir");
            LOG.info("备份文件：" + file.getAbsolutePath());
            LOG.info("目标目录：" + dist);
            FileUtils.copyFile(file, new File(dist, file.getName()));
        } catch (IOException ex) {
            LOG.info("LocalBackupFileSender失败", ex);
        }
    }
}
