package org.jrplat.module.security.service.filter;

import org.apache.commons.lang.StringUtils;
import org.jrplat.module.security.model.User;
import org.jrplat.module.security.service.UserDetailsServiceImpl;
import org.jrplat.module.security.service.UserHolder;
import org.jrplat.module.security.service.UserService;
import org.jrplat.platform.log.JRPlatLogger;
import org.jrplat.platform.log.JRPlatLoggerFactory;
import org.jrplat.platform.push.pushService;
import org.jrplat.platform.util.MD5Util;
import org.jrplat.platform.util.SpringContextUtils;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by 赵腾飞 on 16-10-24.
 * android session 过期后自动登陆filter
 */
public class AndroidLoginFilter implements Filter {
    private static final JRPlatLogger LOG = JRPlatLoggerFactory.getJRPlatLogger(AndroidLoginFilter.class);

    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOG.info("初始化android自动登陆过滤器(Initialize the android login filter)");
        LOG.info("启用android登录过滤器(Enable android login filter)");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = null;
        String userName = "";
        String loginToken = "";
        if (request instanceof HttpServletRequest) {
            httpServletRequest = (HttpServletRequest) request;
        }
        if (!UserHolder.hasLogin()) {
            String token = httpServletRequest.getHeader("loginToken");
            if (StringUtils.isEmpty(token)) {
                token = request.getParameter("loginToken");
            }
            if (StringUtils.isNotEmpty(token)) {
                String[] result = token.split(":");
                if (result.length == 2) {
                    userName = result[0];
                    loginToken = result[1];
                }
                LOG.debug("android login filter working...the userName from the request is : " + userName);
                LOG.debug("android login filter working...the loginToken from the request is : " + loginToken);
                if (StringUtils.isNotEmpty(userName)) {
                    if (userDetailsServiceImpl == null) {
                        userDetailsServiceImpl = SpringContextUtils.getBean("userDetailsServiceImpl");
                    }
                    if (userDetailsServiceImpl != null) {
                        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(userName);
                        User user = (User) userDetails;
                        if (MD5Util.md5(loginToken + "{赵腾飞}").equals(user.getLoginToken())) {
                            UserHolder.saveUserDetailsToContext(userDetails, (HttpServletRequest) request);
                        }
                        String session = ((HttpServletRequest) request).getRequestedSessionId();
                        LOG.debug("android login filter session is : " + session);
                    }
                }
            }
        }
        chain.doFilter(request, response);
        //用户登陆成功后 更新 token信息
        if (UserHolder.hasLogin() && httpServletRequest.getRequestURI().contains("/j_spring_security_check")) {
            //更新user token信息
            loginToken = MD5Util.md5(httpServletRequest.getSession().getId() + "{赵腾飞}");
            User user = UserHolder.getCurrentLoginUser();
            user.setLoginToken(loginToken);
            UserService service = SpringContextUtils.getBean("userService");
            service.update(user);
            LOG.debug("user token .............................................." + loginToken);
            LOG.debug("user sessionid .............................................." + httpServletRequest.getSession().getId());
            //**发送推送
            pushService pushService = new pushService();
            pushService.pushLogin(user.getUsername());
        }
    }

    @Override
    public void destroy() {
        LOG.debug("销毁android登陆过滤器(Destroy the android login filter)");
    }
}
