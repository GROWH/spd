/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.monitor.service;

import org.jrplat.module.monitor.model.RuningTime;
import org.jrplat.platform.action.converter.DateTypeConverter;
import org.jrplat.platform.log.JRPlatLogger;
import org.jrplat.platform.log.JRPlatLoggerFactory;
import org.jrplat.platform.util.ConvertUtils;

import java.util.*;

/**
 *
 * @author 西安捷然
 */
public class RuningTimeChartDataService {
    private static final JRPlatLogger LOG = JRPlatLoggerFactory.getJRPlatLogger(RuningTimeChartDataService.class);

    public static LinkedHashMap<String, Long> getRuningSequence(List<RuningTime> models) {
        models = getValidData(models);
        LinkedHashMap<String, Long> data = new LinkedHashMap<>();
        if (models.size() < 1) {
            return data;
        }
        Collections.sort(models, new Comparator() {

            @Override
            public int compare(Object o1, Object o2) {
                RuningTime p1 = (RuningTime) o1;
                RuningTime p2 = (RuningTime) o2;
                return (int) (p1.getStartupTime().getTime() - p2.getStartupTime().getTime());
            }

        });
        for (int i = 0; i < models.size(); ) {
            RuningTime item = models.get(i);
            String key = DateTypeConverter.toDefaultDateTime(item.getStartupTime()) + ", 运行" + item.getRuningTimeStr();
            data.put(key, item.getRuningTime());
            i++;
            if (i < models.size()) {
                RuningTime item2 = models.get(i);
                long stop = item2.getStartupTime().getTime() - item.getShutdownTime().getTime();
                key = DateTypeConverter.toDefaultDateTime(item.getShutdownTime()) + ", 停机" + ConvertUtils.getTimeDes(stop);
                data.put(key, -stop);
            }
        }

        return data;
    }

    public static LinkedHashMap<String, Long> getRuningRateData(List<RuningTime> models) {
        models = getValidData(models);
        LinkedHashMap<String, Long> data = new LinkedHashMap<>();
        if (models.size() < 1) {
            return data;
        }
        Collections.sort(models, new Comparator() {

            @Override
            public int compare(Object o1, Object o2) {
                RuningTime p1 = (RuningTime) o1;
                RuningTime p2 = (RuningTime) o2;
                return (int) (p1.getStartupTime().getTime() - p2.getStartupTime().getTime());
            }

        });
        RuningTime first = models.get(0);
        RuningTime latest = models.get(models.size() - 1);
        LOG.debug("系统首次启动时间：" + DateTypeConverter.toDefaultDateTime(first.getStartupTime()));
        LOG.debug("系统最后关闭时间：" + DateTypeConverter.toDefaultDateTime(latest.getShutdownTime()));
        long totalTime = latest.getShutdownTime().getTime() - first.getStartupTime().getTime();
        LOG.debug("系统总时间：" + latest.getShutdownTime().getTime() + "-" + first.getStartupTime().getTime() + "=" + totalTime);
        long runingTime = 0;
        for (RuningTime item : models) {
            LOG.debug("      增加系统运行时间：" + item.getRuningTime());
            runingTime += item.getRuningTime();
        }
        LOG.debug("系统运行时间：" + runingTime);
        long stopTime = totalTime - runingTime;
        LOG.debug("系统停机时间：" + stopTime);
        data.put("运行时间", runingTime);
        data.put("停机时间", -stopTime);

        return data;
    }

    public static List<RuningTime> getValidData(List<RuningTime> runingTimes) {
        List<RuningTime> models = new ArrayList<>();
        for (RuningTime runingTime : runingTimes) {
            //如果系统启动时间或是关闭时间有一项为空，则忽略
            if (runingTime.getStartupTime() != null && runingTime.getShutdownTime() != null) {
                models.add(runingTime);
            }
        }
        return models;
    }
}