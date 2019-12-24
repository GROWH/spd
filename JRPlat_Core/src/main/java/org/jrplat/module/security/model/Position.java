/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.security.model;

import org.jrplat.module.module.model.Command;
import org.jrplat.module.module.model.Module;
import org.jrplat.module.module.service.ModuleService;
import org.jrplat.platform.annotation.*;
import org.jrplat.platform.generator.ActionGenerator;
import org.jrplat.platform.model.SimpleModel;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.util.*;

@Entity
@Scope("prototype")
@Component
@XmlRootElement
@XmlType(name = "Position")
@Database
public class Position extends SimpleModel {

    @ModelAttr("岗位名称")
    protected String positionName;

    @ManyToOne
    @ModelAttr("上级岗位")
    @ModelAttrRef("positionName")
    protected Position parent;

    @RenderIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "parent")
    @OrderBy("id DESC")
    @ModelAttr("下级岗位")
    @ModelCollRef("positionName")
    protected List<Position> child = new ArrayList<>();

    /**
     * 职位拥有的命令
     */
    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinTable(name = "position_command", joinColumns = {
            @JoinColumn(name = "positionID")}, inverseJoinColumns = {
            @JoinColumn(name = "commandID")})
    @OrderBy("id")
    @ModelAttr("岗位拥有的命令列表")
    @ModelCollRef("chinese")
    protected List<Command> commands = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.REFRESH, mappedBy = "positions", fetch = FetchType.LAZY)
    protected List<User> users = new ArrayList<>();

    public static void main(String[] args) {
        Position obj = new Position();
        //生成Action
        ActionGenerator.generate(obj.getClass());
    }

    public String getModuleCommandStr() {
        if (this.commands == null || this.commands.isEmpty()) {
            return "";
        }
        StringBuilder ids = new StringBuilder();

        Set<Integer> moduleIds = new HashSet<>();

        for (Command command : this.commands) {
            ids.append("command-").append(command.getId()).append(",");
            Module module = command.getModule();
            moduleIds.add(module.getId());
            module = module.getParentModule();
            while (module != null) {
                moduleIds.add(module.getId());
                module = module.getParentModule();
            }
        }
        for (Integer moduleId : moduleIds) {
            ids.append("module-").append(moduleId).append(",");
        }
        ids = ids.deleteCharAt(ids.length() - 1);
        return ids.toString();
    }

    /**
     * 获取授予岗位的权利
     * @return
     */
    public List<String> getAuthorities() {
        List<String> result = new ArrayList<>();
        for (Command command : commands) {
            Map<String, String> map = ModuleService.getCommandPathToRole(command);
            for (String role : map.values()) {
                StringBuilder str = new StringBuilder();
                str.append("ROLE_MANAGER").append(role);
                result.add(str.toString());
            }
        }
        return result;
    }

    @XmlAttribute
    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    @XmlTransient
    public Position getParent() {
        return parent;
    }

    public void setParent(Position parent) {
        this.parent = parent;
    }

    @XmlElementWrapper(name = "subPositions")
    @XmlElement(name = "position")
    public List<Position> getChild() {
        return this.child;
    }

    public void addChild(Position child) {
        this.child.add(child);
    }

    public void removeChild(Position child) {
        this.child.remove(child);
    }

    public void clearChild() {
        this.child.clear();
    }

    @XmlTransient
    public List<Command> getCommands() {
        return Collections.unmodifiableList(commands);
    }

    public void setCommands(List<Command> commands) {
        this.commands = commands;
    }

    public void addCommands(Command command) {
        this.commands.add(command);
    }

    public void removeCommand(Command command) {
        this.commands.remove(command);
    }

    public void clearCommand() {
        commands.clear();
    }

    @XmlTransient
    public List<User> getUsers() {
        return Collections.unmodifiableList(users);
    }

    @Override
    public String getMetaData() {
        return "岗位";
    }
}