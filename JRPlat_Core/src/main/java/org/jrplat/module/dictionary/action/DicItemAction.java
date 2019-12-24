/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.dictionary.action;

import org.apache.struts2.convention.annotation.Namespace;
import org.jrplat.module.dictionary.cache.DicCache;
import org.jrplat.module.dictionary.model.Dic;
import org.jrplat.module.dictionary.model.DicItem;
import org.jrplat.module.dictionary.service.DicService;
import org.jrplat.module.security.service.UserHolder;
import org.jrplat.platform.action.ExtJSSimpleAction;
import org.jrplat.platform.util.Struts2Utils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.List;

@Scope("prototype")
@Controller
@Namespace("/dictionary")
public class DicItemAction extends ExtJSSimpleAction<DicItem> {
    @Resource(name = "dicService")
    private DicService dicService;
    private String node;

    /**
     * 返回数据字典目录树
     * @return
     */
    public String store() {
        if (node == null) {
            return null;
        }
        Dic dic = null;
        if (node.trim().startsWith("root")) {
            dic = dicService.getRootDic();
        } else {
            int id = Integer.parseInt(node);
            dic = dicService.getDic(id);
        }

        if (dic != null) {
            String json = dicService.toJson(dic);
            Struts2Utils.renderJson(json);
        }
        return null;
    }

    @Override
    protected void checkModel(DicItem model) throws Exception {
        super.checkModel(model);
        if (!UserHolder.getCurrentLoginUser().getUsername().equals("admin")) {
            throw new RuntimeException("您不能新增数据字典");
        }
    }

    @Override
    protected void old(DicItem model) {
        super.old(model);
        if (!UserHolder.getCurrentLoginUser().getUsername().equals("admin")) {
            throw new RuntimeException("您不能修改数据字典");
        }
    }

    @Override
    protected void afterSuccessCreateModel(DicItem model) {
        super.afterSuccessCreateModel(model);
        //更新dic缓存
        DicCache.saveOrUpdate(model);
    }

    @Override
    protected void afterSuccessPartUpdateModel(DicItem model) {
        super.afterSuccessPartUpdateModel(model);
        //更新dic缓存
        DicCache.saveOrUpdate(model);
    }

    @Override
    public void afterDelete(List<Integer> deletedIds) {
        super.afterDelete(deletedIds);
        //删除后 清除缓存
        DicCache.remove(deletedIds);
    }

    public void setNode(String node) {
        this.node = node;
    }
}