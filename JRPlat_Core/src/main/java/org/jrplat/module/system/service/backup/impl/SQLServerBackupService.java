/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.system.service.backup.impl;

import org.apache.commons.dbcp.BasicDataSource;
import org.jrplat.module.system.service.Lock;
import org.jrplat.module.system.service.PropertyHolder;
import org.jrplat.module.system.service.backup.AbstractBackupService;
import org.jrplat.platform.action.converter.DateTypeConverter;
import org.jrplat.platform.search.IndexManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

/**
 * SQLServer备份恢复实现
 * @author 西安捷然
 */
@Service("SQL_SERVER")
public class SQLServerBackupService extends AbstractBackupService {
    @Resource(name = "dataSource")
    private BasicDataSource dataSource;
    @Resource(name = "indexManager")
    private IndexManager indexManager;

    /**
     * SQLServer备份数据库实现
     * @return
     */
    @Override
    public boolean backup() {
        Connection con = null;
        PreparedStatement bps = null;
        try {
            con = dataSource.getConnection();
            String path = getBackupFilePath() + DateTypeConverter.toFileName(new Date()) + ".bak";
            String bakSQL = PropertyHolder.getProperty("db.backup.sql");
            bps = con.prepareStatement(bakSQL);
            bps.setString(1, path);
            if (!bps.execute()) {
                return true;
            }
            return false;
        } catch (Exception e) {
            LOG.error("备份出错", e);
            return false;
        } finally {
            if (bps != null) {
                try {
                    bps.close();
                } catch (SQLException e) {
                    LOG.error("备份出错", e);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    LOG.error("备份出错", e);
                }
            }
        }
    }

    /**
     * SQLServer恢复数据库实现
     * @return
     */
    @Override
    public boolean restore(String date) {
        Lock.setRestore(true);
        Connection con = null;
        PreparedStatement rps = null;
        try {
            con = DriverManager.getConnection(PropertyHolder.getProperty("db.restore.url"), username, password);
            String path = getBackupFilePath() + date + ".bak";
            String restoreSQL = PropertyHolder.getProperty("db.restore.sql");
            rps = con.prepareStatement(restoreSQL);
            rps.setString(1, path);
            dataSource.close();

            if (!rps.execute()) {
                indexManager.rebuidAll();
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            LOG.error("恢复出错", e);
            return false;
        } finally {
            Lock.setRestore(false);
            if (rps != null) {
                try {
                    rps.close();
                } catch (SQLException e) {
                    LOG.error("恢复出错", e);
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    LOG.error("恢复出错", e);
                }
            }
        }
    }
}