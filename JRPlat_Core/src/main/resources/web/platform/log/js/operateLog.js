/**
 *
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 *
 */

var namespace = 'log';
var action = 'operate-log';

var category = "operateType";
var chartDataURL = contextPath + '/' + namespace + '/' + action + '!chart.action?category=';

OperateLogChart = function () {
    return {
        show: function (url, category, queryString, type) {
            var dataTypeLabel = '数据类型'
            var dataTypeItems = [
                {boxLabel: '用户', name: "dataType", inputValue: 'user', checked: category == 'user'},
                {boxLabel: '操作类型', name: "dataType", inputValue: 'operateType', checked: category == 'operateType'}
            ];
            var dataTypeColumns = 10;
            var dataTypeHeight = 20;

            MultiSeriesChartBaseModel.show("增删改统计", "cud", 100, url, category, queryString, type, dataTypeLabel, dataTypeItems, dataTypeColumns, dataTypeHeight);
        }
    }
}();
//统计图
ChartModel = function () {
    return {
        show: function () {
            OperateLogChart.show(chartDataURL, category, GridBaseModel.queryString, "MSColumn3D");
        }
    }
}();
//高级搜索
AdvancedSearchModel = function () {
    return {
        //搜索表单
        getItems: function () {
            var items = [{
                xtype: 'combo',
                id: 'search_username',
                store: userStore,
                emptyText: '请选择',
                mode: 'remote',
                valueField: 'value',
                displayField: 'text',
                triggerAction: 'all',
                forceSelection: true,
                editable: false,
                fieldLabel: '用户名'
            }, {
                xtype: 'datefield',
                format: "Y-m-d",
                id: 'search_startOperatingTime',
                editable: false,
                fieldLabel: '开始日期'
            }, {
                xtype: 'datefield',
                format: "Y-m-d",
                id: 'search_endOperatingTime',
                editable: false,
                fieldLabel: '结束日期'
            }, {
                xtype: 'combo',
                id: 'search_operatingType',
                store: new Ext.data.SimpleStore({
                    fields: ['value', 'text'],
                    data: [['增加', '增加'], ['删除', '删除'], ['修改', '修改']]
                }),
                emptyText: '请选择',
                mode: 'local',
                valueField: 'value',
                displayField: 'text',
                triggerAction: 'all',
                forceSelection: true,
                editable: false,
                fieldLabel: '操作类型'
            }, {
                xtype: 'combo',
                id: 'search_operatingModel',
                store: modelStore,
                emptyText: '请选择',
                mode: 'remote',
                valueField: 'value',
                displayField: 'text',
                triggerAction: 'all',
                forceSelection: true,
                editable: false,
                fieldLabel: '模型名称'
            }];
            return items;
        },
        //点击搜索之后的回调方法
        callback: function () {
            var data = [];

            var search_username = Ext.getCmp('search_username').getValue();
            if (search_username != "") {
                search_username = 'username:eq:' + search_username;
                data.push(search_username);
            }

            var search_operatingType = Ext.getCmp('search_operatingType').getValue();
            if (search_operatingType != "") {
                search_operatingType = 'operatingType:eq:' + search_operatingType;
                data.push(search_operatingType);
            }

            var search_operatingModel = Ext.getCmp('search_operatingModel').getValue();
            if (search_operatingModel != "") {
                search_operatingModel = 'operatingModel:eq:' + search_operatingModel;
                data.push(search_operatingModel);
            }

            if (!PubFunc.getTimeSearchText('search_startOperatingTime', 'search_endOperatingTime', data, 'operatingTime')) {
                return;
            }
            AdvancedSearchBaseModel.search(data, "OperateLog");
        },

        show: function () {
            AdvancedSearchBaseModel.show('高级搜索', "cud", 420, 240, this.getItems(), this.callback);
        }
    };
}();
//表格
GridModel = function () {
    return {
        getFields: function () {
            var fields = [
                {name: 'id'},
                {name: 'username'},
                {name: 'loginIP'},
                {name: 'serverIP'},
                {name: 'operatingTime'},
                {name: 'operatingType'},
                {name: 'operatingModel'},
                {name: 'operatingID'}
            ];
            return fields;
        },
        getColumns: function () {
            var columns = [
                {header: "编号", width: 10, dataIndex: 'id', sortable: true},
                {header: "用户名称", width: 20, dataIndex: 'username', sortable: true},
                {header: "登录IP地址", width: 20, dataIndex: 'loginIP', sortable: true},
                {header: "服务器IP地址", width: 20, dataIndex: 'serverIP', sortable: true},
                {header: "操作时间", width: 20, dataIndex: 'operatingTime', sortable: true},
                {header: "操作类型", width: 10, dataIndex: 'operatingType', sortable: true},
                {header: "操作模型", width: 20, dataIndex: 'operatingModel', sortable: true},
                {header: "模型ID", width: 20, dataIndex: 'operatingID', sortable: true, hidden: true}
            ];
            return columns;
        },
        show: function () {
            var pageSize = 20;

            var commands = ["search", "query", "chart", "delete"];
            var tips = ['高级搜索', '显示全部', '图表', '删除记录'];
            var callbacks = [GridBaseModel.advancedsearch, GridBaseModel.showall, GridBaseModel.chart, GridBaseModel.remove];
            GridBaseModel.show(contextPath, namespace, action, pageSize, this.getFields(), this.getColumns(), commands, tips, callbacks);
            GridBaseModel.onRowDblClick = function () {
            };
        }
    }
}();

Ext.onReady(function () {
    GridModel.show();
});