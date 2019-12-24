/**
 *
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 *
 */

var namespace = 'monitor';
var action = 'user-login';

var category = "loginTimes";
var chartDataURL = contextPath + '/' + namespace + '/' + action + '!chart.action?category=';

UserLoginChart = function () {
    return {
        show: function (url, category, queryString, type) {
            var dataTypeLabel = '数据类型'
            var dataTypeItems = [
                {boxLabel: '登录次数', name: "dataType", inputValue: 'loginTimes', checked: category == 'loginTimes'},
                {boxLabel: '在线时间', name: "dataType", inputValue: 'onlineTime', checked: category == 'onlineTime'}
            ];
            var dataTypeColumns = 10;
            var dataTypeHeight = 20;

            SingleSeriesChartBaseModel.show("用户登录统计", "userLogin", 100, url, category, queryString, type, dataTypeLabel, dataTypeItems, dataTypeColumns, dataTypeHeight);
        }
    }
}();
//统计图
ChartModel = function () {
    return {
        show: function () {
            UserLoginChart.show(chartDataURL, category, GridBaseModel.queryString, "Column3D");
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
                id: 'search_startLoginTime',
                editable: false,
                fieldLabel: '登录开始日期',
                vtype: "daterange",
                endDateField: "search_endLoginTime"
            }, {
                xtype: 'datefield',
                format: "Y-m-d",
                id: 'search_endLoginTime',
                editable: false,
                fieldLabel: '登录结束日期',
                vtype: "daterange",
                startDateField: "search_startLoginTime"
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
            //loginTime
            if (!PubFunc.getTimeSearchText('search_startLoginTime', 'search_endLoginTime', data, 'loginTime')) {
                return;
            }
            AdvancedSearchBaseModel.search(data, "UserLogin");
        },

        show: function () {
            AdvancedSearchBaseModel.show('高级搜索', "userLogin", 420, 180, this.getItems(), this.callback);
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
                {name: 'loginTime'},
                {name: 'logoutTime'},
                {name: 'onlineTime'}
            ];
            return fields;
        },
        getColumns: function () {
            var columns = [
                {header: "编号", width: 10, dataIndex: 'id', sortable: true},
                {header: "用户名称", width: 10, dataIndex: 'username', sortable: true},
                {header: "登录IP地址", width: 20, dataIndex: 'loginIP', sortable: true},
                {header: "服务器IP地址", width: 20, dataIndex: 'serverIP', sortable: true},
                {header: "登录时间", width: 20, dataIndex: 'loginTime', sortable: true},
                {header: "注销时间", width: 20, dataIndex: 'logoutTime', sortable: true},
                {header: "用户在线时间", width: 20, dataIndex: 'onlineTime', sortable: true}
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