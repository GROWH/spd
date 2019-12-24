/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.monitor.action;

import org.apache.struts2.convention.annotation.Namespace;
import org.jrplat.module.monitor.model.MemoryState;
import org.jrplat.module.monitor.service.MemoryStateCategoryService;
import org.jrplat.module.monitor.service.MemoryStateChartDataService;
import org.jrplat.platform.action.ExtJSSimpleAction;
import org.jrplat.platform.log.BufferLogCollector;
import org.jrplat.platform.service.ServiceFacade;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Scope("prototype")
@Controller
@Namespace("/monitor")
public class MemoryStateAction extends ExtJSSimpleAction<MemoryState> {
    private String category;
    @Resource(name = "memoryStateCategoryService")
    private MemoryStateCategoryService memoryStateCategoryService;
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
    protected void afterRender(Map map, MemoryState obj) {
        map.put("usingMemory", obj.getTotalMemory() - obj.getFreeMemory());
        map.remove("updateTime");
        map.remove("createTime");
        map.remove("appName");
    }

    @Override
    protected String generateReportData(List<MemoryState> models) {
        if ("sequence".equals(category)) {
            //不改变数据，就用models
        }
        if ("sequenceHH".equals(category)) {
            models = MemoryStateChartDataService.getSequenceDataHH(models);
        }
        if ("sequenceDD".equals(category)) {
            models = MemoryStateChartDataService.getSequenceDataDD(models);
        }
        if ("sequenceMonth".equals(category)) {
            models = MemoryStateChartDataService.getSequenceDataMonth(models);
        }
        return memoryStateCategoryService.getXML(models);
    }

    public void setCategory(String category) {
        this.category = category;
    }
}