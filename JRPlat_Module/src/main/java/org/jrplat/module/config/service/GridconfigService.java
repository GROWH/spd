package org.jrplat.module.config.service;

import org.apache.cxf.common.i18n.Exception;
import org.jrplat.module.config.model.Gridconfig;
import org.jrplat.module.security.model.User;
import org.jrplat.module.security.service.UserHolder;
import org.jrplat.platform.service.SimpleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;

/**
 * Created by yin on 17-3-10.
 */
@Service
public class GridconfigService extends SimpleService<Gridconfig> {

    /**
     * 清除表格配置
     *
     * @param whichGrid
     */
    @Transactional(rollbackFor = Exception.class)
    public void clean(String whichGrid) {
        User user = UserHolder.getCurrentLoginUser();
        String jpql = "delete from Gridconfig o where o.ownerUser.id = :id and o.whichGrid = :whichGrid";
        Query query = getService().getEntityManager().createQuery(jpql);
        query.setParameter("id", user.getId());
        query.setParameter("whichGrid", whichGrid);
        query.executeUpdate();
    }
}
