package org.jrplat.platform.wro4j;


import ro.isdc.wro.model.resource.support.naming.NamingStrategy;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

/**
 *MultiMerge是指对platform/include/common.jsp所引用的JS(CSS)合并为多个文件，同一个文件夹下多个文件合并为一个文件
 * @author 西安捷然
 */
public class JRPlatMultiMergeNamingStrategy implements NamingStrategy {
    private static final Map<String, String> map = new HashMap<>();

    static {
        map.put("extjs_css_merge.css", "extjs/css/");
        map.put("extjs_js_merge.js", "extjs/js/");
        map.put("extjs_ux_merge.js", "extjs/ux/");
        map.put("extjs_ux_css_merge.css", "extjs/ux/css/");
        map.put("FusionCharts_merge.js", "FusionCharts/");
        map.put("ckeditor_merge.js", "ckeditor/");
        map.put("ckfinder_merge.js", "ckfinder/");
        map.put("DateTime_merge.css", "DateTime/");
        map.put("DateTime_merge.js", "DateTime/");
        map.put("js_merge.js", "js/");
        map.put("platform_css_merge.css", "platform/css/");
        map.put("platform_js_merge.js", "platform/js/");
        map.put("login_merge.js", "js/");
        map.put("login_merge.css", "css/");
    }

    @Override
    public String rename(String originalName, InputStream inputStream) throws IOException {
        String path = map.get(originalName);
        System.out.println("originalName:" + originalName);
        System.out.println("path:" + path);
        originalName = path + originalName;
        System.out.println("originalName:" + originalName);
        return originalName;
    }
}
