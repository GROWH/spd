package org.jrplat.module.ordermanagement.service;


import org.jrplat.module.ordermanagement.model.OrderInformation;
import org.jrplat.module.ordermanagement.model.OrderInformationDto;
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
    public List<OrderInformationDto> queryOrderInformation(Integer orderManagementId){
        try {


            String sql = "SELECT\n" +
                    "\to.id id,\n" +
                    "\tc.number number,\n" +
                    "\tc.productName productName,\n" +
                    "\to.specifications specifications,\n" +
                    "\tc.PackingUnit packingUnit,\n" +
                    "\tc.manufacturer manufacturer,\n" +
                    "\tc.ZCNumber ZCNumber,\n" +
                    "\to.orderQuantity orderQuantity,\n" +
                    "\to.shipmentQuantity shipmentQuantity,\n" +
                    "\to.PH ph,\n" +
                    "\to.effectiveDate effectiveDate,\n" +
                    "\to.manufacturerDate manufacturerDate,\n" +
                    "\to.KHRemarks,\n" +
                    "\to.GYSRemarks GYSRemarks \n" +
                    "FROM\n" +
                    "\tOrderInformation o\n" +
                    "\tLEFT JOIN Commodity c ON c.id = o.productNo_id \n" +
                    "WHERE\n" +
                    "\to.orderManagement_id = "+orderManagementId;
            Query query = getService().getEntityManager().createNativeQuery(sql,OrderInformationDto.class);
            List<OrderInformationDto> orderManagementList = query.getResultList();
            return orderManagementList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }


}
