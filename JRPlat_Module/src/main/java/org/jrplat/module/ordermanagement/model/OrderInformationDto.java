package org.jrplat.module.ordermanagement.model;

import org.jrplat.platform.annotation.FieldRef;
import org.jrplat.platform.annotation.ModelAttr;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Date;

/**
 * @author liurch
 * @create 2021-01-29 下午6:13
 */
@Entity
@Scope("prototype")
@Component
@XmlRootElement
@XmlType(name = "OrderManagement")
public class OrderInformationDto {

    @Transient
    @FieldRef("id")
    @ModelAttr("细单id")
    private Integer id;


    @Transient
    @FieldRef("number")
    @ModelAttr("商品编号")
    private String number;


    @Transient
    @FieldRef("productName")
    @ModelAttr("商品名称")
    private String productName;

    @Transient
    @FieldRef("specifications")
    @ModelAttr("规格型号")
    private String specifications;


    @Transient
    @FieldRef("packingUnit")
    @ModelAttr("基本单位")
    private String packingUnit;


    @Transient
    @FieldRef("manufacturer")
    @ModelAttr("生产企业名称")
    private String manufacturer;

    @Transient
    @FieldRef("ZCNumber")
    @ModelAttr("注册证编号/备案凭证编码")
    private String ZCNumber;


    @Transient
    @FieldRef("orderQuantity")
    @ModelAttr("客户数量")
    private String orderQuantity;


    @Transient
    @FieldRef("packingUnit")
    @ModelAttr("供应商数量")
    private String shipmentQuantity;


    @Transient
    @FieldRef("ph")
    @ModelAttr("ph")
    private String ph;

    @Transient
    @FieldRef("effectiveDate")
    @ModelAttr("有效期")
    private Date effectiveDate;


    @Transient
    @FieldRef("manufacturerDate")
    @ModelAttr("生产日期")
    private Date manufacturerDate;


    @Transient
    @FieldRef("KHRemarks")
    @ModelAttr("客户产品备注")
    private String KHRemarks;


    @Transient
    @FieldRef("GYSRemarks")
    @ModelAttr("供应商备注")
    private String GYSRemarks;

    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public String getPackingUnit() {
        return packingUnit;
    }

    public void setPackingUnit(String packingUnit) {
        this.packingUnit = packingUnit;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getZCNumber() {
        return ZCNumber;
    }

    public void setZCNumber(String ZCNumber) {
        this.ZCNumber = ZCNumber;
    }

    public String getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(String orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public String getShipmentQuantity() {
        return shipmentQuantity;
    }

    public void setShipmentQuantity(String shipmentQuantity) {
        this.shipmentQuantity = shipmentQuantity;
    }

    public String getPh() {
        return ph;
    }

    public void setPh(String ph) {
        this.ph = ph;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Date getManufacturerDate() {
        return manufacturerDate;
    }

    public void setManufacturerDate(Date manufacturerDate) {
        this.manufacturerDate = manufacturerDate;
    }

    public String getKHRemarks() {
        return KHRemarks;
    }

    public void setKHRemarks(String KHRemarks) {
        this.KHRemarks = KHRemarks;
    }

    public String getGYSRemarks() {
        return GYSRemarks;
    }

    public void setGYSRemarks(String GYSRemarks) {
        this.GYSRemarks = GYSRemarks;
    }


}
