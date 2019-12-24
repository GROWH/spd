/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.security.service.password;

/**
 * 用户密码安全策略
 * @author 西安捷然
 */
public interface PasswordStrategy {
    /**
     * 检查用户的密码是否符合安全策略
     * @param password 用户密码
     * @throws PasswordInvalidException 不合法的原因包含在异常里面
     */
    public void check(String password) throws PasswordInvalidException;
}
