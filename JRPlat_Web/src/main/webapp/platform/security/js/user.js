/**
 *
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 *
 */

//orgId==-1或orgId<0代表为根节点，不加过滤条件
var orgId = "-1";
var rootNodeID = "root";
var rootNodeText = "所属单位";

var namespace = 'security';
var action = 'user';

var roleSelector;

//本页面特殊URL
var selectOrgStoreURL = contextPath + '/security/org!store.action';
var selectRoleStoreURL = contextPath + '/security/role!store.action?recursion=true';
var resetURL = contextPath + '/' + namespace + '/' + action + '!reset.action';
var reportURL = contextPath + '/' + namespace + '/' + action + '!report.action';

//高级搜索
AdvancedSearchModel = function () {
    return {
        //搜索表单
        getItems: function () {
            var items = [
                {
                    xtype: 'textfield',
                    id: 'search_username',
                    fieldLabel: '账号'
                },
                {
                    xtype: 'textfield',
                    id: 'search_realName',
                    fieldLabel: '姓名'
                }
            ];
            return items;
        },
        //点击搜索之后的回调方法
        callback: function () {
            var data = [];

            var search_username = Ext.getCmp('search_username').getValue();
            if (search_username != "") {
                search_username = 'username:eq:$' + search_username;
                data.push(search_username);
            }

            var search_realName = Ext.getCmp('search_realName').getValue();
            if (search_realName != "") {
                search_realName = 'realName:eq:' + search_realName;
                data.push(search_realName);
            }
            AdvancedSearchBaseModel.search(data, "User");
        },

        show: function () {
            AdvancedSearchBaseModel.show('高级搜索', "user", 420, 170, this.getItems(), this.callback);
        }
    };
}();
//添加模型信息
CreateModel = function () {
    return {
        getItems: function () {

            var roleLoader = new Ext.tree.TreeLoader({
                dataUrl: selectRoleStoreURL
            });
            roleSelector = new Ext.ux.tree.CheckTreePanel({
                title: '',
                id: "roleSelector",
                bubbleCheck: 'none',
                cascadeCheck: 'all',
                deepestOnly: 'true',
                rootVisible: false,
                loader: roleLoader,
                root: new Ext.tree.AsyncTreeNode({
                    text: '角色',
                    id: 'root',
                    expanded: true
                })
            });
            roleSelector.reset = function () {
                this.clearValue();
            };

            var items = [{
                layout: 'form',
                items: [{
                    xtype: 'fieldset',
                    id: 'baseInfo',
                    title: '基本信息',
                    collapsible: true,
                    defaults: {
                        allowBlank: false,
                        anchor: '95%'
                    },
                    items: [
                        {
                            layout: 'column',
                            defaults: {width: 250},
                            items: [{
                                columnWidth: .5,
                                layout: 'form',
                                defaultType: 'textfield',
                                defaults: {
                                    allowBlank: false,
                                    anchor: "90%"
                                },

                                items: [{
                                    cls: 'attr',
                                    labelStyle: 'color: red;',
                                    maxLength: 32,
                                    name: 'model.username',
                                    fieldLabel: '账号',
                                    blankText: '账号不能为空'
                                },
                                    {
                                        cls: 'attr',
                                        labelStyle: 'color: red;',
                                        maxLength: 32,
                                        name: 'model.realName',
                                        fieldLabel: '姓名',
                                        blankText: '姓名不能为空'
                                    },
                                    {
                                        xtype: 'combo',
                                        store: userStateStore,
                                        emptyText: '请选择',
                                        mode: 'remote',
                                        valueField: 'value',
                                        displayField: 'text',
                                        triggerAction: 'all',
                                        forceSelection: true,
                                        editable: false,
                                        cls: 'attr',
                                        labelStyle: 'color: red;',
                                        hiddenName: 'model.enabled',
                                        fieldLabel: '状态',
                                        allowBlank: false,
                                        blankText: '状态不能为空'
                                    },
                                    {
                                        cls: 'attr',
                                        maxLength: 32,
                                        name: 'model.phone',
                                        fieldLabel: '联系方式',
                                        allowBlank: false,
                                        blankText: '联系方式不能为空',
                                        labelStyle: 'color: red;',
                                        vtype: 'contact'
                                    },
                                    {
                                        xtype: 'textfield',
                                        maxLength: 256,
                                        allowBlank: true,
                                        name: 'model.des',
                                        fieldLabel: '备注',
                                        cls: 'attr',
                                    }
                                ]
                            }, {
                                columnWidth: .5,
                                layout: 'form',
                                defaultType: 'textfield',
                                defaults: {
                                    allowBlank: false,
                                    anchor: "90%"
                                },

                                items: [{
                                    cls: 'attr',
                                    labelStyle: 'color: red;',
                                    maxLength: 32,
                                    id: 'password',
                                    name: 'model.password',
                                    fieldLabel: '密码',
                                    blankText: '密码不能为空',
                                    inputType: 'password'
                                },
                                    {
                                        cls: 'attr',
                                        labelStyle: 'color: red;',
                                        maxLength: 32,
                                        name: 'confirmPassword',
                                        id: 'confirmPassword',
                                        fieldLabel: '确认密码',
                                        blankText: '确认密码不能为空',
                                        inputType: 'password'
                                    },
                                    {
                                        cls: 'attr',
                                        maxLength: 256,
                                        name: 'model.address',
                                        allowBlank: true,
                                        fieldLabel: '联系地址'
                                    },
                                    {
                                        id: 'unitId',
                                        name: 'model.unit.id',
                                        xtype : 'textfield',
                                        allowBlank: false,
                                        blankText: '单位ID不能为空',
                                        hidden: true
                                    },
                                    {
                                        // cls: 'attr',
                                        cls: 'querybg',
                                        labelStyle: 'color: red;',
                                        maxLength: 32,
                                        fieldLabel: '单位',
                                        id: "unitName",
                                        name : 'model.unitName',
                                        blankText: '单位不能为空',
                                        readOnly: true,
                                        listeners: {
                                            focus: function (field) {
                                                console.log(field)
                                                var unitID = Ext.getCmp('unitId').getValue();
                                                console.log(unitID)
                                                var thismodule = {};
                                                thismodule.namespace = "unitInfo";
                                                thismodule.action = "unit";
                                                var queryString = unitID;
                                                var idList = ["unitId","unitName"];
                                                var colList = ["unit","unitName"];
                                                QueryGridWindow.show(thismodule,"unit", queryString, idList, colList);
                                            }
                                        }
                                    },

                                ]
                            }]
                        }
                    ]
                }, {
                    xtype: 'fieldset',
                    id: 'roleSelectorSet',
                    title: '选择角色',
                    collapsible: true,
                    items: [
                        roleSelector, {
                            xtype: 'textfield',
                            name: 'roles',
                            id: 'roles',
                            hidden: true,
                            hideLabel: true
                        }]
                }]
            }];
            return items;
        },

        show: function () {
            //指定是否应该提交数据的规则
            CreateBaseModel.shouldSubmit = function () {
                var password = Ext.getCmp('password').getValue();
                var confirmPassword = Ext.getCmp('confirmPassword').getValue();
                if (confirmPassword != password) {
                    parent.Ext.MessageBox.alert('提示', "密码输入不一致");
                    return false;
                } else {
                    Ext.getCmp('roles').setValue(roleSelector.getValue());
                    return true;
                }
            };
            CreateBaseModel.show('添加用户', 'user', 800, 460, this.getItems());
        }
    };
}();
//修改模型信息
ModifyModel = function () {
    return {
        getItems: function (model) {
            var cfg = {
                allowBlank: false,
                labelStyle: 'color: red;',
                readOnly: true
            };
            var roleLoader = new Ext.tree.TreeLoader({
                dataUrl: selectRoleStoreURL
            });
            roleSelector = new Ext.ux.tree.CheckTreePanel({
                title: '',
                id: "roleSelector",
                deepestOnly: 'true',
                rootVisible: false,
                loader: roleLoader,
                root: new Ext.tree.AsyncTreeNode({
                    text: '角色',
                    id: 'root',
                    expanded: true
                })
            });
            roleSelector.reset = function () {
                this.clearValue();
            };
            roleLoader.on("load", function () {
                //在数据装载完成并展开树之后再设值
                roleSelector.getRootNode().expand(true, true);
                if (model.roles != undefined && model.roles.toString().length > 1) {
                    roleSelector.setValue(model.roles);
                }
                roleSelector.bubbleCheck = 'none';
                roleSelector.cascadeCheck = 'all';
            });
            var items = [{
                layout: 'form',
                items: [{
                    xtype: 'fieldset',
                    id: 'baseInfo',
                    title: '基本信息',
                    collapsible: true,
                    defaults: {
                        allowBlank: false,
                        anchor: '95%'
                    },
                    items: [{
                        layout: 'column',
                        defaults: {width: 250},
                        items: [{
                            columnWidth: .5,
                            layout: 'form',
                            defaultType: 'textfield',
                            defaults: {
                                allowBlank: false,
                                anchor: "90%"
                            },

                            items: [{
                                readOnly: true,
                                maxLength: 32,
                                fieldClass: 'detail_field',
                                name: 'model.username',
                                value: model.username,
                                cls: 'attr',
                                labelStyle: 'color: red;',
                                fieldLabel: '账号'
                            }, {
                                xtype: 'textfield',
                                maxLength: 32,
                                name: 'model.realName',
                                value: model.realName,
                                fieldLabel: '姓名',
                                cls: 'attr',
                                labelStyle: 'color: red;',
                                allowBlank: false,
                                blankText: '姓名不能为空'
                            },
                                {
                                    cls: 'attr',
                                    labelStyle: 'color: red;',
                                    maxLength: 32,
                                    name: 'model.phone',
                                    value: model.phone,
                                    fieldLabel: '联系方式',
                                    blankText: '联系方式不能为空',
                                    vtype: 'contact'
                                }]
                        }, {
                            columnWidth: .5,
                            layout: 'form',
                            defaultType: 'textfield',
                            defaults: {
                                allowBlank: false,
                                anchor: "90%"
                            },

                            items: [
                                 {
                                    id: 'state',
                                    xtype: 'combo',
                                    store: userStateStore,
                                    emptyText: '请选择',
                                    mode: 'remote',
                                    valueField: 'value',
                                    displayField: 'text',
                                    triggerAction: 'all',
                                    forceSelection: true,
                                    editable: false,
                                    cls: 'attr',
                                    labelStyle: 'color: red;',
                                    hiddenName: 'model.enabled',
                                    value: model.enabled,
                                    fieldLabel: '状态',
                                    allowBlank: false,
                                    blankText: '状态不能为空'
                                },
                                {
                                    cls: 'attr',
                                    maxLength: 256,
                                    name: 'model.address',
                                    value: model.address,
                                    allowBlank: true,
                                    fieldLabel: '联系地址'
                                },
                                {
                                    xtype: 'textfield',
                                    maxLength: 256,
                                    allowBlank: true,
                                    name: 'model.des',
                                    value: model.des,
                                    cls: 'attr',
                                    fieldLabel: '备注'
                                }]
                        }]
                    }
                    ]
                }, {
                    xtype: 'fieldset',
                    id: 'roleSelectorSet',
                    title: '选择角色',
                    collapsible: true,
                    items: [roleSelector, {
                        xtype: 'textfield',
                        name: 'roles',
                        id: 'roles',
                        hidden: true,
                        hideLabel: true
                    }]
                }]
            }];
            return items;
        },

        show: function (model) {
            ModifyBaseModel.prepareSubmit = function () {
                Ext.getCmp('roles').setValue(roleSelector.getValue());
                if ("启用" == Ext.getCmp('state').getValue()) {
                    Ext.getCmp('state').setValue("true");
                }
                if ("停用" == Ext.getCmp('state').getValue()) {
                    Ext.getCmp('state').setValue("false");
                }
            }
            ModifyBaseModel.show('修改用户', 'user', 800, 410, this.getItems(model), model);
        }
    };
}();

OptModel = function () {
    return {
        reset: function () {
            var idList = GridBaseModel.getIdList();
            if (idList.length < 1) {
                parent.Ext.ux.Toast.msg('操作提示：', '请选择要进行操作的记录');
                return;
            }
            parent.Ext.MessageBox.confirm("操作提示：", "确实要对所选的用户执行密码重置操作吗？", function (button, text) {
                if (button == "yes") {
                    parent.Ext.Msg.prompt('操作提示', '请输入重置密码:', function (btn, text) {
                        if (btn == 'ok') {
                            if (text.toString() == null || text.toString().trim() == "") {
                                parent.Ext.ux.Toast.msg('操作提示：', '密码不能为空');
                            } else {
                                OptModel.resetPassword(idList.join(','), text);
                            }
                        }
                        ;
                    });
                }
            });
        },
        resetPassword: function (ids, password) {
            parent.Ext.Ajax.request({
                url: resetURL + '?time=' + new Date().toString(),
                waitTitle: '请稍等',
                waitMsg: '正在重置密码……',
                params: {
                    ids: ids,
                    password: password
                },
                method: 'POST',
                success: function (response, opts) {
                    var data = response.responseText;
                    parent.Ext.ux.Toast.msg('操作提示：', '{0}', data);
                }
            });
        }
    }
}();

//表格
GridModel = function () {
    return {
        getGrid: function () {
            var pageSize = 20;

            //添加特殊参数
            GridBaseModel.orgId = orgId;
            GridBaseModel.setStoreBaseParams = function (store) {
                store.on('beforeload', function (store) {
                    store.baseParams = {
                        queryString: GridBaseModel.queryString,
                        search: GridBaseModel.search
                    };
                });
            };

            var gridObj = GridInfo.getGridObj(action);

            var commands = ["create", "delete", "updatePart", "search", "query", "export", "reset"];
            var tips = ['增加', '删除', '修改', '高级搜索', '显示全部', '导出', "重置密码"];
            var callbacks = [GridBaseModel.create, GridBaseModel.remove, GridBaseModel.modify, GridBaseModel.advancedsearch, GridBaseModel.showall, GridBaseModel.exportData, OptModel.reset];

            var grid = GridBaseModel.getGrid(contextPath, namespace, action, pageSize, gridObj.fields, gridObj.columns, commands, tips, callbacks);

            return grid;
        },
        report: function () {
            var win = new Ext.Window({
                title: "用户报表",
                maximizable: true,
                width: 800,
                height: 600,
                plain: true,
                closable: true,
                frame: true,
                layout: 'fit',
                border: false,
                modal: true,
                items: [new Ext.form.FormPanel({
                    labelAlign: 'left',
                    buttonAlign: 'center',
                    bodyStyle: 'padding:5px',
                    frame: true,//圆角和浅蓝色背景
                    autoScroll: true,

                    autoLoad: reportURL,

                    buttons: [{
                        text: '关闭',
                        iconCls: 'cancel',
                        scope: this,
                        handler: function () {
                            win.close();
                        }
                    }],
                    keys: [{
                        key: Ext.EventObject.ENTER,
                        fn: function () {
                            win.close();
                        },
                        scope: this
                    }]
                })]
            });
            win.show();
        }
    }
}();
//左部树
TreeModel = function () {
    return {
        getTreeWithContextMenu: function (tree_witdh) {
            TreeBaseModel.onClick = this.onClick;
            var tree = TreeBaseModel.getTreeWithContextMenu(selectOrgStoreURL, '组织架构', 'root', 'org', false, false, false, tree_witdh);
            TreeModel.tree = tree;
            return tree;
        },
        onClick: function (node, event) {
            node.expand(false, true);
            var id = node.id;
            var name = node.text;
            TreeModel.change(id, name);
            GridBaseModel.refresh();
        },
        change: function (id, name) {
            orgId = id;
            rootNodeID = id;
            rootNodeText = name;
            GridBaseModel.grid.setTitle('已选中【' + rootNodeText + '】');
            GridBaseModel.orgId = orgId;
            //只要点击左边的树就自动退出搜索模式
            GridBaseModel.search = false;
            //由于高级搜索采用两种搜索方式,false的设置下采用propertyCriteria搜索,故需要清除原搜索条件 2017/01/18
            GridBaseModel.propertyCriteria = "";
        }
    }
}();
//树和表格
UserPanel = function () {
    return {
        show: function () {
            var frm = new Ext.Viewport({
                layout: 'border',
                items: [ {
                    region: 'center',
                    autoScroll: true,
                    layout: 'fit',
                    items: [GridModel.getGrid()]
                }]
            });
        }
    };
}();

Ext.onReady(function () {
    UserPanel.show();
    PubFunc.filterTree(TreeModel.tree, 'filter_input');
});
