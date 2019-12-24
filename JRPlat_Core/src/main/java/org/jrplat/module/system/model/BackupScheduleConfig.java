/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.system.model;

import org.jrplat.platform.annotation.Database;
import org.jrplat.platform.annotation.IgnoreBusinessLog;
import org.jrplat.platform.annotation.ModelAttr;
import org.jrplat.platform.model.SimpleModel;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;

@Entity
@Scope("prototype")
@Component
@IgnoreBusinessLog
@Database
public class BackupScheduleConfig extends SimpleModel {
    @ModelAttr("定时备份的小时（0-23）")
    protected int scheduleHour = 2;
    @ModelAttr("定时备份的分钟（0-59）")
    protected int scheduleMinute = 2;
    @ModelAttr("是否启用定时备份")
    protected boolean enabled = true;

    public int getScheduleHour() {
        return scheduleHour;
    }

    public void setScheduleHour(int scheduleHour) {
        this.scheduleHour = scheduleHour;
    }

    public int getScheduleMinute() {
        return scheduleMinute;
    }

    public void setScheduleMinute(int scheduleMinute) {
        this.scheduleMinute = scheduleMinute;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String getMetaData() {
        return "定时备份数据配置";
    }
}