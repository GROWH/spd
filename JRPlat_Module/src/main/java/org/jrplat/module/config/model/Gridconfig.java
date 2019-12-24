package org.jrplat.module.config.model;

import org.jrplat.platform.annotation.ModelAttr;
import org.jrplat.platform.model.SimpleModel;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created by 赵腾飞 on 2016/10/12.
 */
@Entity
@Scope("prototype")
@Component
public class Gridconfig extends SimpleModel {

    @ModelAttr("表格设置")
    @Column(length = 20000)
    protected String gridSetting;

    @ModelAttr("哪个表格")
    protected String whichGrid;

    protected Integer size;


    public String getGridSetting() {
        return gridSetting;
    }

    public void setGridSetting(String gridSetting) {
        this.gridSetting = gridSetting;
    }

    public String getWhichGrid() {
        return whichGrid;
    }

    public void setWhichGrid(String whichGrid) {
        this.whichGrid = whichGrid;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    @Override
    public String getMetaData() {
        return "表格设置";
    }
}
