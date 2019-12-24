/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.monitor.action;

import org.apache.struts2.convention.annotation.Namespace;
import org.jrplat.module.monitor.model.RuningTime;
import org.jrplat.module.monitor.service.RuningTimeChartDataService;
import org.jrplat.module.monitor.service.RuningTimeSingleService;
import org.jrplat.platform.action.ExtJSSimpleAction;
import org.jrplat.platform.log.BufferLogCollector;
import org.jrplat.platform.service.ServiceFacade;
import org.jrplat.platform.util.Struts2Utils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Scope("prototype")
@Controller
@Namespace("/monitor")
public class RuningTimeAction extends ExtJSSimpleAction<RuningTime> {
    private String category;
    @Resource(name = "runingTimeSingleService")
    private RuningTimeSingleService runingTimeSingleService;
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
    protected void afterRender(Map map, RuningTime obj) {
        map.put("runingTime", obj.getRuningTimeStr());
        map.remove("osName");
        map.remove("osVersion");
        map.remove("osArch");
        map.remove("jvmVersion");
        map.remove("jvmName");
        map.remove("jvmVendor");
        map.remove("updateTime");
        map.remove("createTime");
        map.remove("appName");
    }

    @Override
    protected String generateReportData(List<RuningTime> models) {
        LinkedHashMap<String, Long> data = new LinkedHashMap<>();
        if ("runingRate".equals(category)) {
            data = RuningTimeChartDataService.getRuningRateData(models);
        }
        if ("runingSequence".equals(category)) {
            data = RuningTimeChartDataService.getRuningSequence(models);
        }

        return runingTimeSingleService.getXML(data);
    }

    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * 重启系统
     */
    public void reboot() {
        try {
            Runtime.getRuntime().exec("shutdown -r -f -t 10");
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("重启系统失败：" + e.getMessage());
            Struts2Utils.renderText("重启失败");
            return;
        }
        Struts2Utils.renderText("重启成功");
    }
}