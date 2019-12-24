/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.platform.criteria;

import java.util.ArrayList;
import java.util.List;

/**
 * 多个属性条件
 * @author 西安捷然
 *
 */
public class PropertyCriteria {
    private String collection;
    private String object;
    private Criteria criteria = Criteria.and;
    private List<PropertyEditor> propertyEditors = new ArrayList<>();

    public PropertyCriteria() {
    }

    public PropertyCriteria(Criteria criteria) {
        this.criteria = criteria;
    }

    public List<PropertyEditor> getPropertyEditors() {
        return propertyEditors;
    }

    public void addPropertyEditor(PropertyEditor propertyEditor) {
        this.propertyEditors.add(propertyEditor);
    }

    public Criteria getCriteria() {
        return criteria;
    }

    public void setCriteria(Criteria criteria) {
        this.criteria = criteria;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }
}