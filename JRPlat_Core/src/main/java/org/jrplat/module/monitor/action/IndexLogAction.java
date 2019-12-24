/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.monitor.action;

import org.apache.struts2.convention.annotation.Namespace;
import org.jrplat.module.monitor.model.IndexLog;
import org.jrplat.module.monitor.service.IndexLogChartDataService;
import org.jrplat.module.monitor.service.IndexLogSingleService;
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
public class IndexLogAction extends ExtJSSimpleAction<IndexLog> {
    private String category;
    @Resource(name = "indexLogSingleService")
    private IndexLogSingleService indexLogSingleService;
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
    protected void afterRender(Map map, IndexLog obj) {
        map.put("processTime", obj.getProcessTimeStr());
        map.remove("updateTime");
        map.remove("createTime");
        map.remove("appName");
    }

    @Override
    protected String generateReportData(List<IndexLog> models) {
        LinkedHashMap<String, Long> data = new LinkedHashMap<>();
        if ("rate".equals(category)) {
            data = IndexLogChartDataService.getRateData(models);
        }
        if ("sequence".equals(category)) {
            data = IndexLogChartDataService.getSequenceData(models);
        }

        return indexLogSingleService.getXML(data);
    }

    public void setCategory(String category) {
        this.category = category;
    }
}