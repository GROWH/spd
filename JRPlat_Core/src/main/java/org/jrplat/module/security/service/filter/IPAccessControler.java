/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.security.service.filter;

import org.apache.commons.lang.StringUtils;
import org.jrplat.platform.log.JRPlatLogger;
import org.jrplat.platform.log.JRPlatLoggerFactory;
import org.jrplat.platform.util.FileUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

/**
 *IP地址访问限制
 * @author 西安捷然
 */
public class IPAccessControler {
    private static final JRPlatLogger LOG = JRPlatLoggerFactory.getJRPlatLogger(IPAccessControler.class);

    private Collection<String> allow;
    private Collection<String> deny;

    public IPAccessControler() {
        reInit();
    }

    public static void main(String[] args) {
        System.out.println("127.0.0.1".matches("127.0.*.*"));
    }

    public final void reInit() {
        allow = FileUtils.getTextFileContent("/WEB-INF/ip/allow.txt");
        deny = FileUtils.getTextFileContent("/WEB-INF/ip/deny.txt");
    }

    public boolean deny(HttpServletRequest request) {
        if (request == null) {
            return false;
        }
        try {
            String ip = getIpAddr(request);
            if (ip == null) {
                LOG.info("无法获取到访问者的IP");
                return true;
            }

            if (hasMatch(ip, deny)) {
                LOG.info("ip: " + ip + " 位于黑名单中");
                return true;
            }

            if (!allow.isEmpty() && !hasMatch(ip, allow)) {
                LOG.info("ip: " + ip + " 没有位于白名单中");
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    private boolean hasMatch(String ip, Collection<String> regExps) {
        for (String regExp : regExps) {
            try {
                if (ip.matches(regExp)) {
                    return true;
                }
            } catch (Exception e) {
            }
        }

        return false;
    }

    private String getIpAddr(HttpServletRequest request) {
        String ipString = null;
        String temp = request.getHeader("x-forwarded-for");
        if (temp.indexOf(":") == -1 && temp.indexOf(".") != -1) {
            ipString = temp;
        }
        if (StringUtils.isBlank(ipString) || "unknown".equalsIgnoreCase(ipString)) {
            temp = request.getHeader("Proxy-Client-IP");
            if (temp.indexOf(":") == -1 && temp.indexOf(".") != -1) {
                ipString = temp;
            }
        }
        if (StringUtils.isBlank(ipString) || "unknown".equalsIgnoreCase(ipString)) {
            temp = request.getHeader("WL-Proxy-Client-IP");
            if (temp.indexOf(":") == -1 && temp.indexOf(".") != -1) {
                ipString = temp;
            }
        }
        if (StringUtils.isBlank(ipString) || "unknown".equalsIgnoreCase(ipString)) {
            temp = request.getRemoteAddr();
            if (temp.indexOf(":") == -1 && temp.indexOf(".") != -1) {
                ipString = temp;
            }
        }

        // 多个路由时，取第一个非unknown的ip
        final String[] arr = ipString.split(",");
        for (final String str : arr) {
            if (!"unknown".equalsIgnoreCase(str) && str.indexOf(":") == -1 && str.split(".").length == 4) {
                ipString = str;
                break;
            }
        }

        return ipString;
    }
}