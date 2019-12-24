/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.module.action;

import org.apache.struts2.convention.annotation.Namespace;
import org.jrplat.module.module.model.Module;
import org.jrplat.module.module.service.ModuleCache;
import org.jrplat.module.module.service.ModuleService;
import org.jrplat.module.security.service.UserHolder;
import org.jrplat.platform.action.ExtJSSimpleAction;
import org.jrplat.platform.util.Struts2Utils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * 为树形模块导航菜单服务
 * @author 西安捷然
 */
@Controller
@Scope("prototype")
@Namespace("/module")
public class ModuleAction extends ExtJSSimpleAction<Module> {
    @Resource(name = "moduleService")
    private ModuleService moduleService;
    private String node;
    private boolean privilege = false;
    private boolean recursion = false;

    @Override
    public String query() {
        if (node == null) {
            return super.query();
        }
        //手动缓存控制
        String key = "node:" + node + "_privilege:" + privilege + "_recursion:" + recursion;
        //如果privilege=ture，所有用户共享一份数据
        if (!privilege) {
            key = UserHolder.getCurrentLoginUser().getUsername() + "_" + key;
        }
        String value = ModuleCache.get(key);
        if (value != null) {
            LOG.debug("使用缓存数据，key:" + key + ", value:" + value);
            Struts2Utils.renderJson(value);
            return null;
        }

        long start = System.currentTimeMillis();
        Module module = null;
        if (node.contains("-")) {
            String[] temp = node.split("-");
            int id = Integer.parseInt(temp[1]);
            module = moduleService.getModule(id);
        } else if (node.trim().startsWith("root")) {
            module = moduleService.getRootModule();
        }
        if (module != null) {
            String json = "";
            if (privilege) {
                json = moduleService.toJsonForPrivilege(module);
            } else {
                json = moduleService.toJsonForUser(module, recursion);
            }

            LOG.debug("ModuleAction.query() cost time: " + (System.currentTimeMillis() - start) + " 毫秒");
            LOG.debug("设置缓存数据，key:" + key + ", value:" + json);
            ModuleCache.put(key, json);
            Struts2Utils.renderJson(json);
        }
        return null;
    }

    public void setRecursion(boolean recursion) {
        this.recursion = recursion;
    }

    public void setPrivilege(boolean privilege) {
        this.privilege = privilege;
    }

    public void setNode(String node) {
        this.node = node;
    }
}