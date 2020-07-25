package com.xiaofeng.web.service;

import java.security.InvalidAlgorithmParameterException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiaofeng.entity.Group;
import com.xiaofeng.entity.GroupToken;
import com.xiaofeng.netty.server.DynMessage;
import com.xiaofeng.utils.EncryptMessage;
import com.xiaofeng.utils.MessageVo;
import com.xiaofeng.utils.exception.BaseException;
import com.xiaofeng.web.repository.GroupRepository;

import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class PushService {
	
	
	@Autowired
	private GroupRepository groupRepository;
	@Autowired
	private GroupService groupService;
	/**
	 * 推送消息
	 * 
	 * @param txt
	 * @throws InvalidAlgorithmParameterException 
	 */

	public void pushMessage(String groupId, String txt)  {
		if(!StringUtils.isEmpty(groupId)) {
			
			// 组装返回对象
			MessageVo messageVo = new MessageVo();
			//Integer groupCount = GroupContext.getGroupCount(groupId);
			Integer groupCount = groupService.getGroupCount(groupId);
			messageVo.put("group_count", groupCount);// 当前在线人数
			//GroupToken groupToken = GroupContext.GROUPS.get(groupId).getToken();
			//log.info(String.format("广播消息(%s)开始广播", groupId));
			Group findByGroupId = groupRepository.findByGroupId(groupId);
			if(findByGroupId!=null) {
				
				GroupToken groupToken = findByGroupId.getToken();
				if(groupToken!=null) {
					String msg = "";
					try {
						msg = EncryptMessage.encrypt(txt, groupToken.getAesKey());
					} catch (InvalidAlgorithmParameterException e) {
						log.error("广播aes加密失败!");
						e.printStackTrace();
					}
					messageVo.setContent(msg);
					messageVo.setGroupId(groupId);
					messageVo.setType("S");
					messageVo.setName("系统消息");
					messageVo.setMsg(msg);
					//log.info(String.format("广播消息(%s):%s", groupId,txt));
					DynMessage.broadcast(groupId, com.xiaofeng.utils.string.StringUtils.toJson(messageVo));
				}else {
					throw new BaseException("操作失败,组标识无效!");
				}
			}else {
				log.info(String.format("广播消息(%s)广播失败", groupId));
			}
		}
	}

}
