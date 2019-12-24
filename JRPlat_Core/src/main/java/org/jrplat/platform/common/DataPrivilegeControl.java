/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.platform.common;

import org.jrplat.module.system.service.PropertyHolder;
import org.jrplat.platform.dao.EntityManagerHolder;
import org.jrplat.platform.dao.MultiDatabase;
import org.jrplat.platform.model.Model;

import javax.persistence.Entity;

/**
 *
 * @author 西安捷然
 */
public abstract class DataPrivilegeControl extends EntityManagerHolder {
    private static String[] excludes = null;
    private static String[] specials = null;

    static {
        excludes = PropertyHolder.getProperty("data.privilege.control.exclude").split(",");
        specials = PropertyHolder.getProperty("data.privilege.control.special").split(",");
    }

    public DataPrivilegeControl() {
        super(MultiDatabase.JRPlat);
    }

    public DataPrivilegeControl(MultiDatabase multiDatabase) {
        super(multiDatabase);
    }

    protected boolean needPrivilege(String modelClass) {
        for (String exclude : excludes) {
            if (exclude.equals(modelClass)) {
                return false;
            }
        }
        return true;
    }

    protected <T extends Model> boolean needPrivilege(Class<T> modelClass) {
        String entity = getEntityName(modelClass);
        return needPrivilege(entity);
    }

    protected boolean dataSpecial(String modelClass) {
        for (String exclude : specials) {
            if (exclude.equals(modelClass)) {
                return false;
            }
        }
        return true;
    }

    protected <T extends Model> boolean dataSpecial(Class<T> modelClass) {
        String entity = getEntityName(modelClass);
        return dataSpecial(entity);
    }

    /**
     * 获取实体的名称
     * @param clazz
     * @return
     */
    protected String getEntityName(Class<? extends Model> clazz) {
        String entityname = clazz.getSimpleName();

        Entity entity = clazz.getAnnotation(Entity.class);
        if (entity != null && entity.name() != null && !"".equals(entity.name())) {
            entityname = entity.name();
        }
        return entityname;
    }
}