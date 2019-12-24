/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.security.service.password;

import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * 用户salt服务，salt为：
 * 用户名+JRPlat捷然开发平台的作者是西安捷然，联系方式（邮件：jierankeji@163.com）
 * @author 西安捷然
 */
@Service("saltSource")
public class JRPlatSaltSource implements SaltSource {
    @Override
    public Object getSalt(UserDetails user) {
        //变化的用户名+固定的字符串
        String text = user.getUsername() + "JRPlat捷然开发平台的作者是西安捷然";
        return text;
    }
}
