/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.security.service.password;

import org.jrplat.module.security.model.User;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 用户密码双重加密：
 * 1、使用SHA-512算法，salt为user.getMetaData()，即：用户信息
 * 2、使用SHA-256算法，salt为saltSource.getSalt(user)，即：用户名+JRPlat捷然开发平台的作者是西安捷然
 * @author 西安捷然
 */
@Service
public class PasswordEncoder {
    private final ShaPasswordEncoder shaPasswordEncoder256 = new ShaPasswordEncoder(256);
    private final ShaPasswordEncoder shaPasswordEncoder512 = new ShaPasswordEncoder(512);
    @Resource(name = "saltSource")
    private SaltSource saltSource;

    public static void main(String[] args) {
        User user = new User();
        user.setUsername("admin");
        user.setPassword("admin");

        SaltSource saltSource = new JRPlatSaltSource();
        PasswordEncoder passwordEncoder = new PasswordEncoder();
        passwordEncoder.setSaltSource(saltSource);

        String password = passwordEncoder.encode512(user.getPassword(), user);
        System.out.println("Step 1 use SHA-512: " + password + " length:" + password.length());

        password = passwordEncoder.encode256(password, user);
        System.out.println("Step 2 use SHA-256: " + password + " length:" + password.length());
    }

    public void setSaltSource(SaltSource saltSource) {
        this.saltSource = saltSource;
    }

    public String encode(String password, User user) {
        return encode256(encode512(password, user), user);
    }

    private String encode512(String password, User user) {
        return shaPasswordEncoder512.encodePassword(password, user.getMetaData());
    }

    private String encode256(String password, User user) {
        return shaPasswordEncoder256.encodePassword(password, saltSource.getSalt(user));
    }
}