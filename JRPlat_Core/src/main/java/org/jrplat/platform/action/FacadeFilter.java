/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.platform.action;

import org.jrplat.platform.log.JRPlatLogger;
import org.jrplat.platform.log.JRPlatLoggerFactory;
import org.jrplat.platform.model.Model;
import org.jrplat.platform.util.SpringContextUtils;

import javax.servlet.*;
import java.io.IOException;

/**
 *
 * @author 西安捷然
 */
public class FacadeFilter implements Filter {
    private static final JRPlatLogger LOG = JRPlatLoggerFactory.getJRPlatLogger(FacadeFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String modelName = request.getParameter("modelName");
        if (modelName != null) {
            Model model = SpringContextUtils.getBean(modelName);
            request.setAttribute("model", model);
            LOG.info("用户使用facade action,modelName=" + modelName);
        }
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig fc) throws ServletException {
        LOG.info("初始化facade filter");
    }

    @Override
    public void destroy() {
        LOG.info("销毁facade filter");
    }
}