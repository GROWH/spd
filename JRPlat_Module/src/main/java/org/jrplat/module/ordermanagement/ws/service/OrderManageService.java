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
import org.jrplat.module.ordermanagement.ws.ResultCode;
import org.jrplat.module.security.model.User;
import org.jrplat.module.security.service.password.PasswordEncoder;

import org.jrplat.module.unitInfo.model.Unit;
import org.jrplat.platform.service.ServiceFacade;
import org.jrplat.platform.service.SimpleService;
import org.springframework.stereotype.Service;

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

            //订单类型
            DicItem ordertype = StringUtils.isNotBlank(obj.getString("ddlx"))
                    ? DicCache.get("ordertype", obj.getString("ddlx")) : null;
            orderManagement.setOrdertype(ordertype);
            //供应商发货日期
            Date GYSDate = StringUtils.isNotBlank(obj.getString("GYSfhrq"))
                    ? new Date(obj.getLong("GYSfhrq")) : null;
            orderManagement.setGYSDate(GYSDate);
            //供应商发货单号
            String GYSInvoiceNo = StringUtils.isNotBlank(obj.getString("GYSfhdh"))
                    ? obj.getString("GYSfhrq") : null;
            orderManagement.setGYSInvoiceNo(GYSInvoiceNo);
            //供应商发货物流公司
            String GYSLogistics = StringUtils.isNotBlank(obj.getString("GYSfhwlgs"))
                    ? obj.getString("GYSfhwlgs") : null;
            orderManagement.setGYSLogistics(GYSLogistics);
            //供应商发货物流单号
            String GYSLogisticsNo = StringUtils.isNotBlank(obj.getString("GYSLogisticsNo"))
                    ? obj.getString("GYSLogisticsNo") : null;
            orderManagement.setGYSLogisticsNo(GYSLogisticsNo);
            //供应商发货备注
            String GYSRemarks = StringUtils.isNotBlank(obj.getString("GYSRemarks"))
                    ? obj.getString("GYSRemarks") : null;
            orderManagement.setGYSRemarks(GYSRemarks);
            //客户订单日期
            Date KHDate = StringUtils.isNotBlank(obj.getString("KHddrq"))
                    ? new Date(obj.getLong("KHddrq")) : null;
            orderManagement.setKHDate(KHDate);
            //客户订单单号
            String KHInvoiceNo = StringUtils.isNotBlank(obj.getString("KHdddh"))
                    ? obj.getString("KHdddh") : null;
            orderManagement.setKHInvoiceNo(KHInvoiceNo);
            //客户退货物流公司
            String KHTHLogistics = StringUtils.isNotBlank(obj.getString("KHdddh"))
                    ? obj.getString("KHdddh") : null;
            orderManagement.setKHTHLogistics(KHTHLogistics);
            //客户退货的物流单号
            String KHTHLogisticsNo = StringUtils.isNotBlank(obj.getString("KHthwldh"))
                    ? obj.getString("KHthwldh") : null;
            orderManagement.setKHTHLogisticsNo(KHTHLogisticsNo);
            //客户订单备注
            String KHRemarks = StringUtils.isNotBlank(obj.getString("KHddbz"))
                    ? obj.getString("KHddbz") : null;
            orderManagement.setKHRemarks(KHRemarks);
            orderManagement.setOrderStatus(DicCache.get("orderStatus","上传"));
            getService().create(orderManagement);

            for (Object o1 : obj.getJSONArray("items")) {
                JSONObject ox = (JSONObject) o1;
                OrderInformation orderInformation = new OrderInformation();
                //判断必填字段
                if (StringUtils.isNotBlank(ox.getString("lineNumber"))) {
                    throw new RuntimeException("行号不能为空");
                }
                //获取行号
                Integer lineNumber = ox.getInt("lineNumber");
                orderInformation.setLineNumber(lineNumber);
                //产品编号
                if (StringUtils.isNotBlank(ox.getString("spbh"))) {
                    throw new RuntimeException("产品编号不能为空");
                }
                String productNo = obj.getString("spbh");

                Commodity commodity = getCommitityFromName(productNo);
                if (commodity == null) {
                    throw new RuntimeException("该商品不存在");
                }
                orderInformation.setProductNo(commodity);
                //规格/型号
                String specifications = StringUtils.isNotBlank(obj.getString("spgg"))
                        ? obj.getString("spgg") : null;
                orderInformation.setSpecifications(specifications);
                //包装单位
                String packagingUnit = StringUtils.isNotBlank(obj.getString("bzdw"))
                        ? obj.getString("bzdw") : null;
                orderInformation.setPackagingUnit(packagingUnit);
                //生产企业名称
                String manufacturer = StringUtils.isNotBlank(obj.getString("scqymc"))
                        ? obj.getString("scqymc") : null;

                orderInformation.setManufacturer(manufacturer);
                //客户订货/退货数量
                if (StringUtils.isNotBlank(ox.getString("dhsl"))) {
                    throw new RuntimeException("订货数量不能为空");
                }
                Integer orderQuantity = StringUtils.isNotBlank(obj.getString("dhsl"))
                        ? obj.getInt("dhsl") : null;
                orderInformation.setOrderQuantity(orderQuantity);
                //供应商发货数量
                Integer shipmentQuantity = StringUtils.isNotBlank(obj.getString("GYSfhsl"))
                        ? obj.getInt("GYSfhsl") : null;

                orderInformation.setShipmentQuantity(shipmentQuantity);
                //批号
                String PH = StringUtils.isNotBlank(obj.getString("ph"))
                        ? obj.getString("ph") : null;
                orderInformation.setPH(PH);
                //序列号
                String serialNumber = StringUtils.isNotBlank(obj.getString("xlh"))
                        ? obj.getString("xlh") : null;
                orderInformation.setSerialNumber(serialNumber);

                //生产日期
                Date manufacturerDate = StringUtils.isNotBlank(obj.getString("scrq"))
                        ? new Date(obj.getLong("scrq")) : null;
                orderInformation.setManufacturerDate(manufacturerDate);
                //有效期
                Date effectiveDate = StringUtils.isNotBlank(obj.getString("yxq"))
                        ? new Date(obj.getLong("yxq")) : null;
                orderInformation.setEffectiveDate(effectiveDate);
                //客户产品备注
                String KHRemarksx = StringUtils.isNotBlank(obj.getString("KHspbz"))
                        ? obj.getString("KHspbz") : null;
                orderInformation.setKHRemarks(KHRemarksx);
                //供应商产品备注
                String GYSRemarksx = StringUtils.isNotBlank(obj.getString("GYSspbz"))
                        ? obj.getString("GYSspbz") : null;
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
            if (StringUtils.isNotBlank(obj.getString("ddbh"))) {
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
    public String downloadOrederResult(String json) {
        String checkResult = check(json);
        if (StringUtils.isNotBlank(checkResult)) {
            return checkResult;
        }

        JSONObject object = JSONObject.fromObject(json);
        JSONArray array = object.getJSONArray("data");
        JSONObject head = object.getJSONObject("head");
        Map<String, Object> result = getResultMapForOk();
        List<Map<String, Object>> data = new ArrayList<>();
        for (Object o : array) {
            Map<String, Object> map = new HashMap<>();
            JSONObject obj = (JSONObject) o;

            //获取订单编号
            if (StringUtils.isNotBlank(obj.getString("ddbh"))) {
                throw new RuntimeException("订单编号不能为空");
            }

            //根据订单编号获取订单信息
            OrderManagement orderManagement = getOrderManageById(obj.getInt("ddbh"));

            for (Object o1 : obj.getJSONArray("items")) {
                JSONObject ox = (JSONObject) o1;
                //获取商品编号
                if (StringUtils.isNotBlank(ox.getString("spbm"))) {
                    throw new RuntimeException("商品编码不能为空");
                }
                //根据订单获取订单商品信息
                List<OrderInformation> orderInformations = getOrderInformationById(orderManagement.getId(),ox.getString("spbm"));
                OrderInformation orderInformation = orderInformations.get(0);
                //实际销售数量
                if (StringUtils.isNotBlank(ox.getString("xssl"))) {
                    throw new RuntimeException("实际销售数量不能为空");
                }
                orderInformation.setShipmentQuantity(ox.getInt("xssl"));
                //商品批号
                if (StringUtils.isNotBlank(ox.getString("spph"))) {
                    throw new RuntimeException("商品批号不能为空");
                }
                orderInformation.setPH(ox.getString("spph"));
                //有效期
                if (StringUtils.isNotBlank(ox.getString("yxq"))) {
                    throw new RuntimeException("商品有效期不能为空");
                }
                orderInformation.setEffectiveDate(new Date(ox.getLong("yxq") ));
                getService().update(orderInformation);
            }

            //更改状态为录入
            orderManagement.setOrderStatus(DicCache.get("orderStatus","录入"));
            getService().update(orderManagement);

            map.put("ddbh", orderManagement.getId());
            map.put("code", ResultCode.SUCCESS);
            map.put("message", "操作成功");
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
            if (StringUtils.isNotBlank(obj.getString("ddbh"))) {
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
            jpql += "and o.productNo.number =:number";

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
