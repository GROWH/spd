/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.system.service;

import org.jrplat.platform.log.JRPlatLogger;
import org.jrplat.platform.log.JRPlatLoggerFactory;
import org.jrplat.platform.model.Model;
import org.jrplat.platform.model.ModelMetaData;
import org.jrplat.platform.result.Page;
import org.jrplat.platform.service.ServiceFacade;
import org.jrplat.platform.util.ReflectionUtils;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

public abstract class RegisterService<T extends Model> implements ApplicationListener {
    protected final JRPlatLogger LOG = JRPlatLoggerFactory.getJRPlatLogger(getClass());

    @Resource(name = "serviceFacade")
    protected ServiceFacade serviceFacade;
    @Resource(name = "entityManagerFactory")
    protected EntityManagerFactory entityManagerFactory;

    protected Class<T> modelClass;

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            this.modelClass = ReflectionUtils.getSuperClassGenricType(getClass());
            LOG.info("spring容器初始化完成, 开始检查 " + ModelMetaData.getMetaData(this.modelClass.getSimpleName()) + " 是否需要初始化数据");
            if (shouldRegister()) {
                LOG.info("需要初始化 " + ModelMetaData.getMetaData(this.modelClass.getSimpleName()));
                openEntityManager();
                registe();
                closeEntityManager();
                registeSuccess();
            } else {
                LOG.info("不需要初始化 " + ModelMetaData.getMetaData(this.modelClass.getSimpleName()));
            }
        }
    }

    private void openEntityManager() {
        EntityManager em = entityManagerFactory.createEntityManager();
        TransactionSynchronizationManager.bindResource(entityManagerFactory, new EntityManagerHolder(em));
        LOG.info("打开实体管理器");
    }

    private void closeEntityManager() {
        EntityManagerHolder emHolder = (EntityManagerHolder) TransactionSynchronizationManager.unbindResource(entityManagerFactory);
        LOG.info("关闭实体管理器");
        EntityManagerFactoryUtils.closeEntityManager(emHolder.getEntityManager());
    }

    protected void registeSuccess() {

    }

    protected List<T> getRegisteData() {
        return null;
    }

    protected abstract void registe();

    protected boolean shouldRegister() {
        Page<T> page = serviceFacade.query(modelClass);
        if (page.getTotalRecords() == 0) {
            return true;
        }
        return false;
    }
}