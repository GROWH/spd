/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.dictionary.service;

import org.jrplat.module.dictionary.model.DicItem;
import org.jrplat.platform.criteria.Criteria;
import org.jrplat.platform.criteria.Operator;
import org.jrplat.platform.criteria.PropertyCriteria;
import org.jrplat.platform.criteria.PropertyEditor;
import org.jrplat.platform.log.JRPlatLogger;
import org.jrplat.platform.log.JRPlatLoggerFactory;
import org.jrplat.platform.service.ServiceFacade;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 数据字典项服务
 *
 * @author 西安捷然
 */
@Service
public class DicItemService {
    private static final JRPlatLogger LOG = JRPlatLoggerFactory.getJRPlatLogger(DicItemService.class);
    @Resource(name = "serviceFacade")
    private ServiceFacade serviceFacade;

    /**
     * 根据 数据字典英文名称 以及 数据字典编码 查找 数据字典项
     * 此方法对于遗留数据迁移非常有用
     *
     * @param dicEnglish 数据字典英文名称
     * @param code       数据字典编码
     * @return 数据字典项
     */
    public DicItem getDicItemByCode(String dicEnglish, String code) {
        LOG.debug("根据 数据字典英文名称 [" + dicEnglish + "] 以及 数据字典编码 [" + code + "] 查找 数据字典项");
        PropertyCriteria propertyCriteria = new PropertyCriteria(Criteria.and);
        propertyCriteria.addPropertyEditor(new PropertyEditor("dic.english", Operator.eq, dicEnglish));
        propertyCriteria.addPropertyEditor(new PropertyEditor("code", Operator.eq, "String", code));

        List<DicItem> page = serviceFacade.query(DicItem.class, null, propertyCriteria).getModels();
        if (page.size() != 1) {
            return null;
        }
        return page.get(0);
    }

    /**
     * 根据 数据字典英文名称 以及 数据字典名称 查找 数据字典项
     *
     * @param dicName
     * @param dicItemName
     * @return
     */
    public DicItem getDicItemByName(String dicEnglish, String dicItemName) {
        String jpql = "select o from DicItem o where o.dic.english = :dicEnglish and o.name =:name";
        List<DicItem> itemList = serviceFacade.getEntityManager().createQuery(jpql, DicItem.class)
                .setParameter("dicEnglish", dicEnglish).setParameter("name", dicItemName)
                .getResultList();
        if (itemList.size() != 1) {
            return null;
        }
        return itemList.get(0);
    }
}