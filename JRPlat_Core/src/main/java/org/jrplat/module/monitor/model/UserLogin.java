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

import javax.persistence.Column;
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
public class UserLogin extends Model {
    @SupportLikeQuery
    @ModelAttr("登录IP地址")
    protected String loginIP;
    @SupportLikeQuery
    @ModelAttr("用户代理")
    @Column(length = 2550)
    protected String userAgent;
    @ModelAttr("服务器IP地址")
    protected String serverIP;
    @ModelAttr("应用系统名称")
    protected String appName;
    @SupportLikeQuery
    @Temporal(TemporalType.TIMESTAMP)
    @ModelAttr("登录时间")
    protected Date loginTime;
    @SupportLikeQuery
    @Temporal(TemporalType.TIMESTAMP)
    @ModelAttr("注销时间")
    protected Date logoutTime;
    //单位为毫秒
    @SupportLikeQuery
    @ModelAttr("用户在线时间")
    protected Long onlineTime;
    //用户名不分词
    @SupportLikeQuery
    @ModelAttr("用户名")
    protected String username;

    public static void main(String[] args) {
        UserLogin obj = new UserLogin();
        //生成Action
        ActionGenerator.generate(obj.getClass());
    }

    public String getOnlineTimeStr() {
        return ConvertUtils.getTimeDes(onlineTime);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(Long onlineTime) {
        this.onlineTime = onlineTime;
    }

    public String getLoginIP() {
        return loginIP;
    }

    public void setLoginIP(String loginIP) {
        this.loginIP = loginIP;
    }

    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public Date getLogoutTime() {
        return logoutTime;
    }

    public void setLogoutTime(Date logoutTime) {
        this.logoutTime = logoutTime;
    }

    @Override
    public String getMetaData() {
        return "用户登陆注销日志";
    }
}