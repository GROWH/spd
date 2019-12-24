/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.security.ws;

import org.jrplat.module.security.model.User;
import org.jrplat.module.security.service.UserDetailsServiceImpl;
import org.jrplat.module.security.service.password.PasswordEncoder;
import org.jrplat.platform.log.JRPlatLogger;
import org.jrplat.platform.log.JRPlatLoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jws.WebService;

/**
 * 用户认证服务实现
 * @author 西安捷然
 */
@Service
@WebService(endpointInterface = "org.jrplat.module.security.ws.UserService")
public class UserServiceImpl implements UserService {
    private static final JRPlatLogger LOG = JRPlatLoggerFactory.getJRPlatLogger(UserServiceImpl.class);
    @Resource(name = "userDetailsServiceImpl")
    private UserDetailsServiceImpl userDetailsServiceImpl;
    @Resource(name = "passwordEncoder")
    private PasswordEncoder passwordEncoder;

    @Override
    public String login(String username, String password) {
        try {
            User user = (User) userDetailsServiceImpl.loadUserByUsername(username);
            password = passwordEncoder.encode(password, user);
            if (password.equals(user.getPassword())) {
                return "认证成功";
            } else {
                return "密码不正确";
            }
        } catch (UsernameNotFoundException | DataAccessException e) {
            return e.getMessage();
        }
    }

    @Override
    public User getUserInfo(String username, String password) {
        try {
            User user = (User) userDetailsServiceImpl.loadUserByUsername(username);
            if (user != null) {
                password = passwordEncoder.encode(password, user);
                if (password.equals(user.getPassword())) {
                    return user;
                }
            }
        } catch (UsernameNotFoundException | DataAccessException e) {
            LOG.info("没有获取到用户信息：" + username);
        }
        return null;
    }

}