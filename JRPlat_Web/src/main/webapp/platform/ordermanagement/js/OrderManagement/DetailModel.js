DetailModel = function () {
    return {
        getDetailGrid: function (ids,dis) {
            var gridObj = GridInfo.getGridObj('alias-commodity-relation');
            if (!dis) {
                for(var i = 0; i < gridObj.columns.length; i++) {
                    if (gridObj.columns[i].header === '商品别名') {
                        gridObj.columns[i].editor = new Ext.form.TextField({
                    readOnly: true,
                    listeners: {
                        focus: function (field) {
                            var r = field.gridEditor.record.data;
                            var thismodule = {};
                            thismodule.namespace = namespace;//当前模块的namespace
                            thismodule.action = action; //当前模块action
                            QueryGridWindow.afterSetValueOpt = function (rowData) {
                                r['otherCommName'] = rowData.data.name;
                                r['otherCommid'] = rowData.data.id;
                                r['otherComm'] = rowData.data.code;
                                        r['alias_name'] = rowData.data.name;
                                        r['alias_id'] = rowData.data.id;
                                        r['Commodity_id'] = r.Commodity_id;
                                        r['code'] = rowData.data.code;
                                DetailModel.grid.getView().refresh();
                                PubFunc.autoSetGridColumn(DetailModel.grid);
                            };
                                    var commId = DetailModel.grid.getSelectionModel().getSelections()[0].data.Commodity_id;
                            var queryString = "commodity.id:eq:" + commId; //搜索条件
                            QueryGridWindow.show(thismodule, 'alias', queryString, [], []);
                        }
                    }
                        });
                        gridObj.columns[i].renderer = PubFunc.render_editor;
                        gridObj.columns[i].header = "<span style='color: red'>商品别名</span>";
                    }
                }
            }
            var propertyCriteria = "aliasGroup.id:in:" + ids;
            //明细不分页且页数固定9999
            OtherGridModel.disabledPage = true;
            OtherGridModel.getPageSize = function () {
                return 9999;
            };
            this.grid = OtherGridModel.getGrid(namespace, action, "alias-commodity-relation", gridObj.fields, gridObj.columns, propertyCriteria, null, true);
            return this.grid;
        },
        //批量操作
        batchOperate: function () {
            QueryGridWindow.beforeShow = function (grid, frm, win) {
                var tbar = [{
                    pressed: false,
                    text: '批量增加',
                    iconCls: 'create',
                    handler: function () {
                        var r = grid.getSelectionModel().getSelections();
                        var d = DetailModel.grid.store.data.items.length;
                        for (var i=0; i<r.length; i++) {
                            r[i].data.otherCommName = "";
                            r[i].data.otherCommid = "";
                            r[i].data.otherComm = "";
                        }
                        for (var i = 0; i<r.length; i++) {
                            for (var j = 0; j<d; j++) {
                                if (r[i].data.id === DetailModel.grid.store.data.items[j].id) {
                                    parent.Ext.ux.Toast.msg('操作提示：',　'选项中有重复项');
                                    return;
                                }
                            }
                        }
                        for (var i = 0; i < r.length; i++) {
                            DetailModel.batchSaveDetail(r[i]);//点击'批量增加'按钮后重置详情表格
                        }
                    }
                }, {
                    xtype: 'tbseparator'
                }];
                grid.topToolbar.addButton(tbar);
                var cb = new Ext.grid.CheckboxSelectionModel();
                grid.colModel.config.splice(1, 0, cb);
                grid.selModel = cb;
            };
            QueryGridWindow.afterSetValueOpt = function(selectedRow){
                var d = DetailModel.grid.store.data.items.length;
                selectedRow.data.otherCommName = "";
                selectedRow.data.otherCommid = "";
                selectedRow.data.otherComm = "";
                //选中后设置原表格值
                selectedRow.data.Commodity_codCommodityCode = selectedRow.data.codCommodityCode;//商品编号
                selectedRow.data.alias_name = "";//商品别名
                selectedRow.data.alias_id = "";//商品ID
                selectedRow.data.Commodity_id = selectedRow.data.id;
                selectedRow.data.id = "";

                for (var j = 0; j<d; j++) {
                    if (selectedRow.id === DetailModel.grid.store.data.items[j].id) {
                        parent.Ext.ux.Toast.msg('操作提示：',　'选项中有重复项');
                        return;
                    }
                }
                DetailModel.grid.store.add(selectedRow);
            };
            var oocId = Ext.getCmp('oocID').getValue();
            var thismodule = {};
            thismodule.namespace = namespace;//当前模块的namespace
            thismodule.action = action; //当前模块action
            var queryString = "codItemStatus.name:ne:$停用,ooc.id:eq:" + oocId; //搜索条件 (此处不根据供应商过滤)
            var idList = []; //赋值文本框的id List
            var colList = []; //表格列名List 与idList一一对应
            QueryGridWindow.show(thismodule, "commodity", queryString, idList, colList);
        },
        //批量新增
        batchSaveDetail: function (item) {//type(batch:批量操作按钮触发；one:双击table行后触发)
            var storeLength = DetailModel.grid.store.data.length;
            var data =　item.data || {};
            var p = new Ext.data.Record({
                id: "",

                Commodity_id: data.id,
                codTradeName: data.codTradeName,//商品名称
                Commodity_codCommodityCode: data.codCommodityCode,//商品编号
                codManufacturerIdentification: data.codManufacturerIdentification,//生产厂商标识
                codManufacturer: data.codManufacturer,//生产厂商
                codcommercialSpecification: data.codcommercialSpecification,//商品规格
                codUnit: data.codUnit,//商品基本单位
                alias_name: '',//商品别名
                alias_id: '',//商品ID
            });
            DetailModel.grid.store.insert(storeLength,p);
        },
        //添加-细单删除
        deleteDetailStore: function () {
            DetailModel.grid.store.remove(DetailModel.grid.getSelectionModel().getSelections());
        },
        //取得index
        getIndexList: function (grid) {
            var list = [];
            var r = grid.getSelectionModel().getSelections();
            Ext.each(r, function (item) {
                var rowNum = grid.getStore().indexOf(item); // 得到行号
                list.push(rowNum);
            });
            return list;
        },

        storeChange: function (store) {
            var json = PubFunc.storeToJson(store);
            Ext.getCmp('detail').setValue(json);//json字段赋值
            //自动设置表格列宽
            PubFunc.autoSetGridColumn(this.grid);
        },

        /**
         * 校验是否符合提交条件
         */
        checkNull: function () {
            var items = DetailModel.grid.store.data.items;
            for(var i = 0; i < items.length; i++) {
                if (items[i].data.number == '') {
                    parent.Ext.ux.Toast.msg('操作提示：', '有必填项未填写完整!');
                    return false;
                }
            }
            var json = PubFunc.storeToJson(DetailModel.grid.store);
            Ext.getCmp('detail').setValue(json);//json字段赋值
            return true;
        }
    }
} ();
