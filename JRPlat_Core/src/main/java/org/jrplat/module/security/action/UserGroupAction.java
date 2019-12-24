/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.security.action;

import org.apache.struts2.convention.annotation.Namespace;
import org.jrplat.module.module.service.ModuleCache;
import org.jrplat.module.security.model.Role;
import org.jrplat.module.security.model.User;
import org.jrplat.module.security.model.UserGroup;
import org.jrplat.module.security.service.UserGroupService;
import org.jrplat.module.security.service.UserHolder;
import org.jrplat.platform.action.ExtJSSimpleAction;
import org.jrplat.platform.criteria.PropertyType;
import org.jrplat.platform.util.Struts2Utils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Scope("prototype")
@Controller
@Namespace("/security")
public class UserGroupAction extends ExtJSSimpleAction<UserGroup> {
    @Resource(name = "userGroupService")
    private UserGroupService userGroupService;
    private List<Role> roles = null;

    public String store() {
        String json = userGroupService.toAllUserGroupJson();
        Struts2Utils.renderJson(json);

        return null;
    }

    /**
     * 删除用户组前，把该用户组从所有引用该用户组的用户中移除
     * @param ids
     */
    @Override
    public void prepareForDelete(Integer[] ids) {
        User loginUser = UserHolder.getCurrentLoginUser();
        for (int id : ids) {
            UserGroup userGroup = getService().retrieve(UserGroup.class, id);
            boolean canDel = true;
            //获取拥有等待删除的角色的所有用户
            List<User> users = userGroup.getUsers();
            for (User user : users) {
                if (loginUser.getId() == user.getId()) {
                    canDel = false;
                }
            }
            if (!canDel) {
                continue;
            }
            for (User user : users) {
                user.removeUserGroup(userGroup);
                getService().update(user);
            }
        }
        super.prepareForDelete(ids);
    }

    @Override
    public void assemblyModelForCreate(UserGroup model) {
        model.setRoles(roles);
    }

    @Override
    public void assemblyModelForUpdate(UserGroup model) {
        //默认roles==null
        //当在修改用户组的时候，如果客户端不修改roles，则roles==null
        if (roles != null) {
            model.setRoles(roles);
        }
    }

    /**
     * 更新用户组完成后，将ModuleCache中保存属于该用户组的用户
     * module缓存
     */
    @Override
    protected void afterSuccessPartUpdateModel(UserGroup model) {
        List<User> users = model.getUsers();
        if (users.size() == 0) {
            return;
        } else {
            ModuleCache.remove(users);
        }
    }

    @Override
    protected void retrieveAfterRender(Map map, UserGroup model) {
        map.put("roles", model.getRoleStrs());
    }

    public void setRoles(String roleStr) {
        String[] ids = roleStr.split(",");
        roles = new ArrayList<>();
        for (String id : ids) {
            String[] attr = id.split("-");
            if (attr.length == 2) {
                if ("role".equals(attr[0])) {
                    Role role = getService().retrieve(Role.class, Integer.parseInt(attr[1]));
                    roles.add(role);
                }
            }
        }
    }

    // 判断用户组名称是不是重复
    protected void checkModel(UserGroup model) throws Exception {
        checkRestraint(model, "userGroupName", PropertyType.String, model.getUserGroupName(), "用户组名称");
    }
}