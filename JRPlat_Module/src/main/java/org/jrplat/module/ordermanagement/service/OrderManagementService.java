package org.jrplat.module.ordermanagement.service;


import org.jrplat.module.ordermanagement.model.OrderManagement;
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
 * @Date: 2021/1/21 下午3:18
 */
@Service
public class OrderManagementService extends SimpleService<OrderManagement> {


    @Transactional(rollbackFor = Exception.class)
    public List<OrderManagement> queryOrderManagement(String likeInfo){
        try {

            //获取当前用户
            User user = UserHolder.getCurrentLoginUser();
            //获取当前登录用户的单位
            Unit unit = user.getUnit();
            String jpql = "select o from OrderManagement o ";

            if(likeInfo != null && !likeInfo.equals("") || unit != null ){
                jpql +=  "where 1=1" ;
            }

            //模糊搜索
            if(likeInfo != null && !likeInfo.equals("")){
                jpql += "and (o.id like '%"+likeInfo+"%' or o.GYSNumber like '%"+likeInfo+"%' or o.KHNumber like '%"+likeInfo+"%' or  o.GYSName.unitName like '%"+likeInfo+"%'  or o.KHName.unitName like '%"+likeInfo+"%' )";
            }

            Query query = getService().getEntityManager().createQuery(jpql, OrderManagement.class);
            if(unit != null){
                jpql += "and o.GYSName.paperworkNo =:uNo or o.KHName.paperworkNo =:uNo ";
                query.setParameter("uNo", unit.getPaperworkNo());
            }

            List<OrderManagement> orderManagementList = query.getResultList();
            return orderManagementList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
        
    }
}
