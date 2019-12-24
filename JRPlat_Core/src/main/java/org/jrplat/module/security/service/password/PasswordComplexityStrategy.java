/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.security.service.password;

import org.apache.commons.lang.StringUtils;
import org.jrplat.platform.log.JRPlatLogger;
import org.jrplat.platform.log.JRPlatLoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 密码复杂性安全策略：
 * 1、密码不能为空
 * 2、密码不能全是数字
 * 3、密码不能全是字符
 * @author 西安捷然
 */
@Service
public class PasswordComplexityStrategy implements PasswordStrategy {
    private static final JRPlatLogger LOG = JRPlatLoggerFactory.getJRPlatLogger(PasswordComplexityStrategy.class);

    @Override
    public void check(String password) throws PasswordInvalidException {
        if (StringUtils.isBlank(password)) {
            String message = "密码不能为空";
            LOG.error(message);
            throw new PasswordInvalidException(message);
        }
        if (StringUtils.isNumeric(password)) {
            String message = "密码不能全是数字";
            LOG.error(message);
            throw new PasswordInvalidException(message);
        }
        if (StringUtils.isAlpha(password)) {
            String message = "密码不能全是字符";
            LOG.error(message);
            throw new PasswordInvalidException(message);
        }
        LOG.info("密码符合安全策略");
    }
}
