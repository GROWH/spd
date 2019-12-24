/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.security.service.password;

import org.apache.commons.lang.StringUtils;
import org.jrplat.module.system.service.PropertyHolder;
import org.jrplat.platform.log.JRPlatLogger;
import org.jrplat.platform.log.JRPlatLoggerFactory;
import org.jrplat.platform.util.SpringContextUtils;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * 密码安全策略执行者
 * 根据配置项user.password.strategy
 * 指定的spring bean name
 * 分别执行指定的策略
 * @author 西安捷然
 */
@Service
public class PasswordStrategyExecuter implements PasswordStrategy, ApplicationListener {
    private static final JRPlatLogger LOG = JRPlatLoggerFactory.getJRPlatLogger(ApplicationListener.class);
    private final List<PasswordStrategy> passwordStrategys = new LinkedList<>();

    @Override
    public void check(String password) throws PasswordInvalidException {
        for (PasswordStrategy passwordStrategy : passwordStrategys) {
            passwordStrategy.check(password);
        }
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            LOG.info("spring容器初始化完成,开始解析PasswordStrategy");
            String strategy = PropertyHolder.getProperty("user.password.strategy");
            if (StringUtils.isBlank(strategy)) {
                LOG.info("未配置user.password.strategy");
                return;
            }
            LOG.info("user.password.strategy：" + strategy);
            String[] strategys = strategy.trim().split(";");
            for (String item : strategys) {
                PasswordStrategy passwordStrategy = SpringContextUtils.getBean(item.trim());
                if (passwordStrategy != null) {
                    passwordStrategys.add(passwordStrategy);
                    LOG.info("找到PasswordStrategy：" + passwordStrategy);
                } else {
                    LOG.info("未找到PasswordStrategy：" + passwordStrategy);
                }
            }
        }
    }
}
