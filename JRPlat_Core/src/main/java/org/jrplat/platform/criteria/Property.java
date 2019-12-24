/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.platform.criteria;

/**
 * 属性描述
 * @author 西安捷然
 *
 */
public class Property {
    private static int source;
    private int seq;
    private String name;
    private Object value;

    public Property() {
        this(null, null);
    }

    public Property(String name, Object value) {
        this.name = name;
        this.value = value;
        seq = source++;
    }

    public String getNameParameter() {
        return name.replace(".", "_") + "_" + seq;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}