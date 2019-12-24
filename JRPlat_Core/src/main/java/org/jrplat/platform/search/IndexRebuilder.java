/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.platform.search;

import org.jrplat.platform.log.JRPlatLogger;
import org.jrplat.platform.log.JRPlatLoggerFactory;
import org.jrplat.platform.util.ConvertUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Locale;

/**
 * 索引重建
 * @author 西安捷然
 */
@Service
public class IndexRebuilder {
    private static final JRPlatLogger LOG = JRPlatLoggerFactory.getJRPlatLogger(IndexRebuilder.class);

    /**
     * 同步重建索引
     * 1、非线程安全
     * 如需要线程安全，请使用IndexManager类的rebuidAll方法
     * 2、同步调用
     * 阻塞用户线程直到索引建立完毕
     *
     * @return 是否重建成功
     */
    public boolean build() {
        try {
            LOG.info("开始删除索引文件");
            LOG.info("Start to delete index file", Locale.ENGLISH);

//            delDir(IndexManager.getIndexDir());

            LOG.info("删除索引文件结束");
            LOG.info("Finish delete index file", Locale.ENGLISH);
            LOG.info("开始建立索引文件...");
            LOG.info("Start to create index file...", Locale.ENGLISH);
            long beginTime = System.currentTimeMillis();
            float max = (float) Runtime.getRuntime().maxMemory() / 1000000;
            float total = (float) Runtime.getRuntime().totalMemory() / 1000000;
            float free = (float) Runtime.getRuntime().freeMemory() / 1000000;
            String pre = "执行之前剩余内存:" + max + "-" + total + "+" + free + "=" + (max - total + free);
            String preEn = "Remain memory before execution:" + max + "-" + total + "+" + free + "=" + (max - total + free);

            //在这里重建索引
            //........

            long costTime = System.currentTimeMillis() - beginTime;
            max = (float) Runtime.getRuntime().maxMemory() / 1000000;
            total = (float) Runtime.getRuntime().totalMemory() / 1000000;
            free = (float) Runtime.getRuntime().freeMemory() / 1000000;
            String post = "执行之后剩余内存:" + max + "-" + total + "+" + free + "=" + (max - total + free);
            String postEn = "Remain memory after execution:" + max + "-" + total + "+" + free + "=" + (max - total + free);
            LOG.info("索引文件建立完毕");
            LOG.info("Finish build index", Locale.ENGLISH);
            LOG.info("耗时:" + ConvertUtils.getTimeDes(costTime));
            LOG.info("Elapsed:" + ConvertUtils.getTimeDes(costTime), Locale.ENGLISH);
            LOG.info(pre);
            LOG.info(preEn, Locale.ENGLISH);
            LOG.info(post);
            LOG.info(postEn, Locale.ENGLISH);
        } catch (Exception e) {
            LOG.error("建立索引出错", e);
            LOG.error("Failed in building index", e, Locale.ENGLISH);
            return false;
        }
        return true;
    }

    private void delDir(File file) {
        if (file.isFile()) {
            file.delete();
        } else if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files.length == 0) {
                file.delete();
            } else {
                for (File f : files) {
                    delDir(f);
                }
            }
        }
    }
}