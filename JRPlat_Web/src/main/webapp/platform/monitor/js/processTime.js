/**
 *
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 *
 */

var namespace = 'monitor';
var action = 'process-time';

var category = "userTime";
var top = 50;
var chartDataURL = contextPath + '/' + namespace + '/' + action + '!chart.action?top=' + top + '&category=';

ProcessTimeChart = function () {
    return {
        show: function (url, category, queryString, type) {
            var dataTypeLabel = '数据类型'
            var dataTypeItems = [
                {boxLabel: '用户耗时', name: "dataType", inputValue: 'userTime', checked: category == 'userTime'},
                //{boxLabel: '耗时序列(秒)', name : "dataType", inputValue: 'sequenceSS',checked:category=='sequenceSS'},
                {boxLabel: '系统利用率', name: "dataType", inputValue: 'processRate', checked: category == 'processRate'},
                {boxLabel: '最耗时TOP50', name: "dataType", inputValue: 'top', checked: category == 'top'},
                {boxLabel: '耗时序列(分钟)', name: "dataType", inputValue: 'sequenceMM', checked: category == 'sequenceMM'},
                {boxLabel: '耗时序列(小时)', name: "dataType", inputValue: 'sequenceHH', checked: category == 'sequenceHH'},
                {boxLabel: '耗时序列(天)', name: "dataType", inputValue: 'sequenceDD', checked: category == 'sequenceDD'},
                {
                    boxLabel: '耗时序列(月)',
                    name: "dataType",
                    inputValue: 'sequenceMonth',
                    checked: category == 'sequenceMonth'
                }
            ];
            var dataTypeColumns = 10;
            var dataTypeHeight = 20;

            SingleSeriesChartBaseModel.show("操作耗时统计", "processTime", 100, url, category, queryString, type, dataTypeLabel, dataTypeItems, dataTypeColumns, dataTypeHeight);
        }
    }
}();
//统计图
ChartModel = function () {
    return {
        show: function () {
            ProcessTimeChart.show(chartDataURL, category, GridBaseModel.queryString, "Column3D");
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
                id: 'search_startStartTime',
                editable: false,
                fieldLabel: '处理开始日期',
                vtype: "daterange",
                endDateField: "search_endStartTime"
            }, {
                xtype: 'datefield',
                format: "Y-m-d",
                id: 'search_endStartTime',
                editable: false,
                fieldLabel: '处理结束日期',
                vtype: "daterange",
                startDateField: "search_startStartTime"
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

            if (!PubFunc.getTimeSearchText('search_startStartTime', 'search_endStartTime', data, 'startTime')) {
                return;
            }

            AdvancedSearchBaseModel.search(data, "ProcessTime");
        },

        show: function () {
            AdvancedSearchBaseModel.show('高级搜索', "processTime", 420, 180, this.getItems(), this.callback);
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
                {name: 'userIP'},
                {name: 'serverIP'},
                {name: 'resource'},
                {name: 'startTime'},
                {name: 'endTime'},
                {name: 'processTime'}
            ];
            return fields;
        },
        getColumns: function () {
            var columns = [
                {header: "编号", width: 8, dataIndex: 'id', sortable: true},
                {header: "用户名称", width: 10, dataIndex: 'username', sortable: true},
                {header: "用户IP地址", width: 15, dataIndex: 'userIP', sortable: true},
                {header: "服务器IP地址", width: 15, dataIndex: 'serverIP', sortable: true},
                {header: "资源路径", width: 40, dataIndex: 'resource', sortable: true},
                {header: "开始处理时间", width: 20, dataIndex: 'startTime', sortable: true},
                {header: "处理完成时间", width: 20, dataIndex: 'endTime', sortable: true},
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