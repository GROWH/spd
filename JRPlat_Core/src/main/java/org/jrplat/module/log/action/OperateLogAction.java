/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.log.action;

import org.apache.struts2.convention.annotation.Namespace;
import org.jrplat.module.log.model.OperateLog;
import org.jrplat.module.log.model.OperateStatistics;
import org.jrplat.module.log.service.OperateLogChartDataService;
import org.jrplat.module.log.service.OperateTyeCategoryService;
import org.jrplat.module.log.service.UserCategoryService;
import org.jrplat.platform.action.ExtJSSimpleAction;
import org.jrplat.platform.log.BufferLogCollector;
import org.jrplat.platform.model.ModelMetaData;
import org.jrplat.platform.service.ServiceFacade;
import org.jrplat.platform.util.Struts2Utils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Scope("prototype")
@Controller
@Namespace("/log")
public class OperateLogAction extends ExtJSSimpleAction<OperateLog> {
    @Resource(name = "userCategoryService")
    private UserCategoryService userCategoryService;
    @Resource(name = "operateTyeCategoryService")
    private OperateTyeCategoryService operateTyeCategoryService;
    private String category;
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
    protected void afterRender(Map map, OperateLog obj) {
        map.remove("updateTime");
        map.remove("createTime");
        map.remove("appName");
    }

    @Override
    protected String generateReportData(List<OperateLog> models) {
        List<OperateStatistics> data = OperateLogChartDataService.getData(models);
        if ("user".equals(category)) {
            return userCategoryService.getXML(data);
        } else {
            return operateTyeCategoryService.getXML(data);
        }
    }

    /**
     * 所有模型信息
     * @return
     */
    public String store() {
        List<Map<String, String>> data = new ArrayList<>();
        for (String key : ModelMetaData.getModelDes().keySet()) {
            Map<String, String> temp = new HashMap<>();
            temp.put("value", ModelMetaData.getModelDes().get(key));
            temp.put("text", ModelMetaData.getModelDes().get(key));
            data.add(temp);
        }
        Struts2Utils.renderJson(data);
        return null;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}