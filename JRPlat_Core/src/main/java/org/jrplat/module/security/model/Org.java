/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.security.model;

import org.jrplat.platform.annotation.*;
import org.jrplat.platform.generator.ActionGenerator;
import org.jrplat.platform.model.SimpleModel;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Scope("prototype")
@Component
@XmlRootElement
@XmlType(name = "Org")
@Database
public class Org extends SimpleModel {

    @SupportLikeQuery
    @DisplayIgnore
    @ModelAttrNotNull
    @ModelAttr("单位名称")
    protected String orgName;

    @SupportLikeQuery
    @DisplayIgnore
    @ModelAttrNotNull
    @ModelAttr("单位编号")
    protected String DWBH;

    @SupportLikeQuery
    @DisplayIgnore
    @ModelAttr("法人代表")
    protected String chargeMan;

    @SupportLikeQuery
    @DisplayIgnore
    @ModelAttrNotNull
    @ModelAttr("联系电话")
    protected String phone;

    @SupportLikeQuery
    @DisplayIgnore
    @ModelAttr("注册地址")
    protected String address;

    @SupportLikeQuery
    @DisplayIgnore
    @ModelAttr("备注")
    @Column(length = 1024)
    protected String functions;

    //    @SupportLikeQuery("parent.orgName")
    @ManyToOne
    @ModelAttr("上级单位名称")
    @ModelAttrRef("orgName")
    protected Org parent;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "parent")
    @OrderBy("id DESC")
    @RenderIgnore
    protected List<Org> child = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "org")
    @OrderBy("id DESC")
    @RenderIgnore
    protected List<User> users = new ArrayList<>();
    @DisplayIgnore
    @ModelAttr("传真")
    protected String CZ;
    @DisplayIgnore
    @ModelAttr("邮编")
    protected String YB;
    @DisplayIgnore
    @ModelAttr("手机")
    protected String SJ;
    @SupportLikeQuery
    @DisplayIgnore
    @ModelAttrNotNull
    @ModelAttr("联系人")
    protected String LXR;

    @ModelAttr("月结账号")
    protected String YJZH;

    @ModelAttr("最大用户数")
    protected Integer maxUser;
    @ModelAttr("已用用户数")
    protected Integer usedUser;

    public static void main(String[] args) {
        Org obj = new Org();
        //生成Action
        ActionGenerator.generate(obj.getClass());
    }

    public String getDWBH() {
        return DWBH;
    }

    public void setDWBH(String DWBH) {
        this.DWBH = DWBH;
    }

    public String getCZ() {
        return CZ;
    }

    public void setCZ(String CZ) {
        this.CZ = CZ;
    }

    public String getYB() {
        return YB;
    }

    public void setYB(String YB) {
        this.YB = YB;
    }

    public String getSJ() {
        return SJ;
    }

    public void setSJ(String SJ) {
        this.SJ = SJ;
    }

    public String getLXR() {
        return LXR;
    }

    public void setLXR(String LXR) {
        this.LXR = LXR;
    }

    public String getYJZH() {
        return YJZH;
    }

    public void setYJZH(String YJZH) {
        this.YJZH = YJZH;
    }

    @XmlElementWrapper(name = "subOrgs")
    @XmlElement(name = "org")
    public List<Org> getChild() {
        return child;
    }

    public void setChild(List<Org> child) {
        this.child = child;
    }

    @XmlTransient
    public Org getParent() {
        return parent;
    }

    public void setParent(Org parent) {
        this.parent = parent;
    }

    @XmlAttribute
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @XmlAttribute
    public String getChargeMan() {
        return chargeMan;
    }

    public void setChargeMan(String chargeMan) {
        this.chargeMan = chargeMan;
    }

    @XmlAttribute
    public String getFunctions() {
        return functions;
    }

    public void setFunctions(String functions) {
        this.functions = functions;
    }

    @XmlAttribute
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @XmlAttribute
    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    @XmlTransient
    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public Integer getMaxUser() {
        return maxUser;
    }

    public void setMaxUser(Integer maxUser) {
        this.maxUser = maxUser;
    }

    public Integer getUsedUser() {
        return usedUser;
    }

    public void setUsedUser(Integer usedUser) {
        this.usedUser = usedUser;
    }

    @Override
    public String getMetaData() {
        return "单位信息";
    }
}