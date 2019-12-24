/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.platform.generator;

import org.jrplat.platform.log.JRPlatLogger;
import org.jrplat.platform.log.JRPlatLoggerFactory;
import org.jrplat.platform.model.Model;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author 西安捷然
 */
public abstract class Generator {
    protected static final JRPlatLogger LOG = JRPlatLoggerFactory.getJRPlatLogger(Generator.class);

    protected static final String ENCODING = "utf-8";
    protected static final FreeMarkerConfigurationFactoryBean factory = new FreeMarkerConfigurationFactoryBean();
    protected static final Map<String, Model> actionToModel = new HashMap<>();

    /**
     * 当Action和Model没有遵循约定，即Action为UserAction,Model为User这种方式时：
     * 给特定的Action指定特定的Model
     * 如：CanLendTipAction 对应 Sms
     * 则action为canLendTip，realModel为 sms
     * @param action
     * @param model
     */
    public static <T extends Model> void setActionModelMap(List<String> actions, T model) {
        for (String action : actions) {
            actionToModel.put(action, model);
        }
    }

    protected static void saveFile(File file, String content) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));

            writer.write(content);
            writer.flush();
            LOG.info("生成的文件为(Generated file is)：" + file.getAbsolutePath());
        } catch (IOException e) {
            LOG.error("生成数据字典出错(Error in generate data dictionary)", e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    LOG.info(e.getMessage());
                }
            }
        }
    }
}