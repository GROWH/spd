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
public class IndexLog extends Model {
    @SupportLikeQuery
    @ModelAttr("登录IP地址")
    protected String loginIP;
    @SupportLikeQuery
    @ModelAttr("服务器IP地址")
    protected String serverIP;
    @SupportLikeQuery
    @ModelAttr("应用系统名称")
    protected String appName;
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
    protected Long processTime;
    @SupportLikeQuery
    @ModelAttr("操作结果")
    protected String operatingResult;
    //用户名不分词
    @SupportLikeQuery
    @ModelAttr("用户名")
    protected String username;

    public static void main(String[] args) {
        IndexLog obj = new IndexLog();
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

    public String getOperatingResult() {
        return operatingResult;
    }

    public void setOperatingResult(String operatingResult) {
        this.operatingResult = operatingResult;
    }

    public Long getProcessTime() {
        return processTime;
    }

    public void setProcessTime(Long processTime) {
        this.processTime = processTime;
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

    public String getLoginIP() {
        return loginIP;
    }

    public void setLoginIP(String loginIP) {
        this.loginIP = loginIP;
    }

    @Override
    public String getMetaData() {
        return "重建索引日志";
    }
}