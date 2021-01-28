OtherGridModel = function() {
    return {
        // 数据源
        getStore : function(fields, pageSize) {
            if (undefined == this.propertyCriteria) {
                this.propertyCriteria = "id:eq:0";
            }
            if (undefined == this.likeQueryInfo) {
                this.likeQueryInfo = "";
            }
            if (undefined == this.initUrlParams) {
                this.initUrlParams = "";
            }
            // 定义数据集对象
            var store = new Ext.data.Store({
                reader : new Ext.data.JsonReader({
                    totalProperty : 'totalProperty',
                    root : 'root'
                }, Ext.data.Record.create(fields)),
                proxy : new parent.Ext.data.HttpProxy({
                    url : contextPath + '/' + this.namespace + '/'
                    + this.action + '!popupQuery.action' + OtherGridModel.initUrlParams
                })
            });
            store.on('beforeload', function(store) {
                store.baseParams = {
                    propertyCriteria : OtherGridModel.propertyCriteria,
                    likeQueryInfo: OtherGridModel.likeQueryInfo,
                    likeQueryType: OtherGridModel.queryAction,
                    queryaction : OtherGridModel.queryAction + 'Action'
                };
            });
            if (this.namespace != undefined && this.action != undefined
                && this.namespace != '' && this.action != ''
                && this.namespace != null && this.action != null) {
                store.load({
                    params : {
                        limit: pageSize
                    }
                });
            }
            return store;
        },
        getPageSize: function() {
            return 20;
        },
        setEditor: function(columns){
            return columns;
        },
        refresh: function(propertyCriteria) {
            this.propertyCriteria = propertyCriteria;
            this.store.load({
                params : {
                    //如果当前表格不需要分页则bbar不存在
                    limit: this.bbar!=undefined ? this.bbar.pageSize:this.getPageSize()
                }
            });
        },
        refreshLikequery: function (likequeryString) {
            this.store.on('beforeload', function (store) {
                store.baseParams = {
                    likeQueryType: OtherGridModel.queryAction,
                    likeQueryInfo: likequeryString,
                    propertyCriteria: OtherGridModel.propertyCriteria,
                    queryaction: OtherGridModel.queryAction + 'Action'
                };
            });
            this.store.load({
                params: {
                    //如果当前表格不需要分页则bbar不存在
                    limit: this.bbar!=undefined ? this.bbar.pageSize:this.getPageSize()
                }
            });
        },
        changeURL: function (contextPath, namespace, action) {
            this.storeURL = contextPath + '/' + namespace + '/' + action + '!popupQuery.action';
            if (this.store) {
                this.store.proxy.setUrl(this.storeURL + this.initUrlParams, true);
            }
        },
        autoGridColumn: function(grid){
            grid.store.on('load', function (store) {
                //自动设置表格列宽
                PubFunc.autoSetGridColumn(grid);
            });
            grid.store.on('add', function (store) {
                //自动设置表格列宽
                PubFunc.autoSetGridColumn(grid);
            });
            grid.store.on('remove', function (store) {
                //自动设置表格列宽
                PubFunc.autoSetGridColumn(grid);
            });
            grid.store.on('update', function (store) {
                //自动设置表格列宽
                PubFunc.autoSetGridColumn(grid);
            });
        },
        /**
         * 底部工具条 不和GridBaseModel共用的原因是需要单独重载
         * @param pageSize
         * @param store
         * @param columns
         * @param whichGrid
         * @param disabledPage
         * @returns {*}
         */
        getBBar: GridBaseModel.getBBar,
        /**
         * 表格组装
         * @param namespace
         * @param action
         * @param queryAction 此action的值可为原action值加'*xxxx',主要用于区分一个module中的不同表格
         * @param fields
         * @param columns
         * @param propertyCriteria
         * @param initUrlParams
         * @param disabledPage  禁用分页combo 可通过getGrid(..., disabledPage) 或 OtherGridModel.disabledPage设置
         * @returns {Ext.grid.EditorGridPanel|*|Ext.grid.GridPanel}
         */
        getGrid : function(namespace, action, queryAction, fields, columns, propertyCriteria, initUrlParams, disabledPage) {
            var whichGrid = "other/" + action + '/' + queryAction;
            
            this.namespace = namespace;
            this.action = action;
            this.queryAction = GridInfo.resolveModule(queryAction);
            this.propertyCriteria = propertyCriteria;
            this.initUrlParams = initUrlParams;

            var pageSize = this.getPageSize();

            if (!this.noQueryGridCongif) {
                GridBaseModel.queryGridConfig(whichGrid);
                var gridSetting = Gridconfig.gridSetting;
                var size = Gridconfig.size;
                if (gridSetting != '' && gridSetting != undefined && size != '') {
                    columns = GridBaseModel.mergeGridCfg(columns, gridSetting);
                    pageSize = size;
                }
            }

            columns = GridBaseModel.addColumn(columns);

            this.store = this.getStore(fields, pageSize);
            var cb = new Ext.grid.CheckboxSelectionModel();
            var preColumns = [// 配置表格列
                new Ext.grid.RowNumberer({
                    header : '行号',
                    width : 40
                }), cb];
            columns = preColumns.concat(columns);
            this.bbar = this.getBBar(pageSize, this.store, columns, whichGrid, disabledPage || this.disabledPage);
            this.grid = new Ext.grid.EditorGridPanel({
                store : this.store,
                loadMask : true,
                autoScroll : true,
                bbar : this.bbar,
                minColumnWidth : 80,
                viewConfig : {
                    loadingText : '数据加载中,请稍等...',
                    emptyText : '无对应信息',
                    deferEmptyText : true,
                    autoFill : true,
                    //forceFit:true
                },
                sm: cb,
                columns : columns,
                clicksToEdit: 1,
                enableKeyEvents : true,
                listeners: {
                    destroy: function () {
                        OtherGridModel.propertyCriteria = '';
                        OtherGridModel.likeQueryInfo = '';
                        OtherGridModel.initUrlParams = '';
                    }
                }

            });
            this.autoGridColumn(this.grid);
            return this.grid;
        },
        reset: function () {
            this.getBBar = function (pageSize, store, columns, whichGrid) {
                var saveGridSet = GridBaseModel.saveGridSet;
                var cleanGridSet = GridBaseModel.cleanGridSet;
                return new Ext.ux.PageSizePlugin({
                    rowComboSelect: true,
                    pageSize: pageSize,
                    store: store,
                    displayInfo: true,
                    saveGridSet: saveGridSet,
                    cleanGridSet: cleanGridSet,
                    columns: columns,
                    whichGrid: whichGrid
                });
            }
            this.getPageSize = function() {
                return 20;
            }
        }
    };
} ();