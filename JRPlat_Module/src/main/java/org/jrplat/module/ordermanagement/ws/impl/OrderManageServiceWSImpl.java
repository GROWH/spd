package org.jrplat.module.ordermanagement.ws.impl;

import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.jrplat.module.ordermanagement.ws.OrderManageServiceWS;
import org.jrplat.module.ordermanagement.ws.service.OrderManageService;
import org.jrplat.module.security.model.Org;
import org.jrplat.module.security.service.password.PasswordEncoder;
import org.jrplat.platform.log.JRPlatLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jws.WebService;
import org.jrplat.module.security.model.User;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *  调用数据格式如下
 * {
 *     "head":{
 *         "synid":0,
 *         "name":"",
 *         "pwd":""
 *     },
 *     "data":[
 *         {xx:xx,items:[]},
 * ]
 * }
 * 返回数据格式如下
 * {
 *
 *      "message":"",
 *      "code":ResultCode.CODE,
 *      "data":{
 *      }
 *
 * }
 */

/**
 * @author liurch
 * @create 2021-01-27 上午10:12
 */
@Service
@WebService(endpointInterface = "org.jrplat.module.ordermanagement.ws.OrderManageServiceWS")
public class OrderManageServiceWSImpl  implements OrderManageServiceWS {

    @Autowired
    private OrderManageService orderManageService;

    @Override
    public String uploadOrder(String json) {
        try {
            //调用上传订单接口
            return orderManageService.uploadOrder(json);
        }catch (Exception e){
            return getErrorString(e);
        }

    }

    @Override
    public String downloadOrder(String json) {
        try {
            //调用下载订单接口
            return orderManageService.downloadOrder(json);
        }catch (Exception e){
            return getErrorString(e);
        }

    }

    @Override
    public String downloadOrederResult(String json) {
        try {
            //调用下载结果接口
            return orderManageService.downloadOrederResult(json);
        }catch (Exception e){
            return getErrorString(e);
        }
    }

    @Override
    public String uploadOrederResult(String json) {
        try {
            //调用上传结果接口
            return orderManageService.uploadOrederResult(json);
        }catch (Exception e){
            return getErrorString(e);
        }
    }


    public String getErrorString(Exception e) {
        Map map = new HashMap();
        map.put("message","未知错误:"+e.getMessage());
        map.put("code",0);
        return JSONObject.fromObject(map).toString();
    }



}
