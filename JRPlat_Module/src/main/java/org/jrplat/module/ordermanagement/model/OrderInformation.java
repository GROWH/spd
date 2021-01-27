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

import org.jrplat.module.commodityInfo.model.Commodity;
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
import javax.xml.bind.annotation.XmlType;

@Entity
@Scope("prototype")
@Component
@XmlRootElement
@XmlType(name = "OrderInformation")
public class OrderInformation extends SimpleModel {

    @DisplayIgnore
    @ModelAttrNotNull
    @ModelAttr("行号")
    protected Integer lineNumber;


    @ManyToOne(fetch = FetchType.LAZY)
    @ModelAttr("产品编号")
    @ModelAttrRef("number")
    protected Commodity productNo;

    @DisplayIgnore
    @ModelAttr("产品名称")
    @Column(length = 500)
    protected String productName;

    @DisplayIgnore
    @ModelAttr("规格/型号")
    @Column(length = 500)
    protected String specifications;

    @DisplayIgnore
    @ModelAttr("包装单位")
    @Column(length = 500)
    protected String packagingUnit;

    @DisplayIgnore
    @ModelAttr("生产企业名称")
    @Column(length = 500)
    protected String manufacturer;

    @DisplayIgnore
    @ModelAttr("客户订货/退货数量")
    protected Integer orderQuantity;

    @DisplayIgnore
    @ModelAttr("供应商发货数量")
    protected Integer shipmentQuantity;

    @DisplayIgnore
    @ModelAttr("批号")
    @Column(length = 500)
    protected String PH;

    @DisplayIgnore
    @ModelAttr("序列号")
    @Column(length = 500)
    protected String serialNumber;

    @DisplayIgnore
    @RenderDate
    @Temporal(TemporalType.TIMESTAMP)
    @ModelAttr("生产日期")
    protected Date manufacturerDate;

    @DisplayIgnore
    @RenderDate
    @Temporal(TemporalType.TIMESTAMP)
    @ModelAttr("有效期/失效期")
    protected Date effectiveDate;

    @DisplayIgnore
    @ModelAttr("客户产品备注")
    @Column(length = 500)
    protected String KHRemarks;

    @DisplayIgnore
    @ModelAttr("供应商产品备注")
    @Column(length = 500)
    protected String GYSRemarks;

    @ManyToOne(fetch = FetchType.LAZY)
    @ModelAttr("总单ID")
    @ModelAttrRef("id")
    protected OrderManagement orderManagement;

    public OrderManagement getOrderManagement() {
        return orderManagement;
    }

    public void setOrderManagement(OrderManagement orderManagement) {
        this.orderManagement = orderManagement;
    }

    @XmlAttribute
    public Integer getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }

    public Commodity getProductNo() {
        return productNo;
    }

    public void setProductNo(Commodity productNo) {
        this.productNo = productNo;
    }
    
    @XmlAttribute
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @XmlAttribute
    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    @XmlAttribute
    public String getPackagingUnit() {
        return packagingUnit;
    }

    public void setPackagingUnit(String packagingUnit) {
        this.packagingUnit = packagingUnit;
    }

    @XmlAttribute
    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    @XmlAttribute
    public Integer getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(Integer orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    @XmlAttribute
    public Integer getShipmentQuantity() {
        return shipmentQuantity;
    }

    public void setShipmentQuantity(Integer shipmentQuantity) {
        this.shipmentQuantity = shipmentQuantity;
    }

    @XmlAttribute
    public String getPH() {
        return PH;
    }

    public void setPH(String PH) {
        this.PH = PH;
    }

    @XmlAttribute
    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    @XmlAttribute
    public Date getManufacturerDate() {
        return manufacturerDate;
    }

    public void setManufacturerDate(Date manufacturerDate) {
        this.manufacturerDate = manufacturerDate;
    }

    @XmlAttribute
    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    @XmlAttribute
    public String getKHRemarks() {
        return KHRemarks;
    }

    public void setKHRemarks(String KHRemarks) {
        this.KHRemarks = KHRemarks;
    }

    @XmlAttribute
    public String getGYSRemarks() {
        return GYSRemarks;
    }

    public void setGYSRemarks(String GYSRemarks) {
        this.GYSRemarks = GYSRemarks;
    }

    @Override
    public String getMetaData() {
        return "订货信息";
    }

    public static void main(String[] args) {
        OrderInformation obj = new OrderInformation();
//生成Action
        ActionGenerator.generate(obj.getClass());
    }
}