/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.platform.criteria;

/**
 * 排序条件
 * @author 西安捷然
 */
public class Order {
    private String propertyName;
    private Sequence sequence;

    public Order() {
        super();
    }

    public Order(String propertyName, String sequence) {
        super();
        this.propertyName = propertyName;
        this.sequence = Enum.valueOf(Sequence.class, sequence);
    }

    public Order(String propertyName, Sequence sequence) {
        super();
        this.propertyName = propertyName;
        this.sequence = sequence;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public Sequence getSequence() {
        return sequence;
    }

    public void setSequence(Sequence sequence) {
        this.sequence = sequence;
    }
}