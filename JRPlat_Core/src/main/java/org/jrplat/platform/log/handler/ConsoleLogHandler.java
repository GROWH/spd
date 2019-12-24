/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.platform.log.handler;

import org.jrplat.platform.model.Model;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *将日志输出到控制台
 * @author 西安捷然
 */
@Service
public class ConsoleLogHandler implements LogHandler {
    private static int count = 1;

    @Override
    public <T extends Model> void handle(List<T> list) {
        for (T t : list) {
            System.out.println((count++) + ":");
            System.out.println(t.toString());
        }
    }

}
