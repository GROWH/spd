/*
 * 查询表格QueryGridWindow中的各种属性配置
 * 
 * */

GridInfo = function () {
    // body...
    return {
        getInfo: function (module) {
            var columns, fields, namespace;
            var gridObj = {};
            switch (module) {
                case "test": {
                    columns = [{header: "编号", dataIndex: 'id', sortable: true}];
                    fields = [{name: 'id'}];
                    break;
                }
                case "user": {
                    columns = [
                        {header: "编号", width: 10, dataIndex: 'id', sortable: true, hidden: true},
                        {header: "版本", width: 10, dataIndex: 'version', sortable: true, hidden: true},
                        {header: "账号", width: 20, dataIndex: 'username', sortable: true},
                        {header: "姓名", width: 20, dataIndex: 'realName', sortable: true},
                        {header: "状态", width: 20, dataIndex: 'enabled', sortable: true},
                        {header: "拥有角色", width: 40, dataIndex: 'roles', sortable: true},
                        {header: "所属单位", width: 40, dataIndex: 'orgName', sortable: true},
                        {header: "所属单位类型", width: 40, dataIndex: 'orgType', sortable: true},
                        {header: "联系方式", width: 40, dataIndex: 'phone', sortable: true},
                        {header: "联系地址", width: 40, dataIndex: 'address', sortable: true, hidden: true},
                        {header: "备注", width: 40, dataIndex: 'des', sortable: true, hidden: true},
                        {
                            header: "绑定司机", width: 10, dataIndex: 'driverUse', sortable: true,
                            renderer: PubFunc.columns_render_yesAndNo
                        },
                        {header: "调度单号", width: 10, dataIndex: 'ddid', sortable: true},
                        {
                            header: "创建者",
                            width: 40,
                            dataIndex: 'ownerUser_realNameAndUsername',
                            sortable: true,
                            hidden: true
                        },
                        {header: "创建者所属单位类型", width: 40, dataIndex: 'ownerUser_orgType', sortable: true, hidden: true}
                    ];
                    fields = [
                        {name: 'id'},
                        {name: 'version'},
                        {name: 'username'},
                        {name: 'realName'},
                        {name: 'enabled'},
                        {name: 'roles'},
                        {name: 'positions'},
                        {name: 'orgName'},
                        {name: 'orgType'},
                        {name: 'phone'},
                        {name: 'address'},
                        {name: 'des'},
                        {name: 'driverUse'},
                        {name: 'ddid'},
                        {name: 'ownerUser_realNameAndUsername'},
                        {name: 'ownerUser_orgType'}
                    ];
                    namespace = 'security';
                    break
                }
                case "org": { //单位模块
                    columns = [
                        {header: "编号", width: 20, dataIndex: 'id', sortable: true, hidden: true},
                        {header: "单位类型", width: 40, dataIndex: 'DWLX', sortable: true, renderer: PubFunc.renderOrgType},
                        {header: "单位编号", width: 40, dataIndex: 'DWBH', sortable: true},
                        {header: "单位名称", width: 40, dataIndex: 'orgName', sortable: true},
                        {header: "法人代表", width: 40, dataIndex: 'chargeMan', sortable: true},
                        {header: "注册地址", width: 40, dataIndex: 'address', sortable: true},
                        {header: "联系人", width: 40, dataIndex: 'LXR', sortable: true},
                        {header: "联系电话", width: 40, dataIndex: 'phone', sortable: true},
                        {header: "手机", width: 40, dataIndex: 'SJ', sortable: true},
                        {header: "传真", width: 40, dataIndex: 'CZ', sortable: true},
                        {header: "邮编", width: 40, dataIndex: 'YB', sortable: true},
                        {header: "月结账号", width: 40, dataIndex: 'YJZH', sortable: true},
                        {header: "备注", width: 40, dataIndex: 'functions', sortable: true}
                    ];
                    fields = [
                        {name: 'id'},
                        {name: 'DWLX'},
                        {name: 'DWBH'},
                        {name: 'orgName'},
                        {name: 'chargeMan'},
                        {name: 'address'},
                        {name: 'LXR'},
                        {name: 'phone'},
                        {name: 'SJ'},
                        {name: 'CZ'},
                        {name: 'YB'},
                        {name: 'YJZH'},
                        {name: 'functions'}
                    ];
                    namespace = 'security';
                    break;
                }
                case 'unit':{
                    columns = [
                        {header: "单位名称", dataIndex: 'unitName'},
                        {header: "单位类型", dataIndex: 'unitType'},
                        {header: "证件编号", dataIndex: 'paperworkNo'},
                        {header: "联系人", dataIndex: 'linkman'},
                        {header: "联系人电话", dataIndex: 'contactPhone'},
                        {header: "订单获取密码", dataIndex: 'orderKey'},
                        {header: "统一社会信用代码", dataIndex: 'enSHDM'},
                        {header: "法人", dataIndex: 'legalPerson'},
                        {header: "经营地址", dataIndex: 'businessAddress'},
                        {header: "仓库地址", dataIndex: 'StorehouseAddress'}
                    ];
                    fields = [
                        {name: 'unitName'},
                        {name: 'unitType'},
                        {name: 'paperworkNo'},
                        {name: 'linkman'},
                        {name: 'contactPhone'},
                        {name: 'orderKey'},
                        {name: 'enSHDM'},
                        {name: 'legalPerson'},
                        {name: 'businessAddress'},
                        {name: 'StorehouseAddress'},
                    ];
                    namespace = 'unitinfo';
                    break;
                }
                case 'order-management':{
                    columns = [
                        {header: "平台流水号", dataIndex: 'id'},
                        {header: "订单类型", dataIndex: 'ordertype'},
                        {header: "订单状态", dataIndex: 'orderStatus'},
                        {header: "收单方ID", dataIndex: 'GYSName_id', hidden: true},
                        {header: "收单方名称", dataIndex: 'GYSName_unitName'},
                        {header: "收单方证件编号", dataIndex: 'GYSNumber'},
                        {header: "收单方发货日期", dataIndex: 'GYSDate'},
                        {header: "收单方发货单号", dataIndex: 'GYSInvoiceNo',},
                        {header: "收单方发货物流公司", dataIndex: 'GYSLogistics'},
                        {header: "收单方发货物流单号", dataIndex: 'GYSLogisticsNo'},
                        {header: "收单方发货备注", dataIndex: 'GYSRemarks'},
                        {header: "下单方ID", dataIndex: 'KHName_id', hidden: true},
                        {header: "下单方名称", dataIndex: 'KHName_unitName'},
                        {header: "下单方证件编号", dataIndex: 'KHNumber'},
                        {header: "下单方订单日期", dataIndex: 'KHDate'},
                        {header: "下单方订单单号", dataIndex: 'KHInvoiceNo'},
                        {header: "下单方退货物流公司", dataIndex: 'KHTHLogistics'},
                        {header: "下单方退货物流单号", dataIndex: 'KHTHLogisticsNo'},
                        {header: "下单方订单备注", dataIndex: 'KHRemarks'}
                    ];
                    fields = [
                        {name: 'id'},
                        {name: 'ordertype'},
                        {name: 'orderStatus'},
                        {name: 'GYSName_id'},
                        {name: 'GYSName_unitName'},
                        {name: 'GYSNumber'},
                        {name: 'GYSInvoiceNo'},
                        {name: 'GYSLogisticsNo'},
                        {name: 'KHName_id'},
                        {name: 'KHName_unitName'},
                        {name: 'KHNumber'},
                        {name: 'KHInvoiceNo'},
                        {name: 'KHTHLogisticsNo'}
                    ];
                    namespace = 'ordermanagement';
                    break;
                }
                case 'order-management-x':{
                    columns = [
                        {header: "产品编号", dataIndex: 'number'},
                        {header: "产品名称", dataIndex: 'productName'},
                        {header: "规格/型号", dataIndex: 'specifications'},
                        {header: "包装单位", dataIndex: 'packingUnit'},
                        {header: "生产企业名称", dataIndex: 'manufacturer',},
                        {header: "细单ID", dataIndex: 'id', hidden: true},
                        {header: "下单方订货/退货数量", dataIndex: 'packingUnit'},
                        {header: "收单方发货数量", dataIndex: 'orderQuantity'},
                        {header: "批号", dataIndex: 'ph'},
                        {header: "备案凭证编码", dataIndex: 'ZCNumber'},
                        {header: "序列号", dataIndex: 'serialNumber'},
                        {header: "生产日期", dataIndex: 'manufacturerDate'},
                        {header: "有效期/失效期", dataIndex: 'effectiveDate'},
                        {header: "下单方产品备注", dataIndex: 'GYSRemarks'},
                        {header: "收单方产品备注", dataIndex: 'KHRemarks'},
                    ];
                    fields = [
                        {name: 'id'},
                        {name: 'number'},
                        {name: 'productName'},
                        {name: 'specifications'},
                        {name: 'packingUnit'},
                        {name: 'manufacturer'},
                        {name: 'packingUnit'},
                        {name: 'orderQuantity'},
                        {name: 'ph'},
                        {name: 'ZCNumber'},
                        {name: 'serialNumber'},
                        {name: 'manufacturerDate'},
                        {name: 'effectiveDate'},
                        {name: 'GYSRemarks'},
                        {name: 'KHRemarks'},
                    ];
                    namespace = 'ordermanagement';
                    break;
                }
                default: {
                    break;
                }
            }
            gridObj.columns = columns;
            gridObj.fields = fields;
            gridObj.namespace = namespace;
            gridObj.action = module == "order-management-x" ? 'order-management' : module;
            return gridObj;
        },
        get: function (module) {
            return this.getInfo(module);
        },
        /**
         * 公用渲染列
         * @returns {{}}
         */
        renderColumns: function () {
            return {
                //内部状态
                r_enabled: function (v) {
                    return PubFunc.columns_render_oppose('启用', '停用', v);
                }
            }
        },
        /**
         * 获取基础表格配置
         * 属性顺序 header > dataIndex > hidden > width > renderer > other
         * @param module
         * @returns {{}}
         */
        getBaseColumnsObj: function (module) {
            var namespaces = {
                0: 'security',
                1: 'base',
                2: 'unitInfo',
                3: 'commodityInfo',
                4: 'ordermanagement',
            };
            var gridInfoObj = {}, namespace = '', columns = [];
            var r = this.renderColumns();
            switch (module) {
                /*==================测试===================*/
                case 'test' : {
                    var render_name = function (value, metaData, record, rowIndex, colIndex, store) {
                        return 'render:' + value;
                    }
                    namespace = namespaces[0];
                    columns = [{header: '姓名', dataIndex: 'perName', renderer: render_name}];
                    break;
                }
                /*==================安全管理===================*/
                case 'user' : { //用户管理
                    namespace = namespaces[0];
                    columns = [
                        {header: '单位', dataIndex: 'unitName'},
                        {header: '账号', dataIndex: 'username'},
                        {header: '姓名', dataIndex: 'realName'},
                        {header: '状态', dataIndex: 'enabled'},
                        {header: "拥有角色", dataIndex: 'roles'},
                        {header: "联系方式", dataIndex: 'phone'},
                        {header: "联系地址", dataIndex: 'address', hidden: true},
                        {header: "备注", dataIndex: 'des', hidden: true},
                    ];
                    break;
                }

                /*==================基础资料===================*/

                case 'sku-info' : { //sku管理
                    namespace = namespaces[1];
                    columns = [
                        {header: "托盘含量", dataIndex: 'trayContent'},
                        {header: "标准净重", dataIndex: 'standardNetWeight'},
                        {header: "标准毛重", dataIndex: 'standardGrossWeight'},
                        {header: "标准立方", dataIndex: 'standardCube'},
                        {header: "数量", dataIndex: 'number'},
                        {header: "毛重", dataIndex: 'grossWeight'},
                        {header: "净重", dataIndex: 'netWeight'},
                        {header: "皮重", dataIndex: 'tare'},
                        {header: "长", dataIndex: 'skuLong'},
                        {header: "宽", dataIndex: 'width'},
                        {header: "高", dataIndex: 'high'},
                        {header: "箱单位", dataIndex: 'boxUnit'},
                        {header: "箱数量", dataIndex: 'boxNumber'},
                        {header: "箱毛重", dataIndex: 'boxGrossWeight'},
                        {header: "箱净重", dataIndex: 'boxNetWeight'},
                        {header: "箱皮重", dataIndex: 'boxTare'},
                        {header: "箱长", dataIndex: 'boxLong'},
                        {header: "箱宽", dataIndex: 'boxWidth'},
                        {header: "箱高", dataIndex: 'boxHigh'},
                        {header: "商品", dataIndex: 'commodity_productName'}
                    ];
                    break;
                }
                case 'unit' : { //单位信息
                    namespace = namespaces[2];
                    columns = [
                        {header: "单位名称", dataIndex: 'unitName'},
                        {header: "单位类型", dataIndex: 'unitType'},
                        {header: "证件编号", dataIndex: 'paperworkNo'},
                        {header: "联系人", dataIndex: 'linkman'},
                        {header: "联系人电话", dataIndex: 'contactPhone'},
                        {header: "订单获取密码", dataIndex: 'orderKey'},
                        {header: "统一社会信用代码", dataIndex: 'enSHDM'},
                        {header: "法人", dataIndex: 'legalPerson'},
                        {header: "经营地址", dataIndex: 'businessAddress'},
                        {header: "仓库地址", dataIndex: 'StorehouseAddress'}
                    ];
                    break;
                }
                case 'commodity' : { //商品信息
                    namespace = namespaces[3];
                    columns = [
                        {header: "产品名称", dataIndex: 'productName'},
                        {header: "规格型号", dataIndex: 'specification'},
                        {header: "包装单位", dataIndex: 'packingUnit'},
                        {header: "包装数量", dataIndex: 'quantity'},
                        {header: "编号格式", dataIndex: 'encodingFormat'},
                        {header: "编号", dataIndex: 'number'},
                        {header: "原分类编码", dataIndex: 'YFLNumber'},
                        {header: "分类编码", dataIndex: 'FLNumber'},
                        {header: "生产企业名称", dataIndex: 'manufacturer'},
                        {header: "注册证编号/备案凭证编码", dataIndex: 'ZCNumber'},
                    ];
                    break;
                }
                /*===================订单管理==================*/
                case 'order-management' : { //订单信息
                    namespace = namespaces[4];
                    columns = [
                        {header: "平台流水号", dataIndex: 'id'},
                        {header: "订单类型", dataIndex: 'ordertype'},
                        {header: "订单状态", dataIndex: 'orderStatus'},
                        {header: "收单方ID", dataIndex: 'GYSName_id', hidden: true},
                        {header: "收单方名称", dataIndex: 'GYSName_unitName'},
                        {header: "收单方证件编号", dataIndex: 'GYSNumber'},
                        {header: "收单方发货日期", dataIndex: 'GYSDate'},
                        {header: "收单方发货单号", dataIndex: 'GYSInvoiceNo',},
                        {header: "收单方发货物流公司", dataIndex: 'GYSLogistics'},
                        {header: "收单方发货物流单号", dataIndex: 'GYSLogisticsNo'},
                        {header: "收单方发货备注", dataIndex: 'GYSRemarks'},
                        {header: "下单方ID", dataIndex: 'KHName_id', hidden: true},
                        {header: "下单方名称", dataIndex: 'KHName_unitName'},
                        {header: "下单方证件编号", dataIndex: 'KHNumber'},
                        {header: "下单方订单日期", dataIndex: 'KHDate'},
                        {header: "下单方订单单号", dataIndex: 'KHInvoiceNo'},
                        {header: "下单方退货物流公司", dataIndex: 'KHTHLogistics'},
                        {header: "下单方退货物流单号", dataIndex: 'KHTHLogisticsNo'},
                        {header: "下单方订单备注", dataIndex: 'KHRemarks'}
                    ];
                    break;
                }
                case 'order-management-x' : { //订单信息-细单
                    namespace = namespaces[4];
                    columns = [
                        {header: "产品编号", dataIndex: 'number'},
                        {header: "产品名称", dataIndex: 'productName'},
                        {header: "规格/型号", dataIndex: 'specifications'},
                        {header: "包装单位", dataIndex: 'packingUnit'},
                        {header: "生产企业名称", dataIndex: 'manufacturer',},
                        {header: "细单ID", dataIndex: 'id', hidden: true},
                        {header: "下单方订货数量", dataIndex: 'orderQuantity'},
                        {header: "收单方发货数量", dataIndex: 'shipmentQuantity'},
                        {header: "出货时间", dataIndex: 'deliveryTime'},
                        {header: "批号", dataIndex: 'ph'},
                        {header: "备案凭证编码", dataIndex: 'ZCNumber'},
                        {header: "序列号", dataIndex: 'serialNumber'},
                        {header: "生产日期", dataIndex: 'manufacturerDate'},
                        {header: "有效期/失效期", dataIndex: 'effectiveDate'},
                        {header: "下单方产品备注", dataIndex: 'GYSRemarks'},
                        {header: "收单方产品备注", dataIndex: 'KHRemarks'},
                    ];
                    break;
                }
                case 'outboundrecords' : { //出库记录
                    namespace = namespaces[4];
                    columns = [
                        {header: "产品编号", dataIndex: 'number'},
                        {header: "产品名称", dataIndex: 'productName'},
                        {header: "规格/型号", dataIndex: 'specifications'},
                        {header: "包装单位", dataIndex: 'packagingUnit'},
                        {header: "生产企业名称", dataIndex: 'manufacturer',},
                        {header: "下单方订货数量", dataIndex: 'orderQuantity'},
                        {header: "收单方发货数量", dataIndex: 'shipmentQuantity'},
                        {header: "批号", dataIndex: 'lotNumber'},
                        {header: "序列号", dataIndex: 'serialNumber'},
                        {header: "生产日期", dataIndex: 'manufacturerDate'},
                        {header: "出货时间", dataIndex: 'deliveryTime'},
                        {header: "有效期/失效期", dataIndex: 'effectiveDate'},
                        {header: "下单方产品备注", dataIndex: 'GYSRemarks'},
                        {header: "收单方产品备注", dataIndex: 'KHRemarks'},
                    ];
                    break;
                }
            }
            gridInfoObj.namespace = namespace;
            gridInfoObj.columns = columns;
            return gridInfoObj;
        },
        /**
         * 基础的表格列通用设置
         * @param columns
         */
        gridBaseConfig: function (columns) {
            var idColumnObj = {header: '编号', dataIndex: 'id', width: 50, editor: PubFunc.editor_readOnly};
            var cuColumnObj = {header: '创建人', dataIndex: 'ownerUser_username', hidden: true};
            var ctColumnObj = {header: '创建时间', dataIndex: 'createTime', hidden: true};
            var muColumnObj = {header: '最后一次修改人', dataIndex: 'updateUser_username', hidden: true};
            var mtColumnObj = {header: '最后一次修改时间', dataIndex: 'updateTime', hidden: true};
            var siColumnObj = {header: '同步货主唯一表示', dataIndex: 'synid', hidden: true};
            var sbColumnObj = {header: '同步数据编号', dataIndex: 'synbh', hidden: true};
            var vColumnObj = {header: '版本号', dataIndex: 'version', width: 50};
            columns.unshift(idColumnObj);
            columns.push(cuColumnObj, ctColumnObj, muColumnObj, mtColumnObj, siColumnObj, sbColumnObj, vColumnObj);
            //通用列属性设置
            for (var i = 0; i < columns.length; i++) {
                columns[i].sortable = true; //排序功能
                if (columns[i].width == undefined) {
                    columns[i].width = 100; //默认宽度
                }
            }
        },
        /**
         * 将module名解析为可用的请求action名
         * 解析规则:
         * 1 本身为action名的不处理
         * 2 带-的连接的第一个字母转换为大写
         * 3 带>表示上下级关系,使用>后面的内容
         * 4 带*后面的内容代表操作类型,使用*前面的内容
         * @param module
         */
        resolveModule: function (module) {
            if (module.indexOf('-') != -1) {
                var tolocaleUpperCase = function (str) {
                    var firstCase = str.substring(0, 1);
                    var andCase = str.substring(1);
                    return firstCase.toLocaleUpperCase() + andCase;
                }
                var splitList = module.split('-');
                for (var i = 1; i < splitList.length; i++) {
                    splitList[i] = tolocaleUpperCase(splitList[i]);
                }
                module = splitList.join('');
            }
            if (module.indexOf('>') != -1) {
                var splitList = module.split('>');
                module = splitList[splitList.length - 1];
            }
            if (module.indexOf('*') != -1) {
                var splitList = module.split('*');
                module = splitList[0];
            }
            return module;
        },
        /**
         * 根据表格的列得到对应的解析所需fields
         * @param gridInfoObj
         */
        mappingField: function (gridInfoObj) {
            var columns = gridInfoObj.columns;
            var fields = [];
            for (var i = 0; i < columns.length; i++) {
                if (columns[i].sortable === undefined) {//排序
                    columns[i].sortable = true;
                }
                fields.push(columns[i].dataIndex);
            }
            gridInfoObj.fields = fields;
        },
        /**
         * 最终组合好的可供表格配置使用的列
         * @param module
         * @returns {*}
         */
        getGridObj: function (module) {
            var gridInfoObj = this.getBaseColumnsObj(module);
            this.gridBaseConfig(gridInfoObj.columns);
            gridInfoObj.action = this.resolveModule(module);
            this.mappingField(gridInfoObj);
            //兼容旧表格配置
            if (gridInfoObj.namespace === '') {
                gridInfoObj = this.get(module);
            }
            return gridInfoObj;
        },
    }
}();
