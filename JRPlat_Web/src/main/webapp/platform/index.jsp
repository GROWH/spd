<%--
   JRPlat - JR Development Platform
   Copyright (c) 2014, 西安捷然, jierankeji@163.com
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@page import="org.jrplat.module.security.model.User" %>
<%@page import="org.jrplat.module.security.service.UserHolder" %>
<%@page import="org.jrplat.module.system.service.PropertyHolder" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
    response.addHeader("login_success", "true");
    User loginUser = UserHolder.getCurrentLoginUser();
    String username = "匿名用户";
    String realName = "";
    Integer userId = 0;
    String parentOrgName = "匿名组织架构";
    String orgName = "匿名组织架构";
    String orgType = "未知单位类型";
    String parentOrgType = "未知单位类型";
    String orgIcon = "";
    int parentOrgId = 0;
    int orgId = 0;
    String userPath = "";
    if (loginUser != null) {
        //设置用户的数据上传主目录
        userPath = request.getContextPath() + "/userfiles/" + loginUser.getId() + "/";
        request.getSession().setAttribute("userPath", userPath);
        parentOrgName = loginUser.getOrg() == null ? "匿名组织架构" : (loginUser.getOrg().getParent() == null ? "广州市医维盟信息科技有限公司" : loginUser.getOrg().getParent().getOrgName());
//        parentOrgType = loginUser.getOrg() == null ? "未知单位类型" : (loginUser.getOrg().getParent() == null ? "全国运营商" : loginUser.getOrg().getParent().getDWLX().getName());
        parentOrgId = loginUser.getOrg() == null ? 0 : (loginUser.getOrg().getParent() == null ? 0 : loginUser.getOrg().getParent().getId());
        orgName = loginUser.getOrg() == null ? "匿名组织架构" : loginUser.getOrg().getOrgName();
//        orgType = loginUser.getOrg() == null ? "未知单位类型" : loginUser.getOrg().getDWLX().getName();
        orgId = loginUser.getOrg() == null ? 0 : loginUser.getOrg().getId();
        username = loginUser.getUsername();
        realName = loginUser.getRealName();
        if (realName == null) {
            realName = username;
        }
        if (orgType.equals("承运商")) {
            orgIcon = "../platform/images/car32.png";
        } else if (orgType.equals("货主")) {
            orgIcon = "../platform/images/zhu32.png";
        } else if (orgType.equals("客户")) {
            orgIcon = "../platform/images/header32.png";
        } else {
            orgIcon = "../platform/images/unknown32.png";
        }
        userId = loginUser.getId();
    }
    String appName = PropertyHolder.getProperty("app.name").replace("\"", "'");
    String appCopyright = PropertyHolder.getProperty("app.copyright").replace("\"", "'");
    String appVersion = PropertyHolder.getProperty("app.version").replace("\"", "'");
    String contact = PropertyHolder.getProperty("app.contact").replace("\"", "'");
    String support = PropertyHolder.getProperty("app.support").replace("\"", "'");
    String topnavPage = "include/" + PropertyHolder.getProperty("topnav.page");
    String searchForm = "js/" + PropertyHolder.getProperty("search.js");
    String indexPage = "js/" + PropertyHolder.getProperty("index.page.js");
    String shortcut = PropertyHolder.getProperty("module.short.name");

    String logo = PropertyHolder.getProperty("web.logo");
    Boolean useCitywhenNoArea = Boolean.valueOf(PropertyHolder.getProperty("query.line.useCitywhenNoArea"));
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title><%=appName%>
    </title>
    <link rel="shortcut icon" href="../images/<%= shortcut %>.ico"/>
    <%@include file="include/common.jsp" %>
    <link rel="stylesheet" type="text/css" href="css/qq.css"/>
    <script type="text/javascript" src="js/onlineUser.js"></script>
    <script type="text/javascript" src="js/onlineChat.js"></script>
    <script type="text/javascript" src="<%= indexPage %>"></script>
    <script type="text/javascript" src="<%= searchForm %>"></script>
    <script type="text/javascript" src="js/index.js"></script>
    <script type="text/javascript" src="js/modfiyPassword.js"></script>
    <script type="text/javascript">
        var userPath = "<%=userPath%>";
        var appCopyright = "<%=appCopyright%>";
        var appVersion = "<%=appVersion%>";
        var appName = "<%=appName%>";
        var contact = "<%=contact%>";
        var support = "<%=support%>";
        var userId = "<%=userId%>";
        var username = "<%=username%>";
        var realName = "<%=realName%>";
        var parentOrgId = "<%=parentOrgId%>";
        var parentOrgName = "<%=parentOrgName%>";
        var parentOrgType = "<%=parentOrgType%>";
        var orgName = "<%=orgName%>";
        var orgType = "<%=orgType%>";
        var orgIcon = "<%=orgIcon%>";
        var orgId = "<%=orgId%>";

        var logo = "<%=logo%>";
        var useCitywhenNoArea =<%=useCitywhenNoArea%>;

        var privileges = '<%=loginUser.getAuthoritiesStr()%>';
        function isGranted(namespace, action, command) {
            if (privileges.toString().indexOf("ROLE_SUPERMANAGER") != -1) {
                return true;
            }
            var role = "ROLE_MANAGER_" + namespace.toUpperCase().replace("/", "_") + "_" + process(action).toUpperCase() + "_" + command.toUpperCase();
            if (privileges.toString().indexOf(role) == -1) {
                return false;
            }
            return true;
        }
        //用来保存在tab页面中打开的窗口
        var openingWindows = new Array();
        function refreshAll() {
            for (var i = 0; i < openingWindows.length; i++) {
                if (openingWindows[i] != undefined && openingWindows[i].closed == false) {
                    openingWindows[i].refreshTheme();
                }
            }
            refreshTheme();
        }

        function changeTime() {
            document.getElementById("time").innerHTML = new Date().format('Y年n月j日  H:i:s');
        }
        //setInterval("changeTime()",1000);
        function selectSwitch(current) {
            var lis = document.getElementsByTagName("li")
            for (var i = 0; i < lis.length; i++) {
                if (lis[i].className == "activeli") {
                    lis[i].className = "commonli";
                }
            }
            ;
            current.className = "activeli";
        }
    </script>
</head>
<body id="jrplat_main">
<div id="loading-mask"></div>
<div id="loading">
    <div class="loading-indicator"></div>
</div>
<div id="north">
    <div id="app-header">
        <div id="header-left">
            <%-- <img id ="logo" src="../images/<%=PropertyHolder.getProperty("logo.image")%>" height="50" style="max-width:300px;"/> --%>
        </div>
        <div id="header-right">
            <div id="early-warning" style="padding-top: 3px; float: left; width: auto; padding-right: 30px;"></div>
            <div id="setting">
                <a href="#" onclick='triggerHeader();'><img id="trigger-image" src="images/trigger-up.png"/></a>
            </div>
        </div>
        <div id="header-main">
            <div id="currentTime" style="position: absolute;top: 0;right: 0;"><span id="time"></span></div>
            <div id="loginInfoPanel" style="position: absolute;bottom: 10px;right:15px;">
                <div id="welcomeMsg">欢迎 [ <%=realName%> (<%=username%>) ]</div>
            </div>
        </div>

    </div>
</div>
<div id="west"></div>
<div id="south"></div>
<div id="main"></div>
</body>
</html>