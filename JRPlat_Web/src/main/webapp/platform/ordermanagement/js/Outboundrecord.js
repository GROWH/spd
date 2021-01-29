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
var action='outbound-records';

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
                                    {
                                        cls: 'attr',
                                        maxLength: 32,
                                        name: 'model.version',
                                        fieldLabel: '版本号',
                                        allowBlank: true,
                                    }]
                            }, {
                                columnWidth: .5,
                                layout: 'form',
                                defaultType: 'textfield',
                                defaults: {
                                    allowBlank: false,
                                    anchor: "90%"
                                },


                                items: [{
                                    id: 'unitType',
                                    xtype: 'combo',
                                    store: userStateStore,
                                    emptyText: '',
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
            CreateBaseModel.show('添加出库记录', 'outbound-records', 800, 300, this.getItems());
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
                                        // vtype: 'contact'
                                    },
                                    {
                                        cls: 'attr',
                                        maxLength: 32,
                                        name: 'model.version',
                                        fieldLabel: '版本号',
                                        allowBlank: true,
                                        value: model.version,
                                    }]
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
                                    name: 'model.unitType',
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

        show: function (model) {
            // ModifyBaseModel.prepareSubmit = function () {
            //     Ext.getCmp('roles').setValue(roleSelector.getValue());
            //     if ("启用" == Ext.getCmp('state').getValue()) {
            //         Ext.getCmp('state').setValue("true");
            //     }
            //     if ("停用" == Ext.getCmp('state').getValue()) {
            //         Ext.getCmp('state').setValue("false");
            //     }
            // }
            ModifyBaseModel.show('修改出库记录', 'outbound-records', 800, 300, this.getItems(model), model);
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
