package org.jrplat.module.commodityInfo.action;

import org.apache.struts2.convention.annotation.Namespace;
import org.jrplat.module.commodityInfo.model.Commodity;
import org.jrplat.module.config.model.Gridconfig;
import org.jrplat.platform.action.ExtJSSimpleAction;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * @Author:wangkang
 * @Date: 2021/1/21 下午3:08
 */

@Scope("prototype")
@Controller
@Namespace("/commodityInfo")
public class CommodityAction extends ExtJSSimpleAction<Commodity>  {


}
