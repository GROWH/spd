/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.monitor.service;

import org.jrplat.module.monitor.model.BackupLog;
import org.jrplat.module.monitor.model.BackupLogResult;
import org.jrplat.platform.action.converter.DateTypeConverter;
import org.jrplat.platform.log.JRPlatLogger;
import org.jrplat.platform.log.JRPlatLoggerFactory;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author 西安捷然
 */
public class BackupLogChartDataService {
    private static final JRPlatLogger LOG = JRPlatLoggerFactory.getJRPlatLogger(BackupLogChartDataService.class);

    public static LinkedHashMap<String, Long> getSequenceData(List<BackupLog> models) {
        Collections.sort(models, new Comparator() {

            @Override
            public int compare(Object o1, Object o2) {
                BackupLog p1 = (BackupLog) o1;
                BackupLog p2 = (BackupLog) o2;
                return (int) (p1.getStartTime().getTime() - p2.getStartTime().getTime());
            }

        });
        LinkedHashMap<String, Long> data = new LinkedHashMap<>();
        if (models.size() < 1) {
            return data;
        }
        for (BackupLog item : models) {
            String key = DateTypeConverter.toDefaultDateTime(item.getStartTime());
            data.put(key, item.getProcessTime());
        }
        return data;
    }

    public static LinkedHashMap<String, Long> getRateData(List<BackupLog> models) {
        LinkedHashMap<String, Long> data = new LinkedHashMap<>();
        if (models.size() < 1) {
            return data;
        }
        long success = 0;
        long fail = 0;
        for (BackupLog item : models) {
            if (BackupLogResult.SUCCESS.equals(item.getOperatingResult())) {
                success++;
            }
            if (BackupLogResult.FAIL.equals(item.getOperatingResult())) {
                fail++;
            }
        }
        data.put("备份成功", success);
        data.put("备份失败", fail);
        return data;
    }
}