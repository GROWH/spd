/**
 * JRPlat - Application Product Development Platform
 * Copyright (c) 2013, 杨尚川, yang-shangchuan@qq.com
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see
 * <http://www.gnu.org/licenses/>.
 */

package org.jrplat.module.unitInfo.model;

import org.jrplat.platform.generator.ActionGenerator;
import org.jrplat.platform.model.SimpleModel;
import org.jrplat.platform.annotation.*;
import org.jrplat.module.dictionary.model.DicItem;

import javax.persistence.*;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@Entity
@Scope("prototype")
@Component
@XmlRootElement
@XmlType(name = "Unit")
public class Unit extends SimpleModel {

    @DisplayIgnore
    @ModelAttrNotNull
    @ModelAttr("单位名称")
    @Column(length = 500)
    protected String unitName;

    @DisplayIgnore
    @ModelAttrNotNull
    @ManyToOne
    @ModelAttr("单位类型")
    @SimpleDic("unitType")
    protected DicItem unitType;

    @DisplayIgnore
    @ModelAttrNotNull
    @ModelAttr("证件编号")
    @Column(length = 500)
    protected String paperworkNo;

    @DisplayIgnore
    @ModelAttrNotNull
    @ModelAttr("联系人")
    @Column(length = 500)
    protected String linkman;

    @DisplayIgnore
    @ModelAttrNotNull
    @ModelAttr("联系人电话")
    @Column(length = 500)
    protected String contactPhone;

    @DisplayIgnore
    @ModelAttrNotNull
    @ModelAttr("订单获取密码")
    @Column(length = 500)
    protected String orderKey;

    @DisplayIgnore
    @ModelAttr("统一社会信用代码")
    @Column(length = 500)
    protected String enSHDM;

    @DisplayIgnore
    @ModelAttr("法人")
    @Column(length = 500)
    protected String legalPerson;

    @DisplayIgnore
    @ModelAttr("经营地址")
    @Column(length = 500)
    protected String businessAddress;

    @DisplayIgnore
    @ModelAttr("仓库地址")
    @Column(length = 500)
    protected String StorehouseAddress;


    @XmlAttribute
    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    @XmlTransient
    public DicItem getUnitType() {
        return unitType;
    }

    public void setUnitType(DicItem unitType) {
        this.unitType = unitType;
    }

    @XmlAttribute
    public String getPaperworkNo() {
        return paperworkNo;
    }

    public void setPaperworkNo(String paperworkNo) {
        this.paperworkNo = paperworkNo;
    }

    @XmlAttribute
    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    @XmlAttribute
    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    @XmlAttribute
    public String getOrderKey() {
        return orderKey;
    }

    public void setOrderKey(String orderKey) {
        this.orderKey = orderKey;
    }

    @XmlAttribute
    public String getEnSHDM() {
        return enSHDM;
    }

    public void setEnSHDM(String enSHDM) {
        this.enSHDM = enSHDM;
    }

    @XmlAttribute
    public String getLegalPerson() {
        return legalPerson;
    }

    public void setLegalPerson(String legalPerson) {
        this.legalPerson = legalPerson;
    }

    @XmlAttribute
    public String getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }

    @XmlAttribute
    public String getStorehouseAddress() {
        return StorehouseAddress;
    }

    public void setStorehouseAddress(String StorehouseAddress) {
        this.StorehouseAddress = StorehouseAddress;
    }

    @Override
    public String getMetaData() {
        return "单位信息";
    }

    public static void main(String[] args) {
        Unit obj = new Unit();
//生成Action
        ActionGenerator.generate(obj.getClass());
    }
}