/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.monitor.action;

import org.apache.struts2.convention.annotation.Namespace;
import org.jrplat.module.monitor.model.BackupLog;
import org.jrplat.module.monitor.service.BackupLogChartDataService;
import org.jrplat.module.monitor.service.BackupLogSingleService;
import org.jrplat.platform.action.ExtJSSimpleAction;
import org.jrplat.platform.log.BufferLogCollector;
import org.jrplat.platform.service.ServiceFacade;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Scope("prototype")
@Controller
@Namespace("/monitor")
public class BackupLogAction extends ExtJSSimpleAction<BackupLog> {
    private String category;
    @Resource(name = "backupLogSingleService")
    private BackupLogSingleService backupLogSingleService;
    //使用日志数据库
    @Resource(name = "serviceFacadeForLog")
    private ServiceFacade service;

    @Override
    public ServiceFacade getService() {
        return service;
    }

    @Override
    public String query() {
        BufferLogCollector.handleLog();
        return super.query();
    }

    @Override
    protected void afterRender(Map map, BackupLog obj) {
        map.put("processTime", obj.getProcessTimeStr());
        map.remove("updateTime");
        map.remove("createTime");
        map.remove("appName");
    }

    @Override
    protected String generateReportData(List<BackupLog> models) {
        LinkedHashMap<String, Long> data = new LinkedHashMap<>();
        if ("rate".equals(category)) {
            data = BackupLogChartDataService.getRateData(models);
        }
        if ("sequence".equals(category)) {
            data = BackupLogChartDataService.getSequenceData(models);
        }

        return backupLogSingleService.getXML(data);
    }

    public void setCategory(String category) {
        this.category = category;
    }
}