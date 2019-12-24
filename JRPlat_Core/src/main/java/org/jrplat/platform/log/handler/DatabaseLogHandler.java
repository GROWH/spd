/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.platform.log.handler;

import org.jrplat.platform.log.JRPlatLogger;
import org.jrplat.platform.log.JRPlatLoggerFactory;
import org.jrplat.platform.model.Model;
import org.jrplat.platform.service.ServiceFacade;
import org.jrplat.platform.util.ConvertUtils;
import org.jrplat.platform.util.SpringContextUtils;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Locale;

/**
 * 日志处理
 * 将日志存入独立日志数据库（非业务数据库）
 *
 * @author 西安捷然
 */
@Service
public class DatabaseLogHandler implements LogHandler {
    private static final JRPlatLogger LOG = JRPlatLoggerFactory.getJRPlatLogger(DatabaseLogHandler.class);
    //使用日志数据库
    @Resource(name = "serviceFacadeForLog")
    private ServiceFacade serviceFacade;

    /**
     * 打开日志数据库em
     * @param entityManagerFactory
     */
    private static void openEntityManagerForLog(EntityManagerFactory entityManagerFactory) {
        EntityManager em = entityManagerFactory.createEntityManager();
        TransactionSynchronizationManager.bindResource(entityManagerFactory, new EntityManagerHolder(em));
        LOG.info("打开ForLog实体管理器");
    }

    /**
     * 关闭日志数据库em
     * @param entityManagerFactory
     */
    private static void closeEntityManagerForLog(EntityManagerFactory entityManagerFactory) {
        EntityManagerHolder emHolder = (EntityManagerHolder) TransactionSynchronizationManager.unbindResource(entityManagerFactory);
        LOG.info("关闭ForLog实体管理器");
        EntityManagerFactoryUtils.closeEntityManager(emHolder.getEntityManager());
    }

    @Override
    public <T extends Model> void handle(List<T> list) {
        int len = list.size();
        LOG.info("需要保存的日志数目：" + len);
        LOG.info("The number of logs to be saved：" + len, Locale.ENGLISH);
        long start = System.currentTimeMillis();
        EntityManagerFactory entityManagerFactoryForLog = SpringContextUtils.getBean("entityManagerFactoryForLog");
        openEntityManagerForLog(entityManagerFactoryForLog);
        //保存日志
        serviceFacade.create(list);
        closeEntityManagerForLog(entityManagerFactoryForLog);
        long cost = System.currentTimeMillis() - start;
        LOG.info("成功保存 " + len + " 条日志, 耗时: " + ConvertUtils.getTimeDes(cost));
        LOG.info("Success to save " + len + " logs, elapsed: " + ConvertUtils.getTimeDes(cost), Locale.ENGLISH);
    }
}
