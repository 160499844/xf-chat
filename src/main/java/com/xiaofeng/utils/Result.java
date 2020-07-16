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
public class Result extends MessageVo implements Serializable{

	private String content;
	private Map<String,Object> info = new HashMap<String, Object>();
	
	public static Result of(String content) {
		Result r = new Result();
		r.content = content;
		return r;
		
	}
}
