/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.security.service;

import org.eclipse.birt.report.engine.api.*;
import org.jrplat.module.system.service.SystemListener;
import org.jrplat.platform.log.JRPlatLogger;
import org.jrplat.platform.log.JRPlatLoggerFactory;
import org.jrplat.platform.report.BirtReportEngine;
import org.jrplat.platform.util.ConvertUtils;
import org.jrplat.platform.util.FileUtils;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;

/**
 *
 * @author 西安捷然
 */
@Service
public class UserReportService {
    private static final JRPlatLogger LOG = JRPlatLoggerFactory.getJRPlatLogger(UserReportService.class);
    private static String reportPath = "/platform/reports/security/user.rptdesign";
    private IReportEngine birtReportEngine = null;

    public byte[] getReport(ServletContext sc, HttpServletRequest req) {
        LOG.info("开始渲染报表");
        long start = System.currentTimeMillis();
        float total = (float) Runtime.getRuntime().totalMemory() / 1000000;

        this.birtReportEngine = BirtReportEngine.getBirtEngine(sc);
        IReportRunnable design;
        try {
            LOG.info("report path:" + reportPath);
            reportPath = FileUtils.getAbsolutePath(reportPath);
            LOG.info("report path:" + reportPath);
            design = birtReportEngine.openReportDesign(reportPath);
            IRunAndRenderTask task = birtReportEngine.createRunAndRenderTask(design);

            task.getAppContext().put("BIRT_VIEWER_HTTPSERVLET_REQUEST", req);
            task.setParameterValue("title", "用户图形报表");
            task.setParameterValue("tip", "测试用户报表");

            HTMLRenderOption options = new HTMLRenderOption();
            options.setOutputFormat(HTMLRenderOption.OUTPUT_FORMAT_HTML);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            options.setOutputStream(out);
            options.setImageHandler(new HTMLServerImageHandler());
            options.setBaseImageURL(SystemListener.getContextPath() + "/platform/reports/images");
            options.setImageDirectory(FileUtils.getAbsolutePath("/platform/reports/images"));
            task.setRenderOption(options);

            task.run();
            task.close();
            total = (float) Runtime.getRuntime().totalMemory() / 1000000 - total;
            LOG.info("完成渲染报表，耗时：" + ConvertUtils.getTimeDes(System.currentTimeMillis() - start) + " ,耗费内存：" + total + "M");
            return out.toByteArray();
        } catch (EngineException | NumberFormatException e) {
            LOG.error("输出报表出错", e);
        }
        return null;
    }
}