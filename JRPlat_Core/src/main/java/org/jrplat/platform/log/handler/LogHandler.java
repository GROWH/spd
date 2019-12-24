/**
 * JRPlat - JR Development Platform
 * Copyright (c) 2014, 西安捷然, jierankeji@163.com
 */

package org.jrplat.platform.log.handler;

import org.jrplat.platform.model.Model;

import java.util.List;

/**
 * 日志处理接口:
 * 可将日志存入独立日志数据库（非业务数据库）
 * 可将日志传递到activemq\rabbitmq\zeromq等消息队列
 * 可将日志传递到kafka\flume\chukwa\scribe等日志聚合系统
 * 可将日志传递到elasticsearch\solr等搜索服务器
 * @author 西安捷然
 */
public interface LogHandler {
    public <T extends Model> void handle(List<T> list);
}