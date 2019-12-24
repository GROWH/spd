/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.platform.log.handler;

import org.jrplat.platform.action.converter.DateTypeConverter;
import org.jrplat.platform.model.Model;
import org.jrplat.platform.util.FileUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 *
 * @author 西安捷然
 */
@Service
public class FileLogHandler implements LogHandler {
    private static int count = 1;

    @Override
    public <T extends Model> void handle(List<T> list) {
        StringBuilder str = new StringBuilder();
        for (T t : list) {
            str.append(count++).append(":\n").append(t.toString());
        }
        FileUtils.createAndWriteFile("/WEB-INF/logs/log-" + DateTypeConverter.toDefaultDateTime(new Date()).replace(" ", "-").replace(":", "-") + ".txt", str.toString());
    }
}
