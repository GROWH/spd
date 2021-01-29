/**
 * 公用弹出表格
 * @type {{getField, getFieldList, getStore, getBBar, getGrid, beforeSetValueOpt, afterSetValueOpt, onRowDblClick, onRowClick, refresh, getForm, beforeShow, getQueryWin, getInfo, show}}
 * 使用方式如下
 * ****************************************************************************** *
 * var thismodule = {};                                                           *
 * thismodule.namespace = "base/unitInfo";                                        *
 * thismodule.action = "unit";                                                    *
 * var queryString = "";                                                          *
 * var idList = ["unitCreat_ooc_name", "unitCreat_ooc_id"];                       *
 * var colList = ["oocName", "id"];                                               *
 * QueryGridWindow.show(thismodule, "ooc", queryString, idList, colList);         *
 * ****************************************************************************** *
 */
QueryGridWindow = function () {
    return {
        // 取得所选的数据的特定字段
        getField: function (field) {
            return this.getFieldList(field)[0];
        },
        // 取得所选的数据的特定字段
        getFieldList: function (field) {
            var recs = this.grid.getSelectionModel().getSelections();
            var list = [];
            if (recs.length > 0) {
                for (var i = 0; i < recs.length; i++) {
                    var rec = recs[i];
                    if (rec.json[field] == undefined) {
                        // parent.Ext.ux.Toast.msg('操作提示：', '表格中未定义此列【' + field + '】');
                        //后台懒加载可能会导致所需的数据不返回,直接跳过
                        continue;
                    } else {
                        list.push(rec.json[field])
                    }
                }
            }
            return list;
        },
        // 数据源
        getStore: function (fields, pageSize) {
            if (undefined == this.initUrlParams) {
                this.initUrlParams = "";
            }
            // 定义数据集对象
            var store = new Ext.data.Store({
                reader: new Ext.data.JsonReader({
                    totalProperty: 'totalProperty',
                    root: 'root'
                }, Ext.data.Record.create(fields)),
                proxy: new parent.Ext.data.HttpProxy({
                    url: contextPath + '/' + this.namespace + '/' + this.action + '!query.action' + QueryGridWindow.initUrlParams
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
        // 底部工具条
        getBBar: function (pageSize, store, columns, whichGrid) {
            return new Ext.ux.PageSizePlugin({
                rowComboSelect: true,
                pageSize: pageSize,
                store: store,
                displayInfo: true,
                columns: columns,
                saveGridSet: GridBaseModel.saveGridSet,
                cleanGridSet: GridBaseModel.cleanGridSet,
                whichGrid: whichGrid
            });
        },
        //表格组装
        getGrid: function (columns, fields, childGird) {
            var pageSize = 20;
            this.addAuditRelevant(columns, fields);
            // 表格设置
            var whichGrid = this.action + "+" + this.queryaction;
            GridBaseModel.queryGridConfig(whichGrid);
            //Gridconfig为全局变量
            var gridSetting = Gridconfig.gridSetting;
            var size = Gridconfig.size;
            var module = Gridconfig.whichGrid;
            if (module == whichGrid) {
                if (gridSetting != '' && gridSetting != undefined && size != '') {
                	columns = GridBaseModel.mergeGridCfg(columns,gridSetting);
                    pageSize = size;
                }
            }

            columns = GridBaseModel.addColumn(columns);

            this.store = this.getStore(fields, pageSize);
            var preColumns = [ // 配置表格列
                new Ext.grid.RowNumberer({
                    header: '行号',
                    width: 40
                }) // 表格行号组件
            ];
            this.tbar = [];
            var sm = null;
            if(childGird != undefined || childGird != null){
                var cb = new Ext.grid.CheckboxSelectionModel();
                preColumns = preColumns.concat(cb);
                this.tbar = childGird.tbar;
                sm = cb;
            }
            columns = preColumns.concat(columns);
            this.bbar = this.getBBar(pageSize, this.store, columns, whichGrid);
            var grid = new Ext.grid.GridPanel({
                store: this.store,
                loadMask: true,
                autoScroll: true,
                stripeRows: true,
                bbar: this.bbar,
                minColumnWidth: 80,
                viewConfig: {
                    loadingText: '数据加载中,请稍等...',
                    emptyText: '无对应信息',
                    deferEmptyText: true,
                    autoFill: true
                },
                tbar : this.tbar,
                columns: columns,
                sm: sm,
                enableKeyEvents: true
            });
            grid.on('rowdblclick', function (grid, index, e) {
            	var selectedRow = grid.store.data.items[index];
                QueryGridWindow.onRowDblClick(selectedRow);
            });
            grid.on('rowclick', function (grid, index, e) {
                var selectedRow = grid.store.data.items[index];
                QueryGridWindow.onRowClick(selectedRow);
            });
            return grid;
        },
        addAuditRelevant: function (columns, fields) {
            var arry = parent.audit_module.split(',');
            var isAudit = false;
            for (var i = 0; i < arry.length; i++) {
                //这里action的名字和配置文件（实体类）去匹配，
                //如果action和实体类不匹配，则在每个单面单独重载一下QueryGridWindow.isAudit即可
                if (arry[i] == this.querymodule || QueryGridWindow.isAudit) {
                    isAudit = true;
                    break;
                }
            }
            if (isAudit) {
                fields.unshift(
                    {name: 'eapsAuditLevel'},
                    {name: 'workFlowStatus'});
                columns.unshift(
                    {header: "审核级数", width: 20, dataIndex: 'eapsAuditLevel', sortable: true, hidden: true},
                    {
                        header: "工作流状态",
                        width: 20,
                        dataIndex: 'workFlowStatus',
                        sortable: true,
                        renderer: PubFunc.columns_render_shgz
                    }
                );
            }
        },
        beforeSetValueOpt: function (selectedRow) {
            return true;
        },
        // 单选
        afterSetValueOpt : function(selectedRow){

        },
        //表格双击赋值操作
        onRowDblClick: function (selectedRow) {
            if (this.idList.length != this.colList.length) {
                console.log("ilList长度与colList长度不同,不能操作.");
                return;
            }
            if (!this.beforeSetValueOpt(selectedRow)) {
                return;
            }
            for (var i = 0; i < this.idList.length; i++) {
                //特殊id做特殊处理 <后面不再使用这种方式>
                if (TestSpecial.test(this.idList[i], this.getField(this.colList[i]))) {
                    continue;
                }

                Ext.getCmp(this.idList[i]).setValue(this.getField(this.colList[i]));//实际值
                // Ext.getCmp(this.idList[i]).setRawValue(this.getField(this.colList[i]));//设置看到的值   后传id
            }
            this.afterSetValueOpt(selectedRow);
            this.win.close();
        },
        //表格单击回调函数
        onRowClick: function (selectedRow) {

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
                    html:'模糊查询:'
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
                        'afterrender': function (field, e) { //默认打开搜索窗聚焦
                            field.focus(false, 30);
                        }
                    }
                }]
            });
            return frm;
        },

         //多选
        beforeShow: function (grid, frm, win) {

        },

        getQueryWin: function (childGird, winId) {
            var items = [{
                region: 'north',
                layout: 'fit',
                height: 40,
                items: [this.frm]
            }, {
                region: 'center',
                layout: 'fit',
                items: [this.grid]
            }];
            if(childGird != undefined && childGird != null){
                this.childGrid = SimpleGrid.getGrid(this.namespace, this.action, childGird);
                var child = {
                    collapsible: true,
                    title: childGird.title,
                    region: 'south',
                    height: 350,
                    layout: 'fit',
                    cmargins: '5 0 0 0',
                    items: [this.childGrid]
                };
                items.push(child);
            }
            var win = new Ext.Window({
                id:winId,
                title: '查询结果',
                modal: true,
                height: 700,
                width: 1000,
                layout: 'border',
                defaults: {
                    border:false,
                    bodyStyle: 'padding:1px'
                },
                items: items,
                listeners: {
                    'close': function () {
                    	//窗体关闭后恢复默认处理
                    	QueryGridWindow.afterSetValueOpt = function(selectedRow){};
                    	QueryGridWindow.beforeSetValueOpt = function(selectedRow){ return true; };
                        QueryGridWindow.onRowClick = function(selectedRow){};
                        QueryGridWindow.beforeShow = function(grid, frm, win){};
                    }
                }
            });
            return win;
        },

        closeWin:function (){
            this.win.close();
        },
        

        /**
         * @params thismodule 请求的模块的obj,决定请求链接中的namespace 和action
         * querymodule 模块名 决定要查询哪个表格的columns fields namespace action
         * queryString 表格store查询的过滤条件
         */
        getInfo : function(thismodule, querymodule, queryString, callback, initUrlParams){
            if (undefined == initUrlParams) {
                initUrlParams = "";
            }
        	Ext.Ajax.request({
        		url: contextPath + '/' + thismodule.namespace + '/' + thismodule.action + '!query.action'+initUrlParams,
                waitTitle: '请稍等',
                waitMsg: '正在检索数据……',
                method: 'POST',
                params: {
                    propertyCriteria: queryString,
                    queryaction: querymodule + 'Action',
                    limit: 1000
                },
                success: function (response, options) {
                    var data = response.responseJSON;
                    callback(data);
                },
                failure: function (response, options) {
                    var data = response.responseText;
                    parent.Ext.ux.Toast.msg('操作提示：', '{0}', data);
                }
            });
        },

        /**
         * @params thismodule 请求的模块的obj,决定请求链接中的namespace 和action
         * querymodule 模块名 决定要查询哪个表格的columns fields namespace action
         * queryString 表格store查询的过滤条件
         * idList 要赋值的id数组
         * colList 表格store中要取的值,与idList中的参数一一对应
         * childGird 如果需要子表格，传入该配置
         */
        show: function (thismodule, querymodule, queryString, idList, colList, childGird, winId, initUrlParams) {
            this.namespace = thismodule.namespace;
            this.action = thismodule.action;
            this.querymodule = querymodule;
            // var gridObj = GridInfo.get(querymodule);
            var gridObj = GridInfo.getGridObj(querymodule);
            var columns = gridObj.columns;
            var fields = gridObj.fields;
            this.querynamespace = gridObj.namespace;
            this.queryaction = gridObj.action;

            this.queryString = queryString;
            this.initUrlParams = initUrlParams;
            this.idList = idList;
            this.colList = colList;

            this.grid = this.getGrid(columns, fields, childGird);
            this.frm = this.getForm();
            this.win = this.getQueryWin(childGird, winId);
            this.beforeShow(this.grid, this.frm, this.win);
            this.win.show();
        }
    };

}();


SimpleGrid = function () {
    return {
        newGrid :function (store, bbar, columns) {
            var grid = new Ext.grid.GridPanel({
                store: store,
                loadMask: true,
                autoScroll: true,
                stripeRows: true,
                bbar: bbar,
                viewConfig: {
                    loadingText: '数据加载中,请稍等...',
                    emptyText: '无对应信息',
                    deferEmptyText: true
                },
                columns: columns,
                enableKeyEvents: true
            });
            return grid;
        },
        newStore: function (namespace, action, fields, queryString, queryaction, pagesize) {
            // 定义数据集对象
            var store = new Ext.data.Store({
                reader: new Ext.data.JsonReader({
                    totalProperty: 'totalProperty',
                    root: 'root'
                }, Ext.data.Record.create(fields)),
                proxy: new parent.Ext.data.HttpProxy({
                    url: contextPath + '/' + namespace + '/' + action + '!query.action'
                }),
                baseParams: {
                    limit: pagesize,
                    propertyCriteria: queryString,
                    queryaction: queryaction + 'Action'
                },
            });
            store.load();
            return store;
        },
        newBBar: function (store, columns, whichGrid, pagesize) {
            return new Ext.ux.PageSizePlugin({
                rowComboSelect: true,
                pageSize: pagesize,
                store: store,
                displayInfo: true,
                columns: columns,
                saveGridSet: GridBaseModel.saveGridSet,
                cleanGridSet: GridBaseModel.cleanGridSet,
                whichGrid: whichGrid
            });
        },
        myColumns:function(whichGrid, columns, pageSize){
            GridBaseModel.queryGridConfig(whichGrid);
            var gridSetting = Gridconfig.gridSetting;
            var size = Gridconfig.size;
            var module = Gridconfig.whichGrid;
            if (module == whichGrid && gridSetting != '' && gridSetting != undefined && size != '') {
                columns = GridBaseModel.mergeGridCfg(columns,gridSetting);
                pageSize = size;
            }
            var preColumns = [
                new Ext.grid.RowNumberer({
                    header: '行号',
                    width: 40
                })
            ];
            columns = preColumns.concat(columns);
            return {
                cm:columns,
                pagesize:pageSize
            };
        },
        getGrid: function (namespace, action, childGird) {
            var gridObj = GridInfo.getGridObj(childGird.querymodule);
            var fields = gridObj.fields;
            var queryaction = gridObj.action;
            var whichGrid = action + "+" + queryaction;
            var gridCfg = this.myColumns(whichGrid, gridObj.columns, 20);
            var columns = gridCfg.cm;
            var pageSize = gridCfg.pagesize;
            var store = this.newStore(namespace, action, fields, childGird.initQueryString, queryaction, pageSize);
            var bbar = this.newBBar(store, columns, whichGrid, pageSize);
            var grid = this.newGrid(store, bbar, columns);
            return grid;
        }
    }

}();
