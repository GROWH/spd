/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.platform.action.converter;

import org.apache.struts2.util.StrutsTypeConverter;
import org.jrplat.platform.log.JRPlatLogger;
import org.jrplat.platform.log.JRPlatLoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


/**
 *日期转换
 * @author 西安捷然
 */

public class DateTypeConverter extends StrutsTypeConverter {
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    //暂时只考虑这几种日期格式
    public static final DateFormat[] ACCEPT_DATE_FORMATS = {
            new SimpleDateFormat(DEFAULT_DATE_FORMAT),
            new SimpleDateFormat("yyyy年MM月dd日"),
            new SimpleDateFormat("yyyy/MM/dd")};
    private static final JRPlatLogger LOG = JRPlatLoggerFactory.getJRPlatLogger(DateTypeConverter.class);

    public static Date fromString(String date) {
        if (date == null) {
            return null;
        }
        Date r = null;
        try {
            r = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException ex) {
            LOG.debug(date + "转换成日期失败");
        }
        return r;
    }

    public static String toDefaultDate(Date date) {
        if (date == null) {
            return "";
        }
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    public static String toSwitchContentDateTime(Date date) {
        if (date == null) {
            return "";
        }
        return new SimpleDateFormat("yyyyMMddHHmm").format(date);
    }

    public static String toSwitchContentDate(Date date) {
        if (date == null) {
            return "";
        }
        return new SimpleDateFormat("yyyyMMdd").format(date);
    }

    public static String toDefaultDateTime(Date date) {
        if (date == null) {
            return "";
        }
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    public static Date fromDefaultDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (Exception ex) {
            LOG.debug(ex.getMessage());
        }
        return null;
    }

    public static Date fromDefaultDateTime(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
        } catch (Exception ex) {
            LOG.debug(ex.getMessage());
        }
        return null;
    }

    public static String toFileName(Date date) {
        if (date == null) {
            return "";
        }
        return new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(date);
    }

    @Override
    public Object convertFromString(Map context, String[] values, Class toClass) {
        if (values[0] == null || values[0].trim().equals("")) {
            return null;
        }
        if (values[0].contains(":")) {
            try {
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(values[0]);
            } catch (ParseException e) {
                LOG.error("转换失败", e);
            }
        }
        for (DateFormat format : ACCEPT_DATE_FORMATS) {
            try {
                return format.parse(values[0]);
            } catch (ParseException | RuntimeException e) {
                continue;
            }
        }
        LOG.debug("can not format date string:" + values[0]);
        return null;
    }

    @Override
    public String convertToString(Map context, Object o) {
        if (o instanceof Date) {
            SimpleDateFormat format = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
            try {
                return format.format((Date) o);
            } catch (RuntimeException e) {
                return "";
            }
        }
        return "";
    }
}