<%--
   JRPlat - JR Development Platform
   Copyright (c) 2014, 西安捷然, jierankeji@163.com
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@page import="util.AuthorizationUtils" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
    String code = request.getParameter("code");
    String reg = null;
    if (code != null) {
        reg = AuthorizationUtils.auth(code);
    }
%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>计算注册码</title>
</head>
<body>
<%
    if (reg == null) {
%>
<h1>计算注册码：</h1>
<form action="reg.jsp">
    输入机器码：<input name="code" size="50" maxlength="50">
    <p></p>
    <input type="submit" value="计算"/>
</form>
<%
} else {
%>
<h1>机器码：<%=code%>
</h1>
<h1>注册码：<%=reg%>
</h1>
<h1><a href="/<%=request.getContextPath()%>">返回</a></h1>
<%
    }
%>
</body>
</html>