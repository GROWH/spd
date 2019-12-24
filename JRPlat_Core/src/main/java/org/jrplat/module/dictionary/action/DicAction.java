/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.dictionary.action;

import org.apache.struts2.convention.annotation.Namespace;
import org.jrplat.module.dictionary.model.Dic;
import org.jrplat.module.dictionary.model.DicItem;
import org.jrplat.module.dictionary.service.DicService;
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
@Namespace("/dictionary")
public class DicAction extends ExtJSSimpleAction<Dic> {
    @Resource(name = "dicService")
    private DicService dicService;
    private String dic;
    private String tree;
    private boolean justCode;

    /**
     *
     * 此类用来提供下列列表服务,主要有两中下列类型：
     * 1、普通下拉选项
     * 2、树形下拉选项
     *
     */
    public String store() {
        Dic dictionary = dicService.getDic(dic);
        if (dictionary == null) {
            LOG.info("没有找到数据词典 " + dic);
            return null;
        }
        if ("true".equals(tree)) {
            String json = dicService.toStoreJson(dictionary);
            Struts2Utils.renderJson(json);
        } else {
            List<Map<String, String>> data = new ArrayList<>();
            for (DicItem item : dictionary.getDicItems()) {
                Map<String, String> map = new HashMap<>();
                if (justCode) {
                    map.put("value", item.getCode());
                } else {
                    map.put("value", item.getId().toString());
                }
                map.put("text", item.getName());
                data.add(map);
            }
            Struts2Utils.renderJson(data);
        }
        return null;
    }

    public void setJustCode(boolean justCode) {
        this.justCode = justCode;
    }

    public void setTree(String tree) {
        this.tree = tree;
    }

    public void setDic(String dic) {
        this.dic = dic;
    }
}