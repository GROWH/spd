/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.system.service;

import org.jrplat.module.system.model.JRPlatConfig;
import org.jrplat.platform.log.JRPlatLogger;
import org.jrplat.platform.log.JRPlatLoggerFactory;
import org.jrplat.platform.service.ServiceFacade;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Properties;

/**
 * 数据库和配置文件同步服务
 * @author 西安捷然
 */
@Service
public class JRPlatConfigSyncService implements ApplicationListener {
    private static final JRPlatLogger LOG = JRPlatLoggerFactory.getJRPlatLogger(JRPlatConfigSyncService.class);

    @Resource(name = "serviceFacade")
    private ServiceFacade serviceFacade;

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            LOG.info("spring容器初始化完成, 开始检查是否启用数据库配置，如果启用数据库配置，则数据库中的配置信息有最高优先级，会覆盖配置文件的配置信息");
            if (dbEnable()) {
                LOG.info("启用数据库配置，同步数据库，将配置文件中独有的配置信息加入数据库");
                if (overrideDB()) {
                    LOG.info("删除数据库中的所有配置信息");
                    clearDbConfig();
                }
                syncToDB();
                if (!overrideDB()) {
                    syncFromDB();
                }
            } else {
                LOG.info("未启用数据库配置");
            }
        }
    }

    private boolean dbEnable() {
        return PropertyHolder.getBooleanProperty("config.db.enable");
    }

    private boolean overrideDB() {
        return PropertyHolder.getBooleanProperty("config.db.override");
    }

    /**
     * 删除数据库中的所有配置信息
     */
    private void clearDbConfig() {
        List<JRPlatConfig> configs = serviceFacade.query(JRPlatConfig.class).getModels();
        int len = configs.size();
        if (len < 1) {
            return;
        }
        Integer[] ids = new Integer[len];
        for (int i = 0; i < len; i++) {
            ids[i] = configs.get(i).getId();
        }
        serviceFacade.delete(JRPlatConfig.class, ids);
    }

    /**
     * 将配置文件里面的配置信息导入数据库，如果数据库中已经存在相应的配置信息，则忽略导入
     */
    private void syncToDB() {
        Properties properties = PropertyHolder.getProperties();
        for (Object key : properties.keySet()) {
            JRPlatConfig config = new JRPlatConfig();
            config.setConfigKey(key.toString());
            String value = properties.getProperty(key.toString());
            config.setConfigValue(value);
            try {
                serviceFacade.create(config);
                LOG.info("成功将配置项 " + key + "=" + value + " 加入数据库");
            } catch (Exception e) {
                LOG.info("配置项 " + key + " 已经存在于数据库中，配置文件中的值不会覆盖数据库中的值，如需覆盖，则启用配置config.db.override=true");
            }
        }
    }

    /**
     * 用数据库里面的配置信息覆盖配置文件里面的配置信息
     */
    private void syncFromDB() {
        List<JRPlatConfig> configs = serviceFacade.query(JRPlatConfig.class).getModels();
        LOG.info("从数据库中加载的配置信息数目：" + configs.size());
        for (JRPlatConfig config : configs) {
            LOG.info("旧值（配置文件）：" + config.getConfigKey() + "=" + PropertyHolder.getProperty(config.getConfigKey()));
            LOG.info("新值（数据库）：" + config.getConfigKey() + "=" + config.getConfigValue());
            PropertyHolder.setProperty(config.getConfigKey(), config.getConfigValue());
        }
    }
}
