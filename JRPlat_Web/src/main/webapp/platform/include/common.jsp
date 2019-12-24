<%--
   JRPlat - JR Development Platform
   Copyright (c) 2014, 西安捷然, jierankeji@163.com
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%
    String contextPath = org.jrplat.module.system.service.SystemListener.getContextPath();
    String jsessionid = session.getId();
%>
<link rel="stylesheet" type="text/css" href="<%=contextPath%>/extjs/css/ext-all.css"/>
<link rel="stylesheet" type="text/css" href="<%=contextPath%>/extjs/css/ext-patch.css"/>
<link rel="stylesheet" type="text/css" href="<%=contextPath%>/extjs/ux/css/MultiSelect.css"/>
<link rel="stylesheet" type="text/css" href="<%=contextPath%>/extjs/ux/css/CheckTreePanel.css"/>
<link rel="stylesheet" type="text/css" href="<%=contextPath%>/extjs/ux/css/ux-all.css"/>
<link rel="stylesheet" type="text/css" href="<%=contextPath%>/extjs/ux/css/Portal.css"/>
<link rel="stylesheet" type="text/css" href="<%=contextPath%>/extjs/ux/css/fileuploadfield.css"/>
<link rel="stylesheet" type="text/css" href="<%=contextPath%>/platform/css/operation.css"/>
<link rel="stylesheet" type="text/css" href="<%=contextPath%>/platform/css/module.css"/>
<link rel="stylesheet" type="text/css" href="<%=contextPath%>/platform/css/index.css">
<link rel="stylesheet" type="text/css" href="<%=contextPath%>/platform/css/chart.css">
<link rel="stylesheet" type="text/css" href="<%=contextPath%>/platform/css/IconCombo.css">
<link rel="stylesheet" type="text/css" href="<%=contextPath%>/platform/css/skin.css">
<link rel="stylesheet" type="text/css" href="<%=contextPath%>/platform/css/PrinterFriendly.css">
<link rel="stylesheet" type="text/css" href="<%=contextPath%>/platform/css/myStyle.css">

<script type="text/javascript" src="<%=contextPath%>/extjs/js/adapter/jquery/jquery.min.js"></script>
<script type="text/javascript" src="<%=contextPath%>/extjs/js/adapter/jquery/jquery-cookie.min.js"></script>
<script type="text/javascript" src="<%=contextPath%>/extjs/js/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="<%=contextPath%>/extjs/js/ext-all.js"></script>
<script type="text/javascript" src="<%=contextPath%>/extjs/js/ext-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=contextPath%>/extjs/js/ext-basex.js"></script>
<script type="text/javascript" src="<%=contextPath%>/extjs/ux/MultiSelect.js"></script>
<script type="text/javascript" src="<%=contextPath%>/extjs/ux/ItemSelector.js"></script>
<script type="text/javascript" src="<%=contextPath%>/extjs/ux/Toast.js"></script>
<script type="text/javascript" src="<%=contextPath%>/extjs/ux/CheckTreePanel.js"></script>
<script type="text/javascript" src="<%=contextPath%>/extjs/ux/XmlTreeLoader.js"></script>
<script type="text/javascript" src="<%=contextPath%>/extjs/ux/Portal.js"></script>
<script type="text/javascript" src="<%=contextPath%>/extjs/ux/PortalColumn.js"></script>
<script type="text/javascript" src="<%=contextPath%>/extjs/ux/Portlet.js"></script>
<script type="text/javascript" src="<%=contextPath%>/extjs/ux/FileUploadField.js"></script>

<link rel="stylesheet" type="text/css" href="<%=contextPath%>/DateTime/Spinner.css"/>
<script type="text/javascript" src="<%=contextPath%>/DateTime/Spinner.js"></script>
<script type="text/javascript" src="<%=contextPath%>/DateTime/SpinnerField.js"></script>
<script type="text/javascript" src="<%=contextPath%>/DateTime/DateTimeField.js"></script>

<script type="text/javascript" src="<%=contextPath%>/FusionCharts/FusionCharts.js"></script>
<script type="text/javascript" src="<%=contextPath%>/FusionCharts/FusionChartsExportComponent.js"></script>
<script type="text/javascript" src="<%=contextPath%>/FusionCharts/uxmedia.js"></script>
<script type="text/javascript" src="<%=contextPath%>/FusionCharts/uxflash.js"></script>
<script type="text/javascript" src="<%=contextPath%>/FusionCharts/uxfusion.js"></script>
<script type="text/javascript">
    if (this.parent != this) {
        parent.openingWindows.push(this);
    }
    function refreshTheme() {
        var storeTheme = Ext.util.Cookies.get('theme');
        if (storeTheme == null || storeTheme == '') {
            storeTheme = 'ext-all';
        }
        Ext.util.CSS.swapStyleSheet("theme", contextPath + "/extjs/css/" + storeTheme + ".css");
    }
    var contextPath = '<%=contextPath%>';
    var jsessionid = '<%=jsessionid%>';
    Ext.BLANK_IMAGE_URL = contextPath + '/extjs/images/default/s.gif';
    refreshTheme();
</script>

<script type="text/javascript" src="<%=contextPath%>/platform/js/specialDic.js"></script>
<script type="text/javascript" src="<%=contextPath%>/platform/js/dic.js"></script>
<script type="text/javascript" src="<%=contextPath%>/platform/js/vtypeCheck.js"></script>
<script type="text/javascript" src="<%=contextPath%>/platform/js/PrinterFriendly.js"></script>
<script type="text/javascript" src="<%=contextPath%>/platform/js/map.js"></script>

<script type="text/javascript" src="<%=contextPath%>/platform/js/GridInfo.js"></script>
<script type="text/javascript" src="<%=contextPath%>/platform/js/DateAdd.js"></script>

<script type="text/javascript" src="<%=contextPath%>/platform/js/JRPlat.js"></script>
<script type="text/javascript" src="<%=contextPath%>/platform/js/swfupload.js"></script>
<script type="text/javascript" src="<%=contextPath%>/platform/js/UploadPanel.js"></script>
<script type="text/javascript" src="<%=contextPath%>/platform/js/public.js"></script>

<script type="text/javascript" src="<%=contextPath%>/platform/js/IconCombo.js"></script>
<script type="text/javascript" src="<%=contextPath%>/platform/js/PageSizePlugin.js"></script>

<script type="text/javascript" src="<%=contextPath%>/js/reLogin.js"></script>
<script type="text/javascript" src="<%=contextPath%>/js/sha512.js"></script>

<!--网页编辑器-->
<script type="text/javascript" src="<%=contextPath%>/ckfinder/ckfinder.js"></script>
<script type="text/javascript" src="<%=contextPath%>/ckeditor/ckeditor.js"></script>
<script type="text/javascript" src="<%=contextPath%>/ckeditor/Ext.form.BasicForm.js"></script>
<script type="text/javascript" src="<%=contextPath%>/ckeditor/Ext.form.CKEditor.js"></script>

<script type="text/javascript" src="<%=contextPath%>/js/MSIE.PNG.js"></script>
<script type="text/javascript" src="<%=contextPath%>/platform/js/GridInfo.js"></script>