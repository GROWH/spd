/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.platform.log;

import java.util.HashMap;
import java.util.Map;

/**
 *日志输出支持多国语言切换解决方案工厂类
 * @author 西安捷然
 */
public class JRPlatLoggerFactory {
    private static final Map<Class, JRPlatLogger> CACHE = new HashMap<>();

    private JRPlatLoggerFactory() {
    }

    public static synchronized JRPlatLogger getJRPlatLogger(Class clazz) {
        JRPlatLogger log = CACHE.get(clazz);
        if (log == null) {
            log = new JRPlatLoggerImpl(clazz);
            CACHE.put(clazz, log);
        }
        return log;
    }
}
