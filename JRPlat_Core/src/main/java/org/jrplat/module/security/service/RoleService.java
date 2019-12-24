/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.security.service;

import org.jrplat.module.security.model.Role;
import org.jrplat.module.security.model.User;
import org.jrplat.platform.criteria.Criteria;
import org.jrplat.platform.criteria.Operator;
import org.jrplat.platform.criteria.PropertyCriteria;
import org.jrplat.platform.criteria.PropertyEditor;
import org.jrplat.platform.log.JRPlatLogger;
import org.jrplat.platform.log.JRPlatLoggerFactory;
import org.jrplat.platform.result.Page;
import org.jrplat.platform.service.ServiceFacade;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class RoleService {
    private static final JRPlatLogger LOG = JRPlatLoggerFactory.getJRPlatLogger(RoleService.class);
    @Resource(name = "serviceFacade")
    private ServiceFacade serviceFacade;

    public static List<String> getChildNames(Role role) {
        List<String> names = new ArrayList<>();
        List<Role> child = role.getChild();
        for (Role item : child) {
            names.add(item.getRoleName());
            names.addAll(getChildNames(item));
        }
        return names;
    }

    public static List<Integer> getChildIds(Role role) {
        List<Integer> ids = new ArrayList<>();
        List<Role> child = role.getChild();
        for (Role item : child) {
            ids.add(item.getId());
            ids.addAll(getChildIds(item));
        }
        return ids;
    }

    public static boolean isParentOf(Role parent, Role child) {
        Role role = child.getParent();
        while (role != null) {
            if (role.getId() == parent.getId()) {
                return true;
            }
            role = role.getParent();
        }
        return false;
    }

    public String toRootJson(boolean recursion) {
        Role rootRole = getRootRole();
        if (rootRole == null) {
            LOG.error("获取根角色失败！");
            return "";
        }
        StringBuilder json = new StringBuilder();
        json.append("[");

        json.append("{'text':'")
                .append(rootRole.getRoleName())
                .append("','id':'role-")
                .append(rootRole.getId());
        if (rootRole.getChild().isEmpty()) {
            json.append("','leaf':true,'cls':'file'");
        } else {
            json.append("','leaf':false,'cls':'folder'");

            if (recursion) {
                for (Role item : rootRole.getChild()) {
                    json.append(",children:").append(toJson(item.getId(), recursion));
                }
            }
        }
        json.append("}");
        json.append("]");

        return json.toString();
    }

    public String toJson(int roleId, boolean recursion) {
        Role role = serviceFacade.retrieve(Role.class, roleId);
        if (role == null) {
            LOG.error("获取ID为 " + roleId + " 的角色失败！");
            return "";
        }
        List<Role> child = role.getChild();
        if (child.isEmpty()) {
            return "";
        }
        StringBuilder json = new StringBuilder();
        json.append("[");


        for (Role item : child) {
            //只有admin用户才能看见超级管理员角色
            User user = UserHolder.getCurrentLoginUser();
            if (item.getRoleName().equals("超级管理员")) {
                if (!user.getUsername().equals("admin")) {
                    continue;
                }
            }
            json.append("{'text':'")
                    .append(item.getRoleName())
                    .append("','id':'role-")
                    .append(item.getId());
            if (item.getChild().isEmpty()) {
                json.append("','leaf':true,'iconCls':'role'");
            } else {
                json.append("','leaf':false,'iconCls':'folder'");
                if (recursion) {
                    json.append(",children:").append(toJson(item.getId(), recursion));
                }
            }
            json.append("},");
        }
        //删除最后一个,号，添加一个]号
        json = json.deleteCharAt(json.length() - 1);
        json.append("]");

        return json.toString();
    }

    public Role getRootRole() {
        PropertyCriteria propertyCriteria = new PropertyCriteria(Criteria.or);
        propertyCriteria.addPropertyEditor(new PropertyEditor("roleName", Operator.eq, "String", "角色"));
        Page<Role> page = serviceFacade.query(Role.class, null, propertyCriteria);
        if (page.getTotalRecords() == 1) {
            return page.getModels().get(0);
        }
        return null;
    }
}