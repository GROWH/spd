/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.platform.log;

import org.slf4j.Logger;
import org.slf4j.Marker;

import java.util.Locale;

/**
 * 日志输出支持多国语言切换解决方案接口
 * @author 西安捷然
 */
public interface JRPlatLogger extends Logger {

    public Locale getLocale();

    public void setLocale(Locale locale);

    public void trace(String msg, Locale locale);

    public void trace(String format, Object arg, Locale locale);

    public void trace(String format, Object arg1, Object arg2, Locale locale);

    public void trace(String format, Object[] argArray, Locale locale);

    public void trace(String msg, Throwable t, Locale locale);

    public void trace(Marker marker, String msg, Locale locale);

    public void trace(Marker marker, String format, Object arg, Locale locale);

    public void trace(Marker marker, String format, Object arg1, Object arg2, Locale locale);

    public void trace(Marker marker, String format, Object[] argArray, Locale locale);

    public void trace(Marker marker, String msg, Throwable t, Locale locale);

    public void debug(String msg, Locale locale);

    public void debug(String format, Object arg, Locale locale);

    public void debug(String format, Object arg1, Object arg2, Locale locale);

    public void debug(String format, Object[] argArray, Locale locale);

    public void debug(String msg, Throwable t, Locale locale);

    public void debug(Marker marker, String msg, Locale locale);

    public void debug(Marker marker, String format, Object arg, Locale locale);

    public void debug(Marker marker, String format, Object arg1, Object arg2, Locale locale);

    public void debug(Marker marker, String format, Object[] argArray, Locale locale);

    public void debug(Marker marker, String msg, Throwable t, Locale locale);

    public void info(String msg, Locale locale);

    public void info(String format, Object arg, Locale locale);

    public void info(String format, Object arg1, Object arg2, Locale locale);

    public void info(String format, Object[] argArray, Locale locale);

    public void info(String msg, Throwable t, Locale locale);

    public void info(Marker marker, String msg, Locale locale);

    public void info(Marker marker, String format, Object arg, Locale locale);

    public void info(Marker marker, String format, Object arg1, Object arg2, Locale locale);

    public void info(Marker marker, String format, Object[] argArray, Locale locale);

    public void info(Marker marker, String msg, Throwable t, Locale locale);

    public void warn(String msg, Locale locale);

    public void warn(String format, Object arg, Locale locale);

    public void warn(String format, Object[] argArray, Locale locale);

    public void warn(String format, Object arg1, Object arg2, Locale locale);

    public void warn(String msg, Throwable t, Locale locale);

    public void warn(Marker marker, String msg, Locale locale);

    public void warn(Marker marker, String format, Object arg, Locale locale);

    public void warn(Marker marker, String format, Object arg1, Object arg2, Locale locale);

    public void warn(Marker marker, String format, Object[] argArray, Locale locale);

    public void warn(Marker marker, String msg, Throwable t, Locale locale);

    public void error(String msg, Locale locale);

    public void error(String format, Object arg, Locale locale);

    public void error(String format, Object arg1, Object arg2, Locale locale);

    public void error(String format, Object[] argArray, Locale locale);

    public void error(String msg, Throwable t, Locale locale);

    public void error(Marker marker, String msg, Locale locale);

    public void error(Marker marker, String format, Object arg, Locale locale);

    public void error(Marker marker, String format, Object arg1, Object arg2, Locale locale);

    public void error(Marker marker, String format, Object[] argArray, Locale locale);

    public void error(Marker marker, String msg, Throwable t, Locale locale);
}
