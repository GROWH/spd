/**
 *
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 *
 */

//fieldset的验证
Ext.QuickTips.init();//支持tips提示
Ext.form.Field.prototype.msgTarget = 'side';//提示的方式，枚举值为"qtip","title","under","side",id(元素id)
Ext.BLANK_IMAGE_URL = contextPath + '/extjs/images/default/s.gif';

//公共函数
PubFunc = function () {
    return {
        //获取文本的像素宽度
        getTextLen: function (str) {
            if (str == undefined) {
                return 0;
            }
            str = str.toString();
            if (str.indexOf("<div") != -1) {
                return 0;
            }
            var realLength = 0;
            for (var i = 0; i < str.length; i++) {
                var charCode = str.charCodeAt(i);
                if (charCode >= 65 && charCode <= 90) {
                    realLength += 7;//大写字母设置为7
                } else if (charCode >= 0 && charCode <= 128) {
                    realLength += 6;//其他设置为6
                } else {
                    realLength += 12;//汉子长度设置为12
                }
            }
            return realLength;
        },
        //grid的列宽根据内容自动设定
        autoSetGridColumn: function (grid) {
            if (grid == undefined) {
                return;
            }
            var colModel = grid.colModel;
            var columnLen = colModel.getColumnCount();
            for (var i = 0; i < columnLen; i++) {
                var maxWidth = 0;
                var dataIndex = colModel.getColumnAt(i).dataIndex;
                var headerText = colModel.getColumnAt(i).header;
                // console.log('headerText',headerText);
                //使用正则匹配去除掉html标签
                //主要用于可编辑表格必填项的标记,如果有其他用途,需要更改正则表达式
                headerText = headerText.replace(/<\w+>|<\w+\sstyle=['"].*['"]>|<\/\w+>/g, '');
                maxWidth = PubFunc.getTextLen(headerText);
                if (dataIndex == "") {
                    maxWidth = Math.max(maxWidth, colModel.getColumnWidth(i));
                } else {
                    grid.store.each(function (r) {
                        var text = r.json[dataIndex];
                        var len = PubFunc.getTextLen(text);
                        maxWidth = Math.max(maxWidth, len);
                    });
                    maxWidth = maxWidth + 24;//总文本宽度获取后再加上两个中文字符的宽度，经验调校值
                }
                colModel.setColumnWidth(i, maxWidth);
            }
        },
        //设置随机字符序列
        setHexStringRandom: function (cmpId) {
            Ext.Ajax.request({
                url: contextPath + '/' + namespace + '/' + action + '!HexStringRandom.action',
                method: 'POST',
                success: function (response, options) {
                    var model = eval('(' + response.responseText + ')');
                    Ext.getCmp(cmpId).setValue(model.value);
                }
            });
        },
        //请求结果json转为对象
        json2Model: function (json) {
            return eval('(' + json + ')');
        },
        //过滤树
        filterTree: function (tree, textfieldId) {
            tree.expandAll();
            //将所有匹配节点的父节点设置为可见标识
            //第二个判断条件!node.parentNode.show是只设置未设置过的父节点，这样可以减少遍历次数（该父节点已在处理其他兄弟节点时处理过了）
            var getAllParents = function (node) {
                if (node.parentNode && !node.parentNode.show) {
                    node.parentNode.show = true;
                    getAllParents(node.parentNode);
                }
            }
            var processNode = function (nodes, text, opt) {
                for (var i = 0; i < nodes.length; i++) {
                    var node = nodes[i];
                    switch (opt) {
                        case 'SHOWALL': {
                            node.ui.show();
                            break;
                        }
                        case 'SEARCH' : {
                            node.show = false;
                            if (node.text.indexOf(text) != -1) {
                                node.show = true;
                                getAllParents(node);
                            }
                            break;
                        }
                        case 'HIDE' : {
                            if (node.show == false) {
                                node.ui.hide();
                            }
                            break;
                        }
                    }
                    if (node.hasChildNodes()) {
                        processNode(node.childNodes, text, opt);
                    }
                }
            }
            var field = Ext.get(textfieldId);
            field.on('keyup', function (e) {
                var text = field.dom.value;
                var nodes = [tree.root];
                processNode(nodes, text, 'SHOWALL');  //显示所有节点
                processNode(nodes, text, 'SEARCH');   //搜索所有匹配节点
                processNode(nodes, text, 'HIDE');     //隐藏所有非匹配节点
            });
        },
        //预警
        earlyWarning: function (moduleChinese, module) {
            var activeTabTitle = parent.Ext.getCmp('active_tab').activeTab.title;
            if (activeTabTitle == moduleChinese) {
                var queryString = "";
                var s_callback = function (data) {
                    var ids = data.ids.replace(/\[/g, '').replace(/\]/g, '').replace(/\s+/g, '');
                    console.log(moduleChinese + 'ids:-----' + ids);
                    GridBaseModel.propertyCriteria = "id:in:" + ids;
                    GridBaseModel.refresh();
                };
                var initUrlParams = "warning=true";
                Request.getInfo(module, queryString, s_callback, null, initUrlParams);
            }
        },
        storeToJson: function (store) {
            var items = store.data.items;
            var jsonString = [];
            for (var i = 0; i < items.length; i++) {
                jsonString.push(items[i].data);
            }
            jsonString = JSON.stringify(jsonString);
            return jsonString;
        },
        //检索form表单中name的value
        findFrmValueByName: function (form, name) {
            var items = form.items.items;
            for (var i = 0; i < items.length; i++) {
                if (items[i].name == name || items[i].hiddenName == name) {
                    return items[i].getRawValue();
                }
            }
            return "";
        },
        //检索form表单中name的value并转为json
        formToJson: function (form, names) {
            var obj = {};
            names.forEach(function (item) {
                obj[item] = PubFunc.findFrmValueByName(form, item)
            });
            return JSON.stringify(obj);
        },
        getValueByText: function (store, text) {// 只限于DIC的store
            var array = store.data.items;
            for (var i = 0; i < array.length; i++) {
                if (array[i].data.text == text) {
                    return array[i].data.value;
                }
            }
            return -1;
        },
        getComboText: function (store, value, key) {
            var obj = store.reader.jsonData;
            var returnval = '';
            for (val in obj) {
                if (value == obj[val]['value'] || value == obj[val][key]) {
                    return obj[val][key];
                }
            }
            return returnval;
        },
        //根据主键key的键值value，返回该条记录，其中key为主键名，value为主键值
        getComboRecord: function (store, value, key) {
            //对于一个store来说，即使通过filter过滤了一些数据，但是其实只是过滤了store.data的数据
            //而store.reader.jsonData数据不会被过滤会一直在，因此通过store.reader.jsonData获取想要的数据不会应为store被过滤而影响，是安全的操作
            var obj = store.reader.jsonData;
            var returnval = '';
            for (val in obj) {
                if (value == obj[val][key]) {
                    return obj[val];
                }
            }
            return returnval;
        },
        loadCallback: function (isload, value, fun) {
            isload[value] = true;
            for (var i = 0; i < isload.length; i++) {
                if (isload[i] == false) {
                    return;
                }
            }
            fun();
        },
        MoneyFormat: function (value) {
            return "<span style='color:RGB(221,0,0);'>"
                + Ext.util.Format.number(value, '￥0.00') + "</span>";
        },
        checkGridData: function (grid) {
            var cm = grid.colModel;
            var records = grid.store.getRange(); //获取grid中所有的数据
            for (var i = 0; i < records.length; i++) {
                var record = records[i]; //获取第i行数据
                var fields = grid.store.fields.keys; //获取列头名称集合
                for (var j = 0; j < fields.length; j++) {
                    var name = fields[j]; //grid列名
                    var value = record.data[name]; //对应列名的字段在record中的取值，可能该列名在record中根本找不到该字段，此时value=undefine
                    var rowIndex = grid.store.indexOfId(record.id); //record记录在grid中的行号
                    var colIndex = cm.findColumnIndex(name); //对应列名在grid中的列号
                    if (colIndex == -1) { //可能fiedls定义了，在commodel没有定义
                        continue;
                    }
                    var editor = cm.getCellEditor(colIndex); //对应列名所在列使用的编辑器
                    if (null != editor) {
                        if (value != "") {
                            return true;
                        }
                    }
                }
            }
            return false;
        },
        ExportExcel: function () {
            var vExportContent = GridBaseModel.grid.getExcelXml(true,
                GridBaseModel.excelTile);
            if (Ext.isIE6 || Ext.isIE7 || Ext.isIE8 || Ext.isSafari
                || Ext.isSafari2 || Ext.isSafari3) {
                if (!Ext.fly('frmDummy')) {
                    var frm = document.createElement('form');
                    frm.id = 'frmDummy';
                    frm.name = id;
                    frm.className = 'x-hidden';
                    document.body.appendChild(frm);
                }
                Ext.Ajax.request({
                    url: '/exportexcel.php',
                    method: 'POST',
                    form: Ext.fly('frmDummy'),
                    callback: function (o, s, r) {
                    },
                    isUpload: true,
                    params: {
                        exportContent: vExportContent
                    }
                })
            } else {
                document.location = 'data:application/vnd.ms-excel;base64,'
                    + Base64.encode(vExportContent);
            }
        },
        getNumberField: function (isBlk, xsd, isNegtive, minValue, maxValue) {
            var numberField = new Ext.form.NumberField({
                allowBlank: isBlk, //允许为空
                decimalPrecision: xsd, //小数点后最大精确位数
                allowNegative: isNegtive, //允许负值
                minValue: minValue
                //允许最小值
            });

            if (maxValue != undefined) {
                numberField.maxValue = maxValue; //允许最大值
            }
            return numberField;
        },
        getTimeSearchText: function (TimeFrom, TimeTo, data, Timefield) {
            var From_V = parent.Ext.getCmp(TimeFrom).getValue();
            var From_T = parent.Ext.getCmp(TimeFrom).value;
            var To_V = parent.Ext.getCmp(TimeTo).getValue();
            var To_T = parent.Ext.getCmp(TimeTo).value;
            var BiggerThan = "";
            var SmallerThan = "";
            var code = "";
            if (GridBaseModel.search) {
                BiggerThan = ">='";
                SmallerThan = "<='";
                code = "'";
            } else {
                BiggerThan = ":ge:";
                SmallerThan = ":le:";
                code = "";
            }
            if (From_V.toString() != "" && To_V.toString() != "") {
                if (To_V < From_V) {
                    parent.Ext.MessageBox.alert('操作提示：', '终止时间需大于起始时间!');
                    return false;
                }
                data.push(Timefield + BiggerThan + From_T + code);
                data.push(Timefield + SmallerThan + To_T + code);
            } else if (From_V.toString() == "" && To_V.toString() != "") {
                data.push(Timefield + SmallerThan + To_T + code);
            } else if (From_V.toString() != "" && To_V.toString() == "") {
                data.push(Timefield + BiggerThan + From_T + code);
            } else {
                //未选择时间
            }
            return true;
        },
        r_yesorno: function (val) {
            return val === '' ? val : PubFunc.render_circle(val === '是' ? '<label style="color: #F65D20;"/>是</label>' : '否');
        },
        editor_readOnly: function () {
            return new Ext.form.TextField({
                cls: 'attr',
                readOnly: true
            });
        }(),
        //渲染单位类型
        renderOrgType: function (value) {
            var ele = ""
            if (value == "承运商") {
                ele = "<img src='../images/unitType_carrier.png' style='vertical-align:middle; padding-right:3px;'/>";
            } else if (value == "货主") {
                ele = "<img src='../images/unitType_provider.png'style='vertical-align:middle; padding-right:3px;'/>";
            } else if (value == "客户") {
                ele = "<img src='../images/unitType_customer.png'style='vertical-align:middle; padding-right:3px;'/>";
            } else {
                ele = "<img src='../images/unitType_unknown.png' style='vertical-align:middle; padding-right:3px;'/>";
            }
            ele = ele + "<span>" + value + "</span>";
            return ele;
        },
        //渲染数据所属单位
        //display:inline(内联) div宽度随内容而改变
        renderDom_ownerUser_orgName: function (value) {
            if (value != undefined && value != "")
                return "<div style='background:#222; padding:2px; border: 2px solid #fff; color:#fff; font-weight:normal; display:inline; text-align:center;'>" + value + "</div>";
            else
                return value;
        },
        //渲染线路
        renderLine: function (value) {
            var ele = "<img src='../images/line.png' style='vertical-align:middle; padding-right:3px;'/>";
            ele = ele + "<span style='padding:5px; border-left: 3px solid #0a0; border-radius:2px; color:#fff; font-weight:bold;'>" + value + "</span>";
            ele = "<div style='min-widht:80px; background:#3366FF; padding:5px; border-radius:0 2px 2px 0;'>" + ele + "</div>";
            return ele;
        },
        //渲染车辆
        renderDom_CPHM: function (value) {
            return "<div style='background:#3366FF; padding:2px; border: 2px solid #fff; border-radius:4px; color:#fff; font-weight:normal; width:60px; text-align:center;'>" + value + "</div>";
        },
        //渲染仓库
        render_warehouse: function (value, cellmeta, record) {
            return "<div style='float:left; border-radius:3px; min-width:60px; background:#ee0; height:15px; padding:5px 3px 3px 3px; border: 1px solid #888; color:#000; font-weight:bold; text-align:center;'>" + value + "</div>";
        },
        //渲染圆形标识
        render_circle: function (value, cellmeta, record) {
            return "<div style='float:left; border-radius:15px; margin-left:3px; width:18px; background:#fff; height:15px; padding:5px 3px 3px 3px; border: 1px solid #aaa; color:#000; font-weight:bold; text-align:center;'>" + value + "</div>";
        },
        //渲染空闲和占用状态
        renderDom_States: function (value) {
            var cfg = [{
                value: "空闲",
                bgColor: "#488E48",
                fontColor: "#fff"
            }, {
                value: "占用",
                bgColor: "#CC3300",
                fontColor: "#fff"
            }, {
                value: "出车",
                bgColor: "#CC3300",
                fontColor: "#fff"
            }, {
                value: "完成",
                bgColor: "#7A7A7A",
                fontColor: "#fff"
            }];
            return PubFunc.renderDom_field(value, cfg);
        },
        //跟起提前预警天数渲染日期
        renderDom_Date: function (value, cellmeta, record, rowIndex, columnIndex, store) {
            var PREALARMDAYS = 7; //默认提前预警天数
            var preAlarmDaysFileds = ["YJTS", "xxxx"];
            for (var i in preAlarmDaysFileds) {
                var key = preAlarmDaysFileds[i];
                var predays = record.get(key);
                if (predays != undefined && predays != "") {
                    PREALARMDAYS = parseInt(predays);
                    break;
                }
            }
            var today = new Date();
            today = new Date(today.getFullYear(), today.getMonth(), today.getDate());//这样做是为了去除时分秒
            var strTime = value; //字符串日期格式  "2011-04-16"         
            var date = new Date(Date.parse(strTime.replace(/-/g, "/"))); //转换成Data();
            var iDays = parseInt((date - today) / 1000 / 60 / 60 / 24);    //把相差的毫秒数转换为天数  
            if (iDays <= 0) {
                return "<div style='background:#CC3300; padding:2px; border: 2px solid #fff; border-radius:4px;color:#fff; font-weight:normal; text-align:center;'>(过期" + (-iDays) + "天) " + value + "</div>";
            }
            if (iDays > 0 && iDays <= PREALARMDAYS) {
                return "<div style='background:#EBEB00; padding:2px; border: 2px solid #fff;border-radius:4px; color:#CC3300; font-weight:normal; text-align:center;'>(还剩" + (iDays) + "天) " + value + "</div>";
            }
            if (value == "") {
                return value;
            } else {
                return "<div style='background:#488E48; padding:2px; border: 2px solid #fff; border-radius:4px;color:#fff; font-weight:normal; text-align:center;'>" + value + "</div>";
            }
        },
        //将对象obj2的属性合并到obj1里面去，返回合并后的obj1
        mergeObject: function (obj1, obj2) {
            if (obj1 == null || obj1 == undefined) {
                console.log("对象合并失败，原始对象不存在");
                return;
            }
            if (obj2 == null || obj2 == undefined) {
                return obj1;
            }
            for (var item in obj2) {
                obj1[item] = obj2[item];
            }
            return obj1;
        },
        renderDom_field: function (value, cfg) {
            for (var i = 0; i < cfg.length; i++) {
                var it = cfg[i];
                if (value == it.value) {
                    var display = it.replaceValue ? it.replaceValue : value;
                    return "<div style='background:" + it.bgColor + "; padding:2px; border: 2px solid #fff; border-radius:4px; color:" + it.fontColor + "; font-weight:normal; text-align:center;'>" + display + "</div>";
                }
            }
            return value;
        },
        renderDom_field_no_bg: function (value, cfg) {
            for (var i = 0; i < cfg.length; i++) {
                var it = cfg[i];
                if (value == it.value) {
                    var display = it.replaceValue ? it.replaceValue : value;
                    return "<div style='padding:2px; border: 0px solid #999; border-radius:4px; color:" + it.fontColor + "; font-weight:normal; text-align:left;'>" + display + "</div>";
                }
            }
            return value;
        },
        //计费规则状态
        columns_render_jfgz: function (value) {
            var cfg = [{
                value: "未完善",
                bgColor: "#CC3300",
                fontColor: "#fff"
            }, {
                value: "待完善",
                bgColor: "#EBEB00",
                fontColor: "#CC3300"
            }, {
                value: "已完善",
                bgColor: "#488E48",
                fontColor: "#fff"
            }];
            return PubFunc.renderDom_field(value, cfg);
        },
        //物料状态
        columns_render_wlzt: function (value) {
            var cfg = [{
                value: "占用",
                bgColor: "#CC3300",
                fontColor: "#fff"
            }, {
                value: "待回收",
                bgColor: "#EBEB00",
                fontColor: "#CC3300"
            }, {
                value: "空闲",
                bgColor: "#488E48",
                fontColor: "#fff"
            }];
            return PubFunc.renderDom_field(value, cfg);
        },
        //运单状态
        columns_render_ydzt: function (value) {
            var cfg = [
                {value: "初始", bgColor: "#87CEFA", fontColor: "#333"},
                {value: "待平台受理", bgColor: "#CC3300", fontColor: "#fff"},
                {value: "待派车", bgColor: "#488E48", fontColor: "#fff"},
                {value: "待接单", bgColor: "#FF8247", fontColor: "#fff"},
                {value: "待收货", bgColor: "#488E48", fontColor: "#fff"},
                {value: "待联运受理", bgColor: "#488E48", fontColor: "#fff"},
                {value: "已收货", bgColor: "#488E48", fontColor: "#fff"},
                {value: "已回仓", bgColor: "#488E48", fontColor: "#fff"},
                {value: "到货签收", bgColor: "#fff", fontColor: "#006400"},
                {value: "退货签收", bgColor: "#fff", fontColor: "#CC3300"},
                {value: "待确费", bgColor: "#488E48", fontColor: "#fff"},
                {value: "已取消", bgColor: "#CC3300", fontColor: "#fff"},
                {value: "已拒单", bgColor: "#CC3300", fontColor: "#fff"},
                {value: "已转运", bgColor: "#488E48", fontColor: "#fff"},
                {value: "已退货", bgColor: "#CC3300", fontColor: "#fff"},
                {value: "已修改地址", bgColor: "#CC3300", fontColor: "#fff"}];
            return PubFunc.renderDom_field(value, cfg);
        },
        //运单类型
        columns_render_ydlx: function (value) {
            var cfg = [
                {value: "送货", bgColor: "#fff", fontColor: "#006400"},
                {value: "退货", bgColor: "#fff", fontColor: "#CC3300"}
            ];
            return PubFunc.renderDom_field(value, cfg);
        },
        //费用状态
        columns_render_fyzt: function (value) {
            var cfg = [
                {value: "初始", bgColor: "#87CEFA", fontColor: "#333"},
                {value: "待确费", bgColor: "#CC3300", fontColor: "#fff"},
                {value: "已确费", bgColor: "#488E48", fontColor: "#fff"}]
            return PubFunc.renderDom_field(value, cfg);
        },
        //派单状态
        columns_render_pdzt: function (value) {
            var cfg = [
                {value: "待接单", bgColor: "#fff", fontColor: "#CC3300"},
                {value: "待取货", bgColor: "#fff", fontColor: "#FF8247"},
                {value: "配送中", bgColor: "#fff", fontColor: "#488E48"},
                {value: "已完成", bgColor: "#fff", fontColor: "#333333"}]
            return PubFunc.renderDom_field(value, cfg);
        },
        //结算状态
        columns_render_jszt: function (value) {
            var cfg = [
                {value: "未结算", bgColor: "#fff", fontColor: "#CC3300"},
                {value: "已结算", bgColor: "#fff", fontColor: "#488E48"}]
            return PubFunc.renderDom_field_no_bg(value, cfg);
        },
        //开票状态
        columns_render_kpzt: function (value) {
            var cfg = [
                {value: "未开票", bgColor: "#fff", fontColor: "#CC3300"},
                {value: "已开票", bgColor: "#fff", fontColor: "#488E48"}]
            return PubFunc.renderDom_field_no_bg(value, cfg);
        },
        //折扣确认
        columns_render_zkqr: function (value) {
            var cfg = [
                {value: "未确认", bgColor: "#fff", fontColor: "#CC3300"},
                {value: "已确认", bgColor: "#fff", fontColor: "#488E48"}]
            return PubFunc.renderDom_field_no_bg(value, cfg);
        },
        //物料登记类型
        columns_render_djlx: function (value) {
            var cfg = [
                {value: "领用", bgColor: "#fff", fontColor: "#CC3300"},
                {value: "回收", bgColor: "#fff", fontColor: "#488E48"}]
            return PubFunc.renderDom_field_no_bg(value, cfg);
        },
        //true And false
        columns_render_yesAndNo: function (value) {
            var cfg = [
                {value: true, replaceValue: '是', bgColor: "", fontColor: "#488E48"},
                {value: false, replaceValue: '否', bgColor: "", fontColor: "#CC3300"}
            ];
            return PubFunc.renderDom_field(value, cfg);
        },
        render_dom_edit: function (value, cellmeta, record) {
            return "<div style='background:#FFFF00; height:15px; padding:3px; margin:-3px 0px; border: 1px solid #888; color:#000; font-weight:normal; text-align:left;'>" + value + "</div>";
        },
        render_dom_edit_money: function (value, cellmeta, record) {
            value = value === "" ? "" : "¥" + value;
            return "<div style='background:#FFFF00; height:15px; padding:3px; margin:-3px 0px; border: 1px solid #888; color:#000; font-weight:normal; text-align:left;'>" + value + "</div>";
        },
        render_card: function (value, cellmeta, record) {
            return "<div style='border-radius:3px; background:#ee0; height:15px; padding:3px 0 0px 0; border: 1px solid #888; color:#000; font-weight:bold; text-align:center;'>" + value + "</div>";
        },
        render_flag: function (value) {
            return "<span style='background: #EBEB00; padding:4px; border: 2px solid #fff; border-radius:6px; color: #CC3300; font-weight:normal; text-align:center;'>" + value + "</span>";
        }
    };
}();

ImportBaseDataModel = function () {
    return {
        getItems: function () {
            var items = [{
                layout: 'column',
                bodyStyle: 'padding:15px 0px;',
                items: [{
                    columnWidth: 0.85,
                    layout: 'form',
                    bodyStyle: 'padding-right:5px;',
                    defaultType: 'textfield',
                    defaults: {
                        anchor: "100%"
                    },
                    items: [{
                        fieldLabel: '上传文件',
                        cls: 'attr',
                        xtype: 'textfield',
                        inputType: 'file',
                        allowBlank: false,
                        name: 'importFile',
                        id: 'unit_import_excel'
                    }]
                }, {
                    columnWidth: .15,
                    defaults: {
                        anchor: "100%"
                    },
                    items: [{
                        xtype: "button",
                        iconCls: 'download',
                        text: '下载模板',
                        handler: function () {
                            path = contextPath + '/excelTemplate/' + ImportBaseDataModel.xlsName + '.xls';
                            window.open(path, '_blank', 'width=500,height=1,toolbar=no,menubar=no,location=no');
                        }
                    }]
                }]
            }];
            return items;
        },
        getOtherItems: function () {
            var items = [];
            return items;
        },
        getForm: function () {
            var frm = new Ext.form.FormPanel({
                layout: 'fit',
                fileUpload: true,
                items: [{
                    frame: true,
                    border: false,
                    split: true,
                    items: [this.items, this.otherItems]
                }]
            });
            return frm;
        },
        formIsValid: function () {
            if (this.frm.getForm().isValid()) {
                return true;
            } else {
                parent.Ext.ux.Toast.msg('操作提示：', '有必填项未填写完整！');
                return false;
            }
        },
        getWin: function () {
            var win = new Ext.Window(
                {
                    id: 'base_import_win',
                    title: '导入',
                    maximizable: true,
                    width: this.width ? this.width : 600,
                    height: this.height ? this.height : 200,
                    plain: true,
                    closable: true,
                    frame: true,
                    layout: 'fit',
                    border: false,
                    modal: true,
                    items: [this.frm],
                    buttonAlign: 'center',
                    listeners: {
                        'close': function () {
                            GridBaseModel.refresh();
                        }
                    },
                    buttons: [
                        {
                            text: '导入',
                            iconCls: 'save',
                            scope: this,
                            handler: function () {
                                if (!this.formIsValid()) {
                                    parent.Ext.ux.Toast.msg('操作提示：', '有必填项未填写完整！');
                                    return;
                                }
                                var excel = Ext.getCmp('unit_import_excel').getValue();
                                if (excel == "") {
                                    parent.Ext.ux.Toast.msg('操作提示：', '请选择要导入的文件.');
                                    return;
                                } else if (excel.split(".")[excel.split(".").length - 1] != "xls") {
                                    parent.Ext.ux.Toast.msg('操作提示：', '请选择xls类型的文件.');
                                    return;
                                }
                                this.frm.form.submit({
                                    method: 'post',
                                    waitTitle: '请稍等',
                                    waitMsg: '正在上传数据……',
                                    url: contextPath + '/' + namespace + '/' + action + '!importData.action',
                                    success: function (form, action) {
                                        var data = action.response.responseText;
                                        // 返回的数据是对象，在外层加个括号才能正确执行eval
                                        var model = eval('(' + data + ')');
                                        // Ext.getCmp('downLoadPath').setValue(contextPath+model.importResultfile);
                                        Ext.MessageBox.confirm("提示", model.message + "！是否下载导入结果？", function (button) {
                                            if (button == "yes") {
                                                path = contextPath + model.importResultfile;
                                                window.open(path, '_blank', 'width=500,height=1,toolbar=no,menubar=no,location=no');
                                            } else {
                                                return;
                                            }
                                        }, this);
                                        Ext.getCmp('base_import_win').close();
                                    },
                                    failure: function (form, action) {
                                        var data = action.response.responseText;
                                        // 返回的数据是对象，在外层加个括号才能正确执行eval
                                        var model = eval('(' + data + ')');
                                        parent.Ext.ux.Toast.msg('操作提示：', model.message);
                                        Ext.getCmp('base_import_win').close();
                                    }
                                });
                            }
                        }, {
                            text: '取消',
                            iconCls: 'cancel',
                            scope: this,
                            handler: function () {
                                this.win.close();
                            }
                        }]
                });
            return win;
        },
        show: function (xlsName) {
            this.xlsName = xlsName;
            this.items = this.getItems();
            this.otherItems = this.getOtherItems();
            this.frm = this.getForm();
            this.win = this.getWin();
            this.win.show();
        }
    }
}();

//通用表格
GridBaseModel = function () {
    return {
        setStoreBaseParams: function (store) {
            store.on('beforeload', function (store) {
                store.baseParams = {
                    queryString: GridBaseModel.queryString,
                    search: GridBaseModel.search,
                    propertyCriteria: GridBaseModel.propertyCriteria,
                    likeQueryValue: GridBaseModel.likeQueryValue
                };
            });
        },
        //数据源
        getStore: function (fields, pageSize) {
            if (undefined == this.storeURLParameter) {
                this.storeURLParameter = "";
            }
            //定义数据集对象
            var store = new Ext.data.Store({
                reader: new Ext.data.JsonReader({
                    totalProperty: 'totalProperty',
                    root: 'root'
                }, Ext.data.Record.create(fields)),
                proxy: new parent.Ext.data.HttpProxy({
                    url: this.storeURL + this.storeURLParameter
                })
            });
            this.setStoreBaseParams(store);
            this.loadStore(store, pageSize);
            return store;
        },
        loadStoreSuccess: function (store) {

        },
        loadStore: function (store, pageSize) {
            //第一次装载的时候指定页面大小
            store.load({
                params: {
                    limit: pageSize
                },
                callback: this.loadStoreSuccess(store)
            });
        },
        saveGridSet: function () {
            var gridName = GridBaseModel.namespace + "+" + GridBaseModel.action;
            GridConfig.saveGridSet(GridBaseModel.grid, gridName);
        },
        cleanGridSet: function () {
            var gridName = GridBaseModel.namespace + "+" + GridBaseModel.action;
            GridConfig.cleanGridSet(gridName);
        },
        //底部工具条
        getBBar: function (pageSize, store) {
            return new Ext.ux.PageSizePlugin({
                rowComboSelect: true,
                pageSize: pageSize,
                store: store,
                displayInfo: true,
                saveGridSet: this.saveGridSet,
                cleanGridSet: this.cleanGridSet
            });
        },
        //添加
        create: function () {
            CreateModel.show();
        },
        //删除
        remove: function () {
            var idList = GridBaseModel.getIdList();
            if (idList.length < 1) {
                parent.Ext.ux.Toast.msg('操作提示：', '请选择要进行操作的记录');
                return;
            }
            if (!GridBaseModel.beforeRemove()) {
                return;
            }
            Ext.MessageBox.confirm("请确认", "确实要删除吗？", function (button, text) {
                if (button == "yes") {
                    GridBaseModel.deleteData(idList.join(','));
                }
            });
        },
        beforeRemove: function () {
            return true;
        },
        //删除数据
        deleteData: function (ids) {
            parent.Ext.Ajax.request({
                url: GridBaseModel.deleteURL + '?time='
                + new Date().toString(),
                waitTitle: '请稍等',
                waitMsg: '正在删除数据……',
                params: {
                    ids: ids
                },
                method: 'POST',
                success: function (response, opts) {
                    GridBaseModel.removeSuccess(response, opts);
                }
            });
        },
        //删除数据成功后的回调
        removeSuccess: function (response, opts) {
            var data_json = response.responseJSON;
            var data_text = response.responseText;
            if (data_json) {
                parent.Ext.ux.Toast.msg('操作提示：', '{0}', data_json.message);
            } else {
                parent.Ext.ux.Toast.msg('操作提示：', '{0}', data_text);
            }
            GridBaseModel.refresh();
        },
        //取得所选的数据的特定字段
        getFieldList: function (field, grid) {
            if (!grid) {
                grid = GridBaseModel.grid;
            }
            var recs = grid.getSelectionModel().getSelections();
            var list = [];
            if (recs.length > 0) {
                for (var i = 0; i < recs.length; i++) {
                    var rec = recs[i];
                    if (rec.json[field] == undefined) {
                        parent.Ext.ux.Toast.msg('操作提示：', '表格中未定义此列【' + field + '】');
                        return;
                    } else {
                        list.push(rec.json[field])
                    }
                }
            }
            return list;
        },
        //取得所选
        getIdList: function (grid) {
            return this.getFieldList('id', grid);
        },
        getValueList: function (value) {
            return this.getFieldList(value);
        },
        //只能选择一条记录
        onlySelectSingleRow: function () {
            var idList = GridBaseModel.getIdList();
            if (idList.length < 1) {
                parent.Ext.ux.Toast.msg('操作提示：', '请选择要进行操作的记录');
                return false;
            }
            if (idList.length > 1) {
                parent.Ext.ux.Toast.msg('操作提示：', '只能选择一个要进行操作的记录');
                return false;
            }
            return true;
        },
        //修改
        modify: function () {
            var idList = GridBaseModel.getIdList();
            if (idList.length < 1) {
                parent.Ext.ux.Toast.msg('操作提示：', '请选择要进行操作的记录');
                return;
            }
            if (idList.length == 1) {
                var id = idList[0];
                if (!GridBaseModel.beforeModify()) {
                    return;
                }
                ;//外部重载方便用
                parent.Ext.Ajax.request({
                    url: GridBaseModel.retrieveURL + id
                    + GridBaseModel.extraModifyParameters() + '&time='
                    + new Date().toString(),
                    waitTitle: '请稍等',
                    waitMsg: '正在检索数据……',
                    method: 'POST',
                    success: function (response, options) {
                        var data = response.responseText;
                        //返回的数据是对象，在外层加个括号才能正确执行eval
                        var model = eval('(' + data + ')');
                        ModifyModel.show(model);
                    }
                });
            } else {
                parent.Ext.ux.Toast.msg('操作提示：', '只能选择一个要进行操作的记录！');
            }
        },
        beforeModify: function () {
            return true;
        },
        //详细
        detail: function () {
            var idList = GridBaseModel.getIdList();
            if (idList.length < 1) {
                parent.Ext.ux.Toast.msg('操作提示：', '请选择要进行操作的记录');
                return;
            }
            if (idList.length == 1) {
                var id = idList[0];

                parent.Ext.Ajax.request({
                    url: GridBaseModel.retrieveURL + id
                    + GridBaseModel.extraDetailParameters() + '&time='
                    + new Date().toString(),
                    waitTitle: '请稍等',
                    waitMsg: '正在检索数据……',
                    method: 'POST',
                    success: function (response, opts) {
                        var data = response.responseText;
                        //返回的数据是对象，在外层加个括号才能正确执行eval
                        var model = eval('(' + data + ')');
                        DisplayModel.show(model);
                    }
                });
            } else {
                parent.Ext.ux.Toast.msg('操作提示：', '只能选择一个要进行操作的记录！');
            }
        },
        //高级搜索
        advancedsearch: function () {
            AdvancedSearchModel.show();
        },
        //显示全部
        showall: function () {
            GridBaseModel.initQueryParma();
            GridBaseModel.refresh();
        },
        // 导入
        importData: function () {
            ImportDataModel.show();
        },
        //导出
        exportData: function () {
            if (undefined == GridBaseModel.storeURLParameter) {
                GridBaseModel.storeURLParameter = "";
            }
            if (!GridBaseModel.fileName) {
                GridBaseModel.fileName = '';
            }
            parent.Ext.Ajax.request({
                url: GridBaseModel.exportURL + GridBaseModel.storeURLParameter,
                waitTitle: '请稍等',
                waitMsg: '正在导出数据……',
                params: {
                    queryString: GridBaseModel.queryString,
                    propertyCriteria: GridBaseModel.propertyCriteria,
                    search: GridBaseModel.search,
                    likeQueryValue: GridBaseModel.likeQueryValue, //模糊搜索,
                    name: GridBaseModel.fileName // 导出的文件名
                },
                method: 'POST',
                timeout: 1800000,
                success: function (response, opts) {
                    var path = response.responseText;
                    //contextPath定义在引用了此JS的页面中
                    path = this.contextPath + path;
                    window.open(path, '_blank', 'width=1,height=1,toolbar=no,menubar=no,location=no');
                },
                failure: function (response, options) {
                    parent.Ext.ux.Toast.msg('操作提示：', "导出失败");
                }
            });
        },
        //报表
        chart: function () {
            ChartModel.show();
        },
        //右键菜单
        getContextMenu: function (commands, tips, callbacks) {
            //右键菜单
            var contextmenu = new Ext.menu.Menu({
                id: 'theContextMenu',
                items: []
            });
            this.addCommands(contextmenu, false, commands, tips, callbacks);
            return contextmenu;
        },
        //顶部工具条
        getToolbar: function (commands, tips, callbacks) {
            //工具栏组件
            var toolbar = new Ext.Toolbar();
            this.addCommands(toolbar, true, commands, tips, callbacks);
            return toolbar;
        },
        refreshStoreSuccess: function (store) {

        },
        //1 刷新之前改变propertyCriteria且不丢失之前的搜索条件
        //2 在刷新之后要还原,否则条件一直叠加
        addPropertyCriteria: function (propertyCriteria) {
            if (this.propertyCriteria) {
                this.propertyCriteria += ";" + propertyCriteria;
            } else {
                this.propertyCriteria = propertyCriteria;
            }
        },
        //刷新表格
        refresh: function () {
            this.store.load({
                params: {
                    limit: this.bbar.pageSize,
                    queryString: this.queryString,
                    propertyCriteria: this.propertyCriteria,
                    search: this.search,
                    likeQueryValue: this.likeQueryValue //模糊搜索
                },
                callback: function () {
                    GridBaseModel.refreshStoreSuccess(this.store);
                }
            });
            this.grid.getView().refresh();
        },
        //控制右键菜单及顶部工具条中的命令是否是用户有权限拥有的
        //obj工具条 或 菜单条
        //button 为true表示工具条 false表示菜单条
        addCommands: function (obj, button, commands, tips, callbacks) {
            if (commands == undefined || tips == undefined
                || callbacks == undefined) {
                return;
            }
            this.specialHeadTbar(obj);
            var showAllIsGranted = false;
            for (var i = 0; i < commands.length; i++) {
                var command = commands[i];
                var tip = tips[i];
                var callback = callbacks[i];
                if (button) {
                    if (command && parent.isGranted(namespace, action, command)) {
                        if (command == 'query') {
                            showAllIsGranted = true;
                        }
                        obj.add(new Ext.Button({
                            iconCls: command,
                            text: tip,
                            handler: callback
                        }));
                    }
                } else {
                    if (command && parent.isGranted(namespace, action, command)) {
                        obj.add(new Ext.menu.Item({
                            iconCls: command,
                            text: tip,
                            handler: callback
                        }));
                    }
                }
            }

            // add,如果是顶部工具条并且有查询权限添加模糊搜索文本框
            // 由于print插件总是在最末尾加入,所以在模糊搜索框之前加print按钮代替
            if (showAllIsGranted && button) {
                obj.add(new Ext.Button({
                    iconCls: 'print',
                    text: '打印',
                    handler: ''
                }));
                obj.add(new Ext.form.Label({
                    width: 100,
                    html: '<div style="margin:0 3px 0 20px">模糊搜索</div>',
                }));
                obj.add(new Ext.form.TextField({
                    width: 150,
                    cls: 'attr',
                    emptyText: '输入搜索条件',
                    listeners: {
                        'specialkey': function (field, e) {
                            if (e.keyCode == 13) {
                                GridBaseModel.likeQueryValue = field.getValue();
                                GridBaseModel.refresh();
                            }
                        }
                    }
                }));
            }

            this.specialTailTbar(obj);
        },
        //特殊的头Tbar处理
        specialHeadTbar: function (obj) {

        },
        //特殊的尾Tbar处理
        specialTailTbar: function (obj) {

        },

        //修改单个字段
        afterEdit: function (obj) {
            var key = obj.field;
            var value = obj.value;
            var r = obj.record;
            var id = r.get("id");
            var version = r.get("version");
            this.updateAttr(id, key, value, version);
        },
        //提交单个属性修改数据
        updateAttr: function (id, key, value, version) {
            //在此要利用encodeURI对传递的值进行编码
            //因为ie不会对传递的参数编码，只会对路径进行编码
            //firefox会对参数和路径都编码
            //配合后台    <Connector port="8080" protocol="HTTP/1.1" 
            //            connectionTimeout="20000" 
            //            redirectPort="8443"  URIEncoding="utf-8"/>
            //就可以实现对中文参数的正确使用

            parent.Ext.Ajax.request({
                url: this.updatePartURL + id + "&model." + key + "="
                + encodeURI(value) + "&model.version=" + version
                + GridBaseModel.extraModifyParameters(),
                method: 'POST',
                success: function (response, opts) {
                    var data = response.responseText;
                    var tip = eval('(' + data + ')');
                    if (tip.message == '修改成功') {
                        GridBaseModel.updateAttrSuccess();
                    } else {
                        parent.Ext.ux.Toast.msg('操作提示：', '{0}', tip.message);
                    }
                }
            });
        },
        //提交一条记录多个属性修改数据
        updateAttrs: function (id, keys, values, version, r) {
            //在此要利用encodeURI对传递的值进行编码
            //因为ie不会对传递的参数编码，只会对路径进行编码
            //firefox会对参数和路径都编码
            //配合后台    <Connector port="8080" protocol="HTTP/1.1" 
            //            connectionTimeout="20000" 
            //            redirectPort="8443"  URIEncoding="utf-8"/>
            //就可以实现对中文参数的正确使用
            var url = this.updatePartURL + id + "&model.version=" + version
                + GridBaseModel.extraModifyParameters();
            for (i = 0; i < keys.length; i++) {
                url = url + "&model." + keys[i] + "=" + encodeURI(values[i]);
            }
            parent.Ext.Ajax.request({
                url: url,
                method: 'POST',
                success: function (response, opts) {
                    var data = response.responseText;
                    var tip = eval('(' + data + ')');
                    var tip = eval('(' + data + ')');
                    if (tip.message == '修改成功') {
                        GridBaseModel.updateAttrSuccess();
                    } else {
                        parent.Ext.ux.Toast.msg('操作提示：', '{0}', tip.message);
                    }
                }
            });
        },
        extraModifyParameters: function () {
            return "";
        },
        extraDetailParameters: function () {
            return "";
        },
        extraCreateParameters: function () {
            return "";
        },
        //单个属性更新成功后的回调
        updateAttrSuccess: function (response, opts) {
            GridBaseModel.refresh();
        },
        changeURL: function (contextPath, namespace, action) {
            this.initURL(contextPath, namespace, action);
            if (this.store) {
                this.store.proxy.setUrl(this.storeURL + this.storeURLParameter, true);
            }
        },
        getSearchModel: function () {
            //默认情况下，GridBaseModel.search=false，下发propertyCriteria参数即query查询以及export导出按照系统提供默认的方式进行
            //否则，由自定义的sql语句进行查询，下发queryString参数，此时GridBaseModel.search=true，这个由每个页面单独重载来设置
            return false;
        },
        initStoreURLParameter: function () {
        },
        initQueryParma: function () {
            GridBaseModel.search = this.getSearchModel();
            GridBaseModel.queryString = "";
            GridBaseModel.propertyCriteria = "";
            GridBaseModel.likeQueryValue = "";
        },
        initURL: function (contextPath, namespace, action) {
            this.contextPath = contextPath;
            this.namespace = namespace;
            this.action = action;
            action = this.checkAction(action);
            //批量查询
            this.storeURL = contextPath + '/' + namespace + '/' + action
                + '!query.action';
            //单条查询
            this.retrieveURL = contextPath + '/' + namespace + '/' + action
                + '!retrieve.action?model.id=';
            //批量删除
            this.deleteURL = contextPath + '/' + namespace + '/' + action
                + '!delete.action';
            //导出数据
            this.exportURL = contextPath + '/' + namespace + '/' + action
                + '!export.action';
            //添加一条数据
            this.createURL = contextPath + '/' + namespace + '/' + action
                + '!create.action';
            //修改部分数据
            this.updatePartURL = contextPath + '/' + namespace + '/' + action
                + '!updatePart.action?model.id=';
        },
        showBefore: function () {

        },
        mergeGridCfg: function (oldCfg, newCfg) {
            if (oldCfg.length != newCfg.length) {
                Ext.MessageBox.alert('提示', "自定义表格配置有误！");
            } else {
                for (var i in newCfg) {
                    var newObj = newCfg[i];
                    for (var j in oldCfg) {
                        var oldObj = oldCfg[j];
                        if (oldObj.header == newObj.header) {
                            for (var pro in newObj) {
                                oldObj[pro] = newObj[pro];
                            }
                            newCfg[i] = oldObj;
                            break;
                        }
                    }
                }
            }
            return newCfg;
        },
        //重复的action对应不同的表格配置时加*做特殊处理
        checkAction: function (action) {
            if (action.indexOf("*") != -1) {
                action = action.split("*")[1];
            }
            return action;
        },
        //Tms显示wms同步数据是否删除字段模块
        getWmsSynModule: function () {
            //1组织架构 2地址资料 3仓库 4部门 5备货位
            return ["org", "address-data", "warehouse-data", "department-data", "preparation-of-iocation-data"];
        },
        addColumn: function (columns) {
            var blankColumn = {header: "", width: 0, dataIndex: '', sortable: true};
            columns.push(blankColumn);
            return columns;
        },
        setDefaultCss: function (columns) {
            var defaultCSS = "vertical-align: middle;";
            for (var i in columns) {
                var css = columns[i].css;
                if (css) {
                    css = css + defaultCSS;
                } else {
                    css = "vertical-align: middle;";
                }
                columns[i].css = css;
            }
        },
        addField: function (fields, queryaction) {
            if (this.getWmsSynModule().indexOf(queryaction) != -1) {
                var wmsDelete = {name: 'wmsDelete'}
                fields.push(wmsDelete);
            }
            // var newField = {name: 'ownerUser_orgName'};
            // fields.push(newField);
            return fields;
        },
        getGrid: function (contextPath, namespace, action, pageSize, fields,
                           columns, commands, tips, callbacks) {
            fields = this.addField(fields, action);

            this.initQueryParma();
            this.initURL(contextPath, namespace, action);
            this.initStoreURLParameter();

            //表格设置查询
            var gridConfig = GridConfig.queryGridConfig(namespace + "+"
                + action);
            var gridSetting = gridConfig.gridSetting;
            var size = gridConfig.pageSize;
            if (gridSetting != '' && gridSetting != undefined && size != '') {
                columns = this.mergeGridCfg(columns, gridSetting);
                pageSize = size;
            }
            //添加数据所有者列和空列
            columns = this.addColumn(columns, action);

            this.store = this.getStore(fields, pageSize);
            this.toolbar = this.getToolbar(commands, tips, callbacks);
            this.contextmenu = this.getContextMenu(commands, tips, callbacks);
            this.bbar = this.getBBar(pageSize, this.store);
            var cb = new Ext.grid.CheckboxSelectionModel();
            var preColumns = [//配置表格列
                new Ext.grid.RowNumberer({
                    header: '行号',
                    width: 40
                }),//表格行号组件
                cb];
            columns = preColumns.concat(columns);
            //设置表格默认样式
            this.setDefaultCss(columns);
            this.grid = new Ext.grid.EditorGridPanel({
                title: ' ',
                //autoHeight: true, //注释掉才会出现滚动条
                //frame:true,
                border: false,
                store: this.store,
                tbar: this.toolbar,
                bbar: this.bbar,
                stripeRows: true,
                autoScroll: true,
                loadMask: true,
                trackMouseOver: true,
                minColumnWidth: 80,
                viewConfig: {
                    loadingText: '数据加载中,请稍等...',
                    emptyText: '无对应信息',
                    deferEmptyText: true,
                    autoFill: true,
                    //forceFit:true
                },
                sm: cb,
                columns: columns,
                //clicksToEdit:1,
                plugins: this.getPlugins(),
                keys: this.getKeys()
            });
//			屏蔽右键菜单
//			this.grid.on("rowcontextmenu", function(grid, rowIndex, e) {
//				e.preventDefault();
//				grid.getSelectionModel().selectRow(rowIndex);
//				GridBaseModel.contextmenu.showAt(e.getXY());
//			});
            this.grid.on("afteredit", function (obj) {
                GridBaseModel.afterEdit(obj);
            });
            this.grid.on("cellclick", function (grid, rowIndex, columnIndex, e) {

            });
            this.grid.on('rowclick', function (grid, index, e) {
                GridBaseModel.onRowClick(namespace, action, grid, index, e);
            });
            this.grid.on('rowdblclick', function (grid, index, e) {
                GridBaseModel.onRowDblClick(namespace, action);
            });
            this.grid.getStore().on('load', function (s, records) {
                return;
            });
            this.showBefore();
            return this.grid;
        },
        getPlugins: function () {
            return [Ext.ux.plugins.Print];
            //return [];
        },
        getKeys: function () {
            return [];
        },
        onRowClick: function (namespace, action, grid, index, e) {
            GridBaseModel.detail();
        },
        onRowDblClick: function (namespace, action) {
            /*if (parent.isGranted(namespace, action, "updatePart")) {
             try {
             GridBaseModel.modify();
             } catch (e) {
             console.log('无修改功能');
             }
             }*/
            return;
        },
        getAnchor: function () {
            return '100%';
        },
        getViewport: function () {
            this.viewport = new Ext.Viewport({
                layout: 'border',
                items: [{
                    region: 'center',
                    layout: 'fit',
                    items: [this.grid]
                }]
            });
            return this.viewport;
        },
        beforeShow: function (grid) {

        },

        show: function (contextPath, namespace, action, pageSize, fields,
                        columns, commands, tips, callbacks) {
            this.grid = this.getGrid(contextPath, namespace, action, pageSize,
                fields, columns, commands, tips, callbacks);
            GridBaseModel.beforeShow(this.grid);
            this.getViewport();
        }
    };
}();


DetailGridBaseModel = function () {
    return {
        // 数据源
        getStore: function (fields, pageSize) {
            if (undefined == this.propertyCriteria) {
                this.propertyCriteria = "id:eq:0";
            }
            if (undefined == this.likeQueryValue) {
                this.likeQueryValue = "";
            }
            if (undefined == this.initUrlParams) {
                this.initUrlParams = "";
            }
            // 定义数据集对象
            var store = new Ext.data.Store({
                reader: new Ext.data.JsonReader({
                    totalProperty: 'totalProperty',
                    root: 'detailList'
                }, Ext.data.Record.create(fields)),
                proxy: new parent.Ext.data.HttpProxy({
                    url: contextPath + '/' + this.namespace + '/'
                    + this.action + this.interfaceName +'.action'
                })
            });
            store.on('beforeload', function (store) {
                store.baseParams = {
                    propertyCriteria: DetailGridBaseModel.propertyCriteria,
                    likeQueryInfo: DetailGridBaseModel.likeQueryInfo,
                    likeQueryType:  DetailGridBaseModel.initUrlParams.detail,
                    queryaction: DetailGridBaseModel.initUrlParams.detail + 'Action',
                };
            });
            store.load({
                params: {
                    limit: pageSize
                }
            });
            return store;
        },
        saveGridSet: function () {
            var gridName = "detail/" + DetailGridBaseModel.action;
            GridConfig.saveGridSet(DetailGridBaseModel.grid, gridName);
        },
        cleanGridSet: function () {
            var gridName = "detail/" + DetailGridBaseModel.action;
            GridConfig.cleanGridSet(gridName);
        },
        // 底部工具条
        getBBar: function (pageSize, store, disabledPage) {
            return new Ext.ux.PageSizePlugin({
                rowComboSelect: true,
                pageSize: pageSize,
                store: store,
                displayInfo: true,
                saveGridSet: this.saveGridSet,
                cleanGridSet: this.cleanGridSet,
                disabledPage: disabledPage  //满足可编辑表格时不可分页情景
            });
        },
        getPageSize: function () {
            return 20;
        },
        setEditor: function (columns) {
            return columns;
        },
        // 刷新表格
        refresh: function (propertyCriteria) {
            this.propertyCriteria = propertyCriteria;
            this.store.load({
                params: {
                    //如果当前表格不需要分页则bbar不存在
                    limit: this.bbar != undefined ? this.bbar.pageSize : this.getPageSize(),
                    propertyCriteria: propertyCriteria
                }
            });
        },
        //表格组装
        getGrid: function (interfaceName, namespace, action, fields, columns, propertyCriteria, initUrlParams, disabledPage) {
            var fields = GridBaseModel.addField(fields, action);
            this.namespace = namespace;
            this.action = action;
            this.propertyCriteria = propertyCriteria;
            this.initUrlParams = initUrlParams;
            this.interfaceName = interfaceName;

            var pageSize = this.getPageSize();

            var gridConfig = GridConfig.queryGridConfig("detail/" + this.action);
            var gridSetting = gridConfig.gridSetting;
            var size = gridConfig.pageSize;
            if (gridSetting != '' && gridSetting != undefined && size != '') {
                columns = GridBaseModel.mergeGridCfg(columns, gridSetting);
                pageSize = size;
                columns = this.setEditor(columns);//可编辑表格
            }
            //添加数据所有者列和空列
            columns = GridBaseModel.addColumn(columns, action);

            this.store = this.getStore(fields, pageSize);
            this.bbar = this.getBBar(pageSize, this.store, disabledPage || this.disabledPage);
            var cb = new Ext.grid.CheckboxSelectionModel();
            var preColumns = [// 配置表格列
                new Ext.grid.RowNumberer({
                    header: '行号',
                    width: 40
                }),// 表格行号组件
                cb];
            //嵌套表格
            if (this.plugins != undefined) {
                preColumns = [// 配置表格列
                    this.plugins,
                    new Ext.grid.RowNumberer({
                        header: '行号',
                        width: 40
                    }),// 表格行号组件
                    cb
                ];
            }
            columns = preColumns.concat(columns);
            //设置表格默认样式
            GridBaseModel.setDefaultCss(columns);
            this.grid = new Ext.grid.EditorGridPanel({
                store: this.store,
                loadMask: true,
                autoScroll: true,
                stripeRows: true,
                border: false,
                bbar: this.bbar,
                minColumnWidth: 80,
                viewConfig: {
                    loadingText: '数据加载中,请稍等...',
                    emptyText: '无对应信息',
                    deferEmptyText: true,
                    autoFill: true,
                    //forceFit:true
                },
                sm: cb,
                columns: columns,
                enableKeyEvents: true,
                plugins: this.plugins,

            });
            return this.grid;
        }
    }
}();

//高级搜索模块+
AdvancedSearchBaseModelPlus = function () {
    return {
        dataType: function () {
            //string匹配值需要加$
            return ["$", "", "."];
        },
        //操作符(一一对应)
        operator: function (index) {
            if (index == undefined)
                return ["等于", "大于", "大于等于", "小于", "小于等于", "模糊匹配", "不等于"];
            else
                return ["eq", "gt", "ge", "lt", "le", "like", "ne"][index];
        },
        addList: function (list) {
            //ID列
            var idItem = {text: "编号", name: "id", datatype: ""};
            list.unshift(idItem);
            //数据所属单位
            idItem = {text: "数据所属单位", name: "ownerUser.org.orgName", datatype: "$", module: "org", column: "orgName"};
            list.push(idItem);

            //审批
            /*var arry = parent.audit_module.split(',');
             var isAudit = false;
             for (var i = 0; i < arry.length; i++) {
             //这里action的名字和配置文件（实体类）去匹配，
             //如果action和实体类不匹配，则在每个单面单独重载一下AuditingBaseModel.isAudit即可
             if (arry[i] == action || AuditingBaseModel.isAudit) {
             isAudit = true;
             break;
             }
             }
             if (isAudit) {
             var workFlowStatusItem = {text: "工作流状态", name: "workFlowStatus.name", datatype: "$"};
             var eapsAuditLevelItem = {text: "审核等级", name: "eapsAuditLevel.name", datatype: "$"};
             list.push(workFlowStatusItem, eapsAuditLevelItem);
             }*/
        },
        bulidStore: function (list) {
            this.addList(list);
            var data = [];
            for (var i = 0; i < list.length; i++) {
                var dataItem = [list[i].text, list[i].name, list[i].datatype, list[i].module, list[i].column, "等于", ""];
                data.push(dataItem);
            }
            var store = new Ext.data.SimpleStore({
                auteLoad: true,
                data: data,
                fields: ["text", "name", "datatype", "module", "column",
                    "operator", "value"]
            });
            return store;
        },
        getColumns: function () {
            var columns = [
                {header: "搜索字段", width: 200, dataIndex: 'text', sortable: true, hideable: false},
                {
                    header: "操作符", width: 100, dataIndex: 'operator', sortable: false, hideable: false,
                    editor: new Ext.form.ComboBox({
                        store: this.operator(),
                        emptyText: '',
                        mode: 'local',
                        triggerAction: 'all',
                        selectOnFocus: true,
                        forceSelection: true,
                        editable: false
                    })
                },
                {
                    header: "匹配值", width: 200, dataIndex: 'value', sortable: false, hideable: false,
                    editor: new Ext.form.TextField()
                },
                {
                    header: "", width: 30, dataIndex: 'module', sortable: false, hideable: true,
                    renderer: function (val) {
                        if (val != "") {
                            return "<img id='image' src='../images/query.jpg' width='15px' height='15px' ondblclick='AdvancedSearchBaseModelPlus.ondblClick()'/>";
                        }
                    }
                }
            ];
            return columns;
        },
        getSelectCell: function () {
            var select = AdvancedSearchBaseModelPlus.grid.getSelectionModel().getSelections();
            return select;
        },
        ondblClick: function () {
            if (this.getSelectCell().length != 1) {
                return;
            }
            var data = this.getSelectCell()[0].data;
            QueryGridWindow.afterOptComplete = function (record) {
                data.value = record.json[data.column];
                AdvancedSearchBaseModelPlus.grid.getView().refresh();
            };
            QueryGridWindow.show(data.module, "", [], []);
        },
        //搜索条件组合表格
        getGrid: function (columns, store) {
            var cb = new Ext.grid.CheckboxSelectionModel();
            preColumns = [// 配置表格列
                new Ext.grid.RowNumberer({
                    header: '行号',
                    width: 40
                }), cb
            ];
            columns = preColumns.concat(columns);
            this.grid = new Ext.grid.EditorGridPanel({
                store: store,
                autoScroll: true,
                columns: columns,
                sm: cb
            });
            return this.grid;
        },
        getWindow: function (items) {
            var dlg = new Ext.Window({
                title: "高级搜索",
                maximizable: true,
                iconCls: "search",
                width: 700,
                height: 400,
                plain: true,
                closable: true,
                frame: true,
                layout: 'fit',
                border: true,
                modal: true,
                items: [items],
                buttonAlign: 'center',
                buttons: [{
                    text: '确定',
                    iconCls: 'save',
                    scope: this,
                    handler: function () {
                        if (this.beforeSearch()) {
                            var propertyCriteria = this.bulidParams(this.grid.store);
                            this.search(propertyCriteria);
                        }
                    }
                }, {
                    text: '重置',
                    iconCls: 'reset',
                    scope: this,
                    handler: function () {
                        this.reset();
                    }
                }, {
                    text: '取消',
                    iconCls: 'cancel',
                    scope: this,
                    handler: function () {
                        this.close();
                    }
                }]
            });
            return dlg;
        },
        bulidParams: function (store) {
            var params = "";
            var index = 0;
            var dataList = store.data.items;
            var beforeVal = "";
            var val = "";
            var afterVal = "";
            for (var i = 0; i < dataList.length; i++) {
                if (dataList[i].data.operator != "" && dataList[i].data.value != "") {
                    params += dataList[i].data.name + ":";
                    index = this.operator().indexOf(dataList[i].data.operator);
                    params += this.operator(index) + ":";

                    datatype = dataList[i].data.datatype;
                    val = dataList[i].data.value;

                    //字符串处理
                    if (datatype == "$") {
                        beforeVal = "$";
                    }
                    //浮点值处理
                    if (datatype == "." && val.indexOf(".") == -1) {
                        afterVal = ".0";
                    }
                    //模糊搜索加%%
                    if (this.operator(index) == "like") {
                        params += beforeVal + "%" + val + afterVal + "%;";
                    } else {
                        params += beforeVal + val + afterVal + ";";
                    }
                }
            }
            params = params.substring(0, params.length - 1);
            console.log("search-----" + params);
            return params;
        },
        beforeSearch: function () {
            return true;
        },
        search: function (propertyCriteria) {
            //不改变原有条件
            GridBaseModel.likeQueryValue = "";
            GridBaseModel.addPropertyCriteria(propertyCriteria);
            GridBaseModel.refresh();
            this.close();
        },
        reset: function () {
            var store = this.grid.store;
            var items = store.data.items;
            for (var i = 0; i < items.length; i++) {
                items[i].data.operator = "";
                items[i].data.value = "";
            }
            this.grid.getView().refresh();
        },
        close: function () {
            this.dlg.close();
        },
        show: function (queryItems) {
            var store = this.bulidStore(queryItems);
            var columns = this.getColumns();
            this.grid = this.getGrid(columns, store);
            this.dlg = this.getWindow(this.grid);
            this.dlg.show();
        }

    }
}();

//高级搜索
AdvancedSearchBaseModel = function () {
    return {
        getLabelWidth: function () {
            return 100;
        },
        getAnchor: function () {
            return '95%';
        },
        getForm: function (items) {
            var labelWidth = this.getLabelWidth();
            var anchor = this.getAnchor();
            var frm = new Ext.form.FormPanel({
                labelAlign: 'left',
                buttonAlign: 'center',
                bodyStyle: 'padding:5px',
                frame: true,//圆角和浅蓝色背景
                labelWidth: labelWidth,
                autoScroll: true,

                defaults: {
                    anchor: anchor
                },
                items: items,

                buttons: [{
                    text: '搜索',
                    iconCls: 'search',
                    scope: this,
                    handler: function () {
                        this.sure();
                    }
                }, {
                    text: '重置',
                    iconCls: 'reset',
                    scope: this,
                    handler: function () {
                        this.frm.form.reset();
                    }
                }, {
                    text: '取消',
                    iconCls: 'cancel',
                    scope: this,
                    handler: function () {
                        this.close();
                    }
                }],
                keys: [{
                    key: Ext.EventObject.ENTER,
                    fn: function () {
                        this.sure();
                    },
                    scope: this
                }]
            });
            return frm;
        },

        getDialog: function (title, iconCls, width, height, items) {
            this.frm = this.getForm(items);
            var dlg = new Ext.Window({
                maximizable: true,
                title: title,
                iconCls: iconCls,
                width: width,
                height: height,
                plain: true,
                closable: true,
                frame: true,
                layout: 'fit',
                border: false,
                modal: true,
                items: [this.frm]
            });
            return dlg;
        },

        show: function (title, iconCls, width, height, items, callback) {
            //注册函数，指定处理方式
            this.sure = callback;
            this.dlg = this.getDialog(title, iconCls, width, height, items);
            this.dlg.show();
        },

        reset: function () {
            this.frm.form.reset();
        },

        close: function () {
            if (this.dlg != undefined) {
                this.dlg.close();
            }
        },

        sure: function () {
            //由具体的使用者指定处理方式
        },
        startSearch: function () {
            //方便外部重载
        },
        silentSearch: function (data, alias) {
            var SearchString = "";
            GridBaseModel.likeQueryValue = "";
            if (GridBaseModel.search) {
                //自定义构造搜索搜索语句,用queryString参数
                for (var i = 0; i < data.length; i++) {
                    if (data[i] != "") {
                        if (i > 0) {
                            SearchString += " and ";
                        }
                        SearchString += data[i];
                    }
                }
                if (SearchString != "") {
                    this.startSearch();
                    GridBaseModel.queryString = SearchString;
                    GridBaseModel.refresh();
                    return true;
                }
            } else {
                //系统默认搜索方式,用propertyCriteria参数
                for (var i = 0; i < data.length; i++) {
                    if (data[i] != "") {
                        SearchString += data[i];
                        SearchString += ";";
                    }
                }
                if (SearchString != "") {
                    this.startSearch();
                    GridBaseModel.propertyCriteria = SearchString;
                    GridBaseModel.refresh();
                    return true;
                }
            }
            return false;
        },
        search: function (data, alias) {
            if (this.silentSearch(data, alias)) {
                AdvancedSearchBaseModel.close();
            } else {
                Ext.MessageBox.alert('提示', "请输入查询条件！");
            }
        }
    };
}();

//添加模型信息
CreateBaseModel = function () {
    return {
        getLabelWidth: function () {
            return 100;
        },
        isFileUpload: function () {
            return false;
        },
        getForm: function (items) {
            var labelWidth = this.getLabelWidth();
            var frm = new Ext.form.FormPanel({
                fileUpload: this.isFileUpload(),
                labelAlign: 'left',
                buttonAlign: 'center',
                bodyStyle: 'padding:5px',
                frame: true, // 圆角和浅蓝色背景
                labelWidth: labelWidth,
                autoScroll: true,

                defaults: {
                    anchor: '95%'
                },
                items: items,

                buttons: this.getButtons(),
                keys: this.getKeys()
            });
            return frm;
        },
        getButtons: function () {
            var buttons = [{
                text: '保存',
                iconCls: 'save',
                scope: this,
                handler: function () {
                    this.submit();
                }
            }, {
                text: '重置',
                iconCls: 'reset',
                scope: this,
                handler: function () {
                    this.reset()
                }
            }, {
                text: '取消',
                iconCls: 'cancel',
                scope: this,
                handler: function () {
                    this.close();
                }
            }];
            return buttons;
        },
        getKeys: function () {
            var keys = [{
                key: Ext.EventObject.ENTER,
                fn: function () {
                    this.submit();
                },
                scope: this
            }];
            return keys;
        },

        getDialog: function (title, iconCls, width, height, items) {
            this.frm = this.getForm(items);
            var dlg = new Ext.Window({
                title: title,
                iconCls: iconCls,
                width: width,
                height: height,
                maximizable: true,
                plain: true,
                closable: true,
                frame: true,
                layout: 'fit',
                border: false,
                modal: true,
                items: [this.frm]
            });
            return dlg;
        },

        beforeShow: function () {
            return true;
        },

        show: function (title, iconCls, width, height, items, isMaxSize) {
            if (!this.beforeShow()) {
                return;
            }
            this.dlg = this.getDialog(title, iconCls, width, height, items);
            this.dlg.show();
            this.reset();
            this.dlg.on('close', function () {
                // 刷新表格
                GridBaseModel.refresh();
            });
            if (isMaxSize) {
                this.dlg.maximize();
            }
        },

        reset: function () {
            this.frm.form.reset();
        },

        close: function () {
            this.dlg.close();
        },

        formIsValid: function () {
            if (this.frm.getForm().isValid()) {
                return true;
            }
            return false;
        },

        shouldSubmit: function () {
            return true;
        },

        prepareSubmit: function () {

        },

        submit: function () {
            if (this.formIsValid()) {
                if (this.shouldSubmit()) {
                    this.prepareSubmit();
                    this.submitCreate(this.frm.form);
                }
            } else {
                parent.Ext.ux.Toast.msg('操作提示：', '有必填项未填写完成,请核对.');
            }
        },

        //提交添加数据
        submitCreate: function (form) {
            if (undefined == GridBaseModel.createURLParameter) {
                GridBaseModel.createURLParameter = "";
            }
            form.submit({
                waitTitle: '请稍等',
                waitMsg: '正在' + CreateBaseModel.dlg.title + '……',
                url: GridBaseModel.createURL + GridBaseModel.createURLParameter + GridBaseModel.extraCreateParameters(),
                submitEmptyText: false,
                success: function (form, action) {
                    GridBaseModel.search = false;
                    CreateBaseModel.close();
                    CreateBaseModel.createSuccess(form, action);
                    parent.Ext.ux.Toast.msg('操作提示：', action.result.message);
                },
                failure: function (form, action) {
                    if (action.failureType === Ext.form.Action.SERVER_INVALID) {
                        parent.Ext.ux.Toast.msg('操作提示：', action.result.message);
                    }
                }
            });
        },
        createSuccess: function (form, action) {
            //回调，留给使用者实现
            GridBaseModel.refresh();
        }
    };
}();

//修改模型信息
ModifyBaseModel = function () {
    return {
        getLabelWidth: function () {
            return 100;
        },
        isFileUpload: function () {
            return false;
        },
        getForm: function (items) {
            var labelWidth = this.getLabelWidth();
            var frm = new Ext.form.FormPanel({
                fileUpload: this.isFileUpload(),
                labelAlign: 'left',
                buttonAlign: 'center',
                bodyStyle: 'padding:5px',
                frame: true, // 圆角和浅蓝色背景
                labelWidth: labelWidth,
                autoScroll: true,

                defaults: {
                    anchor: '95%'
                },

                items: items,

                buttons: this.getButtons(),
                keys: this.getKeys()
            });
            return frm;
        },
        getButtons: function () {
            return [{
                text: '保存',
                iconCls: 'save',
                scope: this,
                handler: function () {
                    this.submit();
                }
            }, {
                text: '取消',
                iconCls: 'cancel',
                scope: this,
                handler: function () {
                    this.close();
                }
            }];
        },
        getKeys: function () {
            var keys = [{
                key: Ext.EventObject.ENTER,
                fn: function () {
                    this.submit();
                },
                scope: this
            }];
            return keys;
        },

        getDialog: function (title, iconCls, width, height, items) {
            this.frm = this.getForm(items);
            var dlg = new Ext.Window({
                title: title,
                maximizable: true,
                iconCls: iconCls,
                width: width,
                height: height,
                plain: true,
                closable: true,
                frame: true,
                layout: 'fit',
                border: false,
                modal: true,
                items: [this.frm]
            });
            return dlg;
        },

        beforeShow: function () {
            return true;
        },

        show: function (title, iconCls, width, height, items, model, isMaxSize) {
            this.model = model;
            if (!this.beforeShow()) {
                return;
            }
            this.dlg = this.getDialog(title, iconCls, width, height, items);
            this.dlg.show();
            if (isMaxSize) {
                this.dlg.maximize();
            }
        },

        reset: function () {
            this.frm.form.reset();
        },

        close: function () {
            this.dlg.close();
        },

        formIsValid: function () {
            if (this.frm.getForm().isValid()) {
                return true;
            }
            return false;
        },

        shouldSubmit: function () {
            return true;
        },

        prepareSubmit: function () {

        },

        submit: function () {
            if (this.formIsValid()) {
                if (this.shouldSubmit()) {
                    this.prepareSubmit();
                    this.submitModify(this.frm.form);
                }
            } else {
                parent.Ext.ux.Toast.msg('操作提示：', '有必填项未填写完成,请核对.');
            }
        },
        success: function (form, action) {

        },
        //提交修改数据
        submitModify: function (form) {
            form.submit({
                waitTitle: '请稍等',
                waitMsg: '正在修改……',
                url: GridBaseModel.updatePartURL + this.model.id + '&model.version=' + this.model.version + GridBaseModel.extraModifyParameters(),
                submitEmptyText: false,
                success: function (form, action) {
                    parent.Ext.ux.Toast.msg('操作提示：', '修改成功');
                    ModifyBaseModel.close();
                    ModifyBaseModel.modifySuccess(form, action);
                },
                failure: function (form, action) {
                    if (action.failureType === Ext.form.Action.SERVER_INVALID) {
                        parent.Ext.ux.Toast.msg('操作提示：',
                            action.result.message);
                    }
                    ModifyBaseModel.close();
                }
            });
        },
        modifySuccess: function (form, action) {
            // 回调，留给使用者实现
            GridBaseModel.refresh();
        }
    };
}();

//显示模型详细信息
DisplayBaseModel = function () {
    return {
        getLabelWidth: function () {
            return 80;
        },
        getForm: function (items) {
            var labelWidth = this.getLabelWidth();
            var frm = new Ext.form.FormPanel({
                labelAlign: 'left',
                buttonAlign: 'center',
                bodyStyle: 'padding:5px',
                frame: true, // 圆角和浅蓝色背景
                labelWidth: labelWidth,
                autoScroll: true,

                defaults: {
                    readOnly: true,
                    fieldClass: 'detail_field',
                    anchor: '95%'
                },

                items: items,

                buttons: [{
                    text: '关闭',
                    iconCls: 'cancel',
                    scope: this,
                    handler: function () {
                        this.close();
                    }
                }],
                keys: this.getKeys()
            });
            return frm;
        },

        getKeys: function () {
            return [{
                key: Ext.EventObject.ENTER,
                fn: function () {
                    this.close();
                },
                scope: this
            }];
        },

        getDialog: function (title, iconCls, width, height, items) {
            this.frm = this.getForm(items);
            var dlg = new Ext.Window({
                title: title,
                maximizable: true,
                iconCls: iconCls,
                width: width,
                height: height,
                plain: true,
                closable: true,
                frame: true,
                layout: 'fit',
                border: false,
                modal: true,
                items: [this.frm]
            });
            return dlg;
        },

        show: function (title, iconCls, width, height, items, isMaxSize) {
            this.dlg = this.getDialog(title, iconCls, width, height, items);
            this.dlg.show();
            if (isMaxSize) {
                this.dlg.maximize();
            }
        },

        close: function () {
            this.dlg.close();
        }
    };
}();

//树模型
TreeBaseModel = function () {
    return {
        getTreeWithContextMenu: function (dataUrl, rootText, rootId, icon,
                                          create, remove, modify, tree_width) {
            TreeBaseModel.tree_width = tree_width == undefined ? 300 : tree_width;
            var tree = this.getTree(dataUrl, rootText, rootId, icon);
            var contextMenu = this.getContextMenu(create, remove, modify);
            document.oncontextmenu = function () {
                return false;
            } //屏蔽右键 ，IE6下会有问题
            tree.on('contextmenu', function (node, event) {
                event.preventDefault(); //关闭默认的菜单，以避免弹出两个菜单
                TreeBaseModel.onClick(node, event);
                contextMenu.showAt(event.getXY()); //取得鼠标点击坐标，展示菜单
            });
            return tree;
        },
        //右键菜单
        getContextMenu: function (create, remove, modify) {
            //右键菜单
            var contextmenu = new Ext.menu.Menu({
                id: 'theContextMenu',
                items: []
            });
            this.commandPrivilegeControl(contextmenu, create, remove, modify);
            return contextmenu;
        },
        //控制右键菜单中的命令是否是用户有权限拥有的
        commandPrivilegeControl: function (obj, create, remove, modify) {
            if (create && parent.isGranted(namespace, action, "create")) {
                obj.add(new Ext.menu.Item({
                    iconCls: 'create',
                    text: '添加',
                    handler: this.create
                }));
            }
            if (remove && parent.isGranted(namespace, action, "delete")) {
                obj.add(new Ext.menu.Item({
                    iconCls: 'delete',
                    text: '删除',
                    handler: this.remove
                }));
            }
            if (modify && parent.isGranted(namespace, action, "updatePart")) {
                obj.add(new Ext.menu.Item({
                    iconCls: 'updatePart',
                    text: '修改',
                    handler: this.modify
                }));
            }
        },
        //添加
        create: function () {
            CreateModel.show();
        },
        //删除
        remove: function () {
            //留给使用者实现
        },
        //修改
        modify: function () {
            //留给使用者实现
        },
        getTree: function (dataUrl, rootText, rootId, icon) {
            var Tree = Ext.tree;
            this.tree = new Tree.TreePanel({
                frame: true,// 美化界面
                split: true, //分隔条
                animate: true,
                autoScroll: true,
                collapsible: true,
                region: 'west',
                width: TreeBaseModel.tree_width ? TreeBaseModel.tree_width : 280,
                loader: new Tree.TreeLoader({
                    timeout: 1800000,
                    dataUrl: dataUrl
                }),
                containerScroll: false,
                border: false,
                rootVisible: rootId != "root"
            });

            // set the root node
            this.root = new Tree.AsyncTreeNode({
                text: rootText,
                iconCls: icon,
                draggable: false, // disable root node dragging
                id: rootId
            });
            this.tree.setRootNode(this.root);
            this.tree.on('click', function (node, event) {
                TreeBaseModel.onClick(node, event);
            });
            //根节点装载完毕后，自动选择刚装载的节点
            this.root.reload(function () {
                if (this.afterLoad) {
                    this.afterLoad();
                } else {
                    this.root.expand(false, true);
                    this.onClick(this.root.childNodes[0]);
                }
            }, this);
            return this.tree;
        },
        //选择节点
        onClick: function (node, event) {

        }
    }
}();

/**下拉树
 * 用法: new TreeSelector(_id,_nodeText,_url,_rootNodeID,_rooNodeText,_label,_field,_anchor,_justLeaf); 返回类型为Ext.form.ComboBox
 * @param {} _id:下拉树的ID，用于提交nodeText的表单字段
 * @param {} _nodeText:下拉树初始显示内容
 * @param {} _url:读取下拉树JSON数据的URL,JSON数据的格式是Ext.tree.TreeLoader能接收的数据格式
 * @param {} _rootNodeID:树根节点ID，当_rootNodeID!="root"时显示根节点
 * @param {} _rooNodeText:树根节点文本
 * @param {} _label:是该下拉树的fieldLabel
 * @param {} _field:修改域的ID,根据此ID把其树结点的id的值赋给该域
 * @param {} _anchor:anchor
 * @param {} _justLeaf:为true表示下拉树只能选择叶子节点，其他值表示可选所有节点
 * @param {} _cfg:自定义ComboBox的一些属性
 */
var TreeSelector = function (_id, _nodeText, _url, _rootNodeID, _rooNodeText,
                             _label, _field, _anchor, _justLeaf, _cfg) {

    var config = {
        id: _id,
        store: new Ext.data.SimpleStore({
            fields: [],
            data: [[]]
        }),
        editable: false,
        mode: 'local',
        cls: 'attr',
        fieldLabel: _label,
        emptyText: "请选择",
        triggerAction: 'all',
        anchor: _anchor,
        maxHeight: 280,
        tpl: "<tpl for='.'><div style='height:280px'><div id='tree_" + _field
        + "'></div></div></tpl>",
        selectedClass: '',
        onSelect: Ext.emptyFn,
        //以下方法用于修正点击树的加号后，下拉框被隐藏
        onViewClick: function (doFocus) {
            var index = this.view.getSelectedIndexes()[0], s = this.store, r = s
                .getAt(index);
            if (r) {
                this.onSelect(r, index);
            } else if (s.getCount() === 0) {
                this.collapse();
            }
            if (doFocus !== false) {
                this.el.focus();
            }
        }
    };
    //将自定义配置加进来
    config = PubFunc.mergeObject(config, _cfg);
    var comboxWithTree = new Ext.form.ComboBox(config);

    var tree = new Ext.tree.TreePanel({
        id: 'selectTree',
        height: 280,
        autoScroll: true,
        split: true,
        loader: new Ext.tree.TreeLoader({
            url: _url
        }),
        root: new Ext.tree.AsyncTreeNode({
            id: _rootNodeID,
            text: _rooNodeText,
            expanded: true
        }),
        rootVisible: _rootNodeID != "root"
    });

    tree.on('click', function (node, e) {
        var editField = Ext.getCmp(_field);//根据要修改的域的ID取得该域
        if (node.id != null && node.id != '') {
            if (_justLeaf && !node.isLeaf()) {
                //如果指定只能选叶子节点，当在点击了非叶子节点时没有反应
            } else {
                var index = node.text.indexOf("<span>");
                var selectNode = "";
                if (index != -1) {
                    selectNode = node.text.substring(6, node.text.length - 7);
                } else {
                    selectNode = node.text;
                }
                comboxWithTree.setValue(selectNode);
                comboxWithTree.id = node.id;
                comboxWithTree.collapse();
                if (editField) {
                    editField.setValue(node.id); //把树结点的值赋给要修改的域
                }
            }
        }

    });
    comboxWithTree.on('expand', function () {
        tree.render('tree_' + _field);
        tree.getRootNode().expand(false, true);
    });
    comboxWithTree.setValue(_nodeText);
    return comboxWithTree
};

/**
 下拉树 用法: new
 * TreeSelector(_id,_nodeText,_url,_rootNodeID,_rooNodeText,_label,_field,_anchor,_justLeaf);
 * 返回类型为Ext.form.ComboBox
 *
 * @param {}
 *            _id:下拉树的ID，用于提交nodeText的表单字段
 * @param {}
 *            _nodeText:下拉树初始显示内容
 * @param {}
 *            _url:读取下拉树JSON数据的URL,JSON数据的格式是Ext.tree.TreeLoader能接收的数据格式
 * @param {}
 *            _rootNodeID:树根节点ID，当_rootNodeID!="root"时显示根节点
 * @param {}
 *            _rooNodeText:树根节点文本
 * @param {}
 *            _label:是该下拉树的fieldLabel
 * @param {}
 *            _field:修改域的ID,根据此ID把其树结点的id的值赋给该域
 * @param {}
 *            _anchor:anchor
 * @param {}
 *            _justLeaf:为true表示下拉树只能选择叶子节点，其他值表示可选所有节点
 * @param {}
 *            _callback:选择时执行callback,返回值为所选节点node对象,不必须
 * @param allb
 * @returns {*}
 * @constructor
 */
var TreeSelectorNew = function (_id, _nodeText, _url, _rootNodeID, _rooNodeText,
                             _label, _field, _anchor, _justLeaf, _callback, _width, allb) {

    var config = {
        id: _id,
        store: new Ext.data.SimpleStore({
            fields: [],
            data: [[]]
        }),
        editable: false,
        mode: 'local',
        fieldLabel: _label,
        labelStyle: allb ? '' : 'color: red;',
        emptyText: "请选择",
        triggerAction: 'all',
        allowBlank: allb,
        anchor: _anchor,
        maxHeight: 280,
        listWidth: 300,
        blankText: _label + '不能为空',
        tpl: "<tpl for='.'><div style='height:280px'><div id='tree_" + _field + "'></div></div></tpl>",
        selectedClass: '',
        onSelect: Ext.emptyFn,
        // 以下方法用于修正点击树的加号后，下拉框被隐藏
        onViewClick: function (doFocus) {
            var index = this.view.getSelectedIndexes()[1];
            var s = this.store;
            var r = s.getAt(index);
            if (r) {
                this.onSelect(r, index);
            } else if (s.getCount() === 0) {
                this.collapse();
            }
            if (doFocus !== false) {
                //this.el.focus();  不注释掉这句，输入框无法获取焦点
            }
        }
    };
    if (_width != undefined) {
        config.width = _width;
    }
    var comboxWithTree = new Ext.form.ComboBox(config);

    var tree = new Ext.tree.TreePanel({
        id: 'selectTree',
        height: 280,
        autoScroll: true,
        split: true,
        loader: new Ext.tree.TreeLoader({
            url: _url
        }),
        root: new Ext.tree.AsyncTreeNode({
            id: _rootNodeID,
            text: _rooNodeText,
            expanded: true
        }),
        rootVisible: _rootNodeID != "root"
    });

    tree.on('click', function (node, e) {
        var editField = Ext.getCmp(_field); // 根据要修改的域的ID取得该域
        if (node.id != null && node.id != '') {
            //点击时返回所选节点
            if (_callback) {
                _callback(node);
            }
            if (_justLeaf && !node.isLeaf()) {
                // 如果指定只能选叶子节点，当在点击了非叶子节点时没有反应
            } else {
                var text = node.text;
                if (text.indexOf('</span>') != -1) {
                    text = text.split('</span>')[1];
                }
                comboxWithTree.setValue(text);
                comboxWithTree.id = node.id;
                comboxWithTree.collapse();
                if (editField) {
                    editField.setValue(node.id); // 把树结点的值赋给要修改的域
                }
            }
        }

    });

    tree.getLoader().on('load', function (t, n, res) {
        if (res.responseJSON == null) {
            parent.Ext.ux.Toast.msg('操作提示', res.responseText);
        }
    })

    var tbar = new Ext.Toolbar({
        buttonAlign: 'left',
        items: [{xtype: 'textfield', emptyText: '模糊搜索...', id: 'filter_input', width: 280, cls: 'attr'}]
    });

    var border = new Ext.Panel({
        layout: 'fit',
        border: false,
        height: 280,
        tbar: tbar,
        items: tree
    });

    comboxWithTree.on('expand', function () {
        border.render('tree_' + _field);
        tree.getRootNode().expand(true, true);
        PubFunc.filterTree(tree, 'filter_input'); //过滤树
    });
    comboxWithTree.setValue(_nodeText);
    return comboxWithTree;
};

var IframeTreeSelector = function (_id, _nodeText, _url, _rootNodeID,
                                   _rooNodeText, _label, _field, _anchor, _justLeaf) {

    var config = {
        id: _id,
        store: new Ext.data.SimpleStore({
            fields: [],
            data: [[]]
        }),
        editable: false,
        mode: 'local',
        fieldLabel: _label,
        emptyText: "请选择",
        triggerAction: 'all',
        anchor: _anchor,
        maxHeight: 280,
        tpl: "<tpl for='.'><div style='height:280px'><div id='tree_" + _field
        + "'></div></div></tpl>",
        selectedClass: '',
        onSelect: Ext.emptyFn,
        //以下方法用于修正点击树的加号后，下拉框被隐藏
        onViewClick: function (doFocus) {
            var index = this.view.getSelectedIndexes()[0], s = this.store, r = s
                .getAt(index);
            if (r) {
                this.onSelect(r, index);
            } else if (s.getCount() === 0) {
                this.collapse();
            }
            if (doFocus !== false) {
                this.el.focus();
            }
        }
    };
    var comboxWithTree = new Ext.form.ComboBox(config);

    var tree = new Ext.tree.TreePanel({
        id: 'selectTree',
        height: 280,
        autoScroll: true,
        split: true,
        loader: new Ext.tree.TreeLoader({
            url: _url
        }),
        root: new Ext.tree.AsyncTreeNode({
            id: _rootNodeID,
            text: _rooNodeText,
            expanded: true
        }),
        rootVisible: _rootNodeID != "root"
    });

    tree.on('click', function (node, e) {
        var editField = Ext.getCmp(_field);//根据要修改的域的ID取得该域
        if (node.id != null && node.id != '') {
            if (_justLeaf && !node.isLeaf()) {
                //如果指定只能选叶子节点，当在点击了非叶子节点时没有反应
            } else {
                comboxWithTree.setValue(node.text);
                comboxWithTree.id = node.id;
                comboxWithTree.collapse();
                if (editField) {
                    editField.setValue(node.id); //把树结点的值赋给要修改的域
                }
            }
        }

    });
    comboxWithTree.on('expand', function () {
        tree.render('tree_' + _field);
        tree.getRootNode().expand(false, true);
    });
    comboxWithTree.setValue(_nodeText);
    return comboxWithTree
};

/**
 * 通用模型，主要用来提交表单数据
 */
CommonModel = function () {
    return {
        getLabelWidth: function () {
            return 80;
        },
        getButtonAlign: function () {
            return "center";
        },
        getAnchor: function () {
            return '100%';
        },
        getForm: function (items, buttons, keys) {
            var labelWidth = this.getLabelWidth();
            var buttonAlign = this.getButtonAlign();
            var anchor = this.getAnchor();
            var frm = new Ext.form.FormPanel({
                labelAlign: 'left',
                buttonAlign: buttonAlign,
                bodyStyle: 'padding:5px',
                frame: true,//圆角和浅蓝色背景
                labelWidth: labelWidth,
                autoScroll: true,

                defaults: {
                    anchor: anchor
                },

                items: items,

                buttons: buttons,
                keys: keys ? keys : this.getKey()
            });
            return frm;
        },

        getKey: function () {
            var key = [{
                key: Ext.EventObject.ENTER,
                fn: function () {
                    this.submit();
                },
                scope: this
            }];
            return key;
        },

        getDialog: function (title, iconCls, width, height, items, buttons, keys) {
            this.frm = this.getForm(items, buttons, keys);
            var dlg = new Ext.Window({
                title: title,
                iconCls: iconCls,
                width: width,
                height: height,
                maximizable: true,
                plain: true,
                closable: true,
                frame: true,
                layout: 'fit',
                border: false,
                modal: true,
                items: [this.frm]
            });
            return dlg;
        },

        show: function (title, iconCls, width, height, items, buttons, keys) {
            this.dlg = this.getDialog(title, iconCls, width, height, items, buttons, keys);
            this.dlg.show();
            this.reset();
        },

        reset: function () {
            this.frm.form.reset();
        },

        close: function () {
            this.dlg.close();
        },

        formIsValid: function () {
            if (this.frm.getForm().isValid()) {
                return true;
            }
            return false;
        },

        shouldSubmit: function () {
            return true;
        },

        prepareSubmit: function () {

        },

        submit: function () {
            if (this.formIsValid()) {
                if (this.shouldSubmit()) {
                    this.prepareSubmit();
                    this.submitCreate(this.frm.form);
                }
            } else {
                parent.Ext.ux.Toast.msg('操作提示：', '有必填项未填写完成,请核对.');
            }
        },

        //提交数据
        submitCreate: function (form) {
            form.submit({
                waitTitle: '请稍等',
                waitMsg: '正在' + CommonModel.dlg.title + '……',
                url: this.submitUrl,

                success: function (form, action) {
                    GridBaseModel.search = false;
                    var model = eval('(' + action.response.responseText + ')');
                    parent.Ext.ux.Toast.msg('操作提示：', model.message);
                    CommonModel.close();
                    GridBaseModel.refresh();
                    CommonModel.createSuccess(form, action);
                },
                failure: function (form, action) {
                    var model = eval('(' + action.response.responseText + ')');
                    parent.Ext.ux.Toast.msg('操作提示：', model.message);
                    GridBaseModel.refresh();
                }
            });
        },
        createSuccess: function (form, action) {
            //回调，留给使用者实现
            return;
        }
    };
}();

/**
 * 全局数据初始化
 * 省市区数据字典初始化
 */
InitData = function () {
    /*provinceStore = new Ext.data.Store({ //省
        proxy: new parent.Ext.data.HttpProxy({
            url: contextPath + '/basicData/address!store.action?fatherBm=0'
        }),
        reader: new Ext.data.JsonReader({}, Ext.data.Record.create([{
            name: 'value'
        }, {
            name: 'text'
        }]))
    });
    provinceStore.load();
    cityStore = new Ext.data.Store({ //市
        proxy: new parent.Ext.data.HttpProxy({
            url: ''
        }),
        reader: new Ext.data.JsonReader({}, Ext.data.Record.create([{
            name: 'value'
        }, {
            name: 'text'
        }]))
    });
    areaStore = new Ext.data.Store({ //区
        proxy: new parent.Ext.data.HttpProxy({
            url: ''
        }),
        reader: new Ext.data.JsonReader({}, Ext.data.Record.create([{
            name: 'value'
        }, {
            name: 'text'
        }]))
    });
    timeStore = ["8:00", "9:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00",
        "0:00", "1:00", "2:00", "3:00", "4:00", "5:00", "6:00", "7:00"]; //时间控件Store
    monthStore = ["1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"]; //月份控件Store
    packageTypeStore = new Ext.data.Store({ //包装材料类型
        proxy: new parent.Ext.data.HttpProxy({
            url: ''
        }),
        reader: new Ext.data.JsonReader({}, Ext.data.Record.create([{
            name: 'value'
        }, {
            name: 'text'
        }]))
    });
    thermometerStore = new Ext.data.Store({ //温度计类型
        proxy: new parent.Ext.data.HttpProxy({
            url: contextPath + '/chargeCenter/chargedefined!store.action?dictype=温度计'
        }),
        reader: new Ext.data.JsonReader({}, Ext.data.Record.create([{
            name: 'value'
        }, {
            name: 'text'
        }]))
    });*/
}();

/**
 * GridConfig 表格配置保存和查询
 */
GridConfig = function () {
    return {
        //表格设置查询
        queryGridConfig: function (whichGrid) {
            var url = contextPath + '/extgridconfig/gridconfig!query.action';
            $.ajax({
                type: 'POST',
                url: url,
                async: false,
                data: {
                    whichGrid: whichGrid
                },
                success: function (response, options) {
                    if (response != '') {
                        var model = {};
                        var data = response.gridSetting;
                        model = eval('(' + data + ')');
                        var gridconfig = {};
                        gridconfig.gridSetting = model;
                        gridconfig.pageSize = Number(response.size);
                        GridConfig.gridconfig = gridconfig;
                    } else {
                        GridConfig.gridconfig = {};
                    }
                },
            });
            return this.gridconfig;
        },
        //表格设置保存
        saveGridSet: function (grid, whichGrid) {
            var config = [];
            var oldGridConfig = grid.colModel.config;
            var length = oldGridConfig.length;
            for (var i = 0; i < length; i++) {
                if (oldGridConfig[i].dataIndex == "" || oldGridConfig[i].dataIndex == undefined || oldGridConfig[i].header == "数据所属单位") {
                    continue;
                }
                var configItem = {};
                configItem.dataIndex = oldGridConfig[i].dataIndex;
                configItem.header = oldGridConfig[i].header;
                configItem.sortable = oldGridConfig[i].sortable;
                configItem.width = oldGridConfig[i].width;
                configItem.hidden = oldGridConfig[i].hidden;
                config.push(configItem);
            }
            var gridSetting = JSON.stringify(config); // 可以将json对象转换成json字符串
            var pageSize = grid.bottomToolbar.pageSize;
            parent.Ext.Ajax.request({
                url: contextPath + '/extgridconfig/gridconfig!save.action',
                waitTitle: '请稍等',
                waitMsg: '正在保存表格设置……',
                method: 'POST',
                params: {
                    gridSetting: gridSetting,
                    size: pageSize,
                    whichGrid: whichGrid
                },
                success: function (response, options) {
                    parent.Ext.ux.Toast.msg('操作提示：', '保存成功');
                },
                failure: function (response, options) {
                    parent.Ext.ux.Toast.msg('操作提示：', '保存失败');
                }
            });
        },
        //表格设置清除
        cleanGridSet: function (whichGrid) {
            parent.Ext.Ajax.request({
                url: contextPath + '/extgridconfig/gridconfig!clean.action',
                waitTitle: '请稍等',
                waitMsg: '正在清除表格设置……',
                method: 'POST',
                params: {
                    whichGrid: whichGrid
                },
                success: function (response, options) {
                    parent.Ext.ux.Toast.msg('操作提示：', '清除成功');
                },
                failure: function (response, options) {
                    parent.Ext.ux.Toast.msg('操作提示：', '清除失败');
                }
            });
        }
    }
}();

/**
 * 公用弹出表格
 * 搜索赋值
 */
QueryGridWindow = function () {
    return {
        // 取得所选的数据的特定字段
        getField: function (field) {
            var recs = this.grid.getSelectionModel().getSelections();
            /*var rec = rec[0].get(field); 只能取到表格显示的数据*/
            var rec = recs[0].json[field]; //取值为store中的值,不只是表格显示的数据
            return rec;
        },
        // 数据源
        getStore: function (fields, pageSize) {
            if (undefined == this.initUrlParams) {
                this.initUrlParams = "";
            }
            // 定义数据集对象
            var store = new Ext.data.Store({
                reader: new Ext.data.JsonReader({
                    totalProperty: 'totalProperty',
                    root: 'root'
                }, Ext.data.Record.create(fields)),
                proxy: new parent.Ext.data.HttpProxy({
                    url: contextPath + '/' + this.querynamespace + '/'
                    + this.queryaction + '!query.action' + QueryGridWindow.initUrlParams
                })
            });
            store.on('beforeload', function (store) {
                store.baseParams = {
                    propertyCriteria: QueryGridWindow.queryString
                };
            });
            store.load({
                params: {
                    limit: pageSize
                }
            });
            return store;
        },
        saveGridSet: function () {
            var gridName = "querywin/" + QueryGridWindow.queryaction;
            GridConfig.saveGridSet(QueryGridWindow.grid, gridName);
        },
        cleanGridSet: function () {
            var gridName = "querywin/" + QueryGridWindow.queryaction;
            GridConfig.cleanGridSet(gridName);
        },
        // 底部工具条
        getBBar: function (pageSize, store) {
            return new Ext.ux.PageSizePlugin({
                rowComboSelect: true,
                pageSize: pageSize,
                store: store,
                displayInfo: true,
                saveGridSet: this.saveGridSet,
                cleanGridSet: this.cleanGridSet
            });
        },
        //表格组装
        getGrid: function (columns, fields) {
            var fields = GridBaseModel.addField(fields, this.queryaction);

            var pageSize = 20;

            //表格设置查询
            var gridConfig = GridConfig.queryGridConfig("querywin/"
                + this.queryaction);
            var gridSetting = gridConfig.gridSetting;
            var size = gridConfig.pageSize;
            if (gridSetting != '' && gridSetting != undefined && size != '') {
                columns = GridBaseModel.mergeGridCfg(columns, gridSetting);
                pageSize = size;
            }
            //添加数据所有者列和空列
            columns = GridBaseModel.addColumn(columns, this.queryaction);

            this.store = this.getStore(fields, pageSize);
            this.bbar = this.getBBar(pageSize, this.store);
            var preColumns = [// 配置表格列
                new Ext.grid.RowNumberer({
                    header: '行号',
                    width: 40
                }),// 表格行号组件
            ];
            this.tbar = [];
            var sm = null;
            columns = preColumns.concat(columns);
            //设置表格默认样式
            GridBaseModel.setDefaultCss(columns);
            var grid = new Ext.grid.GridPanel({
                store: this.store,
                loadMask: true,
                autoScroll: true,
                stripeRows: true,
                bbar: this.bbar,
                minColumnWidth: 80,
                viewConfig: {
                    loadingText: '数据加载中,请稍等...',
                    emptyText: '无对应信息',
                    deferEmptyText: true,
                    autoFill: true,
                    //forceFit:true
                },
                tbar: this.tbar,
                sm: sm,
                columns: columns,
                enableKeyEvents: true
            });
            grid.on('rowdblclick', function (grid, index, e) {
                var seleteedRow = grid.store.data.items[index];
                QueryGridWindow.onRowDblClick(seleteedRow);
            });
            return grid;
        },
        beforeOptComplete: function (selectedRow) {
            return true;
        },
        //赋值完成下一步操作
        afterOptComplete: function (seleteedRow) {

        },
        //表格双击赋值操作
        onRowDblClick: function (seleteedRow) {
            if (this.idList.length != this.colList.length) {
                console.log("idList长度与colList长度不同,不能操作.");
                return;
            }
            for (var i = 0; i < this.idList.length; i++) {
                Ext.getCmp(this.idList[i]).setValue(
                    this.getField(this.colList[i]));
            }
            this.afterOptComplete(seleteedRow);
            this.win.close();
        },
        //表格单击回调函数
        onRowClick: function (selectedRow) {

        },
        refresh: function (likequeryString) {
            this.store.on('beforeload', function (store) {
                store.baseParams = {
                    likeQueryValue: likequeryString,
                    propertyCriteria: QueryGridWindow.queryString
                };
            });
            this.store.load({
                params: {
                    limit: this.bbar.pageSize
                }
            });
        },
        getForm: function () {
            var frm = new Ext.form.FormPanel({
                frame: true,
                border: false,
                layout: 'table',
                items: [
                    {
                        width: 80,
                        layout: 'form',
                        border: false,
                        items: [{
                            fieldLabel: '模糊查询',
                        }]
                    },
                    {
                        xtype: 'textfield',
                        cls: 'attr',
                        width: 200,
                        emptyText: '输入搜索条件',
                        listeners: {
                            'specialkey': function (field, e, opt) {
                                if (e.keyCode == 13) {
                                    QueryGridWindow.refresh(Ext.getCmp(
                                        field.id).getValue());
                                }
                            },
                            'afterrender': function (field, e) {//默认打开搜索窗聚焦
                                field.focus(false, 30);
                            }
                        }
                    }]
            });
            return frm;
        },

        beforeShow: function (grid, frm, win) {

        },

        getQueryWin: function (title) {
            var win = new Ext.Window({
                title: title === undefined ? '查询结果' : '查询结果---<span style="color: red;">' + title + '</span>',
                modal: true,
                height: 500,
                width: 1200,
                layout: 'border',
                items: [{
                    region: 'north',
                    layout: 'fit',
                    height: 40,
                    items: [this.frm]
                }, {
                    region: 'center',
                    layout: 'fit',
                    items: [this.grid]
                }],
                listeners: {
                    close: function () {
                        //窗体关闭后恢复默认处理
                        QueryGridWindow.beforeOptComplete = function (selectedRow) {
                            return true;
                        };
                        QueryGridWindow.afterOptComplete = function () {
                        };
                        QueryGridWindow.onRowClick = function (selectedRow) {
                        };
                        QueryGridWindow.beforeShow = function (grid, frm, win) {
                        };
                    }
                }
            });
            return win;
        },

        /**
         * @params querymodule 模块名 决定要查询哪个表格的columns fields namespace action
         * @params queryString 表格store查询的过滤条件
         * @params idList 要赋值的id数组
         * @params colList 表格store中要取的值,与idList中的参数一一对应
         * @params initUrlParams URL参数
         */
        show: function (querymodule, queryString, idList, colList, initUrlParams, title) {
            var gridObj = GridInfo.get(querymodule);
            var columns = gridObj.columns;
            var fields = gridObj.fields;
            this.querynamespace = gridObj.namespace;
            this.queryaction = gridObj.action;
            this.queryString = queryString;
            this.initUrlParams = initUrlParams;
            this.idList = idList;
            this.colList = colList;
            this.title = title

            this.grid = this.getGrid(columns, fields);
            this.frm = this.getForm();
            this.win = this.getQueryWin(this.title);
            this.beforeShow(this.grid, this.frm, this.win);
            this.win.show();
        }
    }
}();

/**
 * 百度地图
 */
BDMap = function () {
    return {
        /**
         * @params
         * address: 打开地图的默认初始位置
         * addressIds(obj): 要输入的详情地址和经纬度id集合
         * winId: 打开地图后要移动的Window的id
         */
        show: function (address, addressIds, winId) {
            this.addressIds = addressIds;
            this.parentWinId = winId;
            this.parentWinX = Ext.getCmp(winId).x;
            this.parentWinY = Ext.getCmp(winId).y;
            Ext.getCmp(winId).setPosition(0, this.parentWinY, false);

            new Ext.Window({
                id: 'mapWin',
                title: '在线地图',
                height: document.body.clientHeight,
                width: document.body.clientWidth * 2 / 3,
                x: document.body.clientWidth / 3,
                y: 0,
                modal: true,
                layout: 'fit',
                items: [{
                    html: '<iframe id="frame" name="frame" src="'
                    + contextPath + '/platform/js/map.html?address='
                    + address + '&detail=' + addressIds.detail
                    + '&lng=' + addressIds.longitude + '&lat='
                    + addressIds.latitude
                    + '" width=100% height=100%/>'
                }],
                maximizable: true,
                listeners: {
                    'close': function () {
                        BDMap.close();
                    }
                }
            }).show();
        },
        close: function () {
            Ext.getCmp(this.parentWinId).setPosition(this.parentWinX,
                this.parentWinY, true); //还原移动位置
            Ext.getCmp(this.addressIds.detail).validate(); //表单验证警告
        }
    }
}();


Request = function () {
    return {
        /**
         * Ext异步请求数据query查询
         * @params querymodule 请求的模块名
         * @params queryString 搜索条件
         * @params s_callback 成功回调
         * @params f_callback 失败回调
         */
        getInfo: function (querymodule, queryString, s_callback, f_callback, initUrlParams) {
            var gridObj = GridInfo.get(querymodule);
            var namespace = gridObj.namespace;
            var action = gridObj.action;
            if (namespace == undefined || action == undefined) {
                parent.Ext.ux.Toast.msg('操作提示：', '所请求的模块在gridInfo中未配置');
                console.log('所请求的模块在gridInfo中未配置');
                return;
            }
            if (initUrlParams == undefined) {
                initUrlParams = "";
            }

            Ext.Ajax.request({
                url: contextPath + '/' + namespace + '/' + action + '!popupQuery.action?' + initUrlParams,
                waitTitle: '请稍等',
                waitMsg: '正在检索数据……',
                method: 'POST',
                params: {
                    propertyCriteria: queryString
                },
                success: function (response, options) {
                    var data = response.responseJSON;
                    if (s_callback)
                        s_callback(data);
                },
                failure: function (response, options) {
                    var data = response.responseText;
                    parent.Ext.ux.Toast.msg('操作提示：', '{0}', data);
                    if (f_callback)
                        f_callback(response);
                }
            });
        },
        /**
         * jquery同步请求数据
         * 只做查询作用 只能查询返回为一条数据的情况
         * example: url = contextPath + '/basicData/address-data!popupQuery.action?propertyCriteria=id:eq:' + id;
         * popopQuery不会因为权限隔离而不能访问
         */
        get: function (url) {
            var failure = function () {
                parent.Ext.ux.Toast.msg('操作提示', '数据请求不正常');
            };
            $.ajax({
                type: 'POST',
                url: url,
                async: false,
                success: function (response, options) {
                    try {
                        if (response != '') {
                            if (response.root.length == 1)
                                Request.model = response.root[0];
                            else
                                failure();
                        } else {
                            failure();
                        }
                    } catch (e) {
                        failure();
                    }
                }
            });
            return this.model;
        },
        /**
         * 打印页面
         * @params url
         */
        printShow: function (url) {
            new parent.Ext.Window({
                title: '打印',
                height: document.body.clientHeight,
                width: document.body.clientWidth,
                modal: true,
                frame: true,
                closable: true,
                resizable: false,
                maximizable: false,
                layout: 'fit',
                items: [{
                    html: '<iframe name="frame" src=' + url + ' width=100% height=100%/>'
                }]
            }).show();
        }
    }
}();



