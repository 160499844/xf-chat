package com.xiaofeng.utils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import lombok.Data;

/**
 * 
 * @ClassName:  Result   
 * @Description: 返回对象
 * @date:   2020年7月15日 上午10:20:09
 */
@Data
public class Result<T> implements Serializable{

	private int code;
	private Object content;
	private Map<String,Object> info = new HashMap<String, Object>();
	
	
	
	
	public static Result of(Object content) {
		Result r = new Result();
		r.content = content;
		r.code = 0;
		return r;
		
	}

	public Result() {
		super();
	}

	public Result(Object content) {
		super();
		this.content = content;
	}
	
	public Result(int code,Object content) {
		super();
		this.code = code;
		this.content = content;
	}
}
