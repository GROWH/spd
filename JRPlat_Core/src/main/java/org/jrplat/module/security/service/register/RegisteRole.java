/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.security.service.register;

import org.jrplat.module.security.model.Role;
import org.jrplat.module.system.service.RegisterService;
import org.jrplat.platform.util.XMLFactory;
import org.jrplat.platform.util.XMLUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author 西安捷然
 */
@Service
public class RegisteRole extends RegisterService<Role> {
    private Role role = null;

    @Override
    public void registe() {
        String xml = "/data/role.xml";
        LOG.info("注册【" + xml + "】文件");
        LOG.info("验证【" + xml + "】文件");
        boolean pass = XMLUtils.validateXML(xml);
        if (!pass) {
            LOG.info("验证没有通过，请参考dtd文件");
            return;
        }
        LOG.info("验证通过");
        XMLFactory factory = new XMLFactory(Role.class);
        role = factory.unmarshal(RegisteRole.class.getResourceAsStream(xml));

        assembleRole(role);
        registeRole(role);
    }

    @Override
    protected List<Role> getRegisteData() {
        ArrayList<Role> data = new ArrayList<Role>();
        data.add(role);
        return data;
    }

    private void assembleRole(Role role) {
        for (Role child : role.getChild()) {
            child.setParent(role);
            assembleRole(child);
        }
    }

    private void registeRole(Role role) {
        serviceFacade.create(role);
    }
}