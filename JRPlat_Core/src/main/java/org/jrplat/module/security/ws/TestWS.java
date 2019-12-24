/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.security.ws;

import org.jrplat.module.security.model.User;
import org.jrplat.platform.log.JRPlatLogger;
import org.jrplat.platform.log.JRPlatLoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *测试WEB SERVICE是否正常
 * @author 西安捷然
 */
@Service
public class TestWS {
    protected final JRPlatLogger LOG = JRPlatLoggerFactory.getJRPlatLogger(getClass());
    @Resource(name = "userServiceClient")
    private UserService userService;

    //@PostConstruct
    public void testws() {
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(120000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                LOG.info("开始检查web service服务端和客户端是否正常");
                LOG.info("userService.login(\"admin\", \"admin\")： " + userService.login("admin", "admin"));
                User user = userService.getUserInfo("admin", "admin");
                if (user != null) {
                    LOG.info("user.getUsername()： " + user.getUsername());
                    LOG.info("user.getPassword()： " + user.getPassword());
                }

                LOG.info("userService.login(\"admin\", \"123456\")： " + userService.login("admin", "123456"));
                user = userService.getUserInfo("admin", "123456");
                if (user != null) {
                    LOG.info("user.getUsername()： " + user.getUsername());
                    LOG.info("user.getPassword()： " + user.getPassword());
                }

                LOG.info("userService.login(\"administrator\", \"123456\")： " + userService.login("administrator", "123456"));
                user = userService.getUserInfo("administrator", "123456");
                if (user != null) {
                    LOG.info("user.getUsername()： " + user.getUsername());
                    LOG.info("user.getPassword()： " + user.getPassword());
                }
                LOG.info("检查完毕");
            }
        }.start();
    }
}