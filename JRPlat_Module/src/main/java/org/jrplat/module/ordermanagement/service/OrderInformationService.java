package org.jrplat.module.ordermanagement.service;


import org.jrplat.module.ordermanagement.model.OrderInformation;
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
 * @Date: 2021/1/21 下午3:19
 */
@Service
public class OrderInformationService  extends SimpleService<OrderInformation> {

    @Transactional(rollbackFor = Exception.class)
    public List<OrderInformation> queryOrderInformation(String orderManagementId){
        try {


            String jpql = "select o from OrderInformation o where o.orderManagement.id =:orderManagementId  ";
            Query query = getService().getEntityManager().createQuery(jpql, OrderInformation.class).setParameter("orderManagementId",orderManagementId);

            List<OrderInformation> orderManagementList = query.getResultList();
            return orderManagementList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }


}
