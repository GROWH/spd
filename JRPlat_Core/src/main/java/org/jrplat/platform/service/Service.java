/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.platform.service;

import org.jrplat.platform.common.Common;
import org.jrplat.platform.model.Model;

import java.util.List;

public interface Service<T extends Model> extends Common<T> {
    public List<Exception> delete(Integer[] modelIds);
}