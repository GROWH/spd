/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.platform.model.handler;

import org.jrplat.module.security.model.User;
import org.jrplat.module.security.service.UserHolder;
import org.jrplat.platform.annotation.IgnoreUser;
import org.jrplat.platform.log.JRPlatLogger;
import org.jrplat.platform.log.JRPlatLoggerFactory;
import org.jrplat.platform.model.Model;
import org.jrplat.platform.model.ModelListener;
import org.jrplat.platform.model.SimpleModel;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;

/**
 * 辅助模型处理器
 * @author 西安捷然
 */
@Service
public class AidModelHandler extends ModelHandler {
    private static final JRPlatLogger LOG = JRPlatLoggerFactory.getJRPlatLogger(AidModelHandler.class);

    /**
     * 注册模型处理器
     */
    @PostConstruct
    public void init() {
        ModelListener.addModelHandler(this);
    }

    /**
     * 设置数据的拥有者
     * 设置创建时间
     * @param model
     */
    @Override
    public void prePersist(Model model) {
        User user = UserHolder.getCurrentLoginUser();
        if (model instanceof SimpleModel) {
            SimpleModel simpleModel = (SimpleModel) model;
            if (user != null && simpleModel.getOwnerUser() == null && !model.getClass().isAnnotationPresent(IgnoreUser.class)) {
                //设置数据的拥有者
                simpleModel.setOwnerUser(user);
                LOG.debug("设置模型" + model + "的拥有者为:" + user.getUsername());
            }
        }
        //设置创建时间
        model.setCreateTime(new Date());
        LOG.debug("设置模型" + model + "的创建时间");
    }

    /**
     * 设置更新时间
     * @param model
     */
    @Override
    public void preUpdate(Model model) {
        //设置更新时间
        model.setUpdateTime(new Date());
        LOG.debug("设置模型" + model + "的更新时间");
    }
}
