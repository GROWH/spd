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
 * 密码长度安全策略
 * 密码长度必须大于等于6
 * @author 西安捷然
 */
@Service
public class PasswordLengthStrategy implements PasswordStrategy {
    private static final JRPlatLogger LOG = JRPlatLoggerFactory.getJRPlatLogger(PasswordLengthStrategy.class);

    @Override
    public void check(String password) throws PasswordInvalidException {
        if (StringUtils.isBlank(password) || password.length() < 6) {
            String message = "密码长度必须大于等于6";
            LOG.error(message);
            throw new PasswordInvalidException(message);
        }
        LOG.info("密码符合安全策略");
    }
}
