/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.platform.log.handler;

import org.jrplat.platform.log.JRPlatLogger;
import org.jrplat.platform.log.JRPlatLoggerFactory;
import org.jrplat.platform.model.Model;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author 西安捷然
 */
@Service
public class ScribeLogHandler implements LogHandler {
    private static final JRPlatLogger LOG = JRPlatLoggerFactory.getJRPlatLogger(ScribeLogHandler.class);

    @Override
    public <T extends Model> void handle(List<T> list) {
        LOG.info("还未实现！");
    }

}
