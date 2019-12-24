/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.security.service.password;

/**
 * 密码不符合安全策略异常
 * @author 西安捷然
 */
public class PasswordInvalidException extends Exception {
    public PasswordInvalidException(String message) {
        super(message);
    }
}
