/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.module.monitor.service;

import org.jdom.Element;
import org.jrplat.platform.service.SingleService;
import org.jrplat.platform.util.SortUtils;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author 西安捷然
 */
@Service
public class ProcessTimeSingleService extends SingleService {

    public String getXML(LinkedHashMap<String, Long> data, boolean sort) {
        //创建根元素
        Element rootElement = createRootElement("", "");

        //创建sets
        createSets(data, rootElement, sort);

        //格式化输出，将对象转换为XML
        String xml = formatXML(rootElement);

        return xml;
    }

    /**
     *
     * @param data
     * @param rootElement
     * @param sort 根据VALUE排序
     */
    private void createSets(LinkedHashMap<String, Long> data, Element rootElement, boolean sort) {
        if (sort) {
            //根据VALUE排序
            Map.Entry[] entrys = SortUtils.getSortedMapByValue(data);
            for (Map.Entry<String, Long> entry : entrys) {
                Element element = createSet(entry.getKey(), entry.getValue());
                rootElement.addContent(element);
            }
        } else {
            //如果不根据VALUE排序，则根据KEY排序
//            Collection<String> keys=data.keySet();
//            List<String> list=new ArrayList<String>();
//            for(String key : keys){
//                list.add(key);
//            }
//            Collections.sort(list);
            for (String key : data.keySet()) {
                Element element = createSet(key, data.get(key));
                rootElement.addContent(element);
            }
        }
    }
}