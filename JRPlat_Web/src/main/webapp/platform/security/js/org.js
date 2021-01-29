/**
 *
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 *
 */

var currentNode;
var currentId = "-1";
var currentName = "组织架构";
var propertyCriteriaPre = "parent.id:eq:";
var propertyCriteria = propertyCriteriaPre + currentId;
var namespace = 'security';
var action = 'org';
var treeDataUrl = contextPath + '/' + namespace + '/' + action + '!query.action';
var default_max_users = 10;
CreateModel = function () {
    return {
        getItems: function () {
            var items = [
                {
                    layout: 'column',
                    items: [{
                        columnWidth: .5,
                        layout: 'form',
                        defaultType: 'textfield',
                        defaults: {
                            anchor: "90%"
                        },

                        items: [{
                            xtype: 'textfield',
                            readOnly: true,
                            disabled: true,
                            fieldClass: 'detail_field',
                            value: currentName,
                            fieldLabel: '上级机构'
                        },
                            {
                                cls: 'attr',
                                labelStyle: 'color: red;',

                                name: 'model.DWBH',
                                fieldLabel: '单位编号',
                                allowBlank: false,
                                blankText: '单位编号不能为空',
                                vtype: 'alphanum',
                                maxLength: 20
                            },
                            {
                                cls: 'attr',
                                labelStyle: 'color: red;',

                                name: 'model.orgName',
                                fieldLabel: '单位名称',
                                allowBlank: false,
                                blankText: '单位名称不能为空',
                                vtype: 'maxlength',
                                maxlen: 50
                            },
                            {
                                cls: 'attr',

                                name: 'model.chargeMan',
                                fieldLabel: '法人代表',
                            },
                            {
                                cls: 'attr',

                                name: 'model.address',
                                fieldLabel: '注册地址',

                            }

                        ]
                    }, {
                        columnWidth: .5,
                        layout: 'form',
                        defaultType: 'textfield',
                        defaults: {
                            anchor: "90%"
                        },

                        items: [
                            {
                                cls: 'attr',
                                labelStyle: 'color: red;',

                                name: 'model.LXR',
                                fieldLabel: '联系人',
                                allowBlank: false,
                                blankText: '联系人不能为空'
                            },
                            {
                                cls: 'attr',
                                labelStyle: 'color: red;',

                                name: 'model.phone',
                                fieldLabel: '联系电话',
                                allowBlank: false,
                                blankText: '联系电话不能为空',
                                vtype: 'contact'
                            },
                            {
                                cls: 'attr',

                                name: 'model.SJ',
                                fieldLabel: '手机',
                                vtype: 'mobilephone'
                            },
                            {
                                cls: 'attr',

                                name: 'model.CZ',
                                fieldLabel: '传真',

                            },
                            {
                                cls: 'attr',

                                name: 'model.YB',
                                fieldLabel: '邮编',
                                vtype: 'zipcode'

                            },
                            {
                                id: 'create_YJZH',
                                cls: 'attr',
                                labelStyle: 'color: red;',

                                name: 'model.YJZH',
                                fieldLabel: '月结账号',
                                allowBlank: false,
                                blankText: '月结账号不能为空',
                                vtype: 'alphanum',
                                maxLength: 20

                            },
                            {
                                cls: 'attr',
                                allowBlank: false,
                                labelStyle: 'color: red;',
                                name: 'model.maxUser',
                                fieldLabel: '最大用户数',
                                value: default_max_users,
                                vtype: 'integer',
                                hidden: parent.username == "admin" ? false : true
                            }
                        ]
                    }]
                },
                { //备注
                    cls: 'attr',
                    xtype: 'textarea',
                    name: 'model.functions',
                    fieldLabel: '备注',
                    anchor: '90%'
                }
            ];
            return items;
        },

        show: function () {
            GridBaseModel.createURLParameter = '?model.parent.id=' + currentId;
            CreateBaseModel.show('添加单位资料', 'org', 800, 364, this.getItems());
            CreateBaseModel.dlg.on('close', function () {
                //刷新表格
                GridBaseModel.refresh();
                //刷新树
                TreeModel.refreshTree(false);
            });
        }
    };
}();

ModifyModel = function () {
    return {
        getItems: function (model) {
            var items = [
                {
                    layout: 'column',
                    items: [{
                        columnWidth: .5,
                        layout: 'form',
                        defaultType: 'textfield',
                        defaults: {
                            anchor: "90%"
                        },

                        items: [
                            {
                                xtype: 'textfield',
                                readOnly: true,
                                disabled: true,
                                fieldClass: 'detail_field',
                                value: model.parent_orgName,
                                fieldLabel: '上级机构'
                            },
                            {
                                cls: 'attr',
                                labelStyle: 'color: red;',

                                name: 'model.DWBH',
                                value: model.DWBH,
                                fieldLabel: '单位编号',
                                allowBlank: false,
                                blankText: '单位编号不能为空',
                                vtype: 'alphanum',
                                maxLength: 20
                            },
                            {
                                cls: 'attr',
                                labelStyle: 'color: red;',

                                name: 'model.orgName',
                                value: model.orgName,
                                fieldLabel: '单位名称',
                                allowBlank: false,
                                blankText: '单位名称不能为空',
                                vtype: 'maxlength',
                                maxlen: 50
                            },
                            {
                                cls: 'attr',
                                name: 'model.chargeMan',
                                value: model.chargeMan,
                                fieldLabel: '法人代表',
                            },
                            {
                                cls: 'attr',
                                name: 'model.address',
                                value: model.address,
                                fieldLabel: '注册地址',

                            }
                        ]
                    }, {
                        columnWidth: .5,
                        layout: 'form',
                        defaultType: 'textfield',
                        defaults: {
                            anchor: "90%"
                        },

                        items: [
                            {
                                cls: 'attr',
                                labelStyle: 'color: red;',

                                name: 'model.LXR',
                                value: model.LXR,
                                fieldLabel: '联系人',
                                allowBlank: false,
                                blankText: '联系人不能为空'
                            },
                            {
                                cls: 'attr',
                                labelStyle: 'color: red;',

                                name: 'model.phone',
                                value: model.phone,
                                fieldLabel: '联系电话',
                                allowBlank: false,
                                blankText: '联系电话不能为空',
                                vtype: 'contact'
                            },
                            {
                                cls: 'attr',

                                name: 'model.SJ',
                                value: model.SJ,
                                fieldLabel: '手机',
                                vtype: 'mobilephone'
                            },
                            {
                                cls: 'attr',
                                name: 'model.CZ',
                                value: model.CZ,
                                fieldLabel: '传真',

                            },
                            {
                                cls: 'attr',
                                name: 'model.YB',
                                value: model.YB,
                                fieldLabel: '邮编',
                                vtype: 'zipcode'

                            },
                            {
                                cls: 'attr',
                                labelStyle: 'color: red;',

                                name: 'model.YJZH',
                                value: model.YJZH,
                                fieldLabel: '月结账号',
                                allowBlank: false,
                                blankText: '月结账号不能为空',
                                // disabled: model.DWLX != "货主"?true:false,
                                // hidden: model.DWLX != "货主"?true:false,
                                vtype: 'alphanum',
                                maxLength: 20

                            },
                            {
                                cls: 'attr',
                                allowBlank: false,
                                labelStyle: 'color: red;',
                                name: 'model.maxUser',
                                fieldLabel: '最大用户数',
                                value: model.maxUser,
                                vtype: 'integer',
                                hidden: parent.username == "admin" ? false : true
                            }
                        ]
                    }]
                },
                {
                    cls: 'attr',
                    xtype: 'textarea',
                    name: 'model.functions',
                    value: model.functions,
                    fieldLabel: '备注',
                    anchor: '90%',
                }
            ];
            return items;
        },

        show: function (model, forceRefreshParentNode) {
            ModifyBaseModel.modifySuccess = function (form, action) {
                //刷新表格
                GridBaseModel.refresh();
                //刷新树
                TreeModel.refreshTree(forceRefreshParentNode);
            };
            ModifyBaseModel.show('修改单位资料', 'org', 800, 364, this.getItems(model), model);
        },

        initStoreLoad: function () {
            // unitTypeStore.load()
        }
    };
}();


//表格
GridModel = function () {
    return {
        getFields: function () {
            var fields = [
                {name: 'id'},
                {name: 'version'},
                {name: 'DWBH'},
                {name: 'orgName'},
                {name: 'chargeMan'},
                {name: 'address'},
                {name: 'LXR'},
                {name: 'phone'},
                {name: 'SJ'},
                {name: 'CZ'},
                {name: 'YB'},
                {name: 'YJZH'},
                {name: 'maxUser'},
                {name: 'usedUser'},
                {name: 'functions'}
            ];
            return fields;
        },
        getColumns: function () {
            var columns = [
                {header: "编号", width: 20, dataIndex: 'id', sortable: true, hidden: true},
                {header: "版本", width: 20, dataIndex: 'version', sortable: true, hidden: true},
                {header: "单位编号", width: 40, dataIndex: 'DWBH', sortable: true},
                {header: "单位名称", width: 40, dataIndex: 'orgName', sortable: true},
                {header: "法人代表", width: 40, dataIndex: 'chargeMan', sortable: true},
                {header: "注册地址", width: 40, dataIndex: 'address', sortable: true},
                {header: "联系人", width: 40, dataIndex: 'LXR', sortable: true},
                {header: "联系电话", width: 40, dataIndex: 'phone', sortable: true},
                {header: "手机", width: 40, dataIndex: 'SJ', sortable: true},
                {header: "传真", width: 40, dataIndex: 'CZ', sortable: true},
                {header: "邮编", width: 40, dataIndex: 'YB', sortable: true},
                {header: "月结账号", width: 40, dataIndex: 'YJZH', sortable: true},
                {header: "最大用户数", width: 40, dataIndex: 'maxUser', sortable: true},
                {header: "已有用户数", width: 40, dataIndex: 'usedUser', sortable: true},
                {header: "备注", width: 40, dataIndex: 'functions', sortable: true}
            ];
            return columns;
        },
        getGrid: function () {
            var pageSize = 20;

            //删除数据命令回调
            GridBaseModel.removeSuccess = function (response, opts) {
                parent.Ext.ux.Toast.msg('操作提示：', '{0}', response.responseText);
                GridBaseModel.refresh();
                TreeModel.refreshTree(false);
            };
            //修改单个属性回调
            GridBaseModel.updateAttrSuccess = function (response, opts) {
                GridBaseModel.refresh();
                TreeModel.refreshTree(false);
            };
            //添加特殊参数
            if (currentId != -1) {
                GridBaseModel.propertyCriteria = propertyCriteria;
            }
            if (currentId == -1) {
                GridBaseModel.loadStore = function () {
                    //不加载表格
                }
            }
            GridBaseModel.setStoreBaseParams = function (store) {
                store.on('beforeload', function (store) {
                    store.baseParams = {propertyCriteria: GridBaseModel.propertyCriteria};
                });
            };
            var commands = ["create", "delete", "updatePart", "query", "query", "export"];
            var tips = ['增加', '删除', '修改', '显示全部', '已开通账号', '导出'];
            var callbacks = [GridBaseModel.create, GridBaseModel.remove, GridBaseModel.modify, GridBaseModel.showall, TreeModel.myUser, GridBaseModel.exportData];

            var grid = GridBaseModel.getGrid(contextPath, namespace, action, pageSize, this.getFields(), this.getColumns(), commands, tips, callbacks);

            //设置标题
            grid.setTitle(" ");

            return grid;
        }
    }
}();
//左部树
TreeModel = function () {
    return {
        getTreeWithContextMenu: function (tree_witdh) {
            TreeBaseModel.onClick = this.onClick;
            TreeBaseModel.remove = this.remove;
            TreeBaseModel.modify = this.modify;

            var create = true;
            var remove = true;
            var modify = true;
            var tree = TreeBaseModel.getTreeWithContextMenu(treeDataUrl, '组织架构', 'root', 'org', create, remove, modify, tree_witdh);
            TreeModel.tree = tree;
            currentNode = TreeBaseModel.root;
            return tree;
        },
        //当forceRefreshParentNode为true时表示需要强制刷新父节点
        refreshTree: function (forceRefreshParentNode) {
            if (currentNode.parentNode && forceRefreshParentNode) {
                currentNode.parentNode.reload(
                    // node loading is asynchronous, use a load listener or callback to handle results
                    function () {
                        currentNode = TreeBaseModel.tree.getNodeById(currentId);
                        TreeModel.select(currentNode);
                    },
                    this);
            } else {
                if (!currentNode.isExpandable()) {
                    //当前节点是叶子节点（新添加的节点是当前节点的第一个子节点）
                    if (currentNode.parentNode == null) {
                        TreeBaseModel.root.reload();
                        TreeBaseModel.root.expand(false, true);
                    } else {
                        //重新加载当前节点的父节点，这样才能把新添加的节点装载进来
                        currentNode.parentNode.reload(
                            // node loading is asynchronous, use a load listener or callback to handle results
                            function () {
                                //重新查找当前节点，因为已经重新加载过数据
                                currentNode = TreeBaseModel.tree.getNodeById(currentId);
                                //展开当前节点
                                currentNode.expand(false, true);
                            },
                            this);
                    }
                } else {
                    //重新加载当前节点
                    currentNode.reload(
                        // node loading is asynchronous, use a load listener or callback to handle results
                        function () {
                            //展开当前节点
                            currentNode.expand(false, true);
                        },
                        this);
                }
            }
        },
        select: function (node, event) {
            node.expand(false, true);
            currentNode = node;
            currentId = node.id;
            var index = node.text.indexOf("<span>");
            if (index != -1) {
                currentName = node.text.substring(6, node.text.length - 7);
            } else {
                currentName = node.text;
            }
            GridBaseModel.grid.setTitle("已选中【" + currentName + "】");
            propertyCriteria = propertyCriteriaPre + currentId;
            //根据所选节点的组织架构的单位类型设置新增时所选的单位类型
            TreeModel.setUnitType(node.id);

            GridBaseModel.propertyCriteria = propertyCriteria;
        },
        onClick: function (node, event) {
            TreeModel.select(node, event);
            GridBaseModel.refresh();
        },
        remove: function () {
            if (currentNode.parentNode == TreeBaseModel.tree.getRootNode()) {
                parent.Ext.ux.Toast.msg('操作提示：', '不能删除根节点');
                return;
            }
            //在删除当前节点之前记住父节点
            var parentNode = currentNode.parentNode;
            Ext.MessageBox.confirm("请确认", "确实要删除【" + currentName + "】吗？", function (button, text) {
                if (button == "yes") {
                    parent.Ext.Ajax.request({
                        url: GridBaseModel.deleteURL + '?time=' + new Date().toString(),
                        params: {
                            ids: currentId
                        },
                        method: 'POST',
                        success: function (response, options) {
                            var data = response.responseText;
                            if ("删除成功" == data) {
                                TreeModel.select(parentNode);
                                GridBaseModel.refresh();
                                TreeModel.refreshTree(false);
                            } else {
                                parent.Ext.MessageBox.alert('提示', "删除失败！");
                            }
                        },
                        failure: function () {
                            alert("删除失败！");
                        }
                    });
                }
            });
        },
        modify: function () {
            if (currentNode.parentNode == TreeBaseModel.tree.getRootNode()) {
                parent.Ext.ux.Toast.msg('操作提示：', '不能修改根节点');
                return;
            }
            Ext.MessageBox.confirm("请确认", "确实要修改【" + currentNode.text + "】吗？", function (button, text) {
                if (button == "yes") {
                    //query org detail info
                    parent.Ext.Ajax.request({
                        url: GridBaseModel.retrieveURL + currentId + '&time=' + new Date().toString(),
                        method: 'POST',
                        success: function (response, options) {
                            var data = response.responseText;
                            //返回的数据是对象，在外层加个括号才能正确执行eval
                            var model = eval('(' + data + ')');
                            ModifyModel.show(model, true);
                        }
                    });
                }
            });
        },
        setUnitType: function (nodeId) {
            return;
        },
        myUser: function () {
            var idList = GridBaseModel.getIdList();
            if (idList.length < 1) {
                parent.Ext.ux.Toast.msg('操作提示：', '请选择一条要操作的单位记录');
                return;
            }
            if (idList.length > 1) {
                parent.Ext.ux.Toast.msg('操作提示：', '只能选择一条要操作的单位记录');
                return;
            }
            var queryString = "org.id:eq:" + idList[0];
            var idList = [];
            var colList = [];
            QueryGridWindow.show("user", queryString, idList, colList);
        }
    }
}();
//左边为树右边为表格的编辑视图
OrgForm = function () {
    return {
        getTreeWidth: function () {
            return 300;
        },
        getTbar: function () {
            var tbar = new Ext.Toolbar({
                buttonAlign: 'left',
                items: [{
                    xtype: 'textfield',
                    emptyText: '搜索单位...',
                    id: 'filter_input',
                    width: this.getTreeWidth() - 30,
                    cls: 'attr'
                }]
            });
            return tbar;
        },
        show: function () {
            var frm = new Ext.Viewport({
                layout: 'border',
                items: [
                    TreeModel.getTreeWithContextMenu(this.getTreeWidth()),
                    this.getTbar(),
                    {
                        region: 'center',
                        //autoScroll:true,
                        layout: 'fit',
                        items: [GridModel.getGrid()]
                    }
                ]
            });
        }
    };
}();

Ext.onReady(function () {
    ModifyModel.initStoreLoad();
    OrgForm.show();
    PubFunc.filterTree(TreeModel.tree, 'filter_input');
});