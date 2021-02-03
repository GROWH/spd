package org.jrplat.module.ordermanagement.ws.service;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.jrplat.module.commodityInfo.model.Commodity;
import org.jrplat.module.dictionary.cache.DicCache;
import org.jrplat.module.dictionary.model.Dic;
import org.jrplat.module.dictionary.model.DicItem;
import org.jrplat.module.ordermanagement.model.OrderInformation;
import org.jrplat.module.ordermanagement.model.OrderManagement;
import org.jrplat.module.ordermanagement.model.Outboundrecords;
import org.jrplat.module.ordermanagement.ws.ResultCode;
import org.jrplat.module.security.model.User;
import org.jrplat.module.security.service.password.PasswordEncoder;

import org.jrplat.module.unitInfo.model.Unit;
import org.jrplat.platform.service.ServiceFacade;
import org.jrplat.platform.service.SimpleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.Query;
import java.util.*;

/**
 * @author liurch
 * @create 2021-01-27 下午12:02
 */
@Service
public class OrderManageService extends SimpleService {

    @Resource(name = "passwordEncoder")
    private PasswordEncoder passwordEncoder;

    @Resource(name = "serviceFacade")
    protected ServiceFacade serviceFacade;


    /**
     * @Description:上传订单接口
     * @Date:  2021/1/28
     * @Param: [json]
     * @return:java.lang.String
     **/
    @Transactional(rollbackFor = Exception.class)
    public String uploadOrder(String json) {
        String checkResult = check(json);
        if (StringUtils.isNotBlank(checkResult)) {
            return checkResult;
        }

        JSONObject object = JSONObject.fromObject(json);
        JSONArray array = object.getJSONArray("data");
        JSONObject head = object.getJSONObject("head");
        Map<String, Object> result = getResultMapForOk();
        OrderManagement orderManagement = new OrderManagement();
        List<Map<String, Object>> data = new ArrayList<>();
        for (Object o : array) {
            Map<String, Object> map = new HashMap<>();
            JSONObject obj = (JSONObject) o;

            //获取下单方单位编码
            String xpaperworkNo = obj.getString("xpaperworkNo");
            Unit xunit = getUnitFromName(xpaperworkNo);
            orderManagement.setKHName(xunit);
            orderManagement.setKHNumber(xunit.getPaperworkNo());
            //获取收单方单位编码
            String spaperworkNo = obj.getString("spaperworkNo");
            Unit sunit = getUnitFromName(spaperworkNo);
            if (sunit == null) {
                throw new RuntimeException("收单方单位不存在");
            }
            orderManagement.setGYSName(sunit);
            orderManagement.setGYSNumber(sunit.getPaperworkNo());

            //订单类型 默认订货
//            DicItem ordertype = StringUtils.isBlank(obj.getString("ddlx"))
//                    ? DicCache.get("ordertype", obj.getString("ddlx")) : null;

            orderManagement.setOrdertype(DicCache.get("ordertype","订货"));
            //供应商发货日期
            if(obj.get("GYSfhsl") != null && !obj.getString("GYSfhsl").equals("") ){
                Date GYSDate = new Date(obj.getLong("GYSfhrq"));
                orderManagement.setGYSDate(GYSDate);
            }

            //供应商发货单号
            if(obj.get("GYSfhrq") != null && !obj.getString("GYSfhrq").equals("") ){
                String GYSInvoiceNo =  obj.getString("GYSfhrq");
                orderManagement.setGYSInvoiceNo(GYSInvoiceNo);
            }

            //供应商发货物流公司
            if(obj.get("GYSfhwlgs") != null && !obj.getString("GYSfhwlgs").equals("") ){
                String GYSLogistics =  obj.getString("GYSfhwlgs") ;
                orderManagement.setGYSLogistics(GYSLogistics);
            }

            //供应商发货物流单号
            if(obj.get("GYSLogisticsNo") != null && !obj.getString("GYSLogisticsNo").equals("") ){
                String GYSLogisticsNo = obj.getString("GYSLogisticsNo") ;
                orderManagement.setGYSLogisticsNo(GYSLogisticsNo);
            }

            //供应商发货备注
            if(obj.get("GYSRemarks") != null && !obj.getString("GYSRemarks").equals("") ){
                String GYSRemarks = obj.getString("GYSRemarks");
                orderManagement.setGYSRemarks(GYSRemarks);
            }

            //客户订单日期
            if(obj.get("KHddrq") != null && !obj.getString("KHddrq").equals("") ){
                Date KHDate = new Date(obj.getLong("KHddrq"));
                orderManagement.setKHDate(KHDate);
            }

            //客户订单单号
            if(obj.get("KHdddh") != null && !obj.getString("KHdddh").equals("") ){
                String KHInvoiceNo =  obj.getString("KHdddh") ;
                orderManagement.setKHInvoiceNo(KHInvoiceNo);
            }

            //客户退货物流公司
            if(obj.get("KHthwlgs") != null && !obj.getString("KHthwlgs").equals("") ){
                String KHTHLogistics = obj.getString("KHthwlgs") ;
                orderManagement.setKHTHLogistics(KHTHLogistics);
            }

            //客户退货的物流单号
            if(obj.get("KHthwldh") != null && !obj.getString("KHthwldh").equals("") ){
                String KHTHLogisticsNo = obj.getString("KHthwldh") ;
                orderManagement.setKHTHLogisticsNo(KHTHLogisticsNo);
            }


            //客户订单备注
            if(obj.get("KHddbz") != null && !obj.getString("KHddbz").equals("") ){
                String KHRemarks = obj.getString("KHddbz");
                orderManagement.setKHRemarks(KHRemarks);
            }

            orderManagement.setOrderStatus(DicCache.get("orderStatus","上传"));
            getService().create(orderManagement);

            for (Object o1 : obj.getJSONArray("items")) {
                JSONObject ox = (JSONObject) o1;
                OrderInformation orderInformation = new OrderInformation();
                //判断必填字段
                if (StringUtils.isBlank(ox.getString("lineNumber"))) {
                    throw new RuntimeException("行号不能为空");
                }
                //获取行号
                Integer lineNumber = ox.getInt("lineNumber");
                orderInformation.setLineNumber(lineNumber);
                //产品编号
                if (StringUtils.isBlank(ox.getString("spbm"))) {
                    throw new RuntimeException("产品编号不能为空");
                }
                String productNo = ox.getString("spbm");

                Commodity commodity = getCommitityFromName(productNo);
                if (commodity == null) {
                    throw new RuntimeException("该商品不存在");
                }
                orderInformation.setProductNo(commodity);
                //规格/型号
                String specifications = StringUtils.isNotBlank(ox.getString("spgg"))
                        ? ox.getString("spgg") : null;
                orderInformation.setSpecifications(specifications);
                //包装单位
                String packagingUnit = StringUtils.isNotBlank(ox.getString("bzdw"))
                        ? ox.getString("bzdw") : null;
                orderInformation.setPackingUnit(packagingUnit);
                //生产企业名称
                String manufacturer = StringUtils.isNotBlank(ox.getString("scqymc"))
                        ? ox.getString("scqymc") : null;

                orderInformation.setManufacturer(manufacturer);
                //客户订货/退货数量
                if (StringUtils.isBlank(ox.getString("dhsl"))) {
                    throw new RuntimeException("订货数量不能为空");
                }
                Integer orderQuantity = StringUtils.isNotBlank(ox.getString("dhsl"))
                        ? ox.getInt("dhsl") : null;
                orderInformation.setOrderQuantity(orderQuantity);
                //供应商发货数量
                if(ox.get("GYSfhsl") != null && !ox.getString("GYSfhsl").equals("")   ){
                    Integer shipmentQuantity =  ox.getInt("GYSfhsl") ;
                    orderInformation.setShipmentQuantity(shipmentQuantity);
                }

                //批号
                String PH = StringUtils.isNotBlank(ox.getString("ph"))
                        ? ox.getString("ph") : null;
                orderInformation.setPH(PH);
                //序列号
                String serialNumber = StringUtils.isNotBlank(ox.getString("xlh"))
                        ? ox.getString("xlh") : null;
                orderInformation.setSerialNumber(serialNumber);

                //生产日期
                if(ox.get("scrq") != null && !ox.getString("scrq").equals("")   ){
                    Date manufacturerDate =  new Date(ox.getLong("scrq"));
                    orderInformation.setManufacturerDate(manufacturerDate);
                }

                //有效期
                if(ox.get("yxq") != null && !ox.getString("yxq").equals("")   ){
                    Date effectiveDate = new Date(ox.getLong("yxq")) ;
                    orderInformation.setEffectiveDate(effectiveDate);
                }

                //客户产品备注
                String KHRemarksx = StringUtils.isNotBlank(ox.getString("KHspbz"))
                        ? ox.getString("KHspbz") : null;
                orderInformation.setKHRemarks(KHRemarksx);
                //供应商产品备注
                String GYSRemarksx = StringUtils.isNotBlank(ox.getString("GYSspbz"))
                        ? ox.getString("GYSspbz") : null;
                orderInformation.setGYSRemarks(GYSRemarksx);
                orderInformation.setOrderManagement(orderManagement);
                getService().create(orderInformation);
            }

            map.put("ddbh", orderManagement.getId());
            map.put("code", ResultCode.SUCCESS);
            map.put("message", "操作成功");
            data.add(map);
        }
        result.put("data",data);
        return JSONObject.fromObject(result).toString();
    }

    /**
     * @Description:下载订单接口
     * @Date:  2021/1/28
     * @Param: [json]
     * @return:java.lang.String
     **/
    @Transactional(rollbackFor = Exception.class)
    public String downloadOrder(String json) {
        String checkResult = check(json);
        if (StringUtils.isNotBlank(checkResult)) {
            return checkResult;
        }

        JSONObject object = JSONObject.fromObject(json);
        JSONArray array = object.getJSONArray("data");
        JSONObject head = object.getJSONObject("head");
        Map<String, Object> result = getResultMapForOk();
        List<Map<String,Object>> spList = null;
        Map<String,Object> spMap = null;
        List<Map<String, Object>> data = new ArrayList<>();
        for (Object o : array) {
            Map<String, Object> map = new HashMap<>();
            JSONObject obj = (JSONObject) o;

            //获取订单编号
            if (StringUtils.isBlank(obj.getString("ddbh"))) {
                throw new RuntimeException("订单编号不能为空");
            }

            //根据订单编号获取订单信息
            OrderManagement orderManagement = getOrderManageById(obj.getInt("ddbh"));
            //订单编号
            map.put("ddbh", orderManagement.getId());
            //下单方单位编码
            map.put("KHdwbm,", orderManagement.getKHName().getPaperworkNo());
            //收单方单位编码
            map.put("GYSdwbm", orderManagement.getGYSName().getPaperworkNo());

            //根据订单获取订单商品信息
            List<OrderInformation> orderInformations = getOrderInformationById(orderManagement.getId(),null);
            spList = new ArrayList<>();
            for (OrderInformation orderInformation : orderInformations){
                 spMap = new HashMap<>();
                //商品编码
                spMap.put("spbm",orderInformation.getProductNo().getNumber());
                //商品数量
                spMap.put("spsl",orderInformation.getOrderQuantity());
                spList.add(spMap);
            }
            //商品信息返回给收单方
            map.put("sp",spList);
            data.add(map);
            //更改状态为下载
            orderManagement.setOrderStatus(DicCache.get("orderStatus","下载"));
            getService().update(orderManagement);
        }
        result.put("data",data);

        return JSONObject.fromObject(result).toString();
    }

    /**
     * @Description:下载订单结果接口
     * @Date:  2021/1/28
     * @Param: [json]
     * @return:java.lang.String
     **/
    @Transactional(rollbackFor = Exception.class)
    public String downloadOrederResult(String json) {
        String checkResult = check(json);
        if (StringUtils.isNotBlank(checkResult)) {
            return checkResult;
        }

        JSONObject object = JSONObject.fromObject(json);
        JSONArray array = object.getJSONArray("data");
        JSONObject head = object.getJSONObject("head");
        //获取用户
        User user = getUserFromName(head.getString("name"));
        Map<String, Object> result = getResultMapForOk();
        List<Map<String, Object>> data = new ArrayList<>();
        for (Object o : array) {
            Map<String, Object> map = new HashMap<>();
            JSONObject obj = (JSONObject) o;

            //获取订单编号
            if (StringUtils.isBlank(obj.getString("ddbh"))) {
                throw new RuntimeException("订单编号不能为空");
            }

            //根据订单编号获取订单信息
            OrderManagement orderManagement = getOrderManageById(obj.getInt("ddbh"));

            for (Object o1 : obj.getJSONArray("items")) {
                JSONObject ox = (JSONObject) o1;
                //获取商品编号
                if (StringUtils.isBlank(ox.getString("spbm"))) {
                    throw new RuntimeException("商品编码不能为空");
                }
                //根据订单获取订单商品信息
                List<OrderInformation> orderInformations = getOrderInformationById(orderManagement.getId(),ox.getString("spbm"));
                OrderInformation orderInformation = orderInformations.get(0);
                Outboundrecords outboundrecords = new Outboundrecords();
                //下单方单位
                outboundrecords.setUnitNo(orderManagement.getGYSName());
                //实际销售数量
                if (StringUtils.isBlank(ox.getString("xssl"))) {
                    throw new RuntimeException("实际销售数量不能为空");
                }
                orderInformation.setShipmentQuantity(ox.getInt("xssl"));
                outboundrecords.setQuantity(ox.getString("xssl"));
                //商品批号
                if (StringUtils.isBlank(ox.getString("spph"))) {
                    throw new RuntimeException("商品批号不能为空");
                }
                orderInformation.setPH(ox.getString("spph"));
                outboundrecords.setLotNumber(ox.getString("spph"));
                //有效期
                if (StringUtils.isBlank(ox.getString("yxq"))) {
                    throw new RuntimeException("商品有效期不能为空");
                }
                orderInformation.setEffectiveDate(new Date(ox.getLong("yxq") ));
                //出货时间
                if (ox.getString("chsj") == null &&  ox.getString("chsj").equals("")) {
                    throw new RuntimeException("出货时间不能为空");
                }
                orderInformation.setDeliveryTime(new Date(ox.getLong("chsj") ));
                outboundrecords.setDeliveryTime(new Date(ox.getLong("chsj") ));
                getService().update(orderInformation);
                //新增出货记录
                outboundrecords.setOrderInformation(orderInformation);
                //添加单位
                outboundrecords.setUnitNo(user.getUnit());
                getService().create(outboundrecords);
            }

            //更改状态为录入
            orderManagement.setOrderStatus(DicCache.get("orderStatus","录入"));
            getService().update(orderManagement);

            map.put("ddbh", orderManagement.getId());
            map.put("code", ResultCode.SUCCESS);
            map.put("message", "操作成功");
            data.add(map);
        }
        result.put("data",data);

        return JSONObject.fromObject(result).toString();
    }


    /**
     * @Description:上传结果接口
     * @Date:  2021/1/28
     * @Param: [json]
     * @return:java.lang.String
     **/
    @Transactional(rollbackFor = Exception.class)
    public String uploadOrederResult(String json) {
        String checkResult = check(json);
        if (StringUtils.isNotBlank(checkResult)) {
            return checkResult;
        }

        JSONObject object = JSONObject.fromObject(json);
        JSONArray array = object.getJSONArray("data");
        JSONObject head = object.getJSONObject("head");
        Map<String, Object> result = getResultMapForOk();
        List<Map<String,Object>> spList = null;
        Map<String,Object> spMap = null;
        List<Map<String, Object>> data = new ArrayList<>();
        for (Object o : array) {
            Map<String, Object> map = new HashMap<>();
            JSONObject obj = (JSONObject) o;

            //获取订单编号
            if (StringUtils.isBlank(obj.getString("ddbh"))) {
                throw new RuntimeException("订单编号不能为空");
            }

            //根据订单编号获取订单信息
            OrderManagement orderManagement = getOrderManageById(obj.getInt("ddbh"));
            //订单编号
            map.put("ddbh", orderManagement.getId());
            //收单方单位编码
            map.put("KHdwbm,", orderManagement.getKHName().getPaperworkNo());
            //收单方单位编码
            map.put("GYSdwbm", orderManagement.getGYSName().getPaperworkNo());
            //根据订单获取订单商品信息
            List<OrderInformation> orderInformations = getOrderInformationById(orderManagement.getId(),null);
            spList = new ArrayList<>();
            for (OrderInformation orderInformation : orderInformations){
                spMap = new HashMap<>();
                //商品编码
                spMap.put("spbm",orderInformation.getProductNo().getNumber());
                //实际的销售商品数量
                spMap.put("sjspsl",orderInformation.getShipmentQuantity());
                //批号
                spMap.put("ph",orderInformation.getPH());
                //有效期
                spMap.put("yxq",orderInformation.getEffectiveDate());
                spList.add(spMap);
            }
            //商品信息返回给收单方
            map.put("sp",spList);
            data.add(map);
            //更改状态为下载
            orderManagement.setOrderStatus(DicCache.get("orderStatus","完成"));
            getService().update(orderManagement);
        }
        result.put("data",data);

        return JSONObject.fromObject(result).toString();
    }


    /**
     * 细单操作成功
     *
     * @param data
     * @param map
     * @param obj
     */
    private void addSuccessResultItem(List<Map<String, Object>> data, Map<String, Object> map, JSONObject obj) {
        map.put("bh", obj.getString("bh"));
        map.put("code", ResultCode.SUCCESS);
        map.put("message", "操作成功");
        data.add(map);
    }

    /**
     * 检查 格式、用户名密码、有没有权限访问
     *
     * @param json
     * @return
     */
    public String check(String json) {
        Map<String, Object> result = new HashMap<>();
        JSONObject object;
        try {
            object = JSONObject.fromObject(json);
            JSONObject head = object.getJSONObject("head");
            String name = head.getString("name");
            String pwd = head.getString("pwd");
            User user = getUserFromName(name);
            if (user != null) {
                pwd = passwordEncoder.encode(pwd.trim(), user);
                if (!pwd.equals(user.getPassword())) {
                    result.put("message", "账号密码校验失败");
                    result.put("code", 0);
                    return JSONObject.fromObject(result).toString();
                }

            } else {
                result.put("message", "账号密码校验失败");
                result.put("code", 0);
                return JSONObject.fromObject(result).toString();
            }
        } catch (Exception e) {
            result.put("message", "json格式错误");
            result.put("code", 0);
            return JSONObject.fromObject(result).toString();
        }
        return null;
    }


    /**
     * 根据 用户名 获取 user
     *
     * @param name
     * @return
     */
    public User getUserFromName(String name) {
        String jpql = "select o from User o where o.username=:name";
        List<User> users = serviceFacade.getEntityManager().createQuery(jpql, User.class)
                .setParameter("name", name).getResultList();
        if (users.size() == 1) {
            return users.get(0);
        } else {
            return null;
        }
    }


    /**
     * 根据 单位编号 获取 Unit
     *
     * @param paperworkNo
     * @return
     */
    public Unit getUnitFromName(String paperworkNo) {
        String jpql = "select o from Unit o where o.paperworkNo=:paperworkNo";
        List<Unit> units = serviceFacade.getEntityManager().createQuery(jpql, Unit.class)
                .setParameter("paperworkNo", paperworkNo).getResultList();
        if (units.size() == 1) {
            return units.get(0);
        } else {
            return null;
        }
    }


    /**
     * 根据 产品编号 获取 Commitity
     *
     * @param productNo
     * @return
     */
    public Commodity getCommitityFromName(String productNo) {
        String jpql = "select o from Commodity o where o.number =:productNo";
        List<Commodity> commoditys = serviceFacade.getEntityManager().createQuery(jpql, Commodity.class)
                .setParameter("productNo", productNo).getResultList();
        if (commoditys.size() == 1) {
            return commoditys.get(0);
        } else {
            return null;
        }
    }


    /**
     * 根据 订单编号获取订单信息
     *
     * @param id
     * @return
     */
    public OrderManagement getOrderManageById(Integer id) {
        String jpql = "select o from OrderManagement o where o.id=:id";
        List<OrderManagement> orderManagements = serviceFacade.getEntityManager().createQuery(jpql, OrderManagement.class)
                .setParameter("id", id).getResultList();
        if ( orderManagements != null  && orderManagements.size() > 0) {
               return  orderManagements.get(0);
        }
       return null;
    }

    /**
     * 根据 订单编号获取商品信息
     *
     * @param id
     * @return
     */
    public List<OrderInformation> getOrderInformationById(Integer id,String number) {
        String jpql = "select o from OrderInformation o where o.orderManagement.id =:id";
        if(number != null && !number.equals("")){
            jpql += " and o.productNo.number =:number";

        }
        Query query = serviceFacade.getEntityManager().createQuery(jpql, OrderInformation.class);
        if(number != null && !number.equals("")){
            query.setParameter("number",number);
        }
        query.setParameter("id", id);
        List<OrderInformation> orderInformations = query.getResultList();
        if ( orderInformations != null  && orderInformations.size() > 0) {
            return orderInformations;
        }
        return null;
    }


    /**
     * 获得 操作成功 基础 result
     *
     * @return
     */
    public Map<String, Object> getResultMapForOk() {
        Map<String, Object> result = new HashMap<>();
        result.put("success", "成功");
        result.put("code", ResultCode.SUCCESS);
        return result;
    }
}
