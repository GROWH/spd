/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.system.service.backup;

import java.io.File;

/**
 * 备份文件发送器
 * 将最新的备份文件发送到其他机器，防止服务器故障丢失数据
 * @author 西安捷然
 */
public interface BackupFileSender {
    public void send(File file);
}
