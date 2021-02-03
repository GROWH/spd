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

package org.jrplat.module.ordermanagement.model;

import org.jrplat.module.unitInfo.model.Unit;
import org.jrplat.platform.generator.ActionGenerator;
import org.jrplat.platform.model.SimpleModel;
import org.jrplat.platform.annotation.*;

import javax.persistence.*;
import java.util.Date;

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
@XmlType(name = "Outboundrecords")
public class Outboundrecords extends SimpleModel {

    @ManyToOne(fetch=FetchType.EAGER)
    @ModelAttr("细单")
    @ModelAttrRef("id")
    protected OrderInformation orderInformation;

    @DisplayIgnore
    @ModelAttr("数量")
    @Column(length = 500)
    protected String quantity;

    @DisplayIgnore
    @ModelAttr("批号")
    @Column(length = 500)
    protected String lotNumber;

    @DisplayIgnore
    @RenderDate
    @ModelAttr("出库时间")
    protected Date deliveryTime;


    @ManyToOne(fetch = FetchType.EAGER)
    @ModelAttr("供应商名称")
    @ModelAttrRef("paperworkNo")
    protected Unit unitNo;

    
    @Transient
    @FieldRef("orderInformation.orderQuantity")
    @ModelAttr("客户订货数量")
    protected Integer orderQuantity;

    @Transient
    @FieldRef("orderInformation.shipmentQuantity")
    @ModelAttr("供应商发货数量")
    protected Integer shipmentQuantity;

    @Transient
    @FieldRef("orderInformation.productNo.number")
    @ModelAttr("商品编号")
    protected String number;


    @Transient
    @FieldRef("orderInformation.productNo.productName")
    @ModelAttr("商品名称")
    protected String productName;


    @Transient
    @FieldRef("orderInformation.productNo.packingUnit")
    @ModelAttr("包装单位")
    protected String packagingUnit;

    @Transient
    @FieldRef("orderInformation.effectiveDate")
    @ModelAttr("有效期")
    protected Date effectiveDate;

    @XmlTransient
    public OrderInformation getOrderInformation() {
        return orderInformation;
    }

    public void setOrderInformation(OrderInformation orderInformation) {
        this.orderInformation = orderInformation;
    }

    @XmlAttribute
    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    @XmlAttribute
    public String getLotNumber() {
        return lotNumber;
    }

    public void setLotNumber(String lotNumber) {
        this.lotNumber = lotNumber;
    }

    @XmlAttribute
    public Date getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    @XmlTransient
    public Unit getUnitNo() {
        return unitNo;
    }

    public void setUnitNo(Unit unitNo) {
        this.unitNo = unitNo;
    }

    public String getPackagingUnit() {
        return packagingUnit;
    }

    public void setPackagingUnit(String packagingUnit) {
        this.packagingUnit = packagingUnit;
    }
    

    public Integer getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(Integer orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public Integer getShipmentQuantity() {
        return shipmentQuantity;
    }

    public void setShipmentQuantity(Integer shipmentQuantity) {
        this.shipmentQuantity = shipmentQuantity;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    @Override
    public String getMetaData() {
        return "订货信息";
    }

    public static void main(String[] args) {
        Outboundrecords obj = new Outboundrecords();
//生成Action
        ActionGenerator.generate(obj.getClass());
    }
}