package com.xiaofeng.web.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xiaofeng.netty.server.DynMessage;
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
		if(StringUtils.isEmpty(groupId)) {
			groupId = DynMessage.DEFAULT_GROUP;
		}
		pushService.pushMessage(groupId, txt);
		
		return Result.of("推送成功");
	}
	
}
