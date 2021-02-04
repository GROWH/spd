package org.jrplat.module.ordermanagement.action;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Namespace;
import org.jrplat.module.ordermanagement.model.OrderInformation;
import org.jrplat.module.ordermanagement.model.OrderInformationDto;
import org.jrplat.module.ordermanagement.model.OrderManagement;
import org.jrplat.module.ordermanagement.service.OrderInformationService;
import org.jrplat.module.ordermanagement.service.OrderManagementService;
import org.jrplat.module.security.model.User;
import org.jrplat.module.security.service.UserHolder;
import org.jrplat.module.unitInfo.model.Unit;
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
 * @Date: 2021/1/21 下午3:20
 */
@Scope("prototype")
@Controller
@Namespace("/ordermanagement")
public class OrderManagementAction extends ExtJSSimpleAction<OrderManagement> {

    private String propertyCriteria;

    @Resource(name = "orderManagementService")
    private OrderManagementService orderManagementService;

    @Resource(name = "orderInformationService")
    private OrderInformationService orderInformationService;

    //模糊查询
    private String likeQueryValue;


    /**
     * 查询时过滤空记录
     *
     * @return
     */
    @Override
    public String query() {

        //判断总单查询还是细单查询
        if(propertyCriteria !=null && !propertyCriteria.equals("")){

            String orderManagementId = propertyCriteria.substring(propertyCriteria.lastIndexOf(":")+1);

            List<OrderInformationDto> orderInformationList = orderInformationService.queryOrderInformation(Integer.valueOf(orderManagementId));
            Map<String,Object> map = new HashMap();
            map.put("detailList",orderInformationList);
            Struts2Utils.renderJson(map);
        }else{
            int start = super.getStart();
            int len = super.getLimit();
            if (start == -1) {
                start = 0;
            }
            if (len == -1) {
                len = 10;
            }
            try {

                List<OrderManagement> orderManagementList = orderManagementService.queryOrderManagement(likeQueryValue);
                if (len > orderManagementList.size()) {
                    len = orderManagementList.size();
                }
                List<OrderManagement> models = new ArrayList<>();
                for (int i = start; i < start + getLimit(); i++) {
                    if (i >= orderManagementList.size()) {
                        break;
                    }
                    models.add(orderManagementList.get(i));
                }
                // 构造当前页面对象
                page = new Page<>();
                page.setModels(models);
                page.setTotalRecords(orderManagementList.size());
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

        }



        return null;
    }

    /**
     * 根据总单id查询细单单
     *
     * @return
     */
    public String queryOrderInformation() {


        try {
            if(propertyCriteria !=null && !propertyCriteria.equals("")){

            String orderManagementId = propertyCriteria.substring(propertyCriteria.lastIndexOf(":")+1);

            List<OrderInformationDto> orderInformationList = orderInformationService.queryOrderInformation(Integer.valueOf(orderManagementId));
            Map<String,Object> map = new HashMap();
            map.put("detailList",orderInformationList);
            Struts2Utils.renderJson(map);
            }
        } catch (Exception e) {
            map = new HashMap();
            map.put("success", false);
            map.put("message", "查询失败:" + e.getMessage());
            Struts2Utils.renderJson(map);
            return null;
        }

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
