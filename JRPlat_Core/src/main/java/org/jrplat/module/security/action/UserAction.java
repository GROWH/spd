/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.security.action;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Namespace;
import org.jrplat.module.security.model.*;
import org.jrplat.module.security.service.UserHolder;
import org.jrplat.module.security.service.UserReportService;
import org.jrplat.module.security.service.UserService;
import org.jrplat.module.system.service.PropertyHolder;
import org.jrplat.platform.action.ExtJSSimpleAction;
import org.jrplat.platform.criteria.Operator;
import org.jrplat.platform.criteria.Property;
import org.jrplat.platform.criteria.PropertyCriteria;
import org.jrplat.platform.criteria.PropertyEditor;
import org.jrplat.platform.result.Page;
import org.jrplat.platform.util.Struts2Utils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Scope("prototype")
@Controller
@Namespace("/security")
public class UserAction extends ExtJSSimpleAction<User> {
    private int orgId;
    private String oldPassword;
    private String newPassword;
    private String roles;
    private String positions;
    private String userGroups;

    //在线用户 根据org查找
    private String org;
    //在线用户 根据role查找
    private String role;
    //用户重置密码
    private String password;

    //用户选择组件
    private boolean select;

    private Integer unit;
    @Resource(name = "userReportService")
    private UserReportService userReportService;
    @Resource(name = "userService")
    private UserService userService;

    @Override
    public String report() {
        byte[] report = userReportService.getReport(ServletActionContext.getServletContext(), ServletActionContext.getRequest());
        Struts2Utils.renderImage(report, "text/html");
        return null;
    }

    @Override
    public String create() {
        try {
            Org org =new Org();
            org.setId(1);
            model.setOrg(org);
            userService.create(model, roles);
        } catch (Exception e) {
            map = new HashMap();
            map.put("success", false);
            map.put("message", "创建失败:" + e.getMessage());
            Struts2Utils.renderJson(map);
            return null;
        }
        map = new HashMap();
        map.put("success", true);
        map.put("message", "创建成功");
        Struts2Utils.renderJson(map);
        return null;
    }

    @Override
    public String delete() {
        try {
            userService.delete(User.class, getIds());
        } catch (Exception e) {
            Struts2Utils.renderText("删除失败:" + e.getMessage());
            return null;
        }
        Struts2Utils.renderText("删除成功");
        return null;
    }

    @Override
    public PropertyCriteria buildPropertyCriteria() {
        return userService.buildPropertyCriteria(super.buildPropertyCriteria(), orgId);
    }

    public String reset() {
        String result = userService.reset(getIds(), password);
        Struts2Utils.renderText(result);
        return null;
    }

    public String online() {
        page = userService.getOnlineUsers(getStart(), getLimit(), org, role);

        Map json = new HashMap();
        json.put("totalProperty", page.getTotalRecords());
        List<Map> result = new ArrayList<>();
        renderJsonForQuery(result);
        json.put("root", result);
        Struts2Utils.renderJson(json);
        return null;
    }

    public String store() {
        if (select) {
            return super.query();
        }
        List<User> users = getService().query(User.class).getModels();
        List<Map<String, String>> data = new ArrayList<>();
        for (User user : users) {
            Map<String, String> temp = new HashMap<>();
            temp.put("value", user.getUsername());
            temp.put("text", user.getUsername());
            data.add(temp);
        }
        Struts2Utils.renderJson(data);
        return null;
    }

    // 用户下拉框返回id和username
    public String storeIdName() {
        if (select) {
            return super.query();
        }
        List<User> users = getService().query(User.class).getModels();
        List<Map<String, String>> data = new ArrayList<>();
        for (User user : users) {
            Map<String, String> temp = new HashMap<>();
            temp.put("value", "" + user.getId());
            temp.put("text", user.getUsername());
            temp.put("orgname", user.getOrg().getOrgName());
            temp.put("phone", user.getPhone());
            temp.put("address", user.getAddress());
            StringBuilder str = new StringBuilder();
            for (Position p : user.getPositions()) {
                str.append(p.getPositionName()).append(",");
            }
            temp.put(
                    "positions",
                    str.length() > 1 ? str.toString().substring(0,
                            str.length() - 1) : "");
            data.add(temp);
        }
        Struts2Utils.renderJson(data);
        return null;
    }

    // 根据org.id查询所有的用户
    public String storeByOrgId() {
        PropertyEditor propertyEditor = new PropertyEditor("org.id",
                Operator.eq, org);
        PropertyCriteria propertyCriteria = new PropertyCriteria();
        propertyCriteria.addPropertyEditor(propertyEditor);
        List<User> users = getService().query(User.class, null,
                propertyCriteria).getModels();
        List<Map<String, String>> data = new ArrayList<>();
        for (User user : users) {
            Map<String, String> temp = new HashMap<>();
            temp.put("value", "" + user.getId());
            temp.put("text", user.getUsername());
            temp.put("orgname", /*user.getOrg().getOrgName()*/"");
            data.add(temp);
        }
        Struts2Utils.renderJson(data);
        return null;
    }

    @Override
    public void assemblyModelForCreate(User model) {
        userService.assemblyModelForCreate(model, roles, positions, userGroups);
    }

    @Override
    protected void old(User model) {
        if (PropertyHolder.getBooleanProperty("demo")) {
            if (model.getUsername().equals("admin")) {
                throw new RuntimeException("演示版本不能修改admin用户");
            }
        }
        /*if (Integer.parseInt(getRequest().getParameter("model.org.id")) != (model.getOrg().getId())) {
            throw new RuntimeException("您不能修改用户的组织架构");
        }*/
    }

    public String modifyPassword() {
        Map result = userService.modifyPassword(oldPassword, newPassword);
        Struts2Utils.renderJson(result);

        return null;
    }

    // 在更新一个特定的部分的Model之前对Model添加需要修改的属性
    @Override
    protected void assemblyModelForPartUpdate(List<Property> properties) {
        userService.assemblyModelForPartUpdate(properties, model);
    }

    @Override
    protected void assemblyModelForUpdate(User model) {
        userService.assemblyModelForUpdate(model, roles, positions, userGroups);
    }

    @Override
    protected void renderJsonForRetrieve(Map map) {
        render(map, model);
        map.put("roles", model.getRoleStrs());
        map.put("positions", model.getPositionStrs());
        map.put("userGroups", model.getUserGroupStrs());
    }

    @Override
    protected void renderJsonForSearch(List result) {
        for (User user : page.getModels()) {
            Map temp = new HashMap();
            render(temp, user);

            StringBuilder str = new StringBuilder();
            //搜索出来的模型已经被detach了，无法获得延迟加载的数据
            User tmp = getService().retrieve(User.class, user.getId());
            for (Role r : tmp.getRoles()) {
                str.append(r.getRoleName()).append(",");
            }
            temp.put("roles", str.length() > 1 ? str.toString().substring(0, str.length() - 1) : "");

            str = new StringBuilder();
            for (Position p : tmp.getPositions()) {
                str.append(p.getPositionName()).append(",");
            }
            temp.put("positions", str.length() > 1 ? str.toString().substring(0, str.length() - 1) : "");
            result.add(temp);

            str = new StringBuilder();
            for (UserGroup p : tmp.getUserGroups()) {
                str.append(p.getUserGroupName()).append(",");
            }
            temp.put("userGroups", str.length() > 1 ? str.toString().substring(0, str.length() - 1) : "");
            result.add(temp);
        }
    }

    @Override
    protected void renderJsonForQuery(List result) {
        for (User user : page.getModels()) {
            //重新加载，避免出现延迟加载错误
            user = getService().retrieve(modelClass, user.getId());
            Map temp = new HashMap();
            render(temp, user);

            StringBuilder str = new StringBuilder();
            for (Role r : user.getRoles()) {
                str.append(r.getRoleName()).append(",");
            }
            temp.put("roles", str.length() > 1 ? str.toString().substring(0, str.length() - 1) : "");

            str = new StringBuilder();
            for (Position p : user.getPositions()) {
                str.append(p.getPositionName()).append(",");
            }
            temp.put("positions", str.length() > 1 ? str.toString().substring(0, str.length() - 1) : "");
            result.add(temp);

            str = new StringBuilder();
            for (UserGroup p : user.getUserGroups()) {
                str.append(p.getUserGroupName()).append(",");
            }
            temp.put("userGroups", str.length() > 1 ? str.toString().substring(0, str.length() - 1) : "");
            result.add(temp);
        }
    }

    @Override
    protected void render(Map map, User model) {
        map.put("id", model.getId());
        map.put("version", model.getVersion());
        map.put("username", model.getUsername());
        map.put("realName", model.getRealName());
        map.put("enabled", model.isEnabled() == true ? "启用" : "停用");
        map.put("address", model.getAddress());
        map.put("phone", model.getPhone());
        if (model.getOwnerUser() != null) {
            map.put("ownerUser_realNameAndUsername", model.getOwnerUser().getRealName() + "(" + model.getOwnerUser().getUsername() + ")");
            map.put("ownerUser_orgName", /*model.getOwnerUser().getOrg().getOrgName()*/"");
        }
        String orgName = "";
        int id = 0;
        /*if (model.getOrg() != null) {
            orgName = model.getOrg().getOrgName();
            id = model.getOrg().getId();
        }*/
        map.put("orgName", orgName);
        map.put("orgId", id + "");
        map.put("des", model.getDes());
        map.put("ddid", model.getDdid());
        map.put("driverUse", model.isDriverUse());
        String unitName = "";
        if (model.getUnit() != null) {
            unitName = model.getUnit().getUnitName();
            id = model.getUnit().getId();
        }
        map.put("unitId",id+ "");
        map.put("unitName", unitName);
    }

    @Override
    public String query() {
        //获取当前用户
        User user = UserHolder.getCurrentLoginUser();
        if(!"admin".equals(user.getUsername())) {
            int start = super.getStart();
            int len = super.getLimit();
            if (start == -1) {
                start = 0;
            }
            if (len == -1) {
                len = 10;
            }
            try {
                List<User> userList = userService.queryUser();
                if (len > userList.size()) {
                    len = userList.size();
                }
                List<User> models = new ArrayList<>();
                for (int i = start; i < start + getLimit(); i++) {
                    if (i >= userList.size()) {
                        break;
                    }
                    models.add(userList.get(i));
                }
                // 构造当前页面对象
                page = new Page<>();
                page.setModels(models);
                page.setTotalRecords(userList.size());
                Map json = new HashMap();
                json.put("totalProperty", page.getTotalRecords());
                List<Map> result = new ArrayList<>();
                renderJsonForQuery(result);
                json.put("root", result);
                Struts2Utils.renderJson(json);

            } catch (Exception e) {
                map = new HashMap();
                map.put("success", false);
                map.put("message", "查询失败:" + e.getMessage());
                Struts2Utils.renderJson(map);
                return null;
            }
        }else {
            return super.query();
        }
        return null;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public void setPositions(String positions) {
        this.positions = positions;
    }

    public void setUserGroups(String userGroups) {
        this.userGroups = userGroups;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public Integer getUnit() {
        return unit;
    }

    public void setUnit(Integer unit) {
        this.unit = unit;
    }
}