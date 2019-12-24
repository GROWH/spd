/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.monitor.model;

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
public class MemoryState extends Model {
    /**
     * 服务器IP地址
     */
    @SupportLikeQuery
    @ModelAttr("服务器IP地址")
    protected String serverIP;

    @SupportLikeQuery
    @ModelAttr("应用系统名称")
    protected String appName;

    @SupportLikeQuery
    @Temporal(TemporalType.TIMESTAMP)
    @ModelAttr("记录时间")
    protected Date recordTime;

    @ModelAttr("最大可用内存")
    protected Float maxMemory;

    @ModelAttr("已分配内存")
    protected Float totalMemory;

    @ModelAttr("已释放内存")
    protected Float freeMemory;

    @ModelAttr("可用内存")
    protected Float usableMemory;

    public static void main(String[] args) {
        MemoryState obj = new MemoryState();
        //生成Action
        ActionGenerator.generate(obj.getClass());
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public Float getFreeMemory() {
        return freeMemory;
    }

    public void setFreeMemory(Float freeMemory) {
        this.freeMemory = freeMemory;
    }

    public Float getMaxMemory() {
        return maxMemory;
    }

    public void setMaxMemory(Float maxMemory) {
        this.maxMemory = maxMemory;
    }

    public Float getTotalMemory() {
        return totalMemory;
    }

    public void setTotalMemory(Float totalMemory) {
        this.totalMemory = totalMemory;
    }

    public Date getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }

    public Float getUsableMemory() {
        return usableMemory;
    }

    public void setUsableMemory(Float usableMemory) {
        this.usableMemory = usableMemory;
    }

    @Override
    public String getMetaData() {
        return "内存使用情况日志";
    }
}