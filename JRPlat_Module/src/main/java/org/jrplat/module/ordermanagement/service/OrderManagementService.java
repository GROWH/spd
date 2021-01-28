package org.jrplat.module.ordermanagement.service;


import org.jrplat.module.ordermanagement.model.OrderManagement;
import org.jrplat.module.security.model.User;
import org.jrplat.module.security.service.UserHolder;
import org.jrplat.module.unitInfo.model.Unit;
import org.jrplat.platform.service.SimpleService;
import org.springframework.stereotype.Service;

import javax.persistence.Query;
import java.util.List;

/**
 * @Author:wangkang
 * @Date: 2021/1/21 下午3:18
 */
@Service
public class OrderManagementService extends SimpleService<OrderManagement> {



    public List<OrderManagement> queryOrderManagement(){
        try {

            //获取当前用户
            User user = UserHolder.getCurrentLoginUser();
            //获取当前登录用户的单位
            Unit unit = user.getUnit();
            String jpql = "select o from OrderManagement o ";
//            if(likeInfo != null && !likeInfo.equals("")){
//                jpqlw += "and  w.workPBBarcode like '%"+likeInfo+"%'";
//            }
            Query queryw = getService().getEntityManager().createQuery(jpql, OrderManagement.class);
            if(unit != null){
                jpql += "where o.GYSName.paperworkNo =:uNo or o.KHName.paperworkNo =:uNo ";
                queryw.setParameter("uNo", unit.getPaperworkNo());
            }

            List<OrderManagement> orderManagementList = queryw.getResultList();
            return orderManagementList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
        
    }
}
