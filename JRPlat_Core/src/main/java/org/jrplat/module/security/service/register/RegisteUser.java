/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.security.service.register;

import org.jrplat.module.security.model.User;
import org.jrplat.module.security.service.password.PasswordEncoder;
import org.jrplat.module.system.service.RegisterService;
import org.jrplat.platform.result.Page;
import org.jrplat.platform.util.XMLUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 注册用户数据，这里需要注意的是：
 * 因为User有一个Org字段和一个Role列表（protected Org org; protected List<Role> roles = new ArrayList<>();）
 * 所以要先注册了Org和Role之后才能注册用户
 * 但是RegisteUser、RegisteOrg以及RegisteRole都继承自RegisterService
 * 都实现了Spring的ApplicationListener接口
 * 那么Spring会先调用谁呢？
 * 为了保证先注册Org和Role
 * RegisteUser类用@Resource注解注入了RegisteOrg和RegisteRole，分别用来获取Org和Role
 * Spring保证会在装配完成RegisteOrg和RegisteRole之后才装配RegisteUser
 * 而装配的先后顺序也就是Spring调用实现ApplicationListener接口的Bean的顺序
 * 因此，这里的注册数据依赖问题就完美地解决了
 * @author 西安捷然
 */
@Service
public class RegisteUser extends RegisterService<User> {
    @Resource(name = "registeOrg")
    protected RegisteOrg registeOrg;
    @Resource(name = "registeRole")
    protected RegisteRole registeRole;
    @Resource(name = "passwordEncoder")
    private PasswordEncoder passwordEncoder;

    @Override
    protected void registe() {
        String xml = "/data/user.xml";
        LOG.info("注册【" + xml + "】文件");
        LOG.info("验证【" + xml + "】文件");
        boolean pass = XMLUtils.validateXML(xml);
        if (!pass) {
            LOG.info("验证没有通过，请参考dtd文件");
            return;
        }
        LOG.info("验证通过");
        Page<User> page = Page.newInstance(User.class, RegisteUser.class.getResourceAsStream(xml));
        if (page != null) {
            for (User user : page.getModels()) {
                user.setPassword(passwordEncoder.encode(user.getPassword(), user));
                user.setOrg(registeOrg.getRegisteData().get(0));
                user.addRole(registeRole.getRegisteData().get(0).getChild().get(0));
                serviceFacade.create(user);
            }
        }
    }
}