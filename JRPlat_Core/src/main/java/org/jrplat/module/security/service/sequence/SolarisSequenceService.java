/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.security.service.sequence;

/**
 *在Solaris平台上生成机器码
 * @author 西安捷然
 */
public class SolarisSequenceService extends AbstractSequenceService {
    public static void main(String[] args) {
        SequenceService s = new SolarisSequenceService();
        String seq = s.getSequence();
        System.out.println(seq);
    }

    @Override
    public String getSequence() {
        return getSigarSequence("solaris");
    }
}