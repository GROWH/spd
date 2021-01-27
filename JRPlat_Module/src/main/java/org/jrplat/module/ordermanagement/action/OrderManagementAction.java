package org.jrplat.module.ordermanagement.action;

import org.apache.struts2.convention.annotation.Namespace;
import org.jrplat.module.ordermanagement.model.OrderManagement;
import org.jrplat.platform.action.ExtJSSimpleAction;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * @Author:wangkang
 * @Date: 2021/1/21 下午3:20
 */
@Scope("prototype")
@Controller
@Namespace("/ordermanagement")
public class OrderManagementAction extends ExtJSSimpleAction<OrderManagement>{
}
