package org.jrplat.module.ordermanagement.service;


import org.jrplat.module.dictionary.cache.DicCache;
import org.jrplat.module.ordermanagement.model.OrderInformation;
import org.jrplat.module.ordermanagement.model.OrderManagement;
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
    public List<Outboundrecords> queryOutboundrecords(String likeInfo){
        try {

            //获取当前用户
            User user = UserHolder.getCurrentLoginUser();
            //获取当前登录用户的单位
            Unit unit = user.getUnit();
            String jpql = "select o from Outboundrecords o ";

            if(likeInfo != null && !likeInfo.equals("") || unit != null ){
                jpql +=  "where 1=1" ;
            }

            //模糊搜索
            if(likeInfo != null && !likeInfo.equals("")){
                jpql += " and (o.id like '%"+likeInfo+"%' or o.orderInformation.productNo.number like '%"+likeInfo+"%' or o.orderInformation.productName like '%"+likeInfo+"%' )";
            }
            if(unit != null){
                jpql += " and o.unitNo.paperworkNo ="+unit.getPaperworkNo();
            }
            Query queryw = getService().getEntityManager().createQuery(jpql, Outboundrecords.class);

            List<Outboundrecords> orderManagementList = queryw.getResultList();
            return orderManagementList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
    @Transactional(rollbackFor = Exception.class)
     public Outboundrecords updateOrderInformationInfo(Outboundrecords outboundrecords){
      try {
          //根据细单id查询细单
          String jpql = "select o from OrderInformation o where o.id =:oId";
          Query query = getService().getEntityManager().createQuery(jpql, OrderInformation.class).setParameter("oId",outboundrecords.getOrderInformation().getId());
          List<OrderInformation> o = query.getResultList();
          OrderInformation orderInformation = o.get(0);
          //出货日期
          orderInformation.setDeliveryTime(outboundrecords.getDeliveryTime());
          //有效日期
          orderInformation.setEffectiveDate(outboundrecords.getEffectiveDate());
          //批号
          orderInformation.setPH(outboundrecords.getLotNumber());
          //实际出货数量
          orderInformation.setShipmentQuantity(outboundrecords.getShipmentQuantity());
          //查询总单
          String jpqlz = "select o from OrderManagement o where o.id =:ozId";
          Query queryz = getService().getEntityManager().createQuery(jpqlz, OrderManagement.class).setParameter("ozId",orderInformation.getOrderManagement().getId());
          List<OrderManagement> oz = queryz.getResultList();
          OrderManagement orderManagement = oz.get(0);
          //更改状态总单状态为录入
          orderManagement.setOrderStatus(DicCache.get("orderStatus","录入"));
          getService().update(orderInformation);
          //供应商单位添加
          outboundrecords.setUnitNo(orderManagement.getGYSName());
          return outboundrecords;
      }catch (Exception e){
          throw new RuntimeException(e.getMessage());
      }


    }



}
