
var namespace = 'commodityInfo';
var action = 'commodity';



//高级搜索
// AdvancedSearchModel = function () {
//     return {
//         //搜索表单
//         getItems: function () {
//             var items = [
//                 {
//                     xtype: 'textfield',
//                     id: 'productName',
//                     fieldLabel: '产品名称'
//                 },
//                 {
//                     xtype: 'textfield',
//                     id: 'specification',
//                     fieldLabel: '规格型号'
//                 }
//             ];
//             return items;
//         },
//         //点击搜索之后的回调方法
//         callback: function () {
//             var data = [];
//
//             var productName = Ext.getCmp('productName').getValue();
//             if (productName != "") {
//                 productName = 'productName:eq:$' + productName;
//                 data.push(productName);
//             }
//
//             var specification = Ext.getCmp('specification').getValue();
//             if (specification != "") {
//                 specification = 'realName:eq:' + specification;
//                 data.push(specification);
//             }
//             AdvancedSearchBaseModel.search(data, "User");
//         },
//
//         show: function () {
//             AdvancedSearchBaseModel.show('高级搜索', "user", 420, 170, this.getItems(), this.callback);
//         }
//     };
// }();

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
                                    name: 'model.productName',
                                    fieldLabel: '产品名称',
                                    blankText: '产品名称不能为空',
                                },
                                    {
                                    cls: 'attr',
                                    labelStyle: 'color: red;',
                                    maxLength: 32,
                                    name: 'model.quantity',
                                    fieldLabel: '包装数量',
                                    blankText: '包装数量不能为空',
                                },

                                    {
                                        xtype: 'combo',
                                        store: encodingFormatStore,
                                        emptyText: '请选择',
                                        mode: 'remote',
                                        valueField: 'value',
                                        displayField: 'text',
                                        triggerAction: 'all',
                                        forceSelection: true,
                                        editable: false,
                                        cls: 'attr',
                                        labelStyle: 'color: red;',
                                        hiddenName: 'model.encodingFormat.id',
                                        name: 'model.encodingFormat.id',
                                        fieldLabel: '编号格式',
                                        blankText: '编号格式不能为空'
                                    },
                                    {
                                        cls: 'attr',
                                        labelStyle: 'color: red;',
                                        maxLength: 32,
                                        name: 'model.number',
                                        fieldLabel: '编号',
                                        blankText: '编号不能为空'
                                    },
                                    {
                                        cls: 'attr',
                                        maxLength: 256,
                                        name: 'model.ZCNumber',
                                        allowBlank: true,
                                        fieldLabel: '注册证编号/备案凭证编码',
                                        height:50
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
                                        cls: 'attr',
                                        labelStyle: 'color: red;',
                                        maxLength: 32,
                                        name: 'model.specification',
                                        fieldLabel: '规格型号',
                                        blankText: '规格型号不能为空'
                                    },
                                    {
                                        cls: 'attr',
                                        labelStyle: 'color: red;',
                                        maxLength: 32,
                                        name: 'model.packingUnit',
                                        fieldLabel: '包装单位',
                                        blankText: '包装单位不能为空'
                                    },
                                    {
                                        cls: 'attr',
                                        labelStyle: 'color: red;',
                                        maxLength: 32,
                                        name: 'model.FLNumber',
                                        fieldLabel: '分类编码',
                                        blankText: '分类编码不能为空'
                                    },
                                    {
                                        cls: 'attr',
                                        maxLength: 32,
                                        name: 'model.manufacturer',
                                        fieldLabel: '生产企业名称',
                                        blankText: '生产企业名称不能为空',
                                        labelStyle: 'color: red;'

                                    },
                                    {
                                        cls: 'attr',
                                        maxLength: 32,
                                        name: 'model.YFLNumber',
                                        fieldLabel: '原分类编码',
                                        allowBlank: true,
                                    },

                                    ]
                            }]
                        }
                    ]
                }
                ]
            }];
            return items;
        },

        show: function () {
            CreateBaseModel.show('添加商品信息', 'user', 800, 300, this.getItems());
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
                                items: [
                                    {
                                        cls: 'attr',
                                        labelStyle: 'color: red;',
                                        maxLength: 32,
                                        name: 'model.productName',
                                        fieldLabel: '产品名称',
                                        blankText: '产品名称不能为空',
                                        value: model.productName,
                                    },
                                    {
                                    cls: 'attr',
                                    labelStyle: 'color: red;',
                                    maxLength: 32,
                                    name: 'model.quantity',
                                    fieldLabel: '包装数量',
                                    blankText: '包装数量不能为空',
                                    value: model.quantity,
                                },

                                    {
                                        xtype: 'combo',
                                        store: encodingFormatStore,
                                        emptyText: '请选择',
                                        mode: 'remote',
                                        valueField: 'value',
                                        displayField: 'text',
                                        triggerAction: 'all',
                                        forceSelection: true,
                                        editable: false,
                                        cls: 'attr',
                                        labelStyle: 'color: red;',
                                        hiddenName: 'model.encodingFormat.id',
                                        name: 'model.encodingFormat.id',
                                        fieldLabel: '编号格式',
                                        blankText: '编号格式不能为空',
                                        value: model.encodingFormat,
                                    },
                                    {
                                        cls: 'attr',
                                        labelStyle: 'color: red;',
                                        maxLength: 32,
                                        name: 'model.number',
                                        fieldLabel: '编号',
                                        blankText: '编号不能为空',
                                        value: model.number,
                                    },
                                    {
                                        cls: 'attr',
                                        maxLength: 256,
                                        name: 'model.ZCNumber',
                                        allowBlank: true,
                                        fieldLabel: '注册证编号/备案凭证编码',
                                        value: model.ZCNumber,
                                        height:50
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
                                        cls: 'attr',
                                        labelStyle: 'color: red;',
                                        maxLength: 32,
                                        name: 'model.specification',
                                        fieldLabel: '规格型号',
                                        blankText: '规格型号不能为空',
                                        value: model.specification,
                                    },
                                    {
                                        cls: 'attr',
                                        labelStyle: 'color: red;',
                                        maxLength: 32,
                                        name: 'model.packingUnit',
                                        fieldLabel: '包装单位',
                                        blankText: '包装单位不能为空',
                                        value: model.PackingUnit,
                                    },
                                    {
                                        cls: 'attr',
                                        labelStyle: 'color: red;',
                                        maxLength: 32,
                                        name: 'model.FLNumber',
                                        fieldLabel: '分类编码',
                                        blankText: '分类编码不能为空',
                                        value: model.FLNumber,
                                    },
                                    {
                                        cls: 'attr',
                                        maxLength: 32,
                                        name: 'model.manufacturer',
                                        fieldLabel: '生产企业名称',
                                        blankText: '生产企业名称不能为空',
                                        labelStyle: 'color: red;',
                                        value: model.manufacturer,

                                    },
                                    {
                                        cls: 'attr',
                                        maxLength: 32,
                                        name: 'model.YFLNumber',
                                        fieldLabel: '原分类编码',
                                        allowBlank: true,
                                        value: model.YFLNumber,
                                    },

                                ]
                            }]
                        }
                    ]
                }
                ]
            }];
            return items;
        },

        show: function (model) {
            ModifyBaseModel.show('修改商品信息', 'user', 800, 300, this.getItems(model), model);
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

            var commands = ["create", "delete", "updatePart", "query"];
            var tips = ['增加', '删除', '修改', '显示全部'];
            var callbacks = [GridBaseModel.create, GridBaseModel.remove, GridBaseModel.modify, GridBaseModel.showall];

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
