package com.xiaofeng.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xiaofeng.utils.Result;
import com.xiaofeng.web.service.PushService;

/**
 * 消息推送
 * @author xiaofeng
 *
 */
@RestController
@RequestMapping("web")
public class PushController {

	@Autowired
	private PushService pushService;
	
	@RequestMapping("push")
	public Result pushTxt(String groupId,String txt) {
		pushService.pushMessage(groupId, txt);
		
		return Result.of("推送成功");
	}
	
}
