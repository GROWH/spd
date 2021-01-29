/*
 * 全局数据初始化 
 * 省市区数据字典初始化
 * 
 */

InitData = function () {
    /*provinceStore = new Ext.data.Store({ //省
        proxy: new parent.Ext.data.HttpProxy({
            url: contextPath + '/basicData/address!store.action?fatherBm=0'
        }),
        reader: new Ext.data.JsonReader({},
            Ext.data.Record.create([{
                name: 'value'
            }, {
                name: 'text'
            }]))
    });
    cityStore = new Ext.data.Store({ //市
        proxy: new parent.Ext.data.HttpProxy({
            url: ''
        }),
        reader: new Ext.data.JsonReader({},
            Ext.data.Record.create([{
                name: 'value'
            }, {
                name: 'text'
            }]))
    });
    areaStore = new Ext.data.Store({ //区
        proxy: new parent.Ext.data.HttpProxy({
            url: ''
        }),
        reader: new Ext.data.JsonReader({},
            Ext.data.Record.create([{
                name: 'value'
            }, {
                name: 'text'
            }]))
    });*/
}();

/*
 * 
 * GridConfig 表格配置保存和查询
 *
 */

GridConfig = function () {
    return {
        //表格设置查询
        queryGridConfig: function (whichGrid) {
            var url = contextPath + '/extgridconfig/gridconfig!query.action';
            $.ajax({
                type: 'POST',
                url: url,
                async: false,
                data: {
                    whichGrid: whichGrid
                },
                success: function (response, options) {
                    if (response != '') {
                        var model = {};
                        var data = response.gridSetting;
                        model = eval('(' + data + ')');
                        var gridconfig = {};
                        gridconfig.gridSetting = model;
                        gridconfig.pageSize = Number(response.size);
                        GridConfig.gridconfig = gridconfig;
                    } else {
                        GridConfig.gridconfig = {};
                    }
                },
            });
            return this.gridconfig;
        },
        //表格设置保存
        saveGridSet: function (grid, whichGrid) {
            var config = [];
            var oldGridConfig = grid.colModel.config;
            var length = oldGridConfig.length;
            for (var i = 0; i < length; i++) {
                if (oldGridConfig[i].dataIndex == "" || oldGridConfig[i].dataIndex == undefined) {
                    continue;
                }
                var configItem = {};
                configItem.dataIndex = oldGridConfig[i].dataIndex;
                configItem.header = oldGridConfig[i].header;
                configItem.sortable = oldGridConfig[i].sortable;
                configItem.width = oldGridConfig[i].width;
                configItem.hidden = oldGridConfig[i].hidden;
                config.push(configItem);
            }
            var gridSetting = JSON.stringify(config); // 可以将json对象转换成json字符串
            var pageSize = grid.bottomToolbar.pageSize;
            parent.Ext.Ajax.request({
                url: contextPath + '/extgridconfig/gridconfig!save.action',
                waitTitle: '请稍等',
                waitMsg: '正在检索数据……',
                method: 'POST',
                params: {
                    gridSetting: gridSetting,
                    size: pageSize,
                    whichGrid: whichGrid
                },
                success: function (response, options) {
                    parent.Ext.ux.Toast.msg('操作提示：', '操作成功');
                },
                failure: function (response, options) {
                    parent.Ext.ux.Toast.msg('操作提示：', '操作失败');
                }
            });
        }
    }
}();

/*
 * 公用弹出表格
 * 搜索赋值
 */

QueryGridWindow = function () {
    return {
        // 取得所选的数据的特定字段
        getField: function (field) {
            var recs = this.grid.getSelectionModel().getSelections();
            var rec = recs[0].get(field);
            return rec;
        },
        // 数据源
        getStore: function (fields, pageSize) {
            // 定义数据集对象
            var store = new Ext.data.Store({
                reader: new Ext.data.JsonReader({
                    totalProperty: 'totalProperty',
                    root: 'root'
                }, Ext.data.Record.create(fields)),
                proxy: new parent.Ext.data.HttpProxy({
                    url: contextPath + '/' + this.namespace + '/' + this.action + '!query.action'
                })
            });
            store.on('beforeload', function (store) {
                store.baseParams = {
                    propertyCriteria: QueryGridWindow.queryString,
                    queryaction: QueryGridWindow.queryaction + 'Action'
                };
            });
            store.load({
                params: {
                    limit: pageSize
                }
            });
            return store;
        },
        saveGridSet: function () {
            var gridName = QueryGridWindow.action + "+" + QueryGridWindow.queryaction;
            GridConfig.saveGridSet(QueryGridWindow.grid, gridName);
        },
        // 底部工具条
        getBBar: function (pageSize, store) {
            return new Ext.ux.PageSizePlugin({
                rowComboSelect: true,
                pageSize: pageSize,
                store: store,
                displayInfo: true,
                saveGridSet: this.saveGridSet
            });
        },
        //表格组装
        getGrid: function (columns, fields) {
            var pageSize = 20;

            //表格设置查询
            var gridConfig = GridConfig.queryGridConfig(this.action + "+" + this.queryaction);
            var gridSetting = gridConfig.gridSetting;
            var size = gridConfig.pageSize;
            if (gridSetting != '' && gridSetting != undefined && size != '') {
                columns = gridSetting;
                pageSize = size;
            }

            this.store = this.getStore(fields, pageSize);
            this.bbar = this.getBBar(pageSize, this.store);
            var preColumns = [// 配置表格列
                new Ext.grid.RowNumberer({
                    header: '行号',
                    width: 40
                }),// 表格行号组件
            ];
            columns = preColumns.concat(columns);
            var grid = new Ext.grid.GridPanel({
                store: this.store,
                loadMask: true,
                autoScroll: true,
                stripeRows: true,
                bbar: this.bbar,
                viewConfig: {
                    loadingText: '数据加载中,请稍等...',
                    emptyText: '无对应信息',
                    deferEmptyText: true
                },
                columns: columns,
                enableKeyEvents: true
            });
            grid.on('rowdblclick', function (grid, index, e) {
                QueryGridWindow.onRowDblClick();
            });
            return grid;
        },
        //表格双击赋值操作
        onRowDblClick: function () {
            if (this.idList.length != this.colList.length) {
                console.log("idList长度与colList长度不同,不能操作.");
                return;
            }
            for (var i = 0; i < this.idList.length; i++) {
                /*
                 * 特殊id做特殊处理
                 * 返回值为true则不继续执行,否则继续后续操作
                 * */
                if (TestSpecial.test(this.idList[i])) {
                    continue;
                }

                Ext.getCmp(this.idList[i]).setValue(this.getField(this.colList[i]));
            }
            this.win.close();
        },
        refresh: function (likequeryString) {
            this.store.on('beforeload', function (store) {
                store.baseParams = {
                    likeQueryType: QueryGridWindow.queryaction,
                    likeQueryInfo: likequeryString,
                    propertyCriteria: QueryGridWindow.queryString,
                    queryaction: QueryGridWindow.queryaction + 'Action'
                };
            });
            this.store.load({
                params: {
                    limit: this.bbar.pageSize
                }
            });
        },
        getForm: function () {
            var frm = new Ext.form.FormPanel({
                frame: true,
                border: false,
                layout: 'table',
                items: [{
                    width: 80,
                    layout: 'form',
                    border: false,
                    items: [{
                        fieldLabel: '模糊查询',
                    }]
                }, {
                    id: 'likequeryTextfieldId',
                    xtype: 'textfield',
                    cls: 'attr',
                    width: 200,
                    emptyText: '输入搜索条件',
                    listeners: {
                        'specialkey': function (field, e) {
                            if (e.keyCode == 13) {
                                QueryGridWindow.refresh(Ext.getCmp('likequeryTextfieldId').getValue());
                            }
                        },
                        'afterrender': function (field, e) {//默认打开搜索窗聚焦
                            field.focus(false, 30);
                        }
                    }
                }]
            });
            return frm;
        },
        getQueryWin: function () {
            var win = new Ext.Window({
                title: '查询结果',
                modal: true,
                height: 400,
                width: 700,
                layout: 'border',
                items: [{
                    region: 'north',
                    layout: 'fit',
                    height: 40,
                    items: [this.frm]
                }, {
                    region: 'center',
                    layout: 'fit',
                    items: [this.grid]
                }]
            });
            return win;
        },

        /*
         * @params thismodule 请求的模块的obj,决定请求链接中的namespace 和action
         * querymodule 模块名 决定要查询哪个表格的columns fields namespace action 
         * queryString 表格store查询的过滤条件
         * idList 要赋值的id数组
         * colList 表格store中要取的值,与idList中的参数一一对应
         */
        show: function (thismodule, querymodule, queryString, idList, colList) {
            this.namespace = thismodule.namespace;
            this.action = thismodule.action;

            var gridObj = GridInfo.get(querymodule);
            var columns = gridObj.columns;
            var fields = gridObj.fields;
            this.querynamespace = gridObj.namespace;
            this.queryaction = gridObj.action;

            this.queryString = queryString;
            this.idList = idList;
            this.colList = colList;

            this.grid = this.getGrid(columns, fields);
            this.frm = this.getForm();
            this.win = this.getQueryWin();
            this.win.show();
        }
    }
}();

/*
 * 百度地图
 * */
BDMap = function () {
    return {
        show: function (address, addressIds) {
            new Ext.Window({
                title: '百度地图',
                height: document.body.clientHeight / 2,
                width: document.body.clientWidth / 2,
                x: document.body.clientWidth / 2,
                y: 0,
                modal: true,
                layout: 'fit',
                items: [{
                    html: '<iframe id="frame" name="frame" src="' +
                        contextPath + '/platform/js/map.html?address=' +
                        address + '&detail=' + addressIds.detail + '&lng=' +
                        addressIds.longitude + '&lat=' + addressIds.latitude + '" width=100% height=100%/>'
                }],
                maximizable: true
            }).show();
        }
    }
}();
