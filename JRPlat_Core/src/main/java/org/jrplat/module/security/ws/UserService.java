/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.security.ws;

/**
 *用户认证服务接口
 * @author 西安捷然
 */

import org.jrplat.module.security.model.User;

import javax.jws.WebService;

@WebService
public interface UserService {
    public String login(String username, String password);

    public User getUserInfo(String username, String password);
}