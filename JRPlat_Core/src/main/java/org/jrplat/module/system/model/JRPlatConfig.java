/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.system.model;

import org.jrplat.platform.annotation.ModelAttr;
import org.jrplat.platform.model.SimpleModel;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Scope("prototype")
@Table(name = "JRPlatConfig",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"configKey"})})
@Component
public class JRPlatConfig extends SimpleModel {
    @ModelAttr("属性名称")
    protected String configKey;
    @ModelAttr("属性值")
    protected String configValue;

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    @Override
    public String getMetaData() {
        return "系统配置信息";
    }
}