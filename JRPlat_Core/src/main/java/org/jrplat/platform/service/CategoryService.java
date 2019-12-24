/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.platform.service;

import org.jdom.Attribute;
import org.jdom.Element;

/**
 *
 * @author 西安捷然
 */
public abstract class CategoryService extends ChartService {
    protected Element createCategory(String categoryName) {
        Element element = new Element("category");
        element.setAttribute(new Attribute("label", categoryName));
        return element;
    }

    protected Element createDataset(Integer value) {
        Element element = new Element("set");
        element.setAttribute(new Attribute("value", value.toString()));
        return element;
    }
}