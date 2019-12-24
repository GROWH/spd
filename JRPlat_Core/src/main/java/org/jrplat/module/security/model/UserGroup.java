/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.security.model;

import org.jrplat.platform.annotation.Database;
import org.jrplat.platform.annotation.ModelAttr;
import org.jrplat.platform.annotation.ModelCollRef;
import org.jrplat.platform.generator.ActionGenerator;
import org.jrplat.platform.model.SimpleModel;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Scope("prototype")
@Component
@Table(name = "UserGroup",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"userGroupName"})})
@XmlRootElement
@XmlType(name = "UserGroup")
@Database
public class UserGroup extends SimpleModel {
    @ModelAttr("用户组名称")
    protected String userGroupName;
    @ModelAttr("备注")
    protected String des;

    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinTable(name = "userGroup_role", joinColumns = {
            @JoinColumn(name = "userGroupID")}, inverseJoinColumns = {
            @JoinColumn(name = "roleID")})
    @OrderBy("id")
    @ModelAttr("用户组拥有的角色列表")
    @ModelCollRef("roleName")
    protected List<Role> roles = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.REFRESH, mappedBy = "userGroups", fetch = FetchType.LAZY)
    protected List<User> users = new ArrayList<>();

    public static void main(String[] args) {
        UserGroup obj = new UserGroup();
        //生成Action
        ActionGenerator.generate(obj.getClass());
    }

    public String getModuleCommandStr() {
        StringBuilder ids = new StringBuilder();
        for (Role role : roles) {
            ids.append(role.getModuleCommandStr());
        }
        return ids.toString();
    }

    public String getRoleStrs() {
        if (this.roles == null || this.roles.isEmpty()) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        for (Role role : this.roles) {
            result.append("role-").append(role.getId()).append(",");
        }
        result = result.deleteCharAt(result.length() - 1);
        return result.toString();
    }

    /**
     * 获取授予用户组的权利
     * @return
     */
    public List<String> getAuthorities() {
        List<String> result = new ArrayList<>();
        for (Role role : roles) {
            result.addAll(role.getAuthorities());
        }
        return result;
    }

    @XmlAttribute
    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    @XmlAttribute
    public String getUserGroupName() {
        return userGroupName;
    }

    public void setUserGroupName(String userGroupName) {
        this.userGroupName = userGroupName;
    }

    public List<Role> getRoles() {
        return Collections.unmodifiableList(this.roles);
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public void removeRole(Role role) {
        this.roles.remove(role);
    }

    public void clearRole() {
        this.roles.clear();
    }

    @XmlTransient
    public List<User> getUsers() {
        return Collections.unmodifiableList(users);
    }

    @Override
    public String getMetaData() {
        return "用户组信息";
    }
}