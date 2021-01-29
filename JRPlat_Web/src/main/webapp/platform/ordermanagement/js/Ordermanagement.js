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
var action='order-management';
var detail = 'order-management-x';

//高级搜索
AdvancedSearchModel = function () {
    return {
        show: function (infor) {
            var elements = [{
                name: "货主",
                field: "ooc.oocName",
                opt: "like"
            }, {
                name: "客户",
                field: "unit.unitName",
                opt: "like"
            }];
            Pub_search.show(2,elements,namespace,action);
        }
    };
}();
//表格
GridModel = function() {
    return {
        /**
         * 总单表格创建及相关操作
         */
        bulidMainGrid: function () {
            var pageSize=20;
            var gridObj = GridInfo.getGridObj(action);

            var commands=["create","delete","updatePart","search","query"];
            var tips=['增加(C)','删除(R)','修改(U)','高级搜索(S)','显示全部(A)'];
            var callbacks=[GridBaseModel.create,GridBaseModel.remove,GridBaseModel.modify,GridBaseModel.advancedsearch,GridBaseModel.showall];

            this.mainGrid = GridBaseModel.getGrid(contextPath, namespace, action, pageSize, gridObj.fields, gridObj.columns, commands, tips, callbacks);

            //选择总单,显示对应的细单
            // GridBaseModel.onRowClick = function (namespace, action, grid, index, e) {
            //     var idList = GridBaseModel.getIdList();
            //     var propertyCriteria = 'orderManagement.id:eq:' + (idList.length === 1 ? idList[0] : 0);
            //     DetailGridBaseModel.refresh(propertyCriteria);
            // };
        },

        /**
         * 细单表格创建及相关操作
         */
        bulidDetailGrid: function () {
            var gridObj = GridInfo.getGridObj(detail);
            var queryString = 'orderManagement.id:eq:0';
            this.detailGrid = DetailGridBaseModel.getGrid(namespace, action, detail, gridObj.fields, gridObj.columns, queryString);
        },


        showViewPort: function () {
            this.viewport = new Ext.Viewport({
                layout: 'border',
                items: [{
                    region: 'center',
                    layout: 'fit',
                    border: false,
                    split: true,
                    items: [this.mainGrid]
                },{
                    region: "south",
                    layout: 'fit',
                    title: '订单信息细单',
                    border: false,
                    height: 200,
                    split: true,
                    items: [this.detailGrid]
                }
                ]
            });
            this.viewport.show();

            //方便外部操作viewport内容
            this.center_item = this.viewport.items.items[0];
            this.south_item = this.viewport.items.items[1];

            //设置细单表格默认高为当前页面的固定百分比
            this.south_item.setHeight(document.body.clientHeight * 0.50);
            this.viewport.doLayout();
        },
        show: function () {
            this.bulidMainGrid();
            // this.bulidDetailGrid();
            this.showViewPort();
        }
    }
} ();

Ext.onReady(function(){
    GridModel.show();
});
