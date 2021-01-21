package org.jrplat.module.unitInfo.action;

import org.apache.struts2.convention.annotation.Namespace;
import org.jrplat.module.commodityInfo.model.Commodity;
import org.jrplat.module.unitInfo.model.Unit;
import org.jrplat.platform.action.ExtJSSimpleAction;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * @Author:wangkang
 * @Date: 2021/1/21 下午3:16
 */
@Scope("prototype")
@Controller
@Namespace("/unitinfo")

public class UnitAction extends ExtJSSimpleAction<Unit>{
}
