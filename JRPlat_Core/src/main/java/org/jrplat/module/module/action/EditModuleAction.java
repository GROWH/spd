/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.module.action;

import org.apache.struts2.convention.annotation.Namespace;
import org.jrplat.module.module.model.Module;
import org.jrplat.module.module.service.ModuleCache;
import org.jrplat.module.module.service.ModuleService;
import org.jrplat.platform.action.ExtJSSimpleAction;
import org.jrplat.platform.util.Struts2Utils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * 维护树形模块，对应于module.xml文件
 * 在module.xml中的数据未导入到数据库之前，可以通过修改module.xml文件的形式修改树形模块
 * 在module.xml中的数据导入到数据库之后，就只能在浏览器网页中对树形模块进行修改
 *
 * 修改模块
 * @author 西安捷然
 */
@Controller
@Scope("prototype")
@Namespace("/module")
public class EditModuleAction extends ExtJSSimpleAction<Module> {
    @Resource(name = "moduleService")
    private ModuleService moduleService;
    private String node;

    @Override
    public String query() {
        if (node == null) {
            return super.query();
        }
        if (node.trim().startsWith("root")) {
            String json = moduleService.toRootJsonForEdit();
            Struts2Utils.renderJson(json);
            return null;
        }

        if (node.contains("-")) {
            try {
                String[] temp = node.split("-");
                int id = Integer.parseInt(temp[1]);
                Module module = moduleService.getModule(id);
                String json = moduleService.toJsonForEdit(module);
                Struts2Utils.renderJson(json);
            } catch (Exception e) {
                LOG.error("获取根模块出错", e);
            }
        }

        return null;
    }

    @Override
    protected void afterSuccessPartUpdateModel(Module model) {
        //手动清空缓存
        ModuleCache.clear();
    }

    public void setNode(String node) {
        this.node = node;
    }
}