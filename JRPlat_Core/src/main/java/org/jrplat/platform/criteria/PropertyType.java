/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.platform.criteria;

import java.sql.Date;
import java.util.List;

/**
 * 属性的类型
 * @author 西安捷然
 *
 */
public enum PropertyType {
    String(String.class), Integer(Integer.class), Double(Double.class), Date(Date.class), Boolean(Boolean.class), Long(Long.class), List(List.class);

    private Class<?> clazz;

    PropertyType(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Class<?> getValue() {
        return clazz;
    }

}