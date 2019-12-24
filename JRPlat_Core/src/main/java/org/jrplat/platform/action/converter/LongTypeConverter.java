/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.platform.action.converter;

/**
 *日期转换
 * @author 西安捷然
 */

import org.apache.struts2.util.StrutsTypeConverter;
import org.jrplat.platform.log.JRPlatLogger;
import org.jrplat.platform.log.JRPlatLoggerFactory;

import java.util.Map;

public class LongTypeConverter extends StrutsTypeConverter {
    private static final JRPlatLogger LOG = JRPlatLoggerFactory.getJRPlatLogger(LongTypeConverter.class);


    @Override
    public Object convertFromString(Map context, String[] values, Class toClass) {
        if (values[0] == null || values[0].trim().equals("")) {
            return 0;
        }
        try {
            return Long.parseLong(values[0].trim());
        } catch (Exception e) {
            LOG.info("字符串:" + values[0].trim() + "转换为数字失败");
        }
        return 0;
    }

    @Override
    public String convertToString(Map context, Object o) {
        if (o == null) {
            return "0";
        }
        return o.toString();
    }
}