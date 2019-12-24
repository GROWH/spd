package org.jrplat.platform.wro4j;


import ro.isdc.wro.model.resource.support.naming.NamingStrategy;

import java.io.IOException;
import java.io.InputStream;

/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

/**
 *SingleMerge是指对platform/include/common.jsp所引用的JS(CSS)合并为一个文件
 * @author 西安捷然
 */
public class JRPlatSingleMergeNamingStrategy implements NamingStrategy {
    @Override
    public String rename(String originalName, InputStream inputStream) throws IOException {
        System.out.println("originalName:" + originalName);
        if (originalName.contains("jrplat_merge")) {
            originalName = "platform/include/" + originalName;
        }
        if (originalName.contains("login_merge.js")) {
            originalName = "js/" + originalName;
        }
        if (originalName.contains("login_merge.css")) {
            originalName = "css/" + originalName;
        }

        System.out.println("originalName:" + originalName);
        return originalName;
    }
}
