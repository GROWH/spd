/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.security.action;

import org.apache.struts2.convention.annotation.Namespace;
import org.jrplat.module.module.model.Command;
import org.jrplat.module.module.service.ModuleCache;
import org.jrplat.module.security.model.Position;
import org.jrplat.module.security.model.User;
import org.jrplat.module.security.service.PositionService;
import org.jrplat.module.security.service.UserHolder;
import org.jrplat.platform.action.ExtJSSimpleAction;
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
public class PositionAction extends ExtJSSimpleAction<Position> {
    private String node;
    @Resource(name = "positionService")
    private PositionService positionService;
    private List<Command> commands;
    private boolean recursion = false;

    public String store() {
        if (recursion) {
            int rootId = positionService.getRootPosition().getId();
            String json = positionService.toJson(rootId, recursion);
            Struts2Utils.renderJson(json);

            return null;
        }

        return query();
    }

    @Override
    public String query() {
        //如果node为null则采用普通查询方式
        if (node == null) {
            return super.query();
        }

        //如果指定了node则采用自定义的查询方式
        if (node.trim().startsWith("root")) {
            String json = positionService.toRootJson(recursion);
            Struts2Utils.renderJson(json);
        } else {
            String[] attr = node.trim().split("-");
            if (attr.length == 2) {
                int positionId = Integer.parseInt(attr[1]);
                String json = positionService.toJson(positionId, recursion);
                Struts2Utils.renderJson(json);
            }
        }
        return null;
    }

    /**
     * 删除岗位前，把该岗位从所有引用该岗位的用户中移除
     *
     * @param ids
     */
    @Override
    public void prepareForDelete(Integer[] ids) {
        User loginUser = UserHolder.getCurrentLoginUser();
        for (int id : ids) {
            Position position = getService().retrieve(Position.class, id);
            boolean canDel = true;
            //获取拥有等待删除的角色的所有用户
            List<User> users = position.getUsers();
            for (User user : users) {
                if (loginUser.getId() == user.getId()) {
                    canDel = false;
                }
            }
            if (!canDel) {
                continue;
            }
            for (User user : users) {
                user.removePosition(position);
                getService().update(user);
            }
        }
        super.prepareForDelete(ids);
    }

    @Override
    protected void retrieveAfterRender(Map map, Position model) {
        map.put("privileges", model.getModuleCommandStr());
    }

    @Override
    public void assemblyModelForCreate(Position model) {
        model.setCommands(commands);
    }

    @Override
    public void assemblyModelForUpdate(Position model) {
        //默认commands==null
        //当在修改角色的时候，如果客户端不修改commands，则commands==null
        if (commands != null) {
            model.setCommands(commands);
        }
    }

    /**
     * 更新岗位完成后，将ModuleCache中保存属于该岗位的用户
     * module缓存
     */
    @Override
    protected void afterSuccessPartUpdateModel(Position model) {
        List<User> users = model.getUsers();
        if (users.size() == 0) {
            return;
        } else {
            ModuleCache.remove(users);
        }
    }

    public void setPrivileges(String privileges) {
        String[] ids = privileges.split(",");
        commands = new ArrayList<>();
        for (String id : ids) {
            String[] attr = id.split("-");
            if (attr.length == 2) {
                if ("command".equals(attr[0])) {
                    Command command = getService().retrieve(Command.class, Integer.parseInt(attr[1]));
                    commands.add(command);
                }
            }
        }
    }

    public void setNode(String node) {
        this.node = node;
    }

    public void setRecursion(boolean recursion) {
        this.recursion = recursion;
    }
}