package org.jrplat.module.commodityInfo.service;

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
 * @Date: 2021/1/21 下午3:12
 */
@Service
public class CommodityService extends SimpleService<Commodity> {


    @Transactional(rollbackFor = Exception.class)
    public List<Commodity> queryCommodity(String likeInfo) {
        try {

            //获取当前用户
            User user = UserHolder.getCurrentLoginUser();
            //获取当前登录用户的单位
            Unit unit = user.getUnit();
            String jpql = "select o from Commodity o ";

            if (likeInfo != null && !likeInfo.equals("") || unit != null) {
                jpql += "where 1=1";
            }
            //模糊搜索
            if (likeInfo != null && !likeInfo.equals("")) {
                jpql += " and (o.id like '%" + likeInfo + "%' or o.productName like '%" + likeInfo + "%' or o.manufacturer like '%" + likeInfo + "%' )";
            }
            Query queryw = getService().getEntityManager().createQuery(jpql, Commodity.class);
            List<Commodity> commodities = queryw.getResultList();
            return commodities;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public void  createCommodity(Commodity model){
        String number = model.getNumber();
        String jpql = "select c from Commodity c where c.number=:number ";
        Query queryw = getService().getEntityManager().createQuery(jpql, Commodity.class)
                .setParameter("number",number);
        List<Commodity> commodities = queryw.getResultList();
        if (commodities.size() > 0) {
            throw new RuntimeException("商品编号重复");
        }else {
            objectReference(model);
            getService().update(model);
        }

    }
}
