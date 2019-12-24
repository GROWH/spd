//重写number的toFixed，实现精准的四舍五入
Number.prototype.toFixed=function (len) {
    var val = this+"";
    if(val.indexOf(".")==-1){
        val = val+".";
    }
    var len_d = val.length-1 - val.indexOf(".");//原数据小数位数

    /*************step1： 对原始数据进行精度还原***********************************/
    /* 因为js自身浮点运算后可能导致精度误差，会导致小数点后会出现类似xxx.xxxx999999x的情况，例如：200.1234*500*0.95=95058.61499999999（实际为95058.615）
    /* 95058.61499999999 四舍五入保留2位就出现错误了
    /* 这种情况下，我们定义如下规则：如果小数位数>=11位(误差出现小数位基本都大于等于11位)，我们就在最后一位加上对应的数值去还原原始数值
    /* 比如：95058.61499999997  我们就给该数值加上0.00000000003去还原
    /* 误差的另一种情况是0.1+0.2=0.30000000000000004，这种情况后续的四舍五入之后就没有影响了，所以可以忽略不处理
    /**************************************************************************/
    if(len_d>=11 && val.indexOf("9999", val.indexOf(".")!=-1)){
        var lastnum = val.substr(val.length-1,1);
        val = Number(val) + Math.pow(10,0-len_d)*(10-Number(lastnum));
        val = val+"";
        //还原原始数据的精度后，重新计算其小数位数
        if(val.indexOf(".")==-1){
            val = val+".";
        }
        len_d = val.length-1 - val.indexOf(".");//原数据小数位数
    }

    /*************step2： 对还原后的数据根据保留位数进行四舍五入***********************************/
    if(len_d<=len){
        //原数据小数位数<=需要保留的位数; 补0
        var add_len = len - len_d;
        for(i=0;i<add_len;i++)
        {
            val=val+"0";
        }
    }else{
        var num = Number(val.substr(val.indexOf(".")+len+1,1));//需要保留位数的下一位数字
        //原数据小数位数>需要保留的位数; 四舍五入
        if(num<5){
            val = val.substr(0,val.indexOf(".")+len+1);
        }else{
            val = val.substr(0,val.indexOf(".")+len+1);
            val = Math.round(Number(val)*Math.pow(10,len)) + 1;  //Number(val)*Math.pow(10,len)--理论上是整数,因为js自身的bug，通过四舍五入保证数字正确
            val = val/Math.pow(10,len);
        }
    }
    return val+"";
};

/*
 * 通用搜索方法 by 梁栋 2017.3.2
 * 调用举例：可参照【折扣管理】的调用
 * */
Pub_search = function () {
    return {
        getTableItems: function (columns, elements) {
            var publicField = [{
                type: "date",
                name: "创建时间起",
                field: "createTime",
                opt: "ge"
            }, {
                type: "date",
                name: "创建时间止",
                field: "createTime",
                opt: "le"
            }, {
                name: "创建人账号",
                field: "ownerUser.username",
                opt: "like"
            }];
            Pub_search.elements = Pub_search.publicField?elements.concat(publicField):elements;
            var table = {
                layout: {
                    type: 'table',
                    columns: columns * 2,
                    tableAttrs: {　　//在这儿控制table标签中的Attrs属性
                        border: 0,
                        width: '100%',
                        style: "border:0px solid gray;border-collapse:collapse; margin:0 auto;  /*background:#fff;*/"
                    }
                },
                defaults: { // 设置每个item的默认设置
                    xtype: 'label',
                    cls: 'attr',
                    // height: 25,
                    width: 220,
                    style: "margin:0px 0px 10px 5px; text-align:left; font-size:14px;"
                },
                items: []
            };

            for (var i = 0; i < Pub_search.elements.length; i++) {
                var item = Pub_search.elements[i];
                var title = {html: "<div style='text-align:right; padding-bottom: 10px;'>" + item.name + ":<div>"};
                var ele = "";
                if (item.store != undefined) {
                    ele = {
                        xtype: 'combo',
                        emptyText: '请选择',
                        mode: 'remote',
                        valueField: 'text',
                        displayField: 'text',
                        triggerAction: 'all',
                        forceSelection: true,
                        editable: false,
                        store: item.store
                    }
                } else if (item.type == 'date') {
                    ele = {
                        xtype: 'datefield',
                        //format: "Y-m-d H:i:s",
                        format:"Y-m-d",
                        editable: true
                    }
                } else if (item.type == 'number' && item.dbtype == "string") {
                    ele = {
                        xtype: 'numberfield',
                        decimalPrecision: 3,
                        minValue: 0
                    }
                } else if (item.type == 'number' && item.dbtype == "double") {
                    ele = {
                        xtype: 'numberfield',
                        decimalPrecision: 3,
                        minValue: 0
                    }
                } else if (item.type == 'number' && item.dbtype == "int") {
                    ele = {
                        xtype: 'numberfield',
                        decimalPrecision: 0,
                        minValue: 0
                    }
                } else {
                    ele = {
                        xtype: 'textfield'
                    }
                }
                ele.id = Pub_search.idpre + i;
                table.items.push(title);
                table.items.push(ele);
            }

            var all = [];
            all.push(table);
            return all;

        },
        getMap: function (index, ele, item) {
            var map_str = {
                'like': ':like:%' + ele + '%',
                'eq': ':eq:$' + ele,
                'ne': ':ne:$' + ele,
                'in': ':in:' + ele,
                'ge': ':ge:$' + ele,
                'gt': ':gt:$' + ele,
                'le': ':le:$' + ele,
                'lt': ':lt:$' + ele
            };
            var map_num = {
                'like': ':like:%' + ele + '%',
                'eq': ':eq:' + ele,
                'ne': ':ne:' + ele,
                'in': ':in:' + ele,
                'ge': ':ge:' + ele,
                'gt': ':gt:' + ele,
                'le': ':le:' + ele,
                'lt': ':lt:' + ele
            };
            if (item.dbtype == "string") {
                return map_str[index];
            } else {
                return map_num[index];
            }
        },
        callback: function () {
            var data = [];
            for (var i = 0; i < Pub_search.elements.length; i++) {
                var item = Pub_search.elements[i];
                var ele1 = Ext.getCmp(Pub_search.idpre + i).getValue();  //用.getValue可以取值文本框，下拉框
                var ele2 = Ext.getCmp(Pub_search.idpre + i).getRawValue();       //用.value可以取值下拉框，日期(.value如果手动删除还是会拿到值)
                var ele = item.type == "date" ? ele2 : ele1;
                if (ele != "" && ele != undefined) {
                    ele = item.field + Pub_search.getMap(item.opt, ele, item);
                    data.push(ele);
                }
            }
            OverwriteAdvancedSearchBaseModel.search(data);
        },
        searchRefresh: function (propertyCriteria) {
            propertyCriteria = Pub_search.addSearchParams(propertyCriteria);
            GridBaseModel.refresh(propertyCriteria);
        },
        addSearchParams: function (propertyCriteria) {//添加额外搜索条件
            return propertyCriteria;
        },
        setCookie: function () {
            for (var i = 0; i < Pub_search.elements.length; i++) {
                var item = Pub_search.elements[i];
                var ele1 = Ext.getCmp(Pub_search.idpre + i).getValue();  //用.getValue可以取值文本框，下拉框
                var ele2 = Ext.getCmp(Pub_search.idpre + i).value;       //用.value可以取值下拉框，日期
                var ele = item.type == "date" ? ele2 : ele1;
                if (ele != "" && ele != undefined && ele != null && ele != 'null' && ele != 'undefined') {
                    Ext.util.Cookies.set(Pub_search.idpre + i, ele);
                }
            }
        },
        getCookie: function () {
            for (var i = 0; i < Pub_search.elements.length; i++) {
                var value = Ext.util.Cookies.get(Pub_search.idpre + i);
                if (Ext.getCmp(Pub_search.idpre + i) != undefined) {
                    Ext.getCmp(Pub_search.idpre + i).setValue(value);
                }
            }
        },
        reset: function () {
            OverwriteAdvancedSearchBaseModel.frm.form.reset();
            for (var i = 0; i < Pub_search.elements.length; i++) {
                Ext.util.Cookies.set(Pub_search.idpre + i, "");
            }
        },
        beforeSearch: function () {
        },
        afterClose: function () {
            GridBaseModel.enableShortcuts();
        },
        //参数： 显示的列数， 所有元素集合 ，后两个参数主要是为了生成唯一的ID在cookies保存
        show: function (col, elements, namespace, action, publicField) {
            Pub_search.publicField = publicField === undefined?true:publicField;
            Pub_search.idpre = "ele_" + namespace + "_" + action;
            var items = Pub_search.getTableItems(col, elements);
            var callback = Pub_search.callback;
            var beforeSearch = Pub_search.beforeSearch;
            var afterClose = Pub_search.afterClose;
            var searchRefresh = Pub_search.searchRefresh;
            var getCookie = Pub_search.getCookie;
            var setCookie = Pub_search.setCookie;
            var height = Pub_search.elements.length / col * 35 + 100;
            OverwriteAdvancedSearchBaseModel.reset = Pub_search.reset;
            OverwriteAdvancedSearchBaseModel.show('高级搜索', 400 * col, height, items, callback, beforeSearch, afterClose, searchRefresh, getCookie, setCookie);
            GridBaseModel.disableShortcuts();
        }

    }
}();

/*
 * 通用调取后台方法 by 梁栋 2017.3.10
 * */
Pub_action = function () {
    return {
        call: function (funcName, namespace, action, moduleMap, callback_success, callback_fail) {
            var time = funcName === 'retrieve'?'?time=' + new Date().toString():'';
            parent.Ext.Ajax.request({
                url: contextPath + '/' + namespace + '/' + action + '!' + funcName + '.action' + time,
                waitTitle: '请稍等',
                waitMsg: '操作进行中……',
                method: 'POST',
                timeout: 1800000,
                params: moduleMap,
                success: function (response, options) {
                    var json = response.responseJSON;
                    var text = response.responseText;
                    var data = (json == null) ? text:json;
                    if (callback_success) {
                        callback_success(data);
                    }
                },
                failure: function (response, options) {
                    var data1 = response.responseText;
                    var data2 = " | 状态：" + response.statusText;
                    parent.Ext.ux.Toast.msg('操作提示：', '{0}', data1 + data2);
                    if (callback_fail) {
                        callback_fail(response);
                    }
                }
            });
        },
        get: function (namespace, action, moduleMap, callback_success, callback_fail) {
            this.call('retrieve', namespace, action, moduleMap, callback_success, callback_fail);
        },
        create: function (namespace, action, moduleMap, callback_success, callback_fail) {
            this.call('create', namespace, action, moduleMap, callback_success, callback_fail);
        },
        update: function (namespace, action, moduleMap, callback_success, callback_fail) {
            this.call('updatePart', namespace, action, moduleMap, callback_success, callback_fail);
        },
        delete: function (namespace, action, moduleMap, callback_success, callback_fail) {
            this.call('delete', namespace, action, moduleMap, callback_success, callback_fail);
        }

    }
}();

/*
 * 通用弹框 by 梁栋 2017.3.10
 * */
Pub_dlg = function () {
    return {
        getTableItems: function (columns, elements) {
            var table = {
                layout: {
                    type: 'table',
                    columns: columns * 2,
                    tableAttrs: {　　//在这儿控制table标签中的Attrs属性
                        border: 0,
                        width: '100%',
                        style: "border:0px solid gray;border-collapse:collapse; margin:0 auto;  /*background:#fff;*/"
                    }
                },
                defaults: { // 设置每个item的默认设置
                    xtype: 'label',
                    cls: 'attr',
                    height: 25,
                    width: 220,
                    style: "margin:0px 0px 10px 5px; text-align:left; font-size:14px;"
                },
                items: []
            };
            for (var i = 0; i < elements.length; i++) {
                var item = elements[i];
                var fontColor = item.require?'red':'#000033';//渲染必填label
                var hidden = (item.hidden != undefined && item.hidden)?'none':'';//隐藏表单实现
                var title = {html: "<div style='text-align:right; padding-bottom: 10px; color: "+fontColor+";display: "+hidden+"'>" + item.name + ":<div>"};
                var ele = "";
                var require = item.require == true ? true : false;
                if (item.store != undefined) {
                    ele = {
                        xtype: 'combo',
                        emptyText: '请选择',
                        mode: 'remote',
                        valueField: 'value',
                        displayField: 'text',
                        triggerAction: 'all',
                        forceSelection: true,
                        editable: false,
                        store: item.store,
                        allowBlank : !require
                    }
                } else if (item.type == 'date') {
                    ele = {
                        xtype: 'datefield',
                        format: item.format == undefined? "Y-m-d H:i:s":"Y-m-d",
                        editable: true,
                        allowBlank : !require
                    }
                } else if (item.type == 'number' && item.dbtype == "string") {
                    ele = {
                        xtype: 'numberfield',
                        decimalPrecision: 3,
                        minValue: 0,
                        allowBlank : !require
                    }
                } else if (item.type == 'number' && item.dbtype == "double") {
                    ele = {
                        xtype: 'numberfield',
                        decimalPrecision: 3,
                        minValue: 0,
                        allowNegative: false,
                        allowBlank : !require
                    }
                } else if (item.type == 'number' && item.dbtype == "int") {
                    ele = {
                        xtype: 'numberfield',
                        decimalPrecision: 0,
                        minValue: 0,
                        allowNegative: false,
                        allowBlank : !require
                    }
                } else if (item.type == 'textarea') {
                    ele = {
                        xtype : 'textarea',
                        height: item.height==undefined? 40:item.height,
                        width: item.width==undefined? 220:item.width,
                        cls:'richtext',
                        allowBlank : !require
                    }
                } else {
                    ele = {
                        xtype: 'textfield',
                        allowBlank : !require
                    }
                }
                if(item.value != undefined){
                    ele.value = item.value;
                }
                if(item.readOnly != undefined){
                    ele.readOnly = item.readOnly;
                    ele.cls = "detail_field";
                }
                if (item.hidden != undefined) {
                    ele.hidden = item.hidden;
                }
                if(item.listeners !== undefined) {//增加监听事件处理
                    ele.listeners = item.listeners;
                    if ('focus' in item.listeners) {
                        ele.cls = "querybg";
                    }
                }
                if (!ele.allowBlank) {//必填项提示
                    ele.blankText = item.name + '不能为空';
                }
                if (item.type == 'number' && !!item.max) {
                    ele.maxValue = Number(item.max);
                }
                ele.id = Pub_dlg.idpre + i;
                table.items.push(title);
                table.items.push(ele);
            }
            var all = [];
            all.push(table);
            return all;
        },
        getDlgData: function () {
            var data = {};
            for (var i = 0; i < this.elements.length; i++) {
                var item = this.elements[i];
                var ele1 = Ext.getCmp(this.idpre + i).getValue();  //用.getValue可以取值文本框，下拉框
                var ele2 = Ext.getCmp(this.idpre + i).value;       //用.value可以取值下拉框，日期
                var ele = item.type == "date" ? ele2 : ele1;
                if (ele !== "" && ele != undefined && item.field != undefined) {
                    data[item.field] = ele;
                }
            }
            return data;
        },
        btn_ok: function () {
            if (this.frm.getForm().isValid()) {
                var data = this.getDlgData();
                if(this.onok){
                    this.onok(data);
                }
                //this.dlg.close();
            } else {
                parent.Ext.ux.Toast.msg('操作提示：', '有必填项未填写完整！');
            }
        },
        btn_cancel: function () {
            var data = this.getDlgData();
            if(this.oncancle){
                this.oncancle(data);
            }
            //this.dlg.close();
        },
        getForm: function () {
            var labelWidth = 120;
            var frm = new Ext.form.FormPanel({
                labelAlign: 'left',
                buttonAlign: 'center',
                bodyStyle: 'padding:5px',
                frame: true,// 圆角和浅蓝色背景
                labelWidth: labelWidth,
                autoScroll: true,
                items: this.items,
                buttons: [{
                    text: '确认',
                    iconCls: 'save',
                    scope: this,
                    handler:this.btn_ok
                }, {
                    text: '取消',
                    iconCls: 'cancel',
                    scope: this,
                    handler:this.btn_cancel
                }]
            });
            return frm;
        },
        getDialog: function (title, width, height, closable) {
            this.frm = this.getForm();
            var dlg = new Ext.Window({
                maximizable: true,
                title: title,
                width: width,
                height: height,
                plain: true,
                closable: closable,
                frame: true,
                layout: 'fit',
                border: false,
                modal: true,
                items: this.frm,
                listeners: {
                    'close': function () {
                        Pub_dlg.afterClose();
                    }
                }
            });
            return dlg;
        },
        afterClose: function () {

        },
        /**
         * 参数： 显示的列数， 所有元素集合 ，后两个参数主要是为了生成唯一的ID在cookies保存
         * @param title 表头
         * @param width 宽度
         * @param height 盖度
         * @param col 列
         * @param elements 表单JSON
         * @param closable 是否可关
         * @param onok 点击确认执行的方法
         * @param oncancle 点击取消执行的方法
         */
        show: function (title, width, height, col, elements, closable, onok, oncancle) {
            this.elements = elements;
            this.onok = onok;
            this.oncancle = oncancle;
            this.idpre = "ele_";
            this.items = this.getTableItems(col, elements);
            this.dlg = this.getDialog(title, width, height, closable);
            this.dlg.show();
        }

    }
}();

Request = function () {
    return{
        queryModel: function (url, propertyCriteria, async) {
            // body...
            $.ajax({
                type : 'POST',
                url : url,
                data: {
                    propertyCriteria: propertyCriteria
                },
                async : async == undefined ? false : true, //false：同步 true:异步
                success : function(response, options) {
                    if (response != "") {
                        Request.model = response.root[0];
                    }
                },
            });
            return this.model;
        },
        /**
         * 只作为查询数据使用 同步查询
         * @param url
         * @param root 0表示返回第一条数据
         * @returns {*}
         */
        get: function (url, root) {
            $.ajax({
                type: 'POST',
                url: url,
                async: false,
                success: function (response, options) {
                    if (response != '') {
                        if (root == undefined) {
                            Request.response = response;
                        } else if (root == 0) {
                            Request.response = response.root[0];
                        } else {
                            Request.response = response.root;
                        }
                    } else {
                        console.log("获取数据失败");
                    }
                },
            });
            return this.response;
        }
    }
} ();


