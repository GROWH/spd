/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.dictionary.model;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.jrplat.platform.annotation.Database;
import org.jrplat.platform.annotation.ModelAttr;
import org.jrplat.platform.annotation.ModelAttrRef;
import org.jrplat.platform.generator.ActionGenerator;
import org.jrplat.platform.model.SimpleModel;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 *数据字典项
 * @author 西安捷然
 */
@Entity
@Scope("prototype")
@Component
@XmlType(name = "DicItem")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Database
public class DicItem extends SimpleModel {

    @ManyToOne
    @ModelAttr("数据字典")
    @ModelAttrRef("chinese")
    protected Dic dic;
    @ModelAttr("编码")
    protected String code;
    @ModelAttr("名称")
    protected String name;
    @ModelAttr("排序号")
    protected int orderNum;

    public static void main(String[] args) {
        DicItem obj = new DicItem();
        //生成Action
        ActionGenerator.generate(obj.getClass());
    }

    @XmlAttribute
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @XmlAttribute
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlAttribute
    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    @XmlTransient
    public Dic getDic() {
        return dic;
    }

    public void setDic(Dic dic) {
        this.dic = dic;
    }

    @Override
    public String getMetaData() {
        return "数据字典项";
    }
}