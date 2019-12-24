/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.platform.dao;

import org.jrplat.module.security.model.Org;
import org.jrplat.module.security.model.User;
import org.jrplat.module.security.service.UserHolder;
import org.jrplat.platform.criteria.OrderCriteria;
import org.jrplat.platform.criteria.PageCriteria;
import org.jrplat.platform.criteria.Property;
import org.jrplat.platform.criteria.PropertyCriteria;
import org.jrplat.platform.model.Model;
import org.jrplat.platform.result.Page;
import org.jrplat.platform.util.ReflectionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 对任何继承自Model的类进行数据存储操作
 *
 * @author 西安捷然
 */
@Repository
public class DaoFacade extends DaoSupport {
    /**
     * 使用默认数据库
     */
    public DaoFacade() {
        super(MultiDatabase.JRPlat);
    }

    /**
     * 使用默认日志数据库
     *
     * @param multiDatabase
     */
    public DaoFacade(MultiDatabase multiDatabase) {
        super(multiDatabase);
    }

    public void clear() {
        getEntityManager().clear();
    }

    public <T extends Model> void create(T model) {
        getEntityManager().persist(model);
    }

    public <T extends Model> void createWms(T model) {
        getEntityManager().persist(model);
    }

    public <T extends Model> T retrieve(Class<T> modelClass, Integer modelId) {
        T model = getEntityManager().find(modelClass, modelId);

        return model;
            /*
                //权限控制
                User user=UserHolder.getCurrentLoginUser();
                if(user!=null && !user.isSuperManager() && needPrivilege(modelClass) && model.getOwnerUser()!=null){
                    if(user.getId().intValue()==model.getOwnerUser().getId().intValue()){
                        return model;
                    }
                    if(OrgWebService.isParentOf(user.getOrg(), model.getOwnerUser().getOrg())){
                        return model;
                    }
                    return null;
                }else{
                    return model;
                }
             * 
             */
    }


    public <T extends Model> void update(Class<T> modelClass, Integer modelId, List<Property> properties) {
        T model = retrieve(modelClass, modelId);
        for (Property property : properties) {
            ReflectionUtils.setFieldValue(model, property.getName(), property.getValue());
        }
        update(model);
    }

    /**
     * 特殊操作,修改所有数据.
     *
     * @param model
     * @param <T>
     */
    public <T extends Model> void updateAll(T model) {
        getEntityManager().merge(model);
    }

    public <T extends Model> void update(T model) {
        getEntityManager().merge(model);
            /*
                User user=UserHolder.getCurrentLoginUser();
                if(user!=null && !user.isSuperManager() && needPrivilege(model.getClass())){
                    if(user.getId().intValue()==model.getOwnerUser().getId().intValue()){
                        em.merge(model);
                    }
                    if(OrgWebService.isParentOf(user.getOrg(), model.getOwnerUser().getOrg())){
                        em.merge(model);
                    }
                }else{
                    em.merge(model);
                }
             * 
             */
    }

    public <T extends Model> void delete(Class<T> modelClass, Integer modelId) {
        T model = retrieve(modelClass, modelId);
        if (model != null) {
            getEntityManager().remove(model);
        }
                /*
                User user=UserHolder.getCurrentLoginUser();
                if(user!=null && !user.isSuperManager() && needPrivilege(modelClass)){
                    if(user.getId().intValue()==model.getOwnerUser().getId().intValue()){
                        em.remove(model);
                    }
                    if(OrgWebService.isParentOf(user.getOrg(), model.getOwnerUser().getOrg())){
                        em.remove(model);
                    }
                }else{
                    em.remove(model);
                }
                 * 
                 */
    }

    public <T extends Model> Page<T> query(Class<T> modelClass) {
        return query(modelClass, null);
    }

    public <T extends Model> Page<T> query(Class<T> modelClass, PageCriteria pageCriteria) {

        return query(modelClass, pageCriteria, null, defaultOrderCriteria);
    }

    public <T extends Model> Page<T> query(Class<T> modelClass, PageCriteria pageCriteria, PropertyCriteria propertyCriteria) {
        return query(modelClass, pageCriteria, propertyCriteria, defaultOrderCriteria);
    }

    public <T extends Model> Page<T> query(Class<T> modelClass, PageCriteria pageCriteria, PropertyCriteria propertyCriteria, OrderCriteria orderCriteria) {
        return super.queryData(modelClass, pageCriteria, propertyCriteria, orderCriteria);
    }
}