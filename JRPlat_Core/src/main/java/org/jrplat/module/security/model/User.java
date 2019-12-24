/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.security.model;

import org.jrplat.module.module.model.Command;
import org.jrplat.module.module.model.Module;
import org.jrplat.platform.annotation.*;
import org.jrplat.platform.criteria.Operator;
import org.jrplat.platform.criteria.PropertyCriteria;
import org.jrplat.platform.criteria.PropertyEditor;
import org.jrplat.platform.criteria.PropertyType;
import org.jrplat.platform.generator.ActionGenerator;
import org.jrplat.platform.model.SimpleModel;
import org.jrplat.platform.service.ServiceFacade;
import org.jrplat.platform.util.SpringContextUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import java.util.*;

@Entity
@Scope("prototype")
@Component
@Table(name = "UserTable",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"username"})})
@XmlRootElement
@XmlType(name = "User")
@Database
public class User extends SimpleModel implements UserDetails {
    @SupportLikeQuery("org.orgName")
    @ManyToOne
    @ModelAttr("组织架构")
    @ModelAttrRef("orgName")
    protected Org org;

    //用户名不分词
    @SupportLikeQuery
    @ModelAttr("用户名")
    protected String username;

    @SupportLikeQuery
    @ModelAttr("姓名")
    protected String realName;

    @ModelAttr("密码")
    protected String password;

    @SupportLikeQuery
    @ModelAttr("备注")
    protected String des;

    @SupportLikeQuery
    @ModelAttr("联系方式")
    protected String phone;

    @SupportLikeQuery
    @ModelAttr("联系地址")
    protected String address;

    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinTable(name = "user_role", joinColumns = {
            @JoinColumn(name = "userID")}, inverseJoinColumns = {
            @JoinColumn(name = "roleID")})
    @OrderBy("id")
    @ModelAttr("用户拥有的角色列表")
    @ModelCollRef("roleName")
    protected List<Role> roles = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinTable(name = "user_userGroup", joinColumns = {
            @JoinColumn(name = "userID")}, inverseJoinColumns = {
            @JoinColumn(name = "userGroupID")})
    @OrderBy("id")
    @ModelAttr("用户拥有的用户组列表")
    @ModelCollRef("userGroupName")
    protected List<UserGroup> userGroups = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinTable(name = "user_position", joinColumns = {
            @JoinColumn(name = "userID")}, inverseJoinColumns = {
            @JoinColumn(name = "positionID")})
    @OrderBy("id")
    @ModelAttr("用户拥有的岗位列表")
    @ModelCollRef("positionName")
    protected List<Position> positions = new ArrayList<>();

    @ModelAttr("账号过期")
    protected boolean accountexpired = false;
    @ModelAttr("账户锁定")
    protected boolean accountlocked = false;
    @ModelAttr("信用过期")
    protected boolean credentialsexpired = false;
    @ModelAttr("账户可用")
    protected boolean enabled = true;
    @RenderIgnore
    protected String loginToken;
    @ModelAttr("调度id")
    protected Integer ddid;
    @ModelAttr("是否绑定司机")
    protected boolean driverUse;

    public static void main(String[] args) {
        User obj = new User();
        //生成Action
        ActionGenerator.generate(obj.getClass());
    }

    /**
     * 用户登录验证
     * 具体的验证规则就写在这里
     *
     * @return 验证结果，null为验证通过，非null则为验证未通过的原因
     */
    public String loginValidate() {
        String message = null;
        if (!isEnabled()) {
            message = "用户账号被禁用";
        }
        if (!isAccountNonExpired()) {
            message = "用户帐号已过期";
        }
        if (!isAccountNonLocked()) {
            message = "用户帐号已被锁定";
        }
        if (!isCredentialsNonExpired()) {
            message = "用户凭证已过期";
        }
        if (getAuthorities() == null) {
            message = "用户帐号未被授予任何权限";
        }
        return message;
    }

    /**
     * 用户是否为超级管理员
     * @return
     */
    public boolean isSuperManager() {
        if (this.roles != null && !this.roles.isEmpty()) {
            for (Role role : this.roles) {
                if (role.isSuperManager()) {
                    return true;
                }
            }
        }
        if (this.userGroups != null && !this.userGroups.isEmpty()) {
            for (UserGroup userGroup : this.userGroups) {
                for (Role role : userGroup.getRoles()) {
                    if (role.isSuperManager()) {
                        return true;
                    }
                }
            }
        }
        return false;
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

    public String getPositionStrs() {
        if (this.positions == null || this.positions.isEmpty()) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        for (Position position : this.positions) {
            result.append("position-").append(position.getId()).append(",");
        }
        result = result.deleteCharAt(result.length() - 1);
        return result.toString();
    }

    public String getUserGroupStrs() {
        if (this.userGroups == null || this.userGroups.isEmpty()) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        for (UserGroup userGroup : this.userGroups) {
            result.append("userGroup-").append(userGroup.getId()).append(",");
        }
        result = result.deleteCharAt(result.length() - 1);
        return result.toString();
    }

    public List<Command> getCommand() {
        List<Command> result = new ArrayList<>();

        if (this.roles != null && !this.roles.isEmpty()) {
            //如果用户为超级管理员
            for (Role role : this.roles) {
                if (role.isSuperManager()) {
                    return getAllCommand();
                }
            }
            //如果用户不是超级管理员则进行一下处理
            for (Role role : this.roles) {
                result.addAll(role.getCommands());
            }
        }
        if (this.userGroups != null && !this.userGroups.isEmpty()) {
            for (UserGroup userGroup : this.userGroups) {
                //如果用户为超级管理员
                for (Role role : userGroup.getRoles()) {
                    if (role.isSuperManager()) {
                        return getAllCommand();
                    }
                }
                //如果用户不是超级管理员则进行一下处理
                for (Role role : userGroup.getRoles()) {
                    result.addAll(role.getCommands());
                }
            }
        }
        if (this.positions != null && !this.positions.isEmpty()) {
            for (Position position : this.positions) {
                result.addAll(position.getCommands());
            }
        }
        return result;
    }

    private List<Command> getAllCommand() {
        ServiceFacade serviceFacade = SpringContextUtils.getBean("serviceFacade");
        List<Command> allCommand = serviceFacade.query(Command.class).getModels();
        return allCommand;
    }

    public List<Module> getModule() {
        List<Module> result = new ArrayList<>();

        if (this.roles != null && !this.roles.isEmpty()) {
            //如果用户为超级管理员
            for (Role role : this.roles) {
                if (role.isSuperManager()) {
                    return getAllModule();
                }
            }
            //如果用户不是超级管理员则进行一下处理
            for (Role role : this.roles) {
                result.addAll(assemblyModule(role.getCommands()));
            }
        }
        if (this.userGroups != null && !this.userGroups.isEmpty()) {
            for (UserGroup userGroup : this.userGroups) {
                //如果用户为超级管理员
                for (Role role : userGroup.getRoles()) {
                    if (role.isSuperManager()) {
                        return getAllModule();
                    }
                }
                //如果用户不是超级管理员则进行一下处理
                for (Role role : userGroup.getRoles()) {
                    result.addAll(assemblyModule(role.getCommands()));
                }
            }
        }
        if (this.positions != null && !this.positions.isEmpty()) {
            for (Position position : this.positions) {
                result.addAll(assemblyModule(position.getCommands()));
            }
        }

        return result;
    }

    private List<Module> getAllModule() {
        ServiceFacade serviceFacade = SpringContextUtils.getBean("serviceFacade");
        //只查询显示的模块
        PropertyCriteria propertyCriteria = new PropertyCriteria();
        propertyCriteria.addPropertyEditor(new PropertyEditor("display", Operator.eq, PropertyType.Boolean, true));
        List<Module> allModule = serviceFacade.query(Module.class, null, propertyCriteria).getModels();
        return allModule;
    }

    private List<Module> assemblyModule(List<Command> commands) {
        List<Module> modules = new ArrayList<>();
        if (commands == null) {
            return modules;
        }

        for (Command command : commands) {
            if (command != null) {
                Module module = command.getModule();
                if (module != null && module.isDisplay()) {
                    modules.add(module);
                    assemblyModule(modules, module);
                }
            }
        }
        return modules;
    }

    private void assemblyModule(List<Module> modules, Module module) {
        if (module != null) {
            Module parentModule = module.getParentModule();
            if (parentModule != null && parentModule.isDisplay()) {
                modules.add(parentModule);
                assemblyModule(modules, parentModule);
            }
        }
    }

    public String getAuthoritiesStr() {
        StringBuilder result = new StringBuilder();
        for (GrantedAuthority auth : getAuthorities()) {
            result.append(auth.getAuthority()).append(",");
        }
        return result.toString();
    }

    /**
     * 获取授予用户的权利
     * @return
     */
    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> grantedAuthArray = new HashSet<>();

        LOG.debug("user privilege:");
        //如果用户是超级管理员，则只需加入ROLE_SUPERMANAGER标识
        //就不用对其他的权限对象进行检查
        if (isSuperManager()) {
            grantedAuthArray.add(new SimpleGrantedAuthority("ROLE_SUPERMANAGER"));
            LOG.debug("ROLE_SUPERMANAGER");
        } else {
            if (this.roles != null && !this.roles.isEmpty()) {
                LOG.debug("     roles:");
                for (Role role : this.roles) {
                    for (String priv : role.getAuthorities()) {
                        LOG.debug(priv);
                        grantedAuthArray.add(new SimpleGrantedAuthority(priv.toUpperCase()));
                    }
                }
            }
            if (this.userGroups != null && !this.userGroups.isEmpty()) {
                LOG.debug("     userGroups:");
                for (UserGroup userGroup : this.userGroups) {
                    for (Role role : userGroup.getRoles()) {
                        for (String priv : role.getAuthorities()) {
                            LOG.debug(priv);
                            grantedAuthArray.add(new SimpleGrantedAuthority(priv.toUpperCase()));
                        }
                    }
                }
            }
            if (this.positions != null && !this.positions.isEmpty()) {
                LOG.debug("     positions:");
                for (Position position : this.positions) {
                    for (String priv : position.getAuthorities()) {
                        LOG.debug(priv);
                        grantedAuthArray.add(new SimpleGrantedAuthority(priv.toUpperCase()));
                    }
                }
            }
        }
        if (grantedAuthArray.isEmpty()) {
            return null;
        }
        grantedAuthArray.add(new SimpleGrantedAuthority("ROLE_MANAGER"));
        LOG.debug("ROLE_MANAGER");
        return grantedAuthArray;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !accountexpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !accountlocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !credentialsexpired;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    @Override
    @XmlAttribute
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setAccountexpired(boolean accountexpired) {
        this.accountexpired = accountexpired;
    }

    public void setAccountlocked(boolean accountlocked) {
        this.accountlocked = accountlocked;
    }

    public void setCredentialsexpired(boolean credentialsexpired) {
        this.credentialsexpired = credentialsexpired;
    }

    @XmlTransient
    public List<UserGroup> getUserGroups() {
        return Collections.unmodifiableList(this.userGroups);
    }

    public void setUserGroups(List<UserGroup> userGroups) {
        this.userGroups = userGroups;
    }

    public void addUserGroup(UserGroup userGroup) {
        this.userGroups.add(userGroup);
    }

    public void removeUserGroup(UserGroup userGroup) {
        this.userGroups.remove(userGroup);
    }

    public void clearUserGroup() {
        this.userGroups.clear();
    }

    @XmlTransient
    public List<Role> getRoles() {
        return Collections.unmodifiableList(this.roles);
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
    public List<Position> getPositions() {
        return Collections.unmodifiableList(this.positions);
    }

    public void addPosition(Position position) {
        this.positions.add(position);
    }

    public void removePosition(Position position) {
        this.positions.remove(position);
    }

    public void clearPosition() {
        this.positions.clear();
    }

    @Override
    @XmlAttribute
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @XmlAttribute
    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    @XmlTransient
    public Org getOrg() {
        return org;
    }

    public void setOrg(Org org) {
        this.org = org;
    }

    @Override
    @XmlAttribute
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLoginToken() {
        return loginToken;
    }

    public void setLoginToken(String token) {
        this.loginToken = token;
    }

    public Integer getDdid() {
        return ddid;
    }

    public void setDdid(Integer pdid) {
        this.ddid = pdid;
    }

    public boolean isDriverUse() {
        return driverUse;
    }

    public void setDriverUse(boolean driverUse) {
        this.driverUse = driverUse;
    }

    @Override
    public String getMetaData() {
        return "用户信息";
    }

    public boolean modulePrivilege(String privileges, String module) {
        if (privileges.toString().indexOf("ROLE_SUPERMANAGER") != -1) {
            //超级管理员，检查模块是否显示
            ServiceFacade serviceFacade = SpringContextUtils.getBean("serviceFacade");
            PropertyCriteria propertyCriteria = new PropertyCriteria();
            propertyCriteria.addPropertyEditor(new PropertyEditor("english", Operator.eq, PropertyType.String, module));
            List<Module> temp = serviceFacade.query(Module.class, null, propertyCriteria).getModels();
            if (temp.size() > 1) {
                throw new RuntimeException("具有同名的模块，Module(english)=" + module);
            } else if (temp.size() == 1) {
                return temp.get(0).isDisplay();
            } else {
                return false;
            }
        }
        String tempStr = "ROLE_MANAGER_"
                + module.toUpperCase().replace("/", "_");
        if (privileges.toString().indexOf(tempStr) == -1) {
            return false;
        }
        return true;
    }
}