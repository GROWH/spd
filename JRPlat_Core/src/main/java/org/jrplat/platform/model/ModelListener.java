/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.platform.model;

import org.jrplat.platform.log.JRPlatLogger;
import org.jrplat.platform.log.JRPlatLoggerFactory;
import org.jrplat.platform.model.handler.ModelHandler;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

/**
 * 模型监听事件调度器
 * 可注册与反注册多个ModelHandler的实现
 * 相应事件发生的时候，改调度器负责转发给所有注册的ModelHandler
 * @author 西安捷然
 *
 */
public class ModelListener {
    private static final JRPlatLogger LOG = JRPlatLoggerFactory.getJRPlatLogger(ModelListener.class);
    private static final List<ModelHandler> modelHandlers = new LinkedList<>();

    public static void addModelHandler(ModelHandler modelHandler) {
        LOG.info("注册模型事件处理器：" + modelHandler.getClass().getName());
        modelHandlers.add(modelHandler);
    }

    public static void removeModelHandler(ModelHandler modelHandler) {
        LOG.info("移除模型事件处理器：" + modelHandler.getClass().getName());
        modelHandlers.remove(modelHandler);
    }

    @PrePersist
    public void prePersist(Model model) {
        for (ModelHandler modelHandler : modelHandlers) {
            modelHandler.prePersist(model);
        }
    }

    @PostPersist
    public void postPersist(Model model) {
        for (ModelHandler modelHandler : modelHandlers) {
            modelHandler.postPersist(model);
        }
    }

    @PreRemove
    public void preRemove(Model model) {
        for (ModelHandler modelHandler : modelHandlers) {
            modelHandler.preRemove(model);
        }
    }

    @PostRemove
    public void postRemove(Model model) {
        for (ModelHandler modelHandler : modelHandlers) {
            modelHandler.postRemove(model);
        }
    }

    @PreUpdate
    public void preUpdate(Model model) {
        for (ModelHandler modelHandler : modelHandlers) {
            modelHandler.preUpdate(model);
        }
    }

    @PostUpdate
    public void postUpdate(Model model) {
        for (ModelHandler modelHandler : modelHandlers) {
            modelHandler.postUpdate(model);
        }
    }

    @PostLoad
    public void postLoad(Model model) {
        for (ModelHandler modelHandler : modelHandlers) {
            modelHandler.postLoad(model);
        }
    }
}