package com.xiaofeng.utils.exception;

/**
 * 
 * @ClassName:  ResultStatus   
 * @Description: 返回状态码   
 * @author: 小峰
 * @date:   2020年7月20日 上午8:53:42
 */
@SuppressWarnings("WeakerAccess")
public class ResultStatus {

    /**
     * 正常
     */
    public static final int OK = 0;

    /**
     * 已知错误
     */
    public static final int FAIL = 1;

    /**
     * 未知错误
     */
    public static final int SERVICE_ERROR = -1;

    /**
     * 已知业务错误（由业务代码自己处理）
     */
    public static final int SERVICE_SERVICE_ERROR = -100;

}
