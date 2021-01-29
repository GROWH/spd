/**
 *
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 *
 */

var namespace = 'monitor';
var action = 'runing-time';

var category = "runingRate";
var chartDataURL = contextPath + '/' + namespace + '/' + action + '!chart.action?category=';

RuningTimeChart = function () {
    return {
        show: function (url, category, queryString, type) {
            var dataTypeLabel = '数据类型'
            var dataTypeItems = [
                {boxLabel: '运行效率', name: "dataType", inputValue: 'runingRate', checked: category == 'runingRate'},
                {
                    boxLabel: '运行序列',
                    name: "dataType",
                    inputValue: 'runingSequence',
                    checked: category == 'runingSequence'
                }
            ];
            var dataTypeColumns = 10;
            var dataTypeHeight = 20;

            SingleSeriesChartBaseModel.show("系统运行时间统计", "runingTime", 100, url, category, queryString, type, dataTypeLabel, dataTypeItems, dataTypeColumns, dataTypeHeight);
        }
    }
}();
//统计图
ChartModel = function () {
    return {
        show: function () {
            RuningTimeChart.show(chartDataURL, category, GridBaseModel.queryString, "Column3D");
        }
    }
}();
//高级搜索
AdvancedSearchModel = function () {
    return {
        //搜索表单
        getItems: function () {
            var items = [{
                xtype: 'datefield',
                format: "Y-m-d",
                id: 'search_startStartupTime',
                editable: false,
                fieldLabel: '启动开始日期',
                vtype: "daterange",
                endDateField: "search_endStartupTime"
            }, {
                xtype: 'datefield',
                format: "Y-m-d",
                id: 'search_endStartupTime',
                editable: false,
                fieldLabel: '启动结束日期',
                vtype: "daterange",
                startDateField: "search_startStartupTime"
            }];
            return items;
        },
        //点击搜索之后的回调方法
        callback: function () {
            var data = [];
            //时间类型
            if (!PubFunc.getTimeSearchText('search_startStartupTime', 'search_endStartupTime', data, 'startupTime')) {
                return;
            }
            AdvancedSearchBaseModel.search(data, "RuningTime");
        },

        show: function () {
            AdvancedSearchBaseModel.show('高级搜索', "runingTime", 420, 180, this.getItems(), this.callback);
        }
    };
}();
//表格
GridModel = function () {
    return {
        getFields: function () {
            var fields = [
                {name: 'id'},
                {name: 'serverIP'},
                {name: 'startupTime'},
                {name: 'shutdownTime'},
                {name: 'runingTime'}
            ];
            return fields;
        },
        getColumns: function () {
            var columns = [
                {header: "编号", width: 10, dataIndex: 'id', sortable: true},
                {header: "服务器IP地址", width: 20, dataIndex: 'serverIP', sortable: true},
                {header: "系统启动时间", width: 20, dataIndex: 'startupTime', sortable: true},
                {header: "系统关闭时间", width: 20, dataIndex: 'shutdownTime', sortable: true},
                {header: "持续运行时间", width: 20, dataIndex: 'runingTime', sortable: true}
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