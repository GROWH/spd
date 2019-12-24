/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.platform.service;

import org.jrplat.platform.criteria.OrderCriteria;
import org.jrplat.platform.criteria.PageCriteria;
import org.jrplat.platform.criteria.Property;
import org.jrplat.platform.criteria.PropertyCriteria;
import org.jrplat.platform.dao.DaoFacade;
import org.jrplat.platform.log.JRPlatLogger;
import org.jrplat.platform.log.JRPlatLoggerFactory;
import org.jrplat.platform.model.Model;
import org.jrplat.platform.result.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

/**
 * 对任何继承自Model的类进行数据存储操作
 *
 * @author 西安捷然
 */
@Service
public class ServiceFacade {
    protected final JRPlatLogger LOG = JRPlatLoggerFactory.getJRPlatLogger(getClass());

    @Resource(name = "daoFacade")
    private DaoFacade dao = null;

    public void setDao(DaoFacade dao) {
        this.dao = dao;
    }

    public void clear() {
        dao.clear();
    }

    /**
     * 获取EntityManager,便于在模块中实现数据查询
     *
     * @return
     */
    public EntityManager getEntityManager() {
        return this.dao.getEntityManager();
    }

    /**
     * 批量保存，批量提交，显著提升性能
     *
     * @param <T>
     * @param models
     */
    @Transactional
    public <T extends Model> void create(List<T> models) {
        for (T model : models) {
            dao.create(model);
        }
    }

    @Transactional
    public <T extends Model> void create(T model) {
        dao.create(model);
    }

    public <T extends Model> T retrieve(Class<T> modelClass, Integer modelId) {
        T model = dao.retrieve(modelClass, modelId);

        if (model == null) {
            return null;
        }
        return model;
    }

    @Transactional
    public <T extends Model> void update(T model) {
        dao.update(model);
    }

    /**
     * 可以修改所有数据,不受同步数据影响.
     *
     * @param model
     * @param <T>
     */
    @Transactional(rollbackFor = Exception.class)
    public <T extends Model> void updateAll(T model) {
        dao.updateAll(model);
    }

    @Transactional(rollbackFor = Exception.class)
    public <T extends Model> void update(Class<T> modelClass, Integer modelId, List<Property> properties) {
        dao.update(modelClass, modelId, properties);
    }

    @Transactional(rollbackFor = Exception.class)
    public <T extends Model> void delete(Class<T> modelClass, Integer modelId) {
        dao.delete(modelClass, modelId);
    }

    @Transactional(rollbackFor = Exception.class)
    public <T extends Model> List<Integer> delete(Class<T> modelClass, Integer[] modelIds) {
        List<Integer> ids = new ArrayList<>();
        for (Integer modelId : modelIds) {
            this.delete(modelClass, modelId);
            ids.add(modelId);
        }
        return ids;
    }

    /**
     * 批量保存，批量提交，显著提升性能
     *
     * @param <T>
     * @param models
     */
    @Transactional(rollbackFor = Exception.class)
    public <T extends Model> void createWms(List<T> models) {
        for (T model : models) {
            dao.createWms(model);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public <T extends Model> void createWms(T model) {
        dao.createWms(model);
    }

    public <T extends Model> Page<T> query(Class<T> modelClass) {
        Page<T> page = dao.query(modelClass, null);
        return page;
    }

    public <T extends Model> Page<T> query(Class<T> modelClass, PageCriteria pageCriteria) {
        Page<T> page = dao.query(modelClass, pageCriteria, null);
        return page;
    }

    public <T extends Model> Page<T> query(Class<T> modelClass, PageCriteria pageCriteria, PropertyCriteria propertyCriteria) {
        Page<T> page = dao.query(modelClass, pageCriteria, propertyCriteria);
        return page;
    }

    public <T extends Model> Page<T> query(Class<T> modelClass, PageCriteria pageCriteria, PropertyCriteria propertyCriteria, OrderCriteria orderCriteria) {
        Page<T> page = dao.query(modelClass, pageCriteria, propertyCriteria, orderCriteria);
        return page;
    }

    @Transactional
    public <T extends Model> Page<T> search(String queryString, PageCriteria pageCriteria, Class<T> modelClass) {
        Page<T> page = dao.search(queryString, pageCriteria, modelClass);
        return page;
    }
}