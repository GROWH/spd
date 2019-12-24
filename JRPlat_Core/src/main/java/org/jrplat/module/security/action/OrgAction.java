/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.security.action;

import org.apache.struts2.convention.annotation.Namespace;
import org.jrplat.module.dictionary.model.DicItem;
import org.jrplat.module.security.model.Org;
import org.jrplat.module.security.model.User;
import org.jrplat.module.security.service.OrgService;
import org.jrplat.module.security.service.UserHolder;
import org.jrplat.module.system.service.PropertyHolder;
import org.jrplat.platform.action.ExtJSSimpleAction;
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
@Namespace("/security")
public class OrgAction extends ExtJSSimpleAction<Org> {
    private String node;
    @Resource(name = "orgService")
    private OrgService orgService;
    private String querySLDW;

    @Override
    protected void checkModel(Org model) throws Exception {
        super.checkModel(model);
        User user = UserHolder.getCurrentLoginUser();
        if (!user.getUsername().equals("admin")) {
            model.setMaxUser(PropertyHolder.getIntProperty("init.org.max_userNumber"));     //最大用户数
        }
        model.setUsedUser(0);    //用户数
        //org信息，只能给自己添加直接下级信息, 超管除外.
        if (!user.isSuperManager() && !model.getParent().getId().equals(user.getOrg().getId())) {
            throw new RuntimeException("您只能给自己所属单位添加直接下级单位");
        }
        if (orgService.repeatNameCheck(model)) {
            throw new RuntimeException("您输入的单位名称已经存在");
        }
        if (orgService.repeatNumberCheck(model)) {
            throw new RuntimeException("您输入的单位编号已经存在");
        }
    }

    @Override
    protected void old(Org model) {
        super.old(model);
        String maxUser = getRequest().getParameter("model.maxUser");
        String usedUser = getRequest().getParameter("model.usedUser");
        if (!UserHolder.getCurrentLoginUser().getUsername().equals("admin")) {
            if (maxUser != null) {
                Integer max = Integer.parseInt(maxUser);
                if (max != model.getMaxUser()) {
                    throw new RuntimeException("你不能修改最大用户数");
                }
            }
            if (usedUser != null) {
                Integer used = Integer.parseInt(usedUser);
                if (used != model.getUsedUser()) {
                    throw new RuntimeException("你不能修改已用用户数");
                }
            }
        }
    }

    public String store() {
        return query();
    }

    @Override
    public String query() {
        //如果node为null则采用普通查询方式
        if (node == null) {
            return super.query();
        }
        //如果指定了node则采用自定义的查询方式
        if (node.trim().startsWith("root")) {
            String json = orgService.toRootJson();
            Struts2Utils.renderJson(json);
        } else {
            int id = Integer.parseInt(node.trim());
            String json = orgService.toJson(id);
            Struts2Utils.renderJson(json);
        }
        return null;
    }

    public void setNode(String node) {
        this.node = node;
    }

    // 返回所有的单位id和name
    public String storeAll() {
        List<Org> orgs = getService().query(Org.class).getModels();
        List<Map<String, String>> data = new ArrayList<>();
        for (Org org : orgs) {
            Map<String, String> temp = new HashMap<>();
            temp.put("value", "" + org.getId());
            temp.put("text", org.getOrgName());
            data.add(temp);
        }
        Struts2Utils.renderJson(data);
        return null;
    }

    public String getQuerySLDW() {
        return querySLDW;
    }

    public void setQuerySLDW(String querySLDW) {
        this.querySLDW = querySLDW;
    }
}