/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.platform.dao;

import org.jrplat.platform.criteria.OrderCriteria;
import org.jrplat.platform.criteria.PageCriteria;
import org.jrplat.platform.criteria.Property;
import org.jrplat.platform.criteria.PropertyCriteria;
import org.jrplat.platform.model.Model;
import org.jrplat.platform.result.Page;
import org.jrplat.platform.util.ReflectionUtils;

import java.util.List;


public abstract class AbstractDao<T extends Model> extends DaoSupport implements Dao<T> {

    protected Class<T> modelClass;

    public AbstractDao() {
        super(MultiDatabase.JRPlat);
        this.modelClass = ReflectionUtils.getSuperClassGenricType(getClass());
    }

    public AbstractDao(MultiDatabase multiDatabase) {
        super(multiDatabase);
        this.modelClass = ReflectionUtils.getSuperClassGenricType(getClass());
    }

    @Override
    public void create(T model) {
        getEntityManager().persist(model);
    }

    @Override
    public T retrieve(Integer modelId) {
        return getEntityManager().find(modelClass, modelId);
    }

    @Override
    public void update(T model) {
        getEntityManager().merge(model);
    }

    @Override
    public void update(Integer modelId, List<Property> properties) {
        T model = retrieve(modelId);
        for (Property property : properties) {
            ReflectionUtils.setFieldValue(model, property.getName(), property.getValue());
        }
        update(model);
    }

    @Override
    public void delete(Integer modelId) {
        getEntityManager().remove(getEntityManager().getReference(modelClass, modelId));
    }

    @Override
    public Page<T> query() {
        return query(null);
    }

    @Override
    public Page<T> query(PageCriteria pageCriteria) {
        return query(pageCriteria, null, defaultOrderCriteria);
    }

    @Override
    public Page<T> query(PageCriteria pageCriteria, PropertyCriteria propertyCriteria) {
        return query(pageCriteria, propertyCriteria, defaultOrderCriteria);
    }

    @Override
    public Page<T> query(PageCriteria pageCriteria, PropertyCriteria propertyCriteria, OrderCriteria orderCriteria) {
        return super.queryData(modelClass, pageCriteria, propertyCriteria, orderCriteria);
    }
}