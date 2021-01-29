/**
 *
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 *
 */

var namespace = 'monitor';
var action = 'backup-log';

var category = "rate";
var chartDataURL = contextPath + '/' + namespace + '/' + action + '!chart.action?category=';

BackupLogChart = function () {
    return {
        show: function (url, category, queryString, type) {
            var dataTypeLabel = '数据类型'
            var dataTypeItems = [
                {boxLabel: '成功率', name: "dataType", inputValue: 'rate', checked: category == 'rate'},
                {boxLabel: '耗时序列', name: "dataType", inputValue: 'sequence', checked: category == 'sequence'}
            ];
            var dataTypeColumns = 10;
            var dataTypeHeight = 20;

            SingleSeriesChartBaseModel.show("备份恢复统计", "backupLog", 100, url, category, queryString, type, dataTypeLabel, dataTypeItems, dataTypeColumns, dataTypeHeight);
        }
    }
}();
//统计图
ChartModel = function () {
    return {
        show: function () {
            BackupLogChart.show(chartDataURL, category, GridBaseModel.queryString, "Column3D");
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
                fieldLabel: '操作开始日期',
                vtype: "daterange",
                endDateField: "search_endOperatingTime"
            }, {
                xtype: 'datefield',
                format: "Y-m-d",
                id: 'search_endOperatingTime',
                editable: false,
                fieldLabel: '操作结束日期',
                vtype: "daterange",
                startDateField: "search_startOperatingTime"
            }, {
                xtype: 'combo',
                id: 'search_operatingType',
                store: new Ext.data.SimpleStore({
                    fields: ['value', 'text'],
                    data: [['备份', '备份'], ['恢复', '恢复']]
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
                id: 'search_operatingResult',
                store: new Ext.data.SimpleStore({
                    fields: ['value', 'text'],
                    data: [['成功', '成功'], ['失败', '失败']]
                }),
                emptyText: '请选择',
                mode: 'local',
                valueField: 'value',
                displayField: 'text',
                triggerAction: 'all',
                forceSelection: true,
                editable: false,
                fieldLabel: '操作结果'
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

            //operatingTime        
            if (!PubFunc.getTimeSearchText('search_startOperatingTime', 'search_endOperatingTime', data, 'startTime')) {
                return;
            }

            var search_operatingType = Ext.getCmp('search_operatingType').getValue();
            if (search_operatingType != "") {
                search_operatingType = 'operatingType:eq:' + search_operatingType;
                data.push(search_operatingType);
            }

            var search_operatingResult = Ext.getCmp('search_operatingResult').getValue();
            if (search_operatingResult != "") {
                search_operatingResult = 'operatingResult:eq:' + search_operatingResult;
                data.push(search_operatingResult);
            }

            AdvancedSearchBaseModel.search(data, "BackupLog");
        },

        show: function () {
            AdvancedSearchBaseModel.show('高级搜索', 'backupLog', 420, 270, this.getItems(), this.callback);
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
                {name: 'startTime'},
                {name: 'endTime'},
                {name: 'processTime'},
                {name: 'operatingType'},
                {name: 'operatingResult'}
            ];
            return fields;
        },
        getColumns: function () {
            var columns = [
                {header: "编号", width: 10, dataIndex: 'id', sortable: true},
                {header: "用户名称", width: 10, dataIndex: 'username', sortable: true},
                {header: "登录IP地址", width: 20, dataIndex: 'loginIP', sortable: true},
                {header: "服务器IP地址", width: 20, dataIndex: 'serverIP', sortable: true},
                {header: "开始处理时间", width: 20, dataIndex: 'startTime', sortable: true},
                {header: "处理完成时间", width: 20, dataIndex: 'endTime', sortable: true},
                {header: "操作类型", width: 10, dataIndex: 'operatingType', sortable: true},
                {header: "操作结果", width: 10, dataIndex: 'operatingResult', sortable: true},
                {header: "操作耗时", width: 20, dataIndex: 'processTime', sortable: true}
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