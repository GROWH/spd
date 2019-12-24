/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.security.action;

import org.apache.struts2.convention.annotation.Namespace;
import org.jrplat.module.security.service.SecurityCheck;
import org.jrplat.platform.action.DefaultAction;
import org.jrplat.platform.util.FileUtils;
import org.jrplat.platform.util.Struts2Utils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 *
 * @author 西安捷然
 */
@Scope("prototype")
@Controller
@Namespace("/security")
public class ActiveAction extends DefaultAction {
    private String licence;

    public String buy() {

        return null;
    }

    public String active() {
        FileUtils.createAndWriteFile("/WEB-INF/classes/licences/jrplat.licence", licence);
        SecurityCheck.check();
        if (FileUtils.existsFile("/WEB-INF/licence")) {
            Struts2Utils.renderText("您的注册码不正确，激活失败！");
        } else {
            Struts2Utils.renderText("激活成功，感谢您的购买！");
        }
        return null;
    }

    public void setLicence(String licence) {
        this.licence = licence;
    }
}