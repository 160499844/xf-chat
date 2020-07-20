package com.xiaofeng.web.handle;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiaofeng.utils.Result;
import com.xiaofeng.utils.exception.BaseException;
import com.xiaofeng.web.controller.FileController;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @ClassName: ExceptionHandle
 * @Description:全局异常处理
 * @author: 小峰
 * @date: 2020年7月20日 上午9:46:18
 */
@Slf4j
@ControllerAdvice
public class ExceptionHandle {
	@ExceptionHandler(value = Exception.class)
	@ResponseBody
	public Result handle(Exception e) {
		if (e instanceof BaseException) {
			BaseException myException = (BaseException) e;
			return new Result(myException.getCode(), myException.getMessage());
		} else {
			log.error("系统异常", e);
			return  new Result(-1, "系统异常!");
		}
	}
}
