/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.platform.spring;

import org.jrplat.module.system.service.SystemListener;
import org.springframework.web.context.ContextLoaderListener;

import javax.servlet.ServletContextEvent;

/**
 * 自定义Spring的ContextLoaderListener
 * @author 西安捷然
 */
public class JRPlatContextLoaderListener extends ContextLoaderListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        //接管系统的启动
        SystemListener.contextInitialized(event);
        super.contextInitialized(event);
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        //接管系统的关闭
        SystemListener.contextDestroyed(event);
        super.contextDestroyed(event);
    }
}