/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.platform.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

import java.io.Serializable;
import java.util.Locale;

/**
 *日志输出支持多国语言切换解决方案实现
 * @author 西安捷然
 */
class JRPlatLoggerImpl implements JRPlatLogger, Serializable {
    private static final long serialVersionUID = 1L;
    //默认使用中文日志输出，在PropertyHolder类加载完配置之后，会根据配置文件的指定重新设置locale的值
    private static Locale locale = Locale.CHINA;
    private Logger log = null;

    public JRPlatLoggerImpl(Class clazz) {
        log = LoggerFactory.getLogger(clazz);
    }

    @Override
    public Locale getLocale() {
        return locale;
    }

    @Override
    public void setLocale(Locale locale) {
        JRPlatLoggerImpl.locale = locale;
    }

    /**
     * 是否输出指定语言的日志
     * @param specifyLocale 要输出的日志使用的语言
     * @return 是或否
     */
    private boolean shouldOutput(Locale specifyLocale) {
        if (specifyLocale == null || locale == null) {
            return false;
        }
        return locale.getLanguage().equalsIgnoreCase(specifyLocale.getLanguage());
    }

    @Override
    public void trace(String msg) {
        trace(msg, Locale.CHINA);
    }

    @Override
    public void trace(String msg, Locale locale) {
        if (shouldOutput(locale)) {
            log.trace(msg);
        }
    }

    @Override
    public void trace(String format, Object arg) {
        trace(format, arg, Locale.CHINA);
    }

    @Override
    public void trace(String format, Object arg, Locale locale) {
        if (shouldOutput(locale)) {
            log.trace(format, arg);
        }
    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {
        trace(format, arg1, arg2, Locale.CHINA);
    }

    @Override
    public void trace(String format, Object arg1, Object arg2, Locale locale) {
        if (shouldOutput(locale)) {
            log.trace(format, arg1, arg2);
        }
    }

    @Override
    public void trace(String format, Object[] argArray) {
        trace(format, argArray, Locale.CHINA);
    }

    @Override
    public void trace(String format, Object[] argArray, Locale locale) {
        if (shouldOutput(locale)) {
            log.trace(format, argArray);
        }
    }

    @Override
    public void trace(String msg, Throwable t) {
        trace(msg, t, Locale.CHINA);
    }

    @Override
    public void trace(String msg, Throwable t, Locale locale) {
        if (shouldOutput(locale)) {
            log.trace(msg, t);
        }
    }

    @Override
    public void trace(Marker marker, String msg) {
        trace(marker, msg, Locale.CHINA);
    }

    @Override
    public void trace(Marker marker, String msg, Locale locale) {
        if (shouldOutput(locale)) {
            log.trace(marker, msg);
        }
    }

    @Override
    public void trace(Marker marker, String format, Object arg) {
        trace(marker, format, arg, Locale.CHINA);
    }

    @Override
    public void trace(Marker marker, String format, Object arg, Locale locale) {
        if (shouldOutput(locale)) {
            log.trace(marker, format, arg);
        }
    }

    @Override
    public void trace(Marker marker, String format, Object arg1, Object arg2) {
        trace(marker, format, arg1, arg2, Locale.CHINA);
    }

    @Override
    public void trace(Marker marker, String format, Object arg1, Object arg2, Locale locale) {
        if (shouldOutput(locale)) {
            log.trace(marker, format, arg1, arg2);
        }
    }

    @Override
    public void trace(Marker marker, String format, Object[] argArray) {
        trace(marker, format, argArray, Locale.CHINA);
    }

    @Override
    public void trace(Marker marker, String format, Object[] argArray, Locale locale) {
        if (shouldOutput(locale)) {
            log.trace(marker, format, argArray);
        }
    }

    @Override
    public void trace(Marker marker, String msg, Throwable t) {
        trace(marker, msg, t, Locale.CHINA);
    }

    @Override
    public void trace(Marker marker, String msg, Throwable t, Locale locale) {
        if (shouldOutput(locale)) {
            log.trace(marker, msg, t);
        }
    }


    @Override
    public void debug(String msg) {
        debug(msg, Locale.CHINA);
    }

    @Override
    public void debug(String msg, Locale locale) {
        if (shouldOutput(locale)) {
            log.debug(msg);
        }
    }

    @Override
    public void debug(String format, Object arg) {
        debug(format, arg, Locale.CHINA);
    }

    @Override
    public void debug(String format, Object arg, Locale locale) {
        if (shouldOutput(locale)) {
            log.debug(format, arg);
        }
    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {
        debug(format, arg1, arg2, Locale.CHINA);
    }

    @Override
    public void debug(String format, Object arg1, Object arg2, Locale locale) {
        if (shouldOutput(locale)) {
            log.debug(format, arg1, arg2);
        }
    }

    @Override
    public void debug(String format, Object[] argArray) {
        debug(format, argArray, Locale.CHINA);
    }

    @Override
    public void debug(String format, Object[] argArray, Locale locale) {
        if (shouldOutput(locale)) {
            log.debug(format, argArray);
        }
    }

    @Override
    public void debug(String msg, Throwable t) {
        debug(msg, t, Locale.CHINA);
    }

    @Override
    public void debug(String msg, Throwable t, Locale locale) {
        if (shouldOutput(locale)) {
            log.debug(msg, t);
        }
    }

    @Override
    public void debug(Marker marker, String msg) {
        debug(marker, msg, Locale.CHINA);
    }

    @Override
    public void debug(Marker marker, String msg, Locale locale) {
        if (shouldOutput(locale)) {
            log.debug(marker, msg);
        }
    }

    @Override
    public void debug(Marker marker, String format, Object arg) {
        debug(marker, format, arg, Locale.CHINA);
    }

    @Override
    public void debug(Marker marker, String format, Object arg, Locale locale) {
        if (shouldOutput(locale)) {
            log.debug(marker, format, arg);
        }
    }

    @Override
    public void debug(Marker marker, String format, Object arg1, Object arg2) {
        debug(marker, format, arg1, arg2, Locale.CHINA);
    }

    @Override
    public void debug(Marker marker, String format, Object arg1, Object arg2, Locale locale) {
        if (shouldOutput(locale)) {
            log.debug(marker, format, arg1, arg2);
        }
    }

    @Override
    public void debug(Marker marker, String format, Object[] argArray) {
        debug(marker, format, argArray, Locale.CHINA);
    }

    @Override
    public void debug(Marker marker, String format, Object[] argArray, Locale locale) {
        if (shouldOutput(locale)) {
            log.debug(marker, format, argArray);
        }
    }

    @Override
    public void debug(Marker marker, String msg, Throwable t) {
        debug(marker, msg, t, Locale.CHINA);
    }

    @Override
    public void debug(Marker marker, String msg, Throwable t, Locale locale) {
        if (shouldOutput(locale)) {
            log.debug(marker, msg, t);
        }
    }


    @Override
    public void info(String msg) {
        info(msg, Locale.CHINA);
    }

    @Override
    public void info(String msg, Locale locale) {
        if (shouldOutput(locale)) {
            log.info(msg);
        }
    }

    @Override
    public void info(String format, Object arg) {
        info(format, arg, Locale.CHINA);
    }

    @Override
    public void info(String format, Object arg, Locale locale) {
        if (shouldOutput(locale)) {
            log.info(format, arg);
        }
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        info(format, arg1, arg2, Locale.CHINA);
    }

    @Override
    public void info(String format, Object arg1, Object arg2, Locale locale) {
        if (shouldOutput(locale)) {
            log.info(format, arg1, arg2);
        }
    }

    @Override
    public void info(String format, Object[] argArray) {
        info(format, argArray, Locale.CHINA);
    }

    @Override
    public void info(String format, Object[] argArray, Locale locale) {
        if (shouldOutput(locale)) {
            log.info(format, argArray);
        }
    }

    @Override
    public void info(String msg, Throwable t) {
        info(msg, t, Locale.CHINA);
    }

    @Override
    public void info(String msg, Throwable t, Locale locale) {
        if (shouldOutput(locale)) {
            log.info(msg, t);
        }
    }

    @Override
    public void info(Marker marker, String msg) {
        info(marker, msg, Locale.CHINA);
    }

    @Override
    public void info(Marker marker, String msg, Locale locale) {
        if (shouldOutput(locale)) {
            log.info(marker, msg);
        }
    }

    @Override
    public void info(Marker marker, String format, Object arg) {
        info(marker, format, arg, Locale.CHINA);
    }

    @Override
    public void info(Marker marker, String format, Object arg, Locale locale) {
        if (shouldOutput(locale)) {
            log.info(marker, format, arg);
        }
    }

    @Override
    public void info(Marker marker, String format, Object arg1, Object arg2) {
        info(marker, format, arg1, arg2, Locale.CHINA);
    }

    @Override
    public void info(Marker marker, String format, Object arg1, Object arg2, Locale locale) {
        if (shouldOutput(locale)) {
            log.info(marker, format, arg1, arg2);
        }
    }

    @Override
    public void info(Marker marker, String format, Object[] argArray) {
        info(marker, format, argArray, Locale.CHINA);
    }

    @Override
    public void info(Marker marker, String format, Object[] argArray, Locale locale) {
        if (shouldOutput(locale)) {
            log.info(marker, format, argArray);
        }
    }

    @Override
    public void info(Marker marker, String msg, Throwable t) {
        info(marker, msg, t, Locale.CHINA);
    }

    @Override
    public void info(Marker marker, String msg, Throwable t, Locale locale) {
        if (shouldOutput(locale)) {
            log.info(marker, msg, t);
        }
    }


    @Override
    public void warn(String msg) {
        warn(msg, Locale.CHINA);
    }

    @Override
    public void warn(String msg, Locale locale) {
        if (shouldOutput(locale)) {
            log.warn(msg);
        }
    }

    @Override
    public void warn(String format, Object arg) {
        warn(format, arg, Locale.CHINA);
    }

    @Override
    public void warn(String format, Object arg, Locale locale) {
        if (shouldOutput(locale)) {
            log.warn(format, arg);
        }
    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        warn(format, arg1, arg2, Locale.CHINA);
    }

    @Override
    public void warn(String format, Object arg1, Object arg2, Locale locale) {
        if (shouldOutput(locale)) {
            log.warn(format, arg1, arg2);
        }
    }

    @Override
    public void warn(String format, Object[] argArray) {
        warn(format, argArray, Locale.CHINA);
    }

    @Override
    public void warn(String format, Object[] argArray, Locale locale) {
        if (shouldOutput(locale)) {
            log.warn(format, argArray);
        }
    }

    @Override
    public void warn(String msg, Throwable t) {
        warn(msg, t, Locale.CHINA);
    }

    @Override
    public void warn(String msg, Throwable t, Locale locale) {
        if (shouldOutput(locale)) {
            log.warn(msg, t);
        }
    }

    @Override
    public void warn(Marker marker, String msg) {
        warn(marker, msg, Locale.CHINA);
    }

    @Override
    public void warn(Marker marker, String msg, Locale locale) {
        if (shouldOutput(locale)) {
            log.warn(marker, msg);
        }
    }

    @Override
    public void warn(Marker marker, String format, Object arg) {
        warn(marker, format, arg, Locale.CHINA);
    }

    @Override
    public void warn(Marker marker, String format, Object arg, Locale locale) {
        if (shouldOutput(locale)) {
            log.warn(marker, format, arg);
        }
    }

    @Override
    public void warn(Marker marker, String format, Object arg1, Object arg2) {
        warn(marker, format, arg1, arg2, Locale.CHINA);
    }

    @Override
    public void warn(Marker marker, String format, Object arg1, Object arg2, Locale locale) {
        if (shouldOutput(locale)) {
            log.warn(marker, format, arg1, arg2);
        }
    }

    @Override
    public void warn(Marker marker, String format, Object[] argArray) {
        warn(marker, format, argArray, Locale.CHINA);
    }

    @Override
    public void warn(Marker marker, String format, Object[] argArray, Locale locale) {
        if (shouldOutput(locale)) {
            log.warn(marker, format, argArray);
        }
    }

    @Override
    public void warn(Marker marker, String msg, Throwable t) {
        warn(marker, msg, t, Locale.CHINA);
    }

    @Override
    public void warn(Marker marker, String msg, Throwable t, Locale locale) {
        if (shouldOutput(locale)) {
            log.warn(marker, msg, t);
        }
    }


    @Override
    public void error(String msg) {
        error(msg, Locale.CHINA);
    }

    @Override
    public void error(String msg, Locale locale) {
        if (shouldOutput(locale)) {
            log.error(msg);
        }
    }

    @Override
    public void error(String format, Object arg) {
        error(format, arg, Locale.CHINA);
    }

    @Override
    public void error(String format, Object arg, Locale locale) {
        if (shouldOutput(locale)) {
            log.error(format, arg);
        }
    }

    @Override
    public void error(String format, Object arg1, Object arg2) {
        error(format, arg1, arg2, Locale.CHINA);
    }

    @Override
    public void error(String format, Object arg1, Object arg2, Locale locale) {
        if (shouldOutput(locale)) {
            log.error(format, arg1, arg2);
        }
    }

    @Override
    public void error(String format, Object[] argArray) {
        error(format, argArray, Locale.CHINA);
    }

    @Override
    public void error(String format, Object[] argArray, Locale locale) {
        if (shouldOutput(locale)) {
            log.error(format, argArray);
        }
    }

    @Override
    public void error(String msg, Throwable t) {
        error(msg, t, Locale.CHINA);
    }

    @Override
    public void error(String msg, Throwable t, Locale locale) {
        if (shouldOutput(locale)) {
            log.error(msg, t);
        }
    }

    @Override
    public void error(Marker marker, String msg) {
        error(marker, msg, Locale.CHINA);
    }

    @Override
    public void error(Marker marker, String msg, Locale locale) {
        if (shouldOutput(locale)) {
            log.error(marker, msg);
        }
    }

    @Override
    public void error(Marker marker, String format, Object arg) {
        error(marker, format, arg, Locale.CHINA);
    }

    @Override
    public void error(Marker marker, String format, Object arg, Locale locale) {
        if (shouldOutput(locale)) {
            log.error(marker, format, arg);
        }
    }

    @Override
    public void error(Marker marker, String format, Object arg1, Object arg2) {
        error(marker, format, arg1, arg2, Locale.CHINA);
    }

    @Override
    public void error(Marker marker, String format, Object arg1, Object arg2, Locale locale) {
        if (shouldOutput(locale)) {
            log.error(marker, format, arg1, arg2);
        }
    }

    @Override
    public void error(Marker marker, String format, Object[] argArray) {
        error(marker, format, argArray, Locale.CHINA);
    }

    @Override
    public void error(Marker marker, String format, Object[] argArray, Locale locale) {
        if (shouldOutput(locale)) {
            log.error(marker, format, argArray);
        }
    }

    @Override
    public void error(Marker marker, String msg, Throwable t) {
        error(marker, msg, t, Locale.CHINA);
    }

    @Override
    public void error(Marker marker, String msg, Throwable t, Locale locale) {
        if (shouldOutput(locale)) {
            log.error(marker, msg, t);
        }
    }


    @Override
    public String getName() {
        return log.getName();
    }

    @Override
    public boolean isTraceEnabled() {
        return log.isTraceEnabled();
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return log.isTraceEnabled(marker);
    }

    @Override
    public boolean isDebugEnabled() {
        return log.isDebugEnabled();
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return log.isDebugEnabled(marker);
    }

    @Override
    public boolean isInfoEnabled() {
        return log.isInfoEnabled();
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return log.isInfoEnabled(marker);
    }

    @Override
    public boolean isWarnEnabled() {
        return log.isWarnEnabled();
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return log.isWarnEnabled(marker);
    }

    @Override
    public boolean isErrorEnabled() {
        return log.isErrorEnabled();
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return log.isErrorEnabled(marker);
    }
}
