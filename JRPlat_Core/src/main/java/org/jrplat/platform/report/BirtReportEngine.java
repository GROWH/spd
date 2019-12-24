/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.platform.report;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.IPlatformContext;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.core.framework.PlatformServletContext;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.jrplat.platform.log.JRPlatLogger;
import org.jrplat.platform.log.JRPlatLoggerFactory;
import org.jrplat.platform.util.ConvertUtils;

import javax.servlet.ServletContext;

/**
 *
 * @author 西安捷然
 */
public class BirtReportEngine {
    private static final JRPlatLogger LOG = JRPlatLoggerFactory.getJRPlatLogger(BirtReportEngine.class);
    private static IReportEngine reportEngine = null;

    private BirtReportEngine() {
    }

    public static synchronized IReportEngine getBirtEngine(ServletContext sc) {
        if (reportEngine == null) {
            LOG.info("开始初始化报表引擎");
            long start = System.currentTimeMillis();
            float total = (float) Runtime.getRuntime().totalMemory() / 1000000;
            EngineConfig config = new EngineConfig();

            config.getAppContext().put(EngineConstants.APPCONTEXT_CLASSLOADER_KEY, BirtReportEngine.class.getClassLoader());
            config.setEngineHome("");

            IPlatformContext context = new PlatformServletContext(sc);
            config.setPlatformContext(context);

            try {
                Platform.startup(config);
            } catch (BirtException e) {
                LOG.error("BIRT启动失败", e);
            }

            IReportEngineFactory factory = (IReportEngineFactory) Platform.createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
            reportEngine = factory.createReportEngine(config);
            total = (float) Runtime.getRuntime().totalMemory() / 1000000 - total;
            LOG.info("完成初始化报表引擎，耗时：" + ConvertUtils.getTimeDes(System.currentTimeMillis() - start) + " ,耗费内存：" + total + "M");
        }
        return reportEngine;
    }
}