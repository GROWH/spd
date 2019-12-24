/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.security.service;

import org.jrplat.module.security.model.UserGroup;
import org.jrplat.platform.log.JRPlatLogger;
import org.jrplat.platform.log.JRPlatLoggerFactory;
import org.jrplat.platform.service.ServiceFacade;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserGroupService {
    private static final JRPlatLogger LOG = JRPlatLoggerFactory.getJRPlatLogger(UserGroupService.class);
    @Resource(name = "serviceFacade")
    private ServiceFacade serviceFacade;


    public String toAllUserGroupJson() {
        List<UserGroup> userGroups = serviceFacade.query(UserGroup.class, null).getModels();
        return toJson(userGroups);
    }

    public String toJson(List<UserGroup> userGroups) {
        if (userGroups == null || userGroups.isEmpty()) {
            return "";
        }

        StringBuilder json = new StringBuilder();

        json.append("[");
        for (UserGroup userGroup : userGroups) {
            json.append("{'text':'")
                    .append(userGroup.getUserGroupName())
                    .append("','id':'userGroup-")
                    .append(userGroup.getId())
                    .append("','iconCls':'")
                    .append("userGroup")
                    .append("'")
                    .append(",'leaf':true")
                    .append("},");
        }
        json = json.deleteCharAt(json.length() - 1);
        json.append("]");

        return json.toString();
    }
}