package org.jrplat.module.commodityInfo.action;

import org.apache.struts2.convention.annotation.Namespace;
import org.jrplat.module.commodityInfo.model.Commodity;
import org.jrplat.module.commodityInfo.service.CommodityService;

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
 * @Date: 2021/1/21 下午3:08
 */

@Scope("prototype")
@Controller
@Namespace("/commodityInfo")
public class CommodityAction extends ExtJSSimpleAction<Commodity> {
    @Resource
    private CommodityService commodityService;
    //模糊查询
    private String likeQueryValue;
    @Override
    public String create() {
        try {
            commodityService.createCommodity(model);
        } catch (Exception e) {
            map = new HashMap();
            map.put("success", false);
            map.put("message", "创建失败:" + e.getMessage());
            Struts2Utils.renderJson(map);
            return null;
        }
        map = new HashMap();
        map.put("success", true);
        map.put("message", "创建成功");
        Struts2Utils.renderJson(map);
        return null;
    }

    @Override
    public String updatePart(){
        try {
            commodityService.updatePart(model, getRequestParameterNames(),null);
        }catch (Exception e){
            map = new HashMap();
            map.put("success", false);
            map.put("message", "修改失败:" + e.getMessage());
            Struts2Utils.renderJson(map);
            return null;
        }
        map = new HashMap();
        map.put("success", true);
        map.put("message", "修改成功");
        Struts2Utils.renderJson(map);
        return null;
    }

    @Override
    public String delete() {
        try {
            commodityService.delete(Commodity.class, getIds());
        } catch (Exception e) {
            LOG.info("删除数据出错:", e);
            Struts2Utils.renderText(e.getMessage());
            return null;
        }
        Struts2Utils.renderText("删除成功");
        return null;
    }

    /**
     * @Param:[] 查询方法+模糊查询
     * @Return:java.lang.String
     * @Date:2021/2/2
     **/
    @Override
    public String query() {
       // 分页请求参数
        int start = super.getStart();
        int len = super.getLimit();
        if (start == -1) {
            start = 0;
        }
        if (len == -1) {
            len = 10;
        }
        try {
            List<Commodity> commodityList = commodityService.queryCommodity(likeQueryValue);
            if (len > commodityList.size()) {
                len = commodityList.size();
            }
            List<Commodity> models = new ArrayList<>();
            for (int i = start; i < start + getLimit(); i++) {
                if (i >= commodityList.size()) {
                    break;
                }
                models.add(commodityList.get(i));
            }
            // 构造当前页面对象
            page = new Page<>();
            page.setModels(models);
            page.setTotalRecords(commodityList.size());
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
    public String getLikeQueryValue() {
        return likeQueryValue;
    }

    @Override
    public void setLikeQueryValue(String likeQueryValue) {
        this.likeQueryValue = likeQueryValue;
    }

}
