/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.security.service.filter;

import org.jrplat.module.security.service.SpringSecurityService;
import org.jrplat.module.security.service.UserDetailsServiceImpl;
import org.jrplat.module.security.service.UserHolder;
import org.jrplat.module.system.service.PropertyHolder;
import org.jrplat.platform.log.JRPlatLogger;
import org.jrplat.platform.log.JRPlatLoggerFactory;
import org.jrplat.platform.util.SpringContextUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 *
 * @author 西安捷然
 */
public class AutoLoginFilter implements Filter {
    private static final JRPlatLogger LOG = JRPlatLoggerFactory.getJRPlatLogger(AutoLoginFilter.class);

    private UserDetailsServiceImpl userDetailsServiceImpl;
    private boolean enabled = false;
    private String defaultUserName;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        if (enabled && !UserHolder.hasLogin()) {
            if (userDetailsServiceImpl == null) {
                userDetailsServiceImpl = SpringContextUtils.getBean("userDetailsServiceImpl");
            }
            if (userDetailsServiceImpl != null) {
                UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(defaultUserName);

                UserHolder.saveUserDetailsToContext(userDetails, (HttpServletRequest) request);
                for (GrantedAuthority au : userDetails.getAuthorities()) {
                    LOG.info("\t" + au.getAuthority());
                }
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig fc) throws ServletException {
        LOG.info("初始化自动登录过滤器(Initialize the automatic login filter)");
        enabled = !SpringSecurityService.isSecurity();
        defaultUserName = PropertyHolder.getProperty("auto.login.username");
        if (enabled) {
            LOG.info("启用自动登录过滤器(Enable automatic login filter)");
        } else {
            LOG.info("禁用自动登录过滤器(Disable automatic login filter)");
        }
    }

    @Override
    public void destroy() {
        LOG.info("销毁自动登录过滤器(Destroy the automatic login filter)");
    }
}