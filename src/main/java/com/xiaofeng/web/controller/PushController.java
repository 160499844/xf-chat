package com.xiaofeng.web.controller;

import java.security.InvalidAlgorithmParameterException;

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
	/**
	 * 
	 * @Title: pushTxt   
	 * @Description: 系统推送
	 * @param: @param groupId 群标识
	 * @param: @param txt 推送文本
	 * @param: @return
	 * @param: @throws InvalidAlgorithmParameterException      
	 * @return: Result      
	 * @throws
	 */
	@RequestMapping("push")
	public Result pushTxt(String groupId,String txt) throws InvalidAlgorithmParameterException {
		if(StringUtils.isEmpty(groupId)) {
			groupId = DynMessage.DEFAULT_GROUP;
		}
		pushService.pushMessage(groupId, txt);
		
		return Result.of("推送成功");
	}
	
}
