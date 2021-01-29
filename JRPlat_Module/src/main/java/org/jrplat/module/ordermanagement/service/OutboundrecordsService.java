package org.jrplat.module.ordermanagement.service;


import org.jrplat.module.ordermanagement.model.OrderInformation;
import org.jrplat.module.ordermanagement.model.Outboundrecords;
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
 * @Date: 2021/1/27 上午9:22
 */
@Service
public class OutboundrecordsService extends SimpleService<Outboundrecords> {


    @Transactional(rollbackFor = Exception.class)
    public List<Outboundrecords> queryOutboundrecords(){
        try {

            //获取当前用户
            User user = UserHolder.getCurrentLoginUser();
            //获取当前登录用户的单位
            Unit unit = user.getUnit();
            String jpql = "select o from Outboundrecords o ";
//            if(likeInfo != null && !likeInfo.equals("")){
//                jpqlw += "and  w.workPBBarcode like '%"+likeInfo+"%'";
//            }
            Query queryw = getService().getEntityManager().createQuery(jpql, Outboundrecords.class);
            if(unit != null){
                jpql += "where o.unitNo.paperworkNo =:uNo";
                queryw.setParameter("uNo", unit.getPaperworkNo());
            }

            List<Outboundrecords> orderManagementList = queryw.getResultList();
            return orderManagementList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

}
