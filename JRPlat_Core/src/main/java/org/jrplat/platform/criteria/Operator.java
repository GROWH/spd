/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.platform.criteria;

/**
 * 操作符号定义
 * @author 西安捷然
 *
 */
public enum Operator {
    ge(">="), gt(">"), le("<="), lt("<"), eq("="), ne("!="), like("like"), is("is"), in("in");
    private String symbol;

    private Operator(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}