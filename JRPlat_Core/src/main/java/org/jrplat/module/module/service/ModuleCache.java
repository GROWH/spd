/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.module.service;

import org.jrplat.module.security.model.User;
import org.jrplat.platform.log.JRPlatLogger;
import org.jrplat.platform.log.JRPlatLoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author 西安捷然
 */
public class ModuleCache {
    private static final JRPlatLogger LOG = JRPlatLoggerFactory.getJRPlatLogger(ModuleCache.class);
    private static final HashMap<String, String> cache = new HashMap<>();

    private ModuleCache() {
    }

    public static HashMap<String, String> getCache() {
        return cache;
    }

    public static void put(String key, String value) {
        LOG.info("添加ModuleCache缓存数据：" + "Key=" + key + "----" + "value=" + value);
        cache.put(key, value);
    }

    public static String get(String key) {
        return cache.get(key);
    }

    public static void clear() {
        cache.clear();
        LOG.info("清空所有缓存");
    }

    /**
     * 清空指定key缓存
     * @param key
     */
    public static void remove(String key) {
        LOG.info("清除ModuleCache缓存数据： key=" + key);
        cache.remove(key);
    }

    /**
     * 删除用户列表缓存的module，方便用户进行配置，不用重启系统
     * @param users
     */
    public static void remove(List<User> users) {
        HashMap<String, String> tempCache = new HashMap<>();
        tempCache = (HashMap<String, String>) cache.clone();
        Iterator<String> it = tempCache.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            int pos = key.indexOf("_");
            String username = key.substring(0, pos);
            for (int i = 0; i < users.size(); i++) {
                if (username.equals(users.get(i).getUsername())) {
                    ModuleCache.remove(key);
                }
            }
        }
    }

    /**
     * 删除用户module缓存
     * @param user
     */
    public static void remove(User user) {
        List<User> users = new ArrayList<>();
        users.add(user);
        remove(users);
    }

}
