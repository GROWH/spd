/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.security.service;

import org.jrplat.module.dictionary.cache.DicCache;
import org.jrplat.module.security.model.Org;
import org.jrplat.module.security.model.User;
import org.jrplat.platform.criteria.Operator;
import org.jrplat.platform.criteria.PropertyCriteria;
import org.jrplat.platform.criteria.PropertyEditor;
import org.jrplat.platform.criteria.PropertyType;
import org.jrplat.platform.log.JRPlatLogger;
import org.jrplat.platform.log.JRPlatLoggerFactory;
import org.jrplat.platform.result.Page;
import org.jrplat.platform.service.ServiceFacade;
import org.jrplat.platform.service.SimpleService;
import org.jrplat.platform.util.SpringContextUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author 西安捷然
 */
@Service
public class OrgService extends SimpleService<Org> {

    private static final JRPlatLogger LOG = JRPlatLoggerFactory.getJRPlatLogger(OrgService.class);
    private static ServiceFacade facade;
    @Resource(name = "serviceFacade")
    private ServiceFacade serviceFacade;

    public static List<String> getChildNames(Org org) {
        List<String> names = new ArrayList<>();
        List<Org> child = org.getChild();
        for (Org item : child) {
            names.add(item.getOrgName());
            names.addAll(getChildNames(item));
        }
        return names;
    }

    public static List<Integer> getChildIds(Org org) {
        List<Integer> ids = new ArrayList<>();
        List<Org> child = org.getChild();
        for (Org item : child) {
            ids.add(item.getId());
            ids.addAll(getChildIds(item));
        }
        return ids;
    }

    public static List<Integer> getMeAndchildIds(Org org) {
        List<Integer> ids = new ArrayList<>();
        ids.add(org.getId());
        if (org.getChild() != null) {
            for (Org org1 : org.getChild()) {
                ids.add(org1.getId());
            }
        }
        return ids;
    }

    //获取 自己单位id和上级单位id和直接下级id和直接下级货主的客户id  数据可见规则
    public static List<Integer> getSpecialIds(Org model) {

        List<Integer> ids = new ArrayList<>();
        //直接上级id
        if (model.getParent() != null) {
            ids.add(model.getParent().getId());
        }
        return getIds(model, ids);

    }

    public static List<Integer> getIds(Org model, List<Integer> ids) {
        //添加自己的id
        ids.add(model.getId());

        if (facade == null) {
            facade = SpringContextUtils.getBean("serviceFacade");
        }
        String sql = "SELECT id " +
                "FROM basic.Org b " +
                "WHERE " +
                "  b.parent_id = :orgid ";
        List<Integer> idList = facade.getEntityManager().createNativeQuery(sql).setParameter("orgid", model.getId()).getResultList();
        ids.addAll(idList);
        return ids;
    }

    public String toRootJson() {

        User user = UserHolder.getCurrentLoginUser();
        List<Integer> specialIds = getSpecialIds(user.getOrg());
        boolean superManager = user.isSuperManager();

        Org rootOrg = getRootOrg();
        if (rootOrg == null) {
            LOG.error("获取根组织架构失败！");
            return "";
        }
        StringBuilder json = new StringBuilder();
        json.append("[");

        json.append("{'text':'")
                .append(rootOrg.getOrgName())
                .append("','id':'")
                .append(rootOrg.getId());
        //判断是否为叶子节点
        if (getchild(rootOrg, superManager, specialIds).isEmpty()) {
            json.append("','leaf':true");
        } else {
            json.append("','leaf':false");
        }
        json.append("}");
        json.append("]");

        return json.toString();
    }

    public String toJson(int orgId) {
        User user = UserHolder.getCurrentLoginUser();
        List<Integer> specialIds = getSpecialIds(user.getOrg());
        boolean superManager = user.isSuperManager();

        Org org = serviceFacade.retrieve(Org.class, orgId);
        if (org == null) {
            LOG.error("获取ID为 " + orgId + " 的组织架构失败！");
            return "";
        }
        List<Org> child = getchild(org, superManager, specialIds);
        if (child.isEmpty()) {
            return "";
        }
        StringBuilder json = new StringBuilder();
        json.append("[");

        User loginUser = UserHolder.getCurrentLoginUser();
        for (Org item : child) {
            String text = item.getOrgName();
            //给当前登录用户的组织添加一个span标签，然后就可以用css进行独立控制了
            if (item.getId().equals(loginUser.getOrg().getId())) {
                text = "<span>" + text + "</span>";
            }
            json.append("{'text':'")
                    .append(text)
                    .append("','id':'")
                    .append(item.getId());

           /* //****性能优化**** item.getDWLX().getName().equals("客户") || item.getChild().isEmpty() 很重要
            if (item.getDWLX().getName().equals("客户") || item.getChild().isEmpty() || getchild(item, superManager, specialIds).isEmpty()) {
                json.append("','leaf':true");
            } else {
                json.append("','leaf':false");
            }

            //根据单位类型设置节点图标
            if (item.getDWLX().getName().equals("承运商")) {
                json.append(",'type':'承运商','iconCls':'unitType_carrier'");
            } else if (item.getDWLX().getName().equals("货主")) {
                json.append(",'type':'货主','iconCls':'unitType_provider'");
            } else if (item.getDWLX().getName().equals("客户")) {
                json.append(",'type':'客户','iconCls':'unitType_customer'");
            } else {
                json.append(",'type':'未知','iconCls':'unitType_unknown'");
            }*/
            json.append("},");
        }
        //删除最后一个,号，添加一个]号
        json = json.deleteCharAt(json.length() - 1);
        json.append("]");
        return json.toString();
    }

    //root 节点为当前账号的组织架构的上级
    public Org getRootOrg() {
        Integer rootId;
        User user = UserHolder.getCurrentLoginUser();
        if (user.getOrg().getParent() != null) {
            rootId = user.getOrg().getParent().getId();
        } else {
            rootId = user.getOrg().getId();
        }
        return getService().retrieve(Org.class, rootId);
    }

    public boolean repeatNameCheck(Org model) {
        PropertyCriteria propertyCriteria = new PropertyCriteria();
        propertyCriteria.addPropertyEditor(new PropertyEditor("orgName", Operator.eq, PropertyType.String, model.getOrgName()));
        Page<Org> page = serviceFacade.query(Org.class, null, propertyCriteria);
        if (page.getTotalRecords() > 0) {
            return true;
        }
        return false;
    }

    //过滤下级单位信息,避免使用数据库查询。
    public List<Org> getchild(Org org, boolean superManager, List<Integer> specialsIds) {

        List<Org> childs = org.getChild();
        if (childs.isEmpty()) {
            return childs;
        }

        if (!superManager) {

            Iterator<Org> iterator = childs.iterator();
            while (iterator.hasNext()) {
                Org childOrg = iterator.next();
                if (!specialsIds.contains(childOrg.getId())) {
                    iterator.remove();
                }
            }
            return childs;
        } else {
            return childs;
        }
    }

    /**
     * 检查单位编号是否重复
     *
     * @param model
     * @return
     */
    public boolean repeatNumberCheck(Org model) {
        PropertyCriteria propertyCriteria = new PropertyCriteria();
        propertyCriteria.addPropertyEditor(new PropertyEditor("DWBH", Operator.eq, PropertyType.String, model.getDWBH()));
        List<Org> orgList = serviceFacade.query(Org.class, null, propertyCriteria).getModels();
        if (!orgList.isEmpty()) {
            return true;
        }
        return false;
    }
}