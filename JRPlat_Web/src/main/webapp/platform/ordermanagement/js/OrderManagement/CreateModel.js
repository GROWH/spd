//添加模型信息
CreateModel = function () {
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
                            {//货主ID隐藏表单
                                id: 'oocID',
                                name: 'model.ooc.id',
                                hidden: true
                            },
                            {
                                id: 'oocName',
                                cls: 'querybg',
                                labelStyle: 'color: red;',
                                name: 'model.oocName',
                                fieldLabel: '货主名称',
                                allowBlank: false,
                                blankText: '货主名称不能为空',
                                readOnly: true,
                                listeners: {
                                    focus: function (field) {
                                        var d_length = DetailModel.grid.getStore().data.length;
                                        if (d_length !== undefined && d_length > 0) {
                                            parent.Ext.ux.Toast.msg('操作提示', '添加细单后不可以变更货主!');
                                            return;
                                        }
                                        var thismodule = {};
                                        thismodule.namespace = namespace;
                                        thismodule.action = action;
                                        var queryString = '';
                                        var idList = [field.previousSibling().id, field.id];
                                        var colList = ['id', 'oocName'];
                                        QueryGridWindow.afterSetValueOpt = function () {
                                            var clearId = ['unitID','unitName'];
                                            PubFunc.clearFormByIds(clearId);
                                        };
                                        QueryGridWindow.show(thismodule, "ooc", queryString, idList, colList);
                                    }
                                }
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
                                id : 'unitID',
                                name: 'model.unit.id',
                                hidden: true
                            },
                            {
                                id: 'unitName',
                                cls: 'querybg',
                                labelStyle: 'color: red;',
                                name: 'model.unitName',
                                fieldLabel: '客户名称',
                                allowBlank: false,
                                blankText : '客户名称不能为空',
                                readOnly: true,
                                listeners: {
                                    focus: function (field) {
                                        var oocID = Ext.getCmp('oocID').getValue();
                                        if (oocID === '') {
                                            parent.Ext.ux.Toast.msg('操作提示：', '请先选择货主');
                                            return;
                                        }
                                        var d_length = DetailModel.grid.getStore().data.length;
                                        if (d_length !== undefined && d_length > 0) {
                                            parent.Ext.ux.Toast.msg('操作提示', '添加细单后不可以变更客户!');
                                            return;
                                        }
                                        var thismodule = {};
                                        thismodule.namespace = namespace;
                                        thismodule.action = action;
                                        var queryString = "ooc.id:eq:" + oocID;
                                        var idList = ["unitID", "unitName"];
                                        var colList = ["id", "unitName"];
                                        QueryGridWindow.show(thismodule, "unit", queryString, idList, colList);
                                    }
                                }
                            },
                            { //细单json
                                id: 'detail',
                                name: 'detail',
                                hidden: true
                            }
                        ]
                    }]
                },
                { //备注
                    cls: 'attr',
                    xtype: 'textarea',
                    name: 'model.slRemark',
                    fieldLabel: '备注',
                    anchor: '90%'
                },
                { //细单表格
                    xtype: 'panel',
                    frame: true,
                    height: 500,
                    bodyStyle: 'background:#ffffff;',
                    layout: 'border',
                    items: [{
                        region: 'north',
                        layout: 'fit',
                        height: 26,
                        items: [{
                            xtype: 'toolbar',
                            frame: true,
                            items: [{
                                pressed: false,
                                text: '新增',
                                iconCls: 'create',
                                handler: function () {
                                    if (CreateBaseModel.formIsValid()) {
                                        DetailModel.batchOperate();
                                    } else {
                                        parent.Ext.ux.Toast.msg('操作提示', '请先填写总单信息!');
                                    }
                                }
                            }, {
                                xtype: 'tbseparator'
                            }, {
                                pressed: false,
                                text: '删除',
                                iconCls: 'delete',
                                handler: DetailModel.deleteDetailStore
                            }]
                        }]
                    }, {
                        region: 'center',
                        layout: 'fit',
                        items: [DetailModel.getDetailGrid(0)]
                    }]
                }
            ];
            return items;
        },
        show: function () {
            CreateBaseModel.getLabelWidth = function () {
                return 110;
            };
            CreateBaseModel.shouldSubmit = function () {
                if (!DetailModel.checkNull()) {
                    return;
                }
                var detailVal = JSON.parse(Ext.getCmp('detail').value);
                var isAliasId = true;
                for(var i = 0;i<detailVal.length;i++){
                    if(detailVal[i].alias_id == ''){
                        isAliasId = false;
                    }
                }
                if(!isAliasId){
                    parent.Ext.ux.Toast.msg('操作提示：', '商品别名不能为空！');
                    return ;
                }
                //小于3则说明细单数量不为空 ""或[]
                if(Ext.getCmp('detail').value.length<3) {
                    parent.Ext.ux.Toast.msg('操作提示：', '细单数量不能为空！');
                    return false;
                }
                return true;
            };
            CreateBaseModel.show('商品别名组合新增', 'aliasGroup', 1000, 600, this.getItems(), true);
        }
    };
}();