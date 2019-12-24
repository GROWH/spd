/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.system.service.backup.impl;

import org.jrplat.module.system.service.PropertyHolder;
import org.jrplat.module.system.service.backup.AbstractBackupService;
import org.jrplat.platform.action.converter.DateTypeConverter;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Date;

/**
 *MySQL备份恢复实现
 * @author 西安捷然
 */
@Service("MYSQL")
public class MySQLBackupService extends AbstractBackupService {

    /**
     * MySQL备份数据库实现
     * @return
     */
    @Override
    public boolean backup() {
        try {
            String path = getBackupFilePath() + DateTypeConverter.toFileName(new Date()) + ".bak";
            String command = PropertyHolder.getProperty("db.backup.command");
            command = command.replace("${db.username}", username);
            command = command.replace("${db.password}", password);
            command = command.replace("${module.short.name}", PropertyHolder.getProperty("module.short.name"));

            Runtime runtime = Runtime.getRuntime();
            Process child = runtime.exec(command);
            InputStream in = child.getInputStream();

            try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(path), "utf8"); BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf8"))) {
                String line = reader.readLine();
                while (line != null) {
                    writer.write(line + "\n");
                    line = reader.readLine();
                }
                writer.flush();
            }
            LOG.debug("备份到：" + path);
            return true;
        } catch (Exception e) {
            LOG.error("备份出错", e);
        }
        return false;
    }

    /**
     * MySQL恢复数据库实现
     * @param date
     * @return
     */
    @Override
    public boolean restore(String date) {
        try {
            String path = getBackupFilePath() + date + ".bak";
            String command = PropertyHolder.getProperty("db.restore.command");
            command = command.replace("${db.username}", username);
            command = command.replace("${db.password}", password);
            command = command.replace("${module.short.name}", PropertyHolder.getProperty("module.short.name"));

            Runtime runtime = Runtime.getRuntime();
            Process child = runtime.exec(command);
            try (OutputStreamWriter writer = new OutputStreamWriter(child.getOutputStream(), "utf8"); BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "utf8"))) {
                String line = reader.readLine();
                while (line != null) {
                    writer.write(line + "\n");
                    line = reader.readLine();
                }
                writer.flush();
            }
            LOG.debug("从 " + path + " 恢复");
            return true;
        } catch (Exception e) {
            LOG.error("恢复出错", e);
        }
        return false;
    }
}