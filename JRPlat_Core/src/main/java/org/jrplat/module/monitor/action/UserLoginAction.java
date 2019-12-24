/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.monitor.action;

import org.apache.struts2.convention.annotation.Namespace;
import org.jrplat.module.monitor.model.UserLogin;
import org.jrplat.module.monitor.service.UserLoginChartDataService;
import org.jrplat.module.monitor.service.UserLoginSingleService;
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
public class UserLoginAction extends ExtJSSimpleAction<UserLogin> {
    private String category;
    @Resource(name = "userLoginSingleService")
    private UserLoginSingleService userLoginSingleService;
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
    protected void afterRender(Map map, UserLogin obj) {
        map.put("onlineTime", obj.getOnlineTimeStr());
        map.remove("userAgent");
        map.remove("updateTime");
        map.remove("createTime");
        map.remove("appName");
    }

    @Override
    protected String generateReportData(List<UserLogin> models) {
        LinkedHashMap<String, Long> data = new LinkedHashMap<>();
        switch (category) {
            case "loginTimes":
                data = UserLoginChartDataService.getUserLoginTimes(models);
                break;
            case "onlineTime":
                data = UserLoginChartDataService.getUserOnlineTime(models);
                break;
        }

        return userLoginSingleService.getXML(data);
    }

    public void setCategory(String category) {
        this.category = category;
    }
}