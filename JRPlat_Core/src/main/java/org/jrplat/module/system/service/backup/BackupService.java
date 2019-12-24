/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.system.service.backup;

import java.io.File;
import java.util.List;

/**
 * 备份恢复数据库接口
 * @author 西安捷然
 */
public interface BackupService {
    /**
     * 备份数据库
     * @return 是否备份成功
     */
    public boolean backup();

    /**
     * 恢复数据库
     * @param date
     * @return 是否恢复成功
     */
    public boolean restore(String date);

    /**
     * 获取已经存在的备份文件名称列表
     * @return 备份文件名称列表
     */
    public List<String> getExistBackupFileNames();

    /**
     * 获取备份文件存放的本地文件系统路径
     * @return 备份文件存放路径
     */
    public String getBackupFilePath();

    /**
     * 获取最新的备份文件
     * @return 最新的备份文件
     */
    public File getNewestBackupFile();
}
