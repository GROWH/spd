package org.jrplat.module.ordermanagement.action;

import org.apache.struts2.convention.annotation.Namespace;
import org.jrplat.module.ordermanagement.model.OrderManagement;
import org.jrplat.module.ordermanagement.model.Outboundrecords;
import org.jrplat.module.ordermanagement.service.OrderInformationService;
import org.jrplat.module.ordermanagement.service.OrderManagementService;
import org.jrplat.module.ordermanagement.service.OutboundrecordsService;
import org.jrplat.platform.action.ExtJSSimpleAction;
import org.jrplat.platform.result.Page;
import org.jrplat.platform.util.Struts2Utils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author:wangkang
 * @Date: 2021/1/27 上午9:22
 */
@Scope("prototype")
@Controller
@Namespace("/ordermanagement")
public class OutboundrecordsAction extends ExtJSSimpleAction<Outboundrecords> {


    private String propertyCriteria;


    @Resource(name = "outboundrecordsService")
    private OutboundrecordsService outboundrecordsService;

    //模糊查询
    private String likeQueryValue;

    /**
     * 查询时过滤空记录
     *
     * @return
     */
    @Override
    public String query() {

        int start = super.getStart();
        int len = super.getLimit();
        if (start == -1) {
            start = 0;
        }
        if (len == -1) {
            len = 10;
        }
        try {

            List<Outboundrecords> outboundrecordsList = outboundrecordsService.queryOutboundrecords(likeQueryValue);
            if (len > outboundrecordsList.size()) {
                len = outboundrecordsList.size();
            }
            List<Outboundrecords> models = new ArrayList<>();
            for (int i = start; i < start + getLimit(); i++) {
                if (i >= outboundrecordsList.size()) {
                    break;
                }
                models.add(outboundrecordsList.get(i));
            }
            // 构造当前页面对象
            page = new Page<>();
            page.setModels(models);
            page.setTotalRecords(outboundrecordsList.size());
            Map json = new HashMap();
            json.put("totalProperty", page.getTotalRecords());
            List<Map> result = new ArrayList<>();
            renderJsonForQuery(result);
            json.put("root", result);
            Struts2Utils.renderJson(json);

        } catch (Exception e) {
            map = new HashMap();
            map.put("success", false);
            map.put("message", "查询失败:" + e.getMessage());
            Struts2Utils.renderJson(map);
            return null;
        }

        return null;
    }


    @Override
    public String create(){
        try {
            Outboundrecords outboundrecords = outboundrecordsService.updateOrderInformationInfo(model);
            model.setUnitNo(outboundrecords.getUnitNo());
        }catch (Exception e){
            map = new HashMap();
            map.put("success", false);
            map.put("message", "新增失败:" + e.getMessage());
            Struts2Utils.renderJson(map);
            return null;
        }
        super.create();
        return null;
    }


    @Override
    public String updatePart(){
        try {
            Outboundrecords outboundrecords = outboundrecordsService.updateOrderInformationInfo(model);
            model.setUnitNo(outboundrecords.getUnitNo());
        }catch (Exception e){
            map = new HashMap();
            map.put("success", false);
            map.put("message", "修改失败:" + e.getMessage());
            Struts2Utils.renderJson(map);
            return null;
        }
        super.updatePart()  ;
        return null;
    }



    public String getLikeQueryValue() {
        return likeQueryValue;
    }

    public void setLikeQueryValue(String likeQueryValue) {
        this.likeQueryValue = likeQueryValue;
    }


    public void setPropertyCriteria(String propertyCriteria) {
        this.propertyCriteria = propertyCriteria;
    }

    public String getPropertyCriteria() {
        return propertyCriteria;
    }
}
