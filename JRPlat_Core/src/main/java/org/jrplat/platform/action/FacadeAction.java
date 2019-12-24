/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.platform.action;

import org.apache.struts2.convention.annotation.Namespace;
import org.jrplat.platform.model.Model;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 *
 *
 * @author 西安捷然
 */
@Controller
@Scope("prototype")
@Namespace("/web")
public class FacadeAction extends SimpleAction<Model> implements Action {
}