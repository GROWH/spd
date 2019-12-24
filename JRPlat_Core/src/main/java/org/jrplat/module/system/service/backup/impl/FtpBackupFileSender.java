/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.system.service.backup.impl;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jrplat.module.system.service.PropertyHolder;
import org.jrplat.module.system.service.backup.BackupFileSender;
import org.jrplat.platform.log.JRPlatLogger;
import org.jrplat.platform.log.JRPlatLoggerFactory;
import org.jrplat.platform.util.FtpUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;

/**
 * 将备份文件发送到FTP服务器上面
 * @author 西安捷然
 */
@Service
public class FtpBackupFileSender implements BackupFileSender {
    protected final JRPlatLogger LOG = JRPlatLoggerFactory.getJRPlatLogger(getClass());

    @Resource(name = "ftpUtils")
    private FtpUtils ftpUtils;

    @Resource(name = "configurationEncryptor")
    private StandardPBEStringEncryptor configurationEncryptor;

    @Override
    public void send(File file) {
        try {
            String host = PropertyHolder.getProperty("ftp.server.host");
            int port = PropertyHolder.getIntProperty("ftp.server.port");
            String username = PropertyHolder.getProperty("ftp.server.username");
            String password = PropertyHolder.getProperty("ftp.server.password");
            if (username != null && username.contains("ENC(") && username.contains(")")) {
                username = username.substring(4, username.length() - 1);
            }
            if (password != null && password.contains("ENC(") && password.contains(")")) {
                password = password.substring(4, password.length() - 1);
            }
            username = configurationEncryptor.decrypt(username);
            password = configurationEncryptor.decrypt(password);
            String dist = PropertyHolder.getProperty("log.backup.file.ftp.dir");
            String database = PropertyHolder.getProperty("jpa.database");
            dist = dist.replace("${database}", database);
            LOG.info("本地备份文件：" + file.getAbsolutePath());
            LOG.info("FTP服务器目标目录：" + dist);
            boolean connect = ftpUtils.connect(host, port, username, password);
            if (connect) {
                boolean result = ftpUtils.uploadTo(file, dist);
                if (result) {
                    LOG.info("备份文件上传到FTP服务器成功");
                } else {
                    LOG.error("备份文件上传到FTP服务器失败");
                }
            }
        } catch (Exception e) {
            LOG.error("备份文件上传到FTP服务器失败", e);
        }
    }
}
