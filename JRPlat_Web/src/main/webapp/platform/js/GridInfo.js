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
                default: {
                    break;
                }
            }
            gridObj.columns = columns;
            gridObj.fields = fields;
            gridObj.namespace = namespace;
            gridObj.action = module;
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
                1: 'base'
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
                case 'commodity' : { //商品管理
                    namespace = namespaces[1];
                    columns = [
                        {header: "商品货号(出厂)", dataIndex: 'productCode'},
                        {header: "商品名称", dataIndex: 'productName'},
                        {header: "生产厂商", dataIndex: 'manufacturer'},
                        {header: "产地", dataIndex: 'placeOrigin'},
                        {header: "商品规格", dataIndex: 'productNorm'},
                        {header: "商品基本单位", dataIndex: 'basicUnit'},
                        {header: "包装大小", dataIndex: 'packageSize'},
                        {header: "条形码", dataIndex: 'barCode'},
                        {header: "储存条件", dataIndex: 'storageConditions'},
                        {header: "储存属性", dataIndex: 'storageAttribute'},
                        {header: "商品经营类别", dataIndex: 'commBusinessCategory'},
                        {header: "双人复核标志", dataIndex: 'reviewMark'},
                        {header: "批准文号", dataIndex: 'approvalNumber'},
                        {header: "批准文号有效期", dataIndex: 'approvalPeriod'},
                        {header: "商品类别", dataIndex: 'productCategory'},
                        {header: "生产许可证/备案凭证编号", dataIndex: 'productionLicense'},
                        {header: "注册证号/备案凭证编号", dataIndex: 'registrationNumber'},
                        {header: "注册有效期", dataIndex: 'registrationPeriod'},
                        {header: "有效期", dataIndex: 'validityPeriod'},
                        {header: "有效期单位", dataIndex: 'validityUnit'},
                        {header: "预警天数", dataIndex: 'warningDays'},
                        {header: "经营类别", dataIndex: 'businessCategory'},
                        {header: "运输温度范围", dataIndex: 'transportTemperature'},
                        {header: "存储温度范围", dataIndex: 'storageTemperature'},
                        {header: "是否有报告书", dataIndex: 'report', renderer: PubFunc.r_yesorno},
                        {header: "处方OTC属性", dataIndex: 'attributeOTC'},
                        {header: "疫苗标志", dataIndex: 'vaccineMark'},
                        {header: "贵重等级", dataIndex: 'valuableLevel'},
                        {header: "防寒标志", dataIndex: 'coldProtectionSign', renderer: PubFunc.r_yesorno},
                        {header: "冷藏类别", dataIndex: 'refrigerationCategory'},
                        {header: "GMP标志", dataIndex: 'markGMP', renderer: PubFunc.r_yesorno},
                        {header: "混装类别", dataIndex: 'mixedCategory'},
                        {header: "剂型分类", dataIndex: 'dosageClassification'},
                        {header: "保管分类", dataIndex: 'custodyClassification'},
                        {header: "税种识别号", dataIndex: 'taxIdNumber'},
                        {header: "药品本位码", dataIndex: 'drugStandardCode'},
                        {header: "药品编码", dataIndex: 'drugCode'},
                        {header: "产品说明", dataIndex: 'productManual'}
                    ];
                    break;
                }
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
                case 'unit' : { //单位管理
                    namespace = namespaces[1];
                    columns = [
                        {header: "单位名称", dataIndex: 'unitName'},
                        {header: "企业类型", dataIndex: 'enterpriseType'},
                        {header: "证件类型", dataIndex: 'paperworkType'},
                        {header: "证件编号", dataIndex: 'paperworkNo'},
                        {header: "是否为医疗机构", dataIndex: 'medicalInstitutions', renderer: PubFunc.r_yesorno},
                        {header: "证件有效期", dataIndex: 'paperworkDate'},
                        {header: "法人代表", dataIndex: 'legalDeputy'},
                        {header: "地区", dataIndex: 'area'},
                        {header: "注册地址", dataIndex: 'registeredAddress'},
                        {header: "仓库地址", dataIndex: 'warehouseAddress'},
                        {header: "开户银行", dataIndex: 'bankAccount'},
                        {header: "银行账号", dataIndex: 'bankNo'},
                        {header: "税号", dataIndex: 'taxNumber'}
                    ];
                    break;
                }
                /*===================订单管理==================*/
                case 'order-management' : { //订单信息
                    namespace = namespaces[2];
                    columns = [
                        {header: "平台流水号", dataIndex: 'serialNumber'},
                        {header: "订单类型", dataIndex: 'ordertype'},
                        {header: "收单方名称", dataIndex: 'unitName'},
                        {header: "收单方发货日期", dataIndex: 'abc3'},
                        {header: "收单方发货单号", dataIndex: 'abc4',},
                        {header: "收单方发货物流公司", dataIndex: 'abc5'},
                        {header: "收单方发货物流单号", dataIndex: 'abc6'},
                        {header: "收单方发货备注", dataIndex: 'abc7'},
                        {header: "下单方名称", dataIndex: 'abc8'},
                        {header: "下单方证件编号", dataIndex: 'abc9'},
                        {header: "下单方订单日期", dataIndex: 'abc10'},
                        {header: "下单方订单单号", dataIndex: 'abc11'},
                        {header: "下单方退货物流公司", dataIndex: 'abc12'},
                        {header: "下单方退货物流单号", dataIndex: 'abc13'},
                        {header: "下单方订单备注", dataIndex: 'abc14'}
                    ];
                    break;
                }
                case 'order-management-x' : { //订单信息-细单
                    namespace = namespaces[2];
                    columns = [
                        {header: "产品编号", dataIndex: 'abc11'},
                        {header: "产品名称", dataIndex: 'abc22'},
                        {header: "规格/型号", dataIndex: 'abc33'},
                        {header: "包装单位", dataIndex: 'abc44'},
                        {header: "生产企业名称", dataIndex: 'abc55',},
                        {header: "下单方订货/退货数量", dataIndex: 'abc66'},
                        {header: "收单方发货数量", dataIndex: 'abc77'},
                        {header: "批号", dataIndex: 'abc88'},
                        {header: "序列号", dataIndex: 'abc99'},
                        {header: "生产日期", dataIndex: 'abc111'},
                        {header: "有效期/失效期", dataIndex: 'abc222'},
                        {header: "下单方产品备注", dataIndex: 'abc333'},
                        {header: "收单方产品备注", dataIndex: 'abc333'},
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
