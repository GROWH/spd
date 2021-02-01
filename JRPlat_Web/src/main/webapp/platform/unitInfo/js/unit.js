
var namespace = 'unitinfo';
var action = 'unit';

//高级搜索
AdvancedSearchModel = function () {
    return {
        //搜索表单
        getItems: function () {
            var items = [
                {
                    xtype: 'textfield',
                    id: 'unitName',
                    fieldLabel: '单位名称'
                },
                {
                    xtype: 'textfield',
                    id: 'paperworkNo',
                    fieldLabel: '证件编号'
                }
            ];
            return items;
        },
        //点击搜索之后的回调方法
        callback: function () {
            var data = [];

            var unitName = Ext.getCmp('unitName').getValue();
            if (unitName != "") {
                unitName = 'unitName:eq:$' + unitName;
                data.push(unitName);
            }

            var paperworkNo = Ext.getCmp('paperworkNo').getValue();
            if (paperworkNo != "") {
                paperworkNo = 'realName:eq:' + paperworkNo;
                data.push(paperworkNo);
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
            var items = [{
                layout: 'form',
                items: [{
                    xtype: 'fieldset',
                    id: 'baseInfo',
                    title: '基本信息',
                    collapsible: true,
                    defaults: {
                        allowBlank: false,
                        anchor: '98%'
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
                                    anchor: "95%"
                                },
                                items: [{
                                    cls: 'attr',
                                    labelStyle: 'color: red;',
                                    maxLength: 32,
                                    name: 'model.unitName',
                                    fieldLabel: '单位名称',
                                    blankText: '单位名称不能为空',
                                },
                                    {
                                        cls: 'attr',
                                        labelStyle: 'color: red;',
                                        maxLength: 32,
                                        name: 'model.paperworkNo',
                                        fieldLabel: '证件编号',
                                        blankText: '证件编号不能为空'
                                    },
                                    {
                                        cls: 'attr',
                                        labelStyle: 'color: red;',
                                        maxLength: 32,
                                        name: 'model.linkman',
                                        fieldLabel: '联系人',
                                        blankText: '联系人不能为空'
                                    },
                                    {
                                        cls: 'attr',
                                        maxLength: 32,
                                        name: 'model.enSHDM',
                                        fieldLabel: '统一社会信用代码',
                                        allowBlank: false,
                                        blankText: '统一社会信用代码不能为空',
                                        labelStyle: 'color: red;',
                                        // vtype: 'contact'
                                    },
                                    {
                                        cls: 'attr',
                                        maxLength: 32,
                                        name: 'model.businessAddress',
                                        fieldLabel: '经营地址',
                                        allowBlank: true,
                                        // vtype: 'contact'
                                    },
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
                                    // id: 'unitType',
                                    name: 'model.unitType.id',
                                    xtype: 'combo',
                                    store: unitTypeStore,
                                    emptyText: '请选择',
                                    mode: 'remote',
                                    valueField: 'value',
                                    displayField: 'text',
                                    triggerAction: 'all',
                                    forceSelection: true,
                                    editable: false,
                                    cls: 'attr',
                                    labelStyle: 'color: red;',
                                    hiddenName: 'model.unitType.id',
                                    fieldLabel: '单位类型',
                                    blankText: '单位类型不能为空'
                                },
                                    {
                                        cls: 'attr',
                                        labelStyle: 'color: red;',
                                        maxLength: 32,
                                        name: 'model.orderKey',
                                        fieldLabel: '订单获取密码',
                                        blankText: '订单获取密码不能为空'
                                    },
                                    {
                                        cls: 'attr',
                                        maxLength: 32,
                                        xtype: 'numberfield',
                                        name: 'model.contactPhone',
                                        fieldLabel: '联系人电话',
                                        allowBlank: false,
                                        blankText: '联系人电话不能为空',
                                        labelStyle: 'color: red;'

                                    },
                                    {
                                        cls: 'attr',
                                        maxLength: 256,
                                        name: 'model.legalPerson',
                                        allowBlank: true,
                                        fieldLabel: '法人'
                                    },
                                    {
                                        xtype: 'textfield',
                                        maxLength: 256,
                                        allowBlank: true,
                                        name: 'model.StorehouseAddress',
                                        fieldLabel: '仓库地址',
                                        cls: 'attr',
                                    }]
                            }]
                        }
                    ]
                }
                ]
            }];
            return items;
        },

        show: function () {
            CreateBaseModel.show('添加单位信息', 'user', 800, 300, this.getItems());
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
                                    name: 'model.unitName',
                                    fieldLabel: '单位名称',
                                    blankText: '单位名称不能为空',
                                    value: model.unitName,
                                },
                                    {
                                        cls: 'attr',
                                        labelStyle: 'color: red;',
                                        maxLength: 32,
                                        name: 'model.paperworkNo',
                                        fieldLabel: '证件编号',
                                        blankText: '证件编号不能为空',
                                        value: model.paperworkNo,
                                    },

                                    {
                                        cls: 'attr',
                                        labelStyle: 'color: red;',
                                        maxLength: 32,
                                        name: 'model.linkman',
                                        fieldLabel: '联系人',
                                        blankText: '联系人不能为空',
                                        value: model.linkman,
                                    },
                                    {
                                        cls: 'attr',
                                        maxLength: 32,
                                        name: 'model.enSHDM',
                                        fieldLabel: '统一社会信用代码',
                                        allowBlank: false,
                                        blankText: '统一社会信用代码不能为空',
                                        labelStyle: 'color: red;',
                                        value: model.enSHDM,
                                        // vtype: 'contact'
                                    },
                                    {
                                        cls: 'attr',
                                        maxLength: 32,
                                        name: 'model.businessAddress',
                                        fieldLabel: '经营地址',
                                        allowBlank: true,
                                        value: model.businessAddress,
                                        // vtype: 'contact'
                                    },
                                   ]
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
                                        // id: 'unitType',
                                        name: 'model.unitType.id',
                                        xtype: 'combo',
                                        store: unitTypeStore,
                                        emptyText: '请选择',
                                        mode: 'remote',
                                        valueField: 'value',
                                        displayField: 'text',
                                        triggerAction: 'all',
                                        forceSelection: true,
                                        editable: false,
                                        cls: 'attr',
                                        labelStyle: 'color: red;',
                                        hiddenName: 'model.unitType.id',
                                        fieldLabel: '单位类型',
                                        blankText: '单位类型不能为空',
                                        value: model.unitType,
                                    },
                                    {
                                        cls: 'attr',
                                        labelStyle: 'color: red;',
                                        maxLength: 32,
                                        name: 'model.orderKey',
                                        fieldLabel: '订单获取密码',
                                        blankText: '订单获取密码不能为空',
                                        value: model.orderKey,
                                    },
                                    {
                                        cls: 'attr',
                                        maxLength: 32,
                                        xtype: 'numberfield',
                                        name: 'model.contactPhone',
                                        fieldLabel: '联系人电话',
                                        allowBlank: false,
                                        blankText: '联系人电话不能为空',
                                        labelStyle: 'color: red;',
                                        value: model.contactPhone,

                                    },
                                    {
                                        cls: 'attr',
                                        maxLength: 256,
                                        name: 'model.legalPerson',
                                        allowBlank: true,
                                        fieldLabel: '法人',
                                        value: model.legalPerson,
                                    },
                                    {
                                        xtype: 'textfield',
                                        maxLength: 256,
                                        allowBlank: true,
                                        name: 'model.StorehouseAddress',
                                        fieldLabel: '仓库地址',
                                        cls: 'attr',
                                        value: model.StorehouseAddress,
                                    }]
                            }]
                        }
                    ]
                }
                ]
            }];
            return items;
        },

        show: function (model) {
            ModifyBaseModel.show('修改单位信息', 'user', 800, 300, this.getItems(model), model);
        }
    };
}();

// OptModel = function () {
// }();


//表格
GridModel = function () {
    return {
        getGrid: function () {
            var pageSize = 20;

            //添加特殊参数
            GridBaseModel.setStoreBaseParams = function (store) {
                store.on('beforeload', function (store) {
                    store.baseParams = {
                        queryString: GridBaseModel.queryString,
                        search: GridBaseModel.search
                    };
                });
            };

            var gridObj = GridInfo.getGridObj(action);

            var commands = ["create", "delete", "updatePart", "search", "query"];
            var tips = ['增加', '删除', '修改', '高级搜索', '显示全部'];
            var callbacks = [GridBaseModel.create, GridBaseModel.remove, GridBaseModel.modify, GridBaseModel.advancedsearch, GridBaseModel.showall];

            var grid = GridBaseModel.getGrid(contextPath, namespace, action, pageSize, gridObj.fields, gridObj.columns, commands, tips, callbacks);

            return grid;
        }
    }
}();


//表格
UnitPanel = function () {
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
    UnitPanel.show();
});
