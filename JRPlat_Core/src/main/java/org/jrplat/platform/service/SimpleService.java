package org.jrplat.platform.service;
/**
 * Created by 赵腾飞 on 2016/7/12.
 */

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.jrplat.module.system.service.PropertyHolder;
import org.jrplat.platform.criteria.Property;
import org.jrplat.platform.log.JRPlatLogger;
import org.jrplat.platform.log.JRPlatLoggerFactory;
import org.jrplat.platform.model.Model;
import org.jrplat.platform.model.ModelMetaData;
import org.jrplat.platform.util.ReflectionUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * service 类的 基类, 包含了常用属性和数据库连接的获取
 *
 * @param <T> 对应的model
 */
public class SimpleService<T extends Model> {
    /**
     * log
     */
    protected final JRPlatLogger LOG = JRPlatLoggerFactory.getJRPlatLogger(getClass());
    protected final String shortAppName = PropertyHolder.getProperty("module.short.name");
    /**
     * 对任何继承自Model的类进行数据存储操作
     */
    @Resource(name = "serviceFacade")
    protected ServiceFacade serviceFacade;
    private String ids;

    protected ServiceFacade getService() {
        return serviceFacade;
    }

    /**
     * 新建模型
     *
     * @param model
     * @param detail
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean create(T model, String detail) {
        try {
            model.setId(null);
            objectReference(model);     //组装
            beforeCreateModel(model, detail);
            getService().create(model);
            afterCreateModel(model, detail);
        } catch (Exception e) {
            LOG.error("创建模型失败", e);
            throw new RuntimeException(e.getMessage());
        }
        return true;
    }

    /**
     * 创建model之前
     *
     * @param model
     * @param detail
     */
    protected void beforeCreateModel(T model, String detail) {

    }

    /**
     * 创建model之后
     *
     * @param model
     * @param detail
     */
    protected void afterCreateModel(T model, String detail) {

    }

    protected void objectReference(T model) {
        Field[] fields = model.getClass().getDeclaredFields();//获得对象方法集合
        for (Field field : fields) {// 遍历该数组
            if (field.isAnnotationPresent(ManyToOne.class) || field.isAnnotationPresent(OneToOne.class)) {
                LOG.debug(model.getMetaData() + " 有ManyToOne 或 OneToOne映射，字段为" + field.getName());
                Model value = (Model) ReflectionUtils.getFieldValue(model, field);
                if (value == null) {
                    LOG.debug(model.getMetaData() + " 的字段" + field.getName() + "没有值，忽略处理");
                    continue;
                }
                int id = value.getId();
                LOG.debug("id: " + id);
                value = getService().retrieve(value.getClass(), id);
                ReflectionUtils.setFieldValue(model, field, value);
            }
        }
    }


    public String getCellData(Row row, int index) {
        Cell cell = row.getCell(index);
        if (cell == null) {
            return "";
        }
        // 强制转换为字符串
        cell.setCellType(Cell.CELL_TYPE_STRING);
        String cellString = getValue(cell);
        if (cellString == null || cellString.equals("")) {
            return "";
        }
        return cellString;
    }

    /**
     * 得到Excel表中的值
     *
     * @param cell
     *            Excel中的每一个格子
     * @return Excel中每一个格子中的值
     */
    protected String getValue(Cell cell) {
        if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
            // 返回布尔类型的值
            return String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            // 返回数值类型的值
            return String.valueOf(cell.getNumericCellValue());
        } else {
            // 返回字符串类型的值
            return String.valueOf(cell.getStringCellValue());
        }
    }

    /**
     * 更新模型，
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePart(T model, Enumeration<?> pars, String detail) {
        try {
            beforeCkeckVersion(model);
            Integer version = model.getVersion();
            //此时的model里面存的值是从浏览器传输过来的
            List<Property> properties = getPartProperties(model, pars);
            //此时的model里面存的值是从数据库里面加载的
            model = (T) getService().retrieve(model.getClass(), model.getId());
            //数据版本控制，防止多个用户同时修改一条数据，造成更新丢失问题
            if (version == null) {
                LOG.info("前台界面没有传递版本信息");
                throw new RuntimeException("您的数据没有版本信息");
            } else {
                LOG.info("前台界面传递了版本信息,version=" + version);
            }
            if (version != model.getVersion()) {
                LOG.info("当前数据的版本为 " + model.getVersion() + ",您的版本为 " + version);
                throw new RuntimeException("您的数据已过期，请重新修改");
            }

            beforeSetValueForModel(model, detail);
            for (Property property : properties) {
                //把从浏览器传来的值射入model
                if (property.getName().contains(".")) {
                    //处理两个对象之间的引用，如：model.org.id=1
                    if (property.getName().contains(".id")) {
                        String[] attr = property.getName().replace(".", ",").split(",");
                        if (attr.length == 2) {
                            Field field = ReflectionUtils.getDeclaredField(model, attr[0]);
                            T change = getService().retrieve((Class<T>) field.getType(), (Integer) property.getValue());
                            ReflectionUtils.setFieldValue(model, attr[0], change);
                        }
                    }
                } else {
                    ReflectionUtils.setFieldValue(model, property.getName(), property.getValue());
                }
            }

            //在更新前调用模板方法对模型进行处理
            afterSetValueForModel(model, detail);
            getService().update(model);
            afterPartUpdateModel(model, detail);
        } catch (Exception e) {
            LOG.error("更新模型失败", e);
            throw new RuntimeException(e.getMessage());
        }
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public String delete(Class deleteClass, Integer[] ids) {
        try {
            prepareForDelete(ids, null);
            List<Integer> deletedIds = getService().delete(deleteClass, ids);
            afterDelete(deletedIds);
        } catch (Exception e) {
            LOG.info("删除数据出错", e);
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    /**
     * 删除后执行
     *
     * @param deletedIds
     */
    protected void afterDelete(List<Integer> deletedIds) {

    }

    /**
     * 删除前执行
     *
     * @param ids
     * @param ignore 检查约束时忽略某些关联. eg: 要删除a时，b中有a的外检，要忽略检查b时传入b的名称.
     */
    public void prepareForDelete(Integer[] ids, List<String> ignore) {
        StringBuilder ids_s = new StringBuilder();
        for (int i = 0; i < ids.length; i++) {
            if (i < (ids.length - 1)) {
                ids_s.append(ids[i] + ",");
            } else {
                ids_s.append(ids[i]);
            }
        }
        String tableName = getDefaultModelName();
        if (tableName == null) {
            LOG.error("获取对象对应的数据库表名失败！不进行删除约束检查，删除可能失败！");
            return;
        }
        if (tableName.equals("user")) {
            tableName = "usertable";  //对应user对应表为usertable，其它的均和类名一致
        }
        String sql = "select TABLE_NAME,REFERENCED_TABLE_NAME,COLUMN_NAME,REFERENCED_COLUMN_NAME "
                + " from information_schema.KEY_COLUMN_USAGE "
                + " where table_schema='"
                + shortAppName + "' "
                + "		and constraint_name<>'primary' "
                + "		and REFERENCED_TABLE_SCHEMA='" +
                shortAppName + "' "
                + "		and REFERENCED_TABLE_NAME='" + tableName + "'";
        LOG.info("检查约束SQL：" + sql);

        List<Object[]> result = getService().getEntityManager()
                .createNativeQuery(sql).getResultList();
        for (int i = 0; i < result.size(); i++) {
            Object[] temp = result.get(i);
            if ((ignore != null && ignore.contains(temp[0])) || temp[0].toString().contains("_")) {
                continue;       //忽略检查.
            }
            String tempSql = "select " + temp[2].toString() + ",id" + " from " +
                    shortAppName + "." + temp[0].toString()
                    + " where " + temp[2].toString() + " in (" + ids_s.toString() + ")";
            LOG.info("查找约束SQL：" + tempSql);
            List<Object> tempResult = getService().getEntityManager()
                    .createNativeQuery(tempSql).getResultList();
            if (tempResult.size() > 0) {
                String refModelName = ModelMetaData.getMetaData(temp[0].toString().toLowerCase());
                if (refModelName.equals("")) {
                    refModelName = "其它模块";
                }
                if (temp[0].toString().toLowerCase().equals("usertable")) {
                    refModelName = "用户信息";
                }
                String idss = "";
                for (Object o : tempResult) {
                    Object[] objects = (Object[]) o;
                    idss += objects[1].toString() + " ";
                }
                throw new RuntimeException("该数据在 《" + refModelName + "》 模块中被编号为: " + idss + " 的数据所引用，请删除引用后，再进行删除！");
            }
        }
    }

    private String getDefaultModelName() {
        return getDefaultModelName(this.getClass());
    }

    private String getDefaultModelName(Class clazz) {
        String modelClassName = ReflectionUtils.getSuperClassGenricType(clazz).getSimpleName();
        return Character.toLowerCase(modelClassName.charAt(0)) + modelClassName.substring(1);
    }

    /**
     * 设置模型数据之前执行，此时model中数据时从数据库加载的。
     *
     * @param model
     * @param detail
     */
    protected void beforeSetValueForModel(T model, String detail) {

    }

    /**
     * 更新model之后执行
     *
     * @param model
     * @param detail
     */
    protected void afterPartUpdateModel(T model, String detail) {

    }

    /**
     * 设置数据之前的model
     *
     * @param model
     * @param detail
     */
    protected void afterSetValueForModel(T model, String detail) {

    }

    private List<Property> getPartProperties(T model, Enumeration<?> pars) {
        List<Property> properties = new ArrayList<>();
        while (pars.hasMoreElements()) {
            String par = (String) pars.nextElement();
            if (par.startsWith("model.") && !par.equals("model.id")) {
                String prop = par.replace("model.", "");
                if (prop.contains(".")) {
                    if (prop.contains(".id")) {
                        //处理两个对象之间的引用，如：model.org.id=1
                        String[] attr = prop.replace(".", ",").split(",");
                        if (attr.length == 2) {
                            Object obj = ReflectionUtils.getFieldValue(model, attr[0]);
                            properties.add(new Property(prop, ReflectionUtils.getFieldValue(obj, attr[1])));
                        }
                    }
                } else {
                    properties.add(new Property(prop, ReflectionUtils.getFieldValue(model, prop)));
                }
            }
        }
        return properties;
    }

    /**
     * 检查版本过期前执行，此时model中的数据是客户端传递的
     *
     * @param model
     */
    protected void beforeCkeckVersion(T model) {

    }

    /**
     * 将字符串用 "," 分割转换为数字
     *
     * @return 转换后的数组
     */
    public Integer[] getIds() {
        if (StringUtils.isNotEmpty(ids)) {
            String[] strings = ids.split(",");
            Integer[] integers = new Integer[strings.length];
            for (int i = 0; i < strings.length; i++) {
                integers[i] = Integer.parseInt(strings[i]);
            }
            return integers;
        }
        return null;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public List<Integer> getIdsList(Integer[] ids) {
        List<Integer> idList = new ArrayList<>();
        Collections.addAll(idList, ids);
        return idList;
    }
}