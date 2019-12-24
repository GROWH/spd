/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.platform.util;

import org.jrplat.platform.log.JRPlatLogger;
import org.jrplat.platform.log.JRPlatLoggerFactory;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author 西安捷然
 */
public class SortUtils {
    private static final JRPlatLogger LOG = JRPlatLoggerFactory.getJRPlatLogger(SortUtils.class);

    private SortUtils() {
    }

    ;

    public static Map.Entry[] getSortedMapByValue(Map map) {
        Set set = map.entrySet();
        Map.Entry[] entries = (Map.Entry[]) set.toArray(new Map.Entry[set.size()]);
        Arrays.sort(entries, new Comparator() {

            @Override
            public int compare(Object arg0, Object arg1) {
                Integer key1 = Integer.valueOf(((Map.Entry) arg0).getValue().toString());
                Integer key2 = Integer.valueOf(((Map.Entry) arg1).getValue().toString());
                return key2.compareTo(key1);
            }
        });
        return entries;
    }
}