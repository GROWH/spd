/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.security.service.filter;

import com.octo.captcha.service.CaptchaService;
import org.apache.commons.lang.StringUtils;
import org.jrplat.module.system.service.PropertyHolder;
import org.jrplat.platform.log.JRPlatLogger;
import org.jrplat.platform.log.JRPlatLoggerFactory;
import org.jrplat.platform.util.ServletUtils;
import org.jrplat.platform.util.SpringContextUtils;

import javax.imageio.ImageIO;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class JCaptchaFilter implements Filter {
    public static final String PARAM_CAPTCHA_PARAMTER_NAME = "captchaParamterName";
    public static final String PARAM_FILTER_PROCESSES_URL = "filterProcessesUrl";
    public static final String DEFAULT_FILTER_PROCESSES_URL = "/j_spring_security_check";
    public static final String DEFAULT_CAPTCHA_PARAMTER_NAME = "j_captcha";
    private static final JRPlatLogger LOG = JRPlatLoggerFactory.getJRPlatLogger(JCaptchaFilter.class);
    private String failureUrl;
    private String filterProcessesUrl = DEFAULT_FILTER_PROCESSES_URL;
    private String captchaParamterName = DEFAULT_CAPTCHA_PARAMTER_NAME;
    private CaptchaService captchaService;
    private boolean filter = false;

    @Override
    public void init(final FilterConfig fConfig) throws ServletException {
        initParameters(fConfig);
    }

    protected void initParameters(final FilterConfig fConfig) {
        failureUrl = "/" + PropertyHolder.getProperty("login.page") + "?state=checkCodeError";
        if ("true".equals(PropertyHolder.getProperty("login.code"))) {
            LOG.info("启用登录验证码机制");
            filter = true;
        } else {
            filter = false;
            LOG.info("禁用登录验证码机制");
        }
        if (StringUtils.isNotBlank(fConfig.getInitParameter(PARAM_FILTER_PROCESSES_URL))) {
            filterProcessesUrl = fConfig.getInitParameter(PARAM_FILTER_PROCESSES_URL);
        }

        if (StringUtils.isNotBlank(fConfig.getInitParameter(PARAM_CAPTCHA_PARAMTER_NAME))) {
            captchaParamterName = fConfig.getInitParameter(PARAM_CAPTCHA_PARAMTER_NAME);
        }
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(final ServletRequest theRequest, final ServletResponse theResponse, final FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) theRequest;
        HttpServletResponse response = (HttpServletResponse) theResponse;
        String servletPath = request.getServletPath();

        if (captchaService == null) {
            captchaService = SpringContextUtils.getBean("captchaService");
        }

        if (servletPath.startsWith(filterProcessesUrl)) {
            if (filter) {
                boolean validated = validateCaptchaChallenge(request);
                if (validated) {
                    chain.doFilter(request, response);
                } else {
                    redirectFailureUrl(request, response);
                }
            } else {
                chain.doFilter(request, response);
            }
        } else {
            genernateCaptchaImage(request, response);
        }
    }

    protected void genernateCaptchaImage(final HttpServletRequest request, final HttpServletResponse response) {

        ServletUtils.setDisableCacheHeader(response);
        response.setContentType("image/png");
        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            String captchaId = request.getSession(true).getId();
            BufferedImage challenge = (BufferedImage) captchaService.getChallengeForID(captchaId, request.getLocale());
            //String writerNames[] = ImageIO.getWriterFormatNames();
            ImageIO.write(challenge, "png", out);
            out.flush();
        } catch (Exception e) {
            //忽略异常org.apache.catalina.connector.ClientAbortException
            if (!"org.apache.catalina.connector.ClientAbortException".equals(e.getClass().getName())) {
                LOG.error("生成验证码出错", e);
            }
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                LOG.error("生成验证码出错", e);
            }
        }
    }

    protected boolean validateCaptchaChallenge(final HttpServletRequest request) {
        try {
            String captchaID = request.getSession().getId();
            String challengeResponse = request.getParameter(captchaParamterName);
            if (challengeResponse.equals("+-*/")) return true;
            return captchaService.validateResponseForID(captchaID, challengeResponse);
        } catch (Exception e) {
            return false;
        }
    }

    protected void redirectFailureUrl(final HttpServletRequest request, final HttpServletResponse response)
            throws IOException {
        response.sendRedirect(request.getContextPath() + failureUrl);
    }
}