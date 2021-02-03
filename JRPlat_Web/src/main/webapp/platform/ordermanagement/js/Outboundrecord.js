/**
 *
 * APDPlat - Application Product Development Platform
 * Copyright (c) 2013, 杨尚川, yang-shangchuan@qq.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

var namespace='ordermanagement';
var action='outboundrecords';

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
                                    anchor: "90%"
                                },
                                items: [
                                    {
                                        id: 'orderId',
                                        name: 'model.order.id',
                                        xtype : 'textfield',
                                        allowBlank: false,
                                        fieldLabel: '订单流水号',
                                        blankText: '订单流水号不能为空',
                                        hidden: true
                                    },
                                    {
                                        id: 'KHName',
                                        name: 'model.KHName',
                                        cls: 'querybg',
                                        labelStyle: 'color: red;',
                                        maxLength: 32,
                                        fieldLabel: '下单方名称',
                                        blankText: '下单方名称不能为空',
                                        readOnly: true,
                                        listeners: {
                                            focus: function (field) {
                                                var querymodule = 'order-management';
                                                var queryString = ""; //搜索条件
                                                var idList = ["orderId", 'KHName']; //赋值文本框的id List
                                                var colList = ["id",'KHName_unitName']; //表格列名List 与idList一一对应
                                                QueryGridWindow.show(querymodule, queryString, idList, colList,'', '订单信息');
                                            }
                                        }
                                    },
                                    {
                                        id:'orderNum',
                                        cls: 'attr',
                                        labelStyle: 'color: red;',
                                        maxLength: 32,
                                        name: 'model.shipmentQuantity',
                                        fieldLabel: '收单方发货数量',
                                        blankText: '收单方发货数量不能为空',
                                    },
                                    {
                                        id: 'effectiveDate',
                                        xtype: 'datefield',
                                        format: "Y-m-d",
                                        editable: false,
                                        vtype: "daterange",
                                        labelStyle: 'color: red;',
                                        maxLength: 32,
                                        name: 'model.effectiveDate',
                                        fieldLabel: '有效期',
                                        blankText: '有效期不能为空',
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
                                        id: 'proId',
                                        name: 'model.orderInformation.id',
                                        xtype : 'textfield',
                                        allowBlank: false,
                                        fieldLabel: '细单号',
                                        blankText: '细单号不能为空',
                                        hidden: true
                                    },
                                    {
                                        id: "proName",
                                        cls: 'querybg',
                                        labelStyle: 'color: red;',
                                        maxLength: 32,
                                        fieldLabel: '产品名称',
                                        name: 'model.proName',
                                        blankText: '产品名称不能为空',
                                        readOnly: true,
                                        listeners: {
                                            focus: function (field) {
                                                var oId = Ext.getCmp('orderId').getValue();
                                                var querymodule = 'order-management-x';
                                                var interfaceName = '!queryOrderInformation.action';//
                                                var queryString = "orderManagement.id:eq:" + oId; //搜索条件
                                                var idList = ["proId","proName","orderNum","orderPH","effectiveDate"]; //赋值文本框的id List
                                                var colList = ["id","productName","orderQuantity","lotNumber","effectiveDate"]; //表格列名List 与idList一一对应
                                                QueryGridWindow.show(querymodule, queryString, idList, colList, '','订单信息-细单', interfaceName);
                                            }
                                        }
                                    },
                                    {
                                        id: 'orderPH',
                                        cls: 'attr',
                                        labelStyle: 'color: red;',
                                        maxLength: 32,
                                        name: 'model.lotNumber',
                                        fieldLabel: '批号',
                                        blankText: '批号不能为空',
                                    },
                                    {
                                        id:'deliveryTime',
                                        xtype: 'datefield',
                                        format: "Y-m-d",
                                        editable: false,
                                        vtype: "daterange",
                                        labelStyle: 'color: red;',
                                        maxLength: 32,
                                        name: 'model.deliveryTime',
                                        fieldLabel: '出货时间',
                                        blankText: '出货时间不能为空',
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
            CreateBaseModel.show('添加出库记录', 'outboundrecords', 800, 250, this.getItems());
        }
    };
}();
//修改模型信息
ModifyModel = function () {
    return {
        getItems: function (model) {
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
                                items: [
                                    {
                                        id: 'proId',
                                        name: 'model.orderInformation.id',
                                        value:model.orderInformation_id,
                                        xtype : 'textfield',
                                        allowBlank: false,
                                        fieldLabel: '细单号',
                                        blankText: '细单号不能为空',
                                        hidden: true
                                    },
                                    {
                                        id: "proId",
                                        cls: 'attr',
                                        labelStyle: 'color: red;',
                                        maxLength: 32,
                                        fieldLabel: '产品名称',
                                        name: 'model.proName',
                                        value:model.productName,
                                        blankText: '产品名称不能为空',
                                        readOnly: true,
                                    },
                                    {
                                        id:'orderNum',
                                        cls: 'attr',
                                        labelStyle: 'color: red;',
                                        maxLength: 32,
                                        name: 'model.shipmentQuantity',
                                        value: model.shipmentQuantity,
                                        fieldLabel: '收单方发货数量',
                                        blankText: '收单方发货数量不能为空',
                                    },
                                    {
                                        id:'deliveryTime',
                                        xtype: 'datefield',
                                        format: "Y-m-d",
                                        editable: false,
                                        vtype: "daterange",
                                        labelStyle: 'color: red;',
                                        maxLength: 32,
                                        name: 'model.deliveryTime',
                                        value: model.deliveryTime,
                                        fieldLabel: '出货时间',
                                        blankText: '出货时间不能为空',
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
                                        id: 'orderPH',
                                        cls: 'attr',
                                        labelStyle: 'color: red;',
                                        maxLength: 32,
                                        name: 'model.lotNumber',
                                        value: model.lotNumber,
                                        fieldLabel: '批号',
                                        blankText: '批号不能为空',
                                    },
                                    {
                                        id: 'effectiveDate',
                                        xtype: 'datefield',
                                        format: "Y-m-d",
                                        editable: false,
                                        vtype: "daterange",
                                        labelStyle: 'color: red;',
                                        maxLength: 32,
                                        name: 'model.effectiveDate',
                                        value: model.effectiveDate,
                                        fieldLabel: '有效期',
                                        blankText: '有效期不能为空',
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
            ModifyBaseModel.show('修改出库记录', 'outboundrecords', 800, 300, this.getItems(model), model);
        }
    };
}();
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
});
