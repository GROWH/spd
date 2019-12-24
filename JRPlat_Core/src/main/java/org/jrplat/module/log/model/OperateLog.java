/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.log.model;

import org.jrplat.platform.annotation.*;
import org.jrplat.platform.generator.ActionGenerator;
import org.jrplat.platform.model.Model;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 *
 *不需要保存该模型的增删改日志
 * 不需要自动设置模型的添加用户
 * @author 西安捷然
 */
@Entity
@Scope("prototype")
@Component
@IgnoreBusinessLog
@IgnoreUser
@Database("log")
public class OperateLog extends Model {
    @SupportLikeQuery
    @ModelAttr("登录IP地址")
    protected String loginIP;
    @ModelAttr("服务器IP地址")
    protected String serverIP;
    @ModelAttr("应用系统名称")
    protected String appName;

    @SupportLikeQuery
    @Temporal(TemporalType.TIMESTAMP)
    @ModelAttr("操作时间")
    protected Date operatingTime;
    @SupportLikeQuery
    @ModelAttr("操作类型")
    protected String operatingType;
    @SupportLikeQuery
    @ModelAttr("操作模型")
    protected String operatingModel;
    @SupportLikeQuery
    @ModelAttr("操作ID")
    protected Integer operatingID;

    //用户名不分词
    @SupportLikeQuery
    @ModelAttr("用户名")
    protected String username;

    public static void main(String[] args) {
        OperateLog obj = new OperateLog();
        //生成Action
        ActionGenerator.generate(obj.getClass());
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getOperatingID() {
        return operatingID;
    }

    public void setOperatingID(Integer operatingID) {
        this.operatingID = operatingID;
    }

    public String getOperatingModel() {
        return operatingModel;
    }

    public void setOperatingModel(String operatingModel) {
        this.operatingModel = operatingModel;
    }

    public Date getOperatingTime() {
        return operatingTime;
    }

    public void setOperatingTime(Date operatingTime) {
        this.operatingTime = operatingTime;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getLoginIP() {
        return loginIP;
    }

    public void setLoginIP(String loginIP) {
        this.loginIP = loginIP;
    }

    public String getOperatingType() {
        return operatingType;
    }

    public void setOperatingType(String operatingType) {
        this.operatingType = operatingType;
    }

    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    @Override
    public String getMetaData() {
        return "业务操作日志";
    }
}