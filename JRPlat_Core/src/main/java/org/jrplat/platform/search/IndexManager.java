/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.platform.search;

import org.jrplat.platform.log.JRPlatLogger;
import org.jrplat.platform.log.JRPlatLoggerFactory;
import org.jrplat.platform.model.Model;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Locale;

/**
 * 实时索引管理，包括：新增、修改、删除
 * @author 西安捷然
 */
@Service
public class IndexManager {
    private static final JRPlatLogger LOG = JRPlatLoggerFactory.getJRPlatLogger(IndexManager.class);

    @Resource(name = "indexRebuilder")
    private IndexRebuilder indexRebuilder;
    private volatile boolean buiding = false;

    /**
     * 异步重建索引（线程安全且为异步调用）
     * 只有一个重建操作完成后才可开始另一个重建操作
     * 利用buiding变量来进行控制
     * 因为涉及到多个线程对同一个变量的读写，所以
     * buiding变量要加volatile
     * 保证线程的可见性
     */
    public void rebuidAll() {
        if (buiding) {
            LOG.info("正在重建索引，请求自动取消");
            LOG.info("Rebuilding index is in progress, request auto cancel", Locale.ENGLISH);
            return;
        }
        buiding = true;
        LOG.info("开始重建索引");
        LOG.info("Begin to rebuild index", Locale.ENGLISH);
        new Thread(new Runnable() {
            @Override
            public void run() {
                indexRebuilder.build();
                LOG.info("结束重建索引");
                LOG.info("Finish rebuild index", Locale.ENGLISH);
                buiding = false;
            }
        }).start();
    }

    @Transactional
    public void createIndex(Model model) {
        try {
            //在这里创建索引
            //.......
        } catch (Exception e) {
            LOG.error("创建索引失败", e);
            LOG.error("Failed in building index", e, Locale.ENGLISH);
        }
    }

    @Transactional
    public void updateIndex(Class<? extends Model> type, Model model) {
        try {
            //在这里更新索引
            //........
        } catch (Exception e) {
            LOG.error("更新索引失败", e);
            LOG.error("Failed to update index", e, Locale.ENGLISH);
        }
    }

    @Transactional
    public void deleteIndex(Class<? extends Model> type, Object objectID) {
        try {
            //在这里删除索引
            //........
        } catch (Exception e) {
            LOG.error("删除索引失败", e);
            LOG.error("Failed to delete index", e, Locale.ENGLISH);
        }
    }
}