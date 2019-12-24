/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.system.service;

import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * 在Spring ApplicationContext中初始化Slf4对Java.util.logging的拦截.
 *
 * @author 西安捷然
 */
@Service
public class JulOverSlf4j {

    //Spring在所有属性注入后自动执行的函数.
    @PostConstruct
    public void init() {
        SLF4JBridgeHandler.install();
    }
}