/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.platform.dao;

import org.hibernate.CacheMode;
import org.jrplat.module.security.model.Org;
import org.jrplat.module.security.model.User;
import org.jrplat.module.security.service.OrgService;
import org.jrplat.module.security.service.UserHolder;
import org.jrplat.platform.common.DataPrivilegeControl;
import org.jrplat.platform.criteria.*;
import org.jrplat.platform.log.JRPlatLogger;
import org.jrplat.platform.log.JRPlatLoggerFactory;
import org.jrplat.platform.model.Model;
import org.jrplat.platform.result.Page;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * 通用的DAO操作支持类
 *
 * @author 西安捷然
 */
public abstract class DaoSupport extends DataPrivilegeControl {
    protected static final OrderCriteria defaultOrderCriteria = new OrderCriteria();

    static {
        defaultOrderCriteria.addOrder(new Order("id", Sequence.DESC));
    }

    protected final JRPlatLogger LOG = JRPlatLoggerFactory.getJRPlatLogger(getClass());

    public DaoSupport(MultiDatabase multiDatabase) {
        super(multiDatabase);
    }

    protected <T extends Model> Page<T> queryData(Class<T> modelClass, PageCriteria pageCriteria, PropertyCriteria propertyCriteria, OrderCriteria sortCriteria) {

        //权限控制
        /*User user = UserHolder.getCurrentLoginUser();
        //如果用户不是超级用户就限制数据访问
        if (user != null && !user.isSuperManager() && needPrivilege(modelClass)) {
            user = getEm().find(User.class, user.getId());
            Org org = user.getOrg();
            if (propertyCriteria == null) {
                propertyCriteria = new PropertyCriteria();
            }
            //如果当前访问的模块不是特殊的，则用户只能操纵自己单位的数据
            if (dataSpecial(modelClass)) {
                propertyCriteria.addPropertyEditor(new PropertyEditor("ownerUser.org.id", Operator.eq, org.getId()));
            }
            //如果当前访问的模块不是特殊的，则用户除了能操纵自己单位的数据，还能操纵下级的所有数据
            else {
                //获取自己和自己的直接下级的信息.
                List<Integer> specials = OrgService.getSpecialIds(org);
                PropertyEditor pe = new PropertyEditor(Criteria.or);
                switch (modelClass.getSimpleName()) {
                    case "User":
                        //或得直接下级的id和自己的id
                        List<Integer> userSpecials = OrgService.getMeAndchildIds(org);
                        PropertyEditor editor4 = new PropertyEditor("org.id", Operator.in, PropertyType.List, userSpecials);
                        editor4.setReplace(true);
                        propertyCriteria.addPropertyEditor(editor4);
                        break;
                    case "Org":
//                        List<String> orgSpecials = OrgService.getSpecialName(org);
//                        List<Integer> orgSpecials = OrgService.getSpecialIds(org);
                        PropertyEditor editor3 = new PropertyEditor("id", Operator.in, PropertyType.List, specials);
                        editor3.setReplace(true);
                        propertyCriteria.addPropertyEditor(editor3);
                        break;
                    case "WaybillManagement":
                        //运单管理  提交单位，受理单位(不是初始状态), 数据所有者
                        pe.addSubPropertyEditor(new PropertyEditor("ownerUser.org.id", Operator.eq, PropertyType.Integer, org.getId()));
                        pe.addSubPropertyEditor(new PropertyEditor("TJDW.id", Operator.eq, PropertyType.Integer, org.getId()));
                        PropertyEditor subpe = new PropertyEditor(Criteria.and);
                        subpe.addSubPropertyEditor(new PropertyEditor("SLDW.id", Operator.eq, PropertyType.Integer, org.getId()));
                        subpe.addSubPropertyEditor(new PropertyEditor("DQZT.code", Operator.ne, PropertyType.String, "1"));
                        pe.addSubPropertyEditor(subpe);
                        propertyCriteria.addPropertyEditor(pe);
                        break;
                    case "Chargequery":
                        //费用管理  付款方 收款方
                        pe.addSubPropertyEditor(new PropertyEditor("FKFDWMC.id", Operator.eq, PropertyType.Integer, org.getId()));
                        pe.addSubPropertyEditor(new PropertyEditor("SKFDWMC.id", Operator.eq, PropertyType.Integer, org.getId()));
                        propertyCriteria.addPropertyEditor(pe);
                        break;
                    case "AddressData":
                        PropertyEditor editor2 = new PropertyEditor("DWMC.id", Operator.in, PropertyType.List, specials);
                        editor2.setReplace(true);
                        pe.addSubPropertyEditor(editor2);//地址
                        pe.addSubPropertyEditor(new PropertyEditor("SSDW.id", Operator.eq, PropertyType.Integer, org.getId()));//仓库
                        propertyCriteria.addPropertyEditor(pe);
                        break;
                    case "DepartmentData":
                        PropertyEditor editor1 = new PropertyEditor("SSDW.id", Operator.in, PropertyType.List, specials);
                        editor1.setReplace(true);
                        propertyCriteria.addPropertyEditor(editor1);
                        break;
                    case "LicenseData":
                        PropertyEditor editor = new PropertyEditor("SUDW.id", Operator.in, PropertyType.List, specials);
                        editor.setReplace(true);
                        propertyCriteria.addPropertyEditor(editor);
                        break;
                    //备货位查询时根据仓库id和当前账号的单位.
                    case "PreparationOfIocationData":
                        propertyCriteria.addPropertyEditor(new PropertyEditor("SSCK.SSDW.id", Operator.eq, PropertyType.Integer, user.getOrg().getId()));
                        break;
                    default:
                        break;
                }
            }
        }*/

        //根据属性过滤条件、排序条件构造jpql查询语句
        StringBuilder jpql = new StringBuilder("select o from ");
        jpql.append(getEntityName(modelClass)).append(" o ").append(buildPropertyCriteria(propertyCriteria)).append(buildOrderCriteria(sortCriteria));
        LOG.debug("jpql:" + jpql);
        Query query = getEntityManager().createQuery(jpql.toString());
        //绑定属性过滤条件值
        bindingPropertyCriteria(query, propertyCriteria);
        //根据页面条件设置query参数
        buildPageCriteria(pageCriteria, query);

        setQueryCache(query);

        Page<T> page = new Page<>();
        List<T> models = query.getResultList();

        if (models != null) {
            page.setModels(models);
            //根据属性过滤条件获取查询数据的总记录数
            page.setTotalRecords(getCount(modelClass, propertyCriteria));
        }
        return page;
    }

    private void setQueryCache(Query query) {
        if (query instanceof org.hibernate.ejb.QueryImpl) {
//            ((org.hibernate.ejb.QueryImpl) query).getHibernateQuery().setCacheable(true);
            ((org.hibernate.ejb.QueryImpl) query).getHibernateQuery().setCacheMode(CacheMode.IGNORE);
        }
    }

    private void bindingPropertyCriteria(Query query, PropertyCriteria propertyCriteria) {
        if (query != null && propertyCriteria != null) {
            List<PropertyEditor> propertyEditors = propertyCriteria.getPropertyEditors();
            int len = propertyEditors.size();
            for (int i = 0; i < len; i++) {
                PropertyEditor propertyEditor = propertyEditors.get(i);
                List<PropertyEditor> subPropertyEditor = propertyEditor.getSubPropertyEditor();
                if (subPropertyEditor == null || subPropertyEditor.isEmpty()) {
                    if (!propertyEditor.isReplace()) {
                        query.setParameter(propertyEditor.getProperty().getNameParameter(), propertyEditor.getProperty().getValue());
                    }
                } else {
                    binding(query, propertyEditor, 1);
                }
            }
        }
    }

    private void binding(Query query, PropertyEditor propertyEditor, int level) {
        List<PropertyEditor> subPropertyEditor = propertyEditor.getSubPropertyEditor();

        int l = subPropertyEditor.size();
        for (int j = 0; j < l; j++) {
            PropertyEditor p = subPropertyEditor.get(j);
            List<PropertyEditor> ss = p.getSubPropertyEditor();
            if (ss == null || ss.isEmpty()) {
                if (!p.isReplace()) {
                    query.setParameter(p.getProperty().getNameParameter() + "_" + level + "_" + j, p.getProperty().getValue());
                }
            } else {
                binding(query, p, ++level);
            }
        }
    }

    /**
     * 设置查询页面参数
     *
     * @param pageCriteria
     * @param query
     */
    private void buildPageCriteria(PageCriteria pageCriteria, Query query) {
        if (query != null && pageCriteria != null) {
            int firstindex = (pageCriteria.getPage() - 1) * pageCriteria.getSize();
            int maxresult = pageCriteria.getSize();
            query.setFirstResult(firstindex).setMaxResults(maxresult);
        }
    }

    /**
     * 组装where 语句
     *
     * @param propertyCriteria
     * @return
     */
    private <T extends Model> String buildPropertyCriteria(PropertyCriteria propertyCriteria) {
        StringBuilder wherejpql = new StringBuilder("");
        String result = "";
        if (propertyCriteria != null && propertyCriteria.getPropertyEditors().size() > 0) {
            //判断是否支持集合查询
            if (propertyCriteria.getCollection() != null && propertyCriteria.getObject() != null) {
                wherejpql.append(" join o.").append(propertyCriteria.getCollection()).append(" ").append(propertyCriteria.getObject());
            }

            wherejpql.append(" where ");

            List<PropertyEditor> propertyEditors = propertyCriteria.getPropertyEditors();
            int len = propertyEditors.size();
            for (int i = 0; i < len; i++) {
                PropertyEditor propertyEditor = propertyEditors.get(i);
                List<PropertyEditor> subPropertyEditor = propertyEditor.getSubPropertyEditor();
                //当没有子属性的时候时，属性编辑器本身才是有效的，否则他就是子属性的一个容器而已
                if (subPropertyEditor == null || subPropertyEditor.isEmpty()) {
                    //判断是否支持集合查询
                    if (propertyCriteria.getCollection() != null && propertyCriteria.getObject() != null && propertyEditor.getProperty().getName().startsWith(propertyCriteria.getObject())) {
                        wherejpql.append(" ");
                    } else {
                        wherejpql.append(" o.");
                    }
                    wherejpql.append(propertyEditor.getProperty().getName())
                            .append(" ")
                            .append(propertyEditor.getPropertyOperator().getSymbol())
                            .append(" ");
                    if (propertyEditor.isReplace()) {
                        //只替换 in的 parameter
                        if (propertyEditor.getPropertyOperator().getSymbol().equals("in")) {
                            wherejpql.append("(");
                            for (Integer id : (List<Integer>) propertyEditor.getProperty().getValue()) {
                                wherejpql.append(id).append(",");
                            }
                            wherejpql.deleteCharAt(wherejpql.lastIndexOf(","));
                            wherejpql.append(")");
                        }
                    } else {
                        wherejpql.append(":")
                                .append(propertyEditor.getProperty().getNameParameter());
                    }
                    if (i < len - 1) {
                        wherejpql.append(" ");
                        wherejpql.append(propertyCriteria.getCriteria().name());
                        wherejpql.append(" ");
                    }
                } else {
                    wherejpql.append(dealWithSubPropertyEditor(propertyEditor, 1));
                    if (i < len - 1) wherejpql.append(" and ");
                }
            }
            result = wherejpql.toString();
        }
        return result;
    }

    private String dealWithSubPropertyEditor(PropertyEditor propertyEditor, int level) {
        StringBuilder wherejpql = new StringBuilder();
        List<PropertyEditor> subPropertyEditor = propertyEditor.getSubPropertyEditor();
        wherejpql.append(" ( ");
        for (int j = 0; j < subPropertyEditor.size(); j++) {
            PropertyEditor sub = subPropertyEditor.get(j);
            List<PropertyEditor> ss = sub.getSubPropertyEditor();
            //当没有子属性的时候时，属性编辑器本身才是有效的，否则他就是子属性的一个容器而已
            if (ss != null && !ss.isEmpty()) {
                wherejpql.append(dealWithSubPropertyEditor(sub, ++level));
            } else {
                wherejpql.append(" o.").append(sub.getProperty().getName()).append(" ").append(sub.getPropertyOperator().getSymbol()).append(" ");
                if (sub.isReplace()) {
                    //只替换 in的 parameter
                    if (sub.getPropertyOperator().getSymbol().equals("in")) {
                        wherejpql.append("(");
                        for (Integer id : (List<Integer>) sub.getProperty().getValue()) {
                            wherejpql.append(id).append(",");
                        }
                        wherejpql.deleteCharAt(wherejpql.lastIndexOf(","));
                        wherejpql.append(")");
                    }
                } else {
                    wherejpql.append(":")
                            .append(sub.getProperty().getNameParameter())
                            .append("_")
                            .append(level)
                            .append("_")
                            .append(j);
                }
            }
            if (j < subPropertyEditor.size() - 1) {
                wherejpql.append(" ").append(propertyEditor.getCriteria().name()).append(" ");
            }
        }
        wherejpql.append(" ) ");
        return wherejpql.toString();
    }

    /**
     * 组装order by语句
     *
     * @param sortCriteria
     * @return
     */
    private String buildOrderCriteria(OrderCriteria sortCriteria) {
        StringBuilder orderbyql = new StringBuilder("");
        if (sortCriteria != null && sortCriteria.getOrders().size() > 0) {
            orderbyql.append(" order by ");

            for (Order order : sortCriteria.getOrders()) {
                orderbyql.append("o.").append(order.getPropertyName()).append(" ").append(order.getSequence().getValue()).append(",");
            }
            orderbyql.deleteCharAt(orderbyql.length() - 1);
        }
        return orderbyql.toString();
    }

    /**
     * 根据属性过滤条件获取查询数据的总记录数
     *
     * @param clazz
     * @param propertyCriteria
     * @return
     */
    private Long getCount(Class<? extends Model> clazz, PropertyCriteria propertyCriteria) {
        Query query = getEntityManager().createQuery("select count(o.id) from " + getEntityName(clazz) + " o " + buildPropertyCriteria(propertyCriteria));
        //绑定属性过滤条件值
        bindingPropertyCriteria(query, propertyCriteria);
        setQueryCache(query);
        return (Long) query.getSingleResult();
    }

    public Long getCount(Class<? extends Model> clazz) {
        Query query = getEntityManager().createQuery("select count(o.id) from " + getEntityName(clazz) + " o ");
        return (Long) query.getSingleResult();
    }

    public <T extends Model> Page<T> search(String queryString, PageCriteria pageCriteria, Class<T> modelClass) {
        List<T> result = new ArrayList<>();

        //建立页面对象
        Page<T> page = new Page<>();
        page.setModels(result);
        page.setTotalRecords(0);

        return page;
    }

}