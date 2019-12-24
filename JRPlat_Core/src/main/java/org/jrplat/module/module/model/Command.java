/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.module.model;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.jrplat.module.module.service.ModuleService;
import org.jrplat.platform.annotation.Database;
import org.jrplat.platform.annotation.ModelAttr;
import org.jrplat.platform.annotation.ModelAttrRef;
import org.jrplat.platform.model.SimpleModel;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 *命令对象
 * @author 西安捷然
 */
@Entity
@Scope("prototype")
@Component
@XmlType(name = "Command")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Database
public class Command extends SimpleModel {

    @ManyToOne
    @ModelAttr("所属模块")
    @ModelAttrRef("chinese")
    protected Module module;
    @ModelAttr("命令英文名称")
    protected String english;
    @ModelAttr("命令中文名称")
    protected String chinese;
    @ModelAttr("链接地址")
    protected String url;
    @ModelAttr("专属用户名")
    protected String username;
    @ModelAttr("排序号")
    protected int orderNum;
    @ModelAttr("是否显示")
    protected boolean display = true;

    @XmlTransient
    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    @XmlAttribute
    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    @XmlAttribute
    public String getChinese() {
        return chinese;
    }

    public void setChinese(String chinese) {
        this.chinese = chinese;
    }

    @XmlAttribute
    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    @XmlAttribute
    public String getUrl() {
        String result = "";
        if (url == null) {
            result = "../platform/" + ModuleService.getModulePath(this.getModule()) + this.getEnglish() + ".jsp";
        } else {
            result = url;
        }
        return result;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @XmlAttribute
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @XmlAttribute
    public boolean isDisplay() {
        return display;
    }

    public void setDisplay(boolean display) {
        this.display = display;
    }

    @Override
    public String getMetaData() {
        return "命令信息";
    }
}