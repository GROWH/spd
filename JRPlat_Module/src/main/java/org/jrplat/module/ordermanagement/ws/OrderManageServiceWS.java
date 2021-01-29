package org.jrplat.module.ordermanagement.ws;

import javax.jws.WebService;

/**
 * @author liurch
 * @create 2021-01-27 上午10:11
 */
@WebService
public interface OrderManageServiceWS {


    /**
     * @Description:上传订单
     * @Date:  2021/1/28 
     * @Param: [json]
     * @return:java.lang.String
     **/
    String uploadOrder(String json);

    /**
     * @Description:下载订单
     * @Date:  2021/1/28 
     * @Param: [json]
     * @return:java.lang.String
     **/
    String downloadOrder(String json);

    /**
     * @Description:下载结果
     * @Date:  2021/1/28 
     * @Param: [json]
     * @return:java.lang.String
     **/
    String downloadOrederResult(String json);

    /**
     * @Description:上传结果
     * @Date:  2021/1/28
     * @Param: [json]
     * @return:java.lang.String
     **/
    String uploadOrederResult(String json);

}
