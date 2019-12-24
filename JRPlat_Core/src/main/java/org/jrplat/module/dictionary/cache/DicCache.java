package org.jrplat.module.dictionary.cache;

import org.jrplat.module.dictionary.model.DicItem;
import org.jrplat.platform.log.JRPlatLogger;
import org.jrplat.platform.log.JRPlatLoggerFactory;
import org.jrplat.platform.service.ServiceFacade;
import org.jrplat.platform.util.SpringContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by 赵腾飞 on 4/20/17.
 * <p>
 * Dicitem 数据字典缓存，可直接从此获取数据字典，服务器启动缓存一次。避免查询数据库
 */
@Component
public final class DicCache {

    private static final JRPlatLogger LOG = JRPlatLoggerFactory.getJRPlatLogger(DicCache.class);
    /**
     * 缓存所有数据字典的集合。
     * key: 数据字典名称+code value：字典
     */
    private static final Map<String, Map<String, DicItem>> DICITEM_MAP = new HashMap<>();
    @Autowired
    private ServiceFacade serviceFacade;

    private DicCache() {
    }

    /**
     * 将某个数据字典添加或更新至缓存中
     *
     * @param dicItem
     */
    public static void saveOrUpdate(DicItem dicItem) {
        Map<String, DicItem> dicItemMap;
        dicItemMap = DICITEM_MAP.get(dicItem.getDic().getEnglish());
        if (dicItemMap == null) {
            dicItemMap = new HashMap<>();
            DICITEM_MAP.put(dicItem.getDic().getEnglish(), dicItemMap);
        }
        dicItemMap.put(dicItem.getCode(), dicItem);
        dicItemMap.put(dicItem.getName(), dicItem);
    }

    /**
     * 删除后更新dic缓存
     *
     * @param ids
     */
    public static void remove(List<Integer> ids) {
        for (Map<String, DicItem> dicItemMap : DICITEM_MAP.values()) {
            Iterator iterator = dicItemMap.keySet().iterator();
            while (iterator.hasNext()) {
                DicItem item = dicItemMap.get(iterator.next());
                if (ids.contains(item.getId())) {
                    iterator.remove();
                }
            }
        }
    }

    /**
     * 通过缓存获取数据字典，避免查询数据库。
     *
     * @param dicEnglish
     * @param codeOrName
     * @return
     */
    public static DicItem get(String dicEnglish, String codeOrName) {
        try {
            DicItem item = null;
            if (DICITEM_MAP.get(dicEnglish) != null) {
                item = DICITEM_MAP.get(dicEnglish).get(codeOrName);
            }
            if (null == item) {
                DicCache dicCache = SpringContextUtils.getBean("dicCache");
                String jpql = "select o from DicItem o where o.dic.english = :dicEnglish and (o.name = :name or o.code = :code)";
                item = dicCache.serviceFacade.getEntityManager().createQuery(jpql, DicItem.class)
                        .setParameter("dicEnglish", dicEnglish).setParameter("name", codeOrName)
                        .setParameter("code", codeOrName).getSingleResult();
                if (null != item) {
                    saveOrUpdate(item);
                }
            }
            return item;
        } catch (Exception e) {
            LOG.debug("dic【" + dicEnglish + "】查询失败【名称/编码】：" + codeOrName + "不存在");
            return null;
        }
    }

    /**
     * 初始化数据字典集合
     */
    @PostConstruct
    public void init() {
        LOG.info("初始化数据字典缓存。");
        Map<String, DicItem> dicItemMap;
        List<DicItem> dicItemList = serviceFacade.getEntityManager().createQuery("select o from DicItem o", DicItem.class).getResultList();
        for (DicItem dicItem : dicItemList) {
            saveOrUpdate(dicItem);
        }

        int count = 0;
        if (LOG.isDebugEnabled()) {
            for (Map.Entry<String, Map<String, DicItem>> entry : DICITEM_MAP.entrySet()) {
                count += entry.getValue().size();
                LOG.debug(entry.getKey() + "----------" + count);
            }
        }

        LOG.info("数据字典缓存完成。共初始化：" + count + "条数据 ");
    }
}
