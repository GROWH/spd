package org.jrplat.module.ordermanagement.ws;

/**
 * Created by ztf on 5/5/17.
 */
public final class ResultCode {
    //成功
    public static final int SUCCESS = 0;
    //未知错误
    public static final int UNKNOWN_ERROR = 999;
    //登录验证失败
    public static final int LOGIN_ERROR = 1;
    //未授权调用
    public static final int NOT_ALLOW_INVOKE = 2;
    //json 格式错误
    public static final int JSON_FORMAT_ERROR = 3;
    //审批通过 不允许修改
    public static final int NOT_ALLOW_UPDATE = 4;
    //该数据　未同步
    public static final int NOT_SYN = 5;

}
