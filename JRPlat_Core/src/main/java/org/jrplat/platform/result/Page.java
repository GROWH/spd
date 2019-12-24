/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.platform.result;

import org.jrplat.platform.log.JRPlatLogger;
import org.jrplat.platform.log.JRPlatLoggerFactory;
import org.jrplat.platform.model.Model;
import org.jrplat.platform.util.XMLFactory;

import javax.xml.bind.annotation.*;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@XmlType(name = "Page")
public class Page<T extends Model> implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final JRPlatLogger LOG = JRPlatLoggerFactory.getJRPlatLogger(Page.class);

    private long totalRecords = 0;
    private List<T> models = new ArrayList<>();

    public static <T extends Model> Page<T> newInstance(Class<T> modelClass, InputStream in) {
        XMLFactory factory = new XMLFactory(Page.class, modelClass);
        try {
            return factory.unmarshal(in);
        } catch (Exception e) {
            LOG.error("生成对象出错", e);
        }
        return null;
    }

    public String toXml() {
        XMLFactory factory = new XMLFactory(Page.class, getModelClass());
        String xml = null;
        try {
            xml = factory.marshal(this);
        } catch (Exception e) {
            LOG.error("生成XML出错", e);
        }
        return xml;
    }

    private Class getModelClass() {
        if (models.size() > 0) {
            return models.get(0).getClass();
        }
        return null;
    }

    @XmlTransient
    public long getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(long totalRecords) {
        this.totalRecords = totalRecords;
    }

    @XmlElementWrapper(name = "models")
    @XmlElement(name = "model")
    public List<T> getModels() {
        return models;
    }

    public void setModels(List<T> models) {
        this.models = models;
    }
}