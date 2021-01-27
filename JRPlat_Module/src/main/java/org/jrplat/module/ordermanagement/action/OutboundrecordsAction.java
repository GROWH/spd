package org.jrplat.module.ordermanagement.action;

import org.apache.struts2.convention.annotation.Namespace;
import org.jrplat.module.ordermanagement.model.Outboundrecords;
import org.jrplat.platform.action.ExtJSSimpleAction;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * @Author:wangkang
 * @Date: 2021/1/27 上午9:22
 */
@Scope("prototype")
@Controller
@Namespace("/ordermanagement")
public class OutboundrecordsAction extends ExtJSSimpleAction<Outboundrecords> {
}
