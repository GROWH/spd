/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.monitor.service;

import org.jrplat.module.monitor.model.IndexLog;
import org.jrplat.module.monitor.model.IndexLogResult;
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
public class IndexLogChartDataService {
    private static final JRPlatLogger LOG = JRPlatLoggerFactory.getJRPlatLogger(IndexLogChartDataService.class);

    public static LinkedHashMap<String, Long> getSequenceData(List<IndexLog> models) {
        Collections.sort(models, new Comparator() {

            @Override
            public int compare(Object o1, Object o2) {
                IndexLog p1 = (IndexLog) o1;
                IndexLog p2 = (IndexLog) o2;
                return (int) (p1.getStartTime().getTime() - p2.getStartTime().getTime());
            }

        });
        LinkedHashMap<String, Long> data = new LinkedHashMap<>();
        if (models.size() < 1) {
            return data;
        }
        for (IndexLog item : models) {
            String key = DateTypeConverter.toDefaultDateTime(item.getStartTime());
            data.put(key, item.getProcessTime());
        }
        return data;
    }

    public static LinkedHashMap<String, Long> getRateData(List<IndexLog> models) {
        LinkedHashMap<String, Long> data = new LinkedHashMap<>();
        if (models.size() < 1) {
            return data;
        }
        long success = 0;
        long fail = 0;
        for (IndexLog item : models) {
            if (IndexLogResult.SUCCESS.equals(item.getOperatingResult())) {
                success++;
            }
            if (IndexLogResult.FAIL.equals(item.getOperatingResult())) {
                fail++;
            }
        }
        data.put("重建索引成功", success);
        data.put("重建索引失败", fail);
        return data;
    }
}