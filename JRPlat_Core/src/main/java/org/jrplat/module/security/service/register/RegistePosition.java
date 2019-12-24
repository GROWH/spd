/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.security.service.register;

import org.jrplat.module.security.model.Position;
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
public class RegistePosition extends RegisterService<Position> {
    private Position position = null;

    @Override
    public void registe() {
        String xml = "/data/position.xml";
        LOG.info("注册【" + xml + "】文件");
        LOG.info("验证【" + xml + "】文件");
        boolean pass = XMLUtils.validateXML(xml);
        if (!pass) {
            LOG.info("验证没有通过，请参考dtd文件");
            return;
        }
        LOG.info("验证通过");
        XMLFactory factory = new XMLFactory(Position.class);
        position = factory.unmarshal(RegistePosition.class.getResourceAsStream(xml));

        assemblePosition(position);
        registePosition(position);
    }

    @Override
    protected List<Position> getRegisteData() {
        ArrayList<Position> data = new ArrayList<>();
        data.add(position);
        return data;
    }

    private void assemblePosition(Position position) {
        for (Position child : position.getChild()) {
            child.setParent(position);
            assemblePosition(child);
        }
    }

    private void registePosition(Position position) {
        serviceFacade.create(position);
    }
}