/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.platform.model.handler;

import org.jrplat.platform.model.Model;

/**
 * 模型事件处理接口
 * @author 西安捷然
 */
public abstract class ModelHandler {
    public void prePersist(Model model) {
    }

    public void postPersist(Model model) {
    }

    public void preRemove(Model model) {
    }

    public void postRemove(Model model) {
    }

    public void preUpdate(Model model) {
    }

    public void postUpdate(Model model) {
    }

    public void postLoad(Model model) {
    }
}
