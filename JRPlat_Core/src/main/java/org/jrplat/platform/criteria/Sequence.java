/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.platform.criteria;

/**
 *排序条件的顺序，升序和降序
 * @author 西安捷然
 */
public enum Sequence {
    DESC("desc"), ASC("asc");
    private String value;

    private Sequence(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}