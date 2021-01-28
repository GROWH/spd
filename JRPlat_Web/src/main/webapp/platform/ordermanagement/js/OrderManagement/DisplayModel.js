//显示模型详细信息
DisplayModel = function () {
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
                            readOnly: true,
                            fieldClass: 'detail_field',
                            anchor: "90%"
                        },

                        items: [
                            {
                                value: model.oocName,
                                fieldLabel: '货主名称'
                            }
                        ]
                    }, {
                        columnWidth: .5,
                        layout: 'form',
                        defaultType: 'textfield',
                        defaults: {
                            readOnly: true,
                            fieldClass: 'detail_field',
                            anchor: "90%"
                        },

                        items: [

                            {
                                value: model.unitName,
                                fieldLabel: '客户名称'
                            },
                            {
                                value: model.commoditys,
                                fieldLabel: '商品ID组',
                                hidden: true
                            },
                            {
                                value: model.commAlias,
                                fieldLabel: '商品对应数量',
                                hidden: true
                            }
                        ]
                    }]
                },
                { //备注
                    xtype: 'textarea',
                    value: model.slRemark,
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
                        region: 'center',
                        layout: 'fit',
                        items: [DetailModel.getDetailGrid(model.id,true)]
                    }]
                }
            ];
            return items;
        },
        show: function (model) {
            // setTimeout(function () {
            //     parent.Ext.Msg.wait("正在查询数据......", '请稍候');
            // },300)
            DisplayBaseModel.show('商品别名详细信息', 'aliasGroup', 1000, 600, this.getItems(model), true);
        }
    };
}();