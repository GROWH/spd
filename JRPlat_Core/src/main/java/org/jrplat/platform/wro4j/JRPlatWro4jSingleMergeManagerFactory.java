package org.jrplat.platform.wro4j;


import ro.isdc.wro.manager.factory.standalone.DefaultStandaloneContextAwareManagerFactory;

/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

/**
 *
 * @author 西安捷然
 */
public class JRPlatWro4jSingleMergeManagerFactory extends DefaultStandaloneContextAwareManagerFactory {

    public JRPlatWro4jSingleMergeManagerFactory() {
        setNamingStrategy(new JRPlatSingleMergeNamingStrategy());
    }
}
