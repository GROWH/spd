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

import org.jrplat.module.dictionary.model.DicItem;
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
@XmlType(name = "OrderManagement")
public class OrderManagement extends SimpleModel {

    @DisplayIgnore
    @ModelAttrNotNull
    @ModelAttr("平台流水号")
    @Column(length = 500)
    protected String serialNumber;


    @DisplayIgnore
    @ModelAttrNotNull
    @ManyToOne
    @ModelAttr("订单类型")
    @SimpleDic("ordertype")
    protected DicItem ordertype;


    @ManyToOne(fetch = FetchType.LAZY)
    @ModelAttr("供应商名称")
    @ModelAttrRef("unitName")
    protected Unit GYSName;

    @DisplayIgnore
    @ModelAttr("供应商证件编号")
    @Column(length = 500)
    protected String GYSNumber;

    @DisplayIgnore
    @RenderDate
    @Temporal(TemporalType.TIMESTAMP)
    @ModelAttr("供应商发货日期")
    protected Date GYSDate;

    @DisplayIgnore
    @ModelAttr("供应商发货单号")
    @Column(length = 500)
    protected String GYSInvoiceNo;

    @DisplayIgnore
    @ModelAttr("供应商发货物流公司")
    @Column(length = 500)
    protected String GYSLogistics;

    @DisplayIgnore
    @ModelAttr("供应商发货物流单号")
    @Column(length = 500)
    protected String GYSLogisticsNo;

    @DisplayIgnore
    @ModelAttr("供应商发货备注")
    @Column(length = 500)
    protected String GYSRemarks;


    @ManyToOne(fetch = FetchType.LAZY)
    @ModelAttr("客户名称")
    @ModelAttrRef("unitName")
    protected Unit KHName;

    @DisplayIgnore
    @ModelAttr("客户证件编号")
    @Column(length = 500)
    protected String KHNumber;

    @DisplayIgnore
    @RenderDate
    @Temporal(TemporalType.TIMESTAMP)
    @ModelAttr("客户订单日期")
    protected Date KHDate;

    @DisplayIgnore
    @ModelAttr("客户订单单号")
    @Column(length = 500)
    protected String KHInvoiceNo;

    @DisplayIgnore
    @ModelAttr("客户退货物流公司")
    @Column(length = 500)
    protected String KHTHLogistics;

    @DisplayIgnore
    @ModelAttr("客户退货物流单号")
    @Column(length = 500)
    protected String KHTHLogisticsNo;

    @DisplayIgnore
    @ModelAttr("客户订单备注")
    @Column(length = 500)
    protected String KHRemarks;


    @ManyToOne
    @ModelAttr("订单状态")
    @SimpleDic("orderStatus")
    protected DicItem orderStatus;

    @XmlAttribute
    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public DicItem getOrdertype() {
        return ordertype;
    }

    public void setOrdertype(DicItem ordertype) {
        this.ordertype = ordertype;
    }


    @XmlAttribute
    public String getGYSNumber() {
        return GYSNumber;
    }

    public void setGYSNumber(String GYSNumber) {
        this.GYSNumber = GYSNumber;
    }

    @XmlAttribute
    public Date getGYSDate() {
        return GYSDate;
    }

    public void setGYSDate(Date GYSDate) {
        this.GYSDate = GYSDate;
    }

    @XmlAttribute
    public String getGYSInvoiceNo() {
        return GYSInvoiceNo;
    }

    public void setGYSInvoiceNo(String GYSInvoiceNo) {
        this.GYSInvoiceNo = GYSInvoiceNo;
    }

    @XmlAttribute
    public String getGYSLogistics() {
        return GYSLogistics;
    }

    public void setGYSLogistics(String GYSLogistics) {
        this.GYSLogistics = GYSLogistics;
    }

    @XmlAttribute
    public String getGYSLogisticsNo() {
        return GYSLogisticsNo;
    }

    public void setGYSLogisticsNo(String GYSLogisticsNo) {
        this.GYSLogisticsNo = GYSLogisticsNo;
    }

    @XmlAttribute
    public String getGYSRemarks() {
        return GYSRemarks;
    }

    public void setGYSRemarks(String GYSRemarks) {
        this.GYSRemarks = GYSRemarks;
    }


    @XmlAttribute
    public String getKHNumber() {
        return KHNumber;
    }

    public void setKHNumber(String KHNumber) {
        this.KHNumber = KHNumber;
    }

    @XmlAttribute
    public Date getKHDate() {
        return KHDate;
    }

    public void setKHDate(Date KHDate) {
        this.KHDate = KHDate;
    }

    @XmlAttribute
    public String getKHInvoiceNo() {
        return KHInvoiceNo;
    }

    public void setKHInvoiceNo(String KHInvoiceNo) {
        this.KHInvoiceNo = KHInvoiceNo;
    }

    @XmlAttribute
    public String getKHTHLogistics() {
        return KHTHLogistics;
    }

    public void setKHTHLogistics(String KHTHLogistics) {
        this.KHTHLogistics = KHTHLogistics;
    }

    @XmlAttribute
    public String getKHTHLogisticsNo() {
        return KHTHLogisticsNo;
    }

    public void setKHTHLogisticsNo(String KHTHLogisticsNo) {
        this.KHTHLogisticsNo = KHTHLogisticsNo;
    }

    @XmlAttribute
    public String getKHRemarks() {
        return KHRemarks;
    }

    public void setKHRemarks(String KHRemarks) {
        this.KHRemarks = KHRemarks;
    }

    public Unit getGYSName() {
        return GYSName;
    }

    public void setGYSName(Unit GYSName) {
        this.GYSName = GYSName;
    }

    public Unit getKHName() {
        return KHName;
    }

    public void setKHName(Unit KHName) {
        this.KHName = KHName;
    }

    public DicItem getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(DicItem orderStatus) {
        this.orderStatus = orderStatus;
    }


    @Override
    public String getMetaData() {
        return "订单信息";
    }

    public static void main(String[] args) {
        OrderManagement obj = new OrderManagement();
//生成Action
        ActionGenerator.generate(obj.getClass());
    }
}