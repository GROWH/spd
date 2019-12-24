/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.platform.criteria;

/**
 * 条件符号定义
 * @author 西安捷然
 *
 */
public enum Criteria {
    and("and"), or("or");
    private String symbol;

    private Criteria(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}