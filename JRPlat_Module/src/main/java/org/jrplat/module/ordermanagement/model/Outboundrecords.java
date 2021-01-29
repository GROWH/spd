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

    @DisplayIgnore
    @ManyToOne
    @ModelAttr("细单")
    @ModelAttrRef("lineNumber")
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
    @Temporal(TemporalType.TIMESTAMP)
    @ModelAttr("出库时间")
    protected Date deliveryTime;


    @ManyToOne(fetch = FetchType.LAZY)
    @ModelAttr("供应商名称")
    @ModelAttrRef("paperworkNo")
    protected Unit unitNo;


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


    public Unit getUnitNo() {
        return unitNo;
    }

    public void setUnitNo(Unit unitNo) {
        this.unitNo = unitNo;
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