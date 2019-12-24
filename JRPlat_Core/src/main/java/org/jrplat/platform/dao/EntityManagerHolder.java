/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.platform.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author 西安捷然
 */
public class EntityManagerHolder {
    private MultiDatabase multiDatabase;
    //遗憾的是：这里的unitName用不了配置文件中的变量了
    @PersistenceContext(unitName = "jrplat")
    private EntityManager em;
    @PersistenceContext(unitName = "jrplatForLog")
    private EntityManager emForLog;

    public EntityManagerHolder(MultiDatabase multiDatabase) {
        this.multiDatabase = multiDatabase;
    }

    public EntityManager getEntityManager() {
        if (multiDatabase == MultiDatabase.JRPlat) {
            return em;
        }
        if (multiDatabase == MultiDatabase.JRPlatForLog) {
            return emForLog;
        }
        return em;
    }

    public EntityManager getEm() {
        return em;
    }
}
