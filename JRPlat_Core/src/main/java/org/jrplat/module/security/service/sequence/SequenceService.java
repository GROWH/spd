/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.security.service.sequence;

/**
 *生成机器码的接口，不同平台有不同实现
 * @author 西安捷然
 */
public interface SequenceService {

    /**
     * 获取机器码
     * @return 机器码
     */
    public String getSequence();

}