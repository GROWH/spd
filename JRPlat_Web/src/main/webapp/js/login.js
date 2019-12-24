/**
 *
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 *
 */

Ext.BLANK_IMAGE_URL = '../extjs/images/default/s.gif';
var model;
var loginTitle = '登录系统';
var requestCode;
var activeURL = "security/active!active.action";
//fieldset的验证
Ext.QuickTips.init();//支持tips提示
Ext.form.Field.prototype.msgTarget = 'side';//提示的方式，枚举值为"qtip","title","under","side",id(元素id)  
LoginWindow = function () {
    this.formPanel = function () {
        this.form = new Ext.form.FormPanel(
            {
                bodyStyle: 'background-color: #fff; opacity: 0.8; margin:10px; border:0px #888 solid;',
                defaultType: 'textfield',
                labelAlign: 'right',
                columnWidth: 0.75,
                border: false,
                layout: "form",
                labelWidth: 80,
                buttonAlign: 'center',
                defaults: {
                    allowBlank: false,
                    anchor: "80%"
                },
                items: [
                    {
                        xtype: "panel",
                        border: false,
                        bodyStyle: "background-color: transparent",
                        height: 10,
                    },
                    {
                        cls: 'j_username',
                        style: 'padding-left:18px',
                        name: 'j_username',
                        id: 'j_username',
                        fieldLabel: '<span class="loginform">账&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号</span>',
                        blankText: '帐号不能为空'
                    },
                    {
                        cls: 'j_password',
                        style: 'padding-left:18px',
                        name: 'j_password',
                        id: 'j_password',
                        fieldLabel: '<span class="loginform">密&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;码</span>',
                        blankText: '密码不能为空',
                        inputType: 'password'
                    },
                    {
                        cls: 'j_rand',
                        style: 'padding-left:18px',
                        name: 'j_captcha',
                        id: 'j_captcha',
                        fieldLabel: '<span class="loginform">验&nbsp;证&nbsp;码</span>',
                        allowBlank: !loginCode,
                        blankText: '验证码不能为空',
                        hidden: !loginCode
                    },
                    {
                        xtype: 'panel',
                        layout: 'table',
                        hideLabel: true,
                        border: false,
                        layoutConfig: {
                            columns: 3
                        },
                        items: [
                            {
                                width: 20,
                                xtype: 'panel',
                                bodyStyle: "background-color: transparent;",
                                border: false
                            },
                            {
                                width: 180,
                                xtype: 'panel',
                                border: false,
                                id: "codePicture",
                                html: '<img border="0" height="50" width="150" src="'
                                + contextPath
                                + '/security/jcaptcha.png?rand='
                                + Math.random()
                                + '"/>'
                            },
                            {
                                width: 60,
                                xtype: 'panel',
                                border: false,
                                bodyStyle: 'font-size:15px;padding-left:12px',
                                html: '<a href="javascript:refeshCode()">看不清</a>'
                            }],
                        hidden: !loginCode
                    }],
                buttons: [{
                    text: '登录',
                    style: 'padding-left: 10px;',
                    height: 40,
                    width: 100,
                    iconCls: 'save',
                    scope: this,
                    handler: function () {
                        this.login();
                    }
                }, {
                    text: "重置",
                    style: 'padding-left: 10px;',
                    height: 40,
                    width: 100,
                    iconCls: 'save',
                    scope: this,
                    handler: function () {
                        this.reset();
                    }
                }],
                keys: [{
                    key: Ext.EventObject.ENTER,
                    fn: function () {
                        this.login();
                    },
                    scope: this
                }]
            });
        this.reset = function () {
            this.form.form.reset();
        },
            this.login = function () {
                var now1 = new Date(serverNowDate).getTime();
                var now2 = new Date(clientNowDate).getTime();
                if (Math.abs(now1 - now2) > 0.5*60*60*1000) {
                    Ext.ux.Toast.msg('操作提示：', '当前时间为<span style="color: red;">'+serverNowDate+'</span>,您的电脑时间为<span style="color: red;">'+clientNowDate+'</span>,请校准后再登录系统,谢谢!');
                    return;
                }
                var loginTip = Ext.Msg.wait("正在登录......", '请稍候');
                var j_captcha = parent.Ext.getCmp('j_captcha').getValue();
                var j_username = parent.Ext.getCmp('j_username').getValue();
                var j_password = parent.Ext.getCmp('j_password').getValue();
                if (j_username.toString().trim() == "" || j_password.toString().trim() == "" || (j_captcha.toString().trim() == "" && loginCode)) {
                    parent.Ext.getCmp('j_username').validate();
                    parent.Ext.getCmp('j_password').validate();
                    parent.Ext.getCmp('j_captcha').validate();
                    loginTip.hide();
                    return false;
                }
                var url = 'j_spring_security_check';
                j_password = hex_sha512(j_password + '{用户信息}');
                Ext.Ajax.request({
                    url: url,
                    params: {
                        j_captcha: j_captcha,
                        j_username: j_username,
                        j_password: j_password
                    },
                    method: 'POST',
                    success: function (response, opts) {
                        if (response.getResponseHeader('login_success') && response.responseText.length > 20) {
                            Ext.getCmp("loginWindow").hide();
                            //防止用户登录成功之后点击浏览器的后退按钮回到登录页面
                            //在浏览器的历史记录里面不记录登录页面
                            location.replace(contextPath + "/platform/index.jsp");
                            return;
                        }
                        refeshCode();
                        parent.Ext.getCmp('j_password').setValue("");
                        parent.Ext.getCmp('j_captcha').setValue("");
                        parent.Ext.getCmp('j_password').focus();
                        loginTip.hide();
                        if (response.getResponseHeader('checkCodeError')) {
                            Ext.ux.Toast.msg('登陆失败：', '验证码错误，请重新登录!');
                            return;
                        }
                        if (response.getResponseHeader('login_error')) {
                            Ext.ux.Toast.msg('登陆失败：', "账号或密码错误!");
                            /*var resp = response.responseText;
                             if (resp.indexOf("您还没有购买产品") != -1) {
                             //购买产品
                             var attr = resp.split(":");
                             if (attr.length == 2) {
                             var requestCode = attr[1];
                             resp = attr[0];

                             BuyModel.show(requestCode);
                             }
                             }
                             Ext.ux.Toast.msg('登陆失败：', resp);*/
                        }
                    },
                    failure: function (response, opts) {
                        location.replace("platform/index.jsp");
                    }
                });
            }
        return this.form;
    }(),
        this.show = function () {
            var support = '<p align="center" style="color: #087c1a; font-size:35px; padding-top:10px; font-weight:bold;">' + parent.appSupport + '</p>'
                + '<p align="center" style="color: #087c1a;">Guangzhoushi Yiweimeng Information Technology Co.,Ltd.</p>';
            var appName = '<p align="center" style="color: #555; font-size:22px;">' + parent.appName + '</p>';
            var window = new Ext.Window(
                {
                    id: "loginWindow",
                    bodyStyle: "background-image:url('images/login_win_bg.jpg'); background-color: transparent; opacity: 1; border:0px solid #999;",
                    border: true,
                    closable: false,
                    resizable: false,
                    buttonAlign: "center",
                    width: 800,
                    height: 400,
                    frame: false,
                    shadow: true,
                    shadowOffset: 5,
                    layout: {
                        type: "vbox",
                        align: "stretch"
                    },
                    items: [
                        {
                            xtype: "panel",
                            border: false,
                            bodyStyle: "background-color: transparent;",
                            width: 800,
                            height: 80,
                            html: support
                        },
                        {
                            xtype: "panel",
                            border: false,
                            height: 290,
                            bodyStyle: "background-color: transparent;",
                            layout: "column",
                            items: [
                                {
                                    xtype: "panel",
                                    border: false,
                                    columnWidth: 0.55,
                                    height: 290,
                                    bodyStyle: "background-color: transparent;",
                                },
                                {
                                    xtype: "panel",
                                    border: false,
                                    columnWidth: 0.45,
                                    height: 290,
                                    bodyStyle: "background-color: transparent;",
                                    items: [{
                                        xtype: "panel",
                                        border: false,
                                        height: 300,
                                        bodyStyle: "background-color: transparent;",
                                        layout: "border",
                                        items: [
                                            {
                                                xtype: "panel",
                                                region: 'north',
                                                border: false,
                                                bodyStyle: "background-color: transparent; padding-top: 25px;",
                                                //width : 230,
                                                height: 60,
                                                html: appName
                                            },
                                            {
                                                region: 'center',
                                                border: false,
                                                bodyStyle: "background-color: transparent",
                                                items: this.formPanel
                                            }]
                                    }]
                                }]
                        },
                        {
                            xtype: "panel",
                            border: false,
                            bodyStyle: "background-color: transparent;",
                            width: 800,
                            height: 30,
                            html: '<div align="center" style="color: #F00; font-size:13px;">注：建议使用谷歌浏览器获得最佳使用体验</div>'
                        }]
                });
            window.show();
        }
};
/**
 * 更新验证码
 */
function refeshCode() {
    var loginCode = Ext.getCmp('codePicture');
    loginCode.body.update('<img border="0" height="50" width="180" src="'
        + contextPath + '/security/jcaptcha.png?rand=' + Math.random()
        + '"/>');
    fixPng();
};
