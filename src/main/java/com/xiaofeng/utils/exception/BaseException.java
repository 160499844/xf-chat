package com.xiaofeng.utils.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @ClassName:  BaseException   
 * @Description:自定义异常基类  
 * @author: 小峰
 * @date:   2020年7月20日 上午8:53:56
 */
@Getter
@Setter
public class BaseException extends RuntimeException {

    private static final long serialVersionUID = 4801559114427165218L;

    private int code = ResultStatus.FAIL;

    /**
     * 异常情况携带的信息 (非错误信息 errMsg)
     */
    private Object content;


    public BaseException(String message) {
        this(null, null, message, null);
    }

    public BaseException(Throwable cause) {
        this(null, null, null, cause);
    }

    public BaseException(int code, String message) {
        this(code, null, message, null);
    }

    public BaseException(int code, Throwable cause) {
        this(code, null, null, cause);
    }

    public BaseException(int code, Object content, String message) {
        this(code, content, message, null);
    }

    private BaseException(Integer code, Object content, String message, Throwable cause) {
        super(message, cause);
        if (code != null) {
            this.code = code;
        } else {
            this.code = ResultStatus.FAIL;
        }
        this.content = content;
    }

}