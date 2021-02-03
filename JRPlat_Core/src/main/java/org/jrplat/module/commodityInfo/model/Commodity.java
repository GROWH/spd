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

package org.jrplat.module.commodityInfo.model;

import org.jrplat.module.dictionary.model.DicItem;
import org.jrplat.platform.annotation.*;
import org.jrplat.platform.generator.ActionGenerator;
import org.jrplat.platform.model.SimpleModel;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@Entity
@Scope("prototype")
@Component
@XmlRootElement
@XmlType(name = "Commodity")
public class Commodity extends SimpleModel {
    @DisplayIgnore
    @ModelAttr("产品名称")
    @Column(length = 500)
    protected String productName;

    @DisplayIgnore
    @ModelAttr("规格型号")
    @Column(length = 500)
    protected String specification;

    @DisplayIgnore
    @ModelAttr("包装单位")
    @SupportLikeQuery
    @Column(length = 500)
    protected String packingUnit;

    @DisplayIgnore
    @ModelAttrNotNull
    @ModelAttr("包装数量")
    @Column(length = 500)
    protected String quantity;

    @DisplayIgnore
    @ModelAttrNotNull
    @ManyToOne
    @ModelAttr("编号格式")
    @SimpleDic("encodingFormat")
    protected DicItem encodingFormat;

    @DisplayIgnore
    @ModelAttrNotNull
    @ModelAttr("编号")
    @Column(length = 500)
    protected String number;

    @DisplayIgnore
    @ModelAttr("原分类编码")
    @Column(length = 500)
    protected String YFLNumber;

    @DisplayIgnore
    @ModelAttrNotNull
    @ModelAttr("分类编码")
    @Column(length = 500)
    protected String FLNumber;

    @DisplayIgnore
    @ModelAttrNotNull
    @ModelAttr("生产企业名称")
    protected String manufacturer;

    @DisplayIgnore
    @ModelAttr("注册证编号/备案凭证编码")
    @Column(length = 500)
    protected String ZCNumber;


    @XmlAttribute
    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    @XmlTransient
    public DicItem getEncodingFormat() {
        return encodingFormat;
    }

    public void setEncodingFormat(DicItem encodingFormat) {
        this.encodingFormat = encodingFormat;
    }

    @XmlAttribute
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @XmlAttribute
    public String getYFLNumber() {
        return YFLNumber;
    }

    public void setYFLNumber(String YFLNumber) {
        this.YFLNumber = YFLNumber;
    }

    @XmlAttribute
    public String getFLNumber() {
        return FLNumber;
    }

    public void setFLNumber(String FLNumber) {
        this.FLNumber = FLNumber;
    }

    @XmlAttribute
    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    @XmlAttribute
    public String getZCNumber() {
        return ZCNumber;
    }

    public void setZCNumber(String ZCNumber) {
        this.ZCNumber = ZCNumber;
    }
    @XmlAttribute
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
    @XmlAttribute
    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getPackingUnit() {
        return packingUnit;
    }

    public void setPackingUnit(String packingUnit) {
        this.packingUnit = packingUnit;
    }

    @Override
    public String getMetaData() {
        return "商品信息";
    }

    public static void main(String[] args) {
        Commodity obj = new Commodity();
//生成Action
        ActionGenerator.generate(obj.getClass());
    }
}