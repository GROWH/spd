/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.monitor.model;

import org.jrplat.platform.annotation.*;
import org.jrplat.platform.generator.ActionGenerator;
import org.jrplat.platform.model.Model;
import org.jrplat.platform.util.ConvertUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Date;

/**
 *
 *不需要保存该模型的增删改日志
 * 不需要自动设置模型的添加用户
 * @author 西安捷然
 */
@Entity
@Table(name = "ProcessTimeTable")
@Scope("prototype")
@Component
@IgnoreBusinessLog
@IgnoreUser
@Database("log")
public class ProcessTime extends Model {
    @SupportLikeQuery
    @ModelAttr("用户IP地址")
    protected String userIP;
    @ModelAttr("服务器IP地址")
    protected String serverIP;
    @ModelAttr("应用系统名称")
    protected String appName;
    @SupportLikeQuery
    @ModelAttr("资源路径")
    @Column(name = "resourceField")
    protected String resource;
    @SupportLikeQuery
    @Temporal(TemporalType.TIMESTAMP)
    @ModelAttr("开始处理时间")
    protected Date startTime;
    @SupportLikeQuery
    @Temporal(TemporalType.TIMESTAMP)
    @ModelAttr("处理完成时间")
    protected Date endTime;
    //单位为毫秒
    @ModelAttr("操作耗时")
    @Column(name = "processTimeField")
    protected Long processTime;
    //用户名不分词
    @SupportLikeQuery
    @ModelAttr("用户名")
    protected String username;

    public static void main(String[] args) {
        ProcessTime obj = new ProcessTime();
        //生成Action
        ActionGenerator.generate(obj.getClass());
    }

    public String getProcessTimeStr() {
        return ConvertUtils.getTimeDes(processTime);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getUserIP() {
        return userIP;
    }

    public void setUserIP(String userIP) {
        this.userIP = userIP;
    }

    public Long getProcessTime() {
        return processTime;
    }

    public void setProcessTime(Long processTime) {
        this.processTime = processTime;
    }

    @Override
    public String getMetaData() {
        return "请求处理时间日志";
    }
}