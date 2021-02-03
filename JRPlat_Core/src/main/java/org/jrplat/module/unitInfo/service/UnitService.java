package org.jrplat.module.unitInfo.service;

import org.jrplat.module.commodityInfo.model.Commodity;
import org.jrplat.module.security.model.User;
import org.jrplat.module.security.service.UserHolder;
import org.jrplat.module.unitInfo.model.Unit;
import org.jrplat.platform.service.SimpleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.List;

/**
 * @Author:wangkang
 * @Date: 2021/1/21 下午3:17
 */
@Service
public class UnitService extends SimpleService<Unit> {


    @Transactional(rollbackFor = Exception.class)
    public List<Unit> queryUnit(String likeInfo) {
        try {
            //获取当前用户
            User user = UserHolder.getCurrentLoginUser();
            //获取当前登录用户的单位
            Unit unit = user.getUnit();
            String jpql = "select o from Unit o ";

            if (likeInfo != null && !likeInfo.equals("") || unit != null) {
                jpql += "where 1=1";
            }
            //模糊搜索
            if (likeInfo != null && !likeInfo.equals("")) {
                jpql += " and (o.id like '%" + likeInfo + "%' or o.unitName like '%" + likeInfo + "%' or o.paperworkNo like '%" + likeInfo + "%' )";
            }
            Query queryw = getService().getEntityManager().createQuery(jpql, Unit.class);
            List<Unit> units = queryw.getResultList();
            return units;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public void  createUnit(Unit model){
        String paperworkNo = model.getPaperworkNo();
        String jpql = "select u from Unit u where u.paperworkNo=:paperworkNo ";
        Query queryw = getService().getEntityManager().createQuery(jpql, Unit.class)
                .setParameter("paperworkNo",paperworkNo);
        List<Unit> units = queryw.getResultList();
        if (units.size() > 0) {
            throw new RuntimeException("证件编号重复");
        }else {
            objectReference(model);
            getService().update(model);
        }

    }
}
