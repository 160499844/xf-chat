package com.xiaofeng.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.xiaofeng.web.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.xiaofeng.entity.Group;
import com.xiaofeng.entity.GroupToken;
import com.xiaofeng.global.GroupContext;
import com.xiaofeng.global.UtilConstants;
import com.xiaofeng.utils.RSAEncrypt;
import com.xiaofeng.utils.RedisUtil;
import com.xiaofeng.utils.Result;
import com.xiaofeng.utils.aes.AESUtils;
import com.xiaofeng.utils.exception.BaseException;
import com.xiaofeng.web.repository.GroupRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: UserController
 * @Description: 组相关
 * @author: 小峰
 * @date: 2020年7月20日 下午3:56:29
 */
@Slf4j
@RestController
@RequestMapping("group")
public class GroupController {

	//项目地址
	@Value("${project.pattern}")
	private String projectPattern;
	@Value("${project.websocket.pattern}")
	private String projectWebSocketPattern;
	@Autowired
	private GroupRepository groupRepository;
	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private UserService userService;
	

	/*@RequestMapping(value = "/test")
	public Result<List<Group>> test() {
		Group group = new Group();
		String groupId = com.xiaofeng.utils.string.StringUtils.getUUID();
		group.setGroupId(groupId);
		group.setGroupName("测试");
		GroupToken token = new GroupToken("123", AESUtils.generateDesKey(), "fdsfdf");
		group.setToken(token);
		groupRepository.saveGroup(group);
		Group findGroup = groupRepository.findByGroupId(groupId);
		return new Result<List<Group>>(findGroup);
	}*/
	/**
	 * 校验群 组密码
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "/checkPassword", method = RequestMethod.POST)
	public Result<Boolean> checkPassword(String password,HttpServletRequest request) {
		//判断当前登录用户是否点击过分享链接,如果点击过session中会有群组groupId
		HttpSession session = request.getSession();
		if(session==null) {
			throw new BaseException("当前会话已过期!");
		}
		Object attribute = session.getAttribute(UtilConstants.SESSION_GROUP_ID);
		String groupId = "";
		if(attribute!=null) {
			groupId= (String) attribute;
		}else {
			throw new BaseException("当前会话已过期!");
		}
		boolean c = false;
		//GroupToken groupToken = GroupContext.GROUP_KEYS.get(groupId);
		//Group group = GroupContext.GROUPS.get(groupId);
		Group group = groupRepository.findByGroupId(groupId);
		if(group==null) {
			//小组不存在,直接返回
			return new Result<Boolean>(c);
		}
		GroupToken groupToken = group.getToken();
		//校验小组口令
		if (groupToken != null && groupToken.getKey().equals(password)) {
			c = true;
			//添加标志
			session.setAttribute(UtilConstants.SESSION_GROUP_PASSWORD, password);
			//更新用户信息
			//String userName = (String) session.getAttribute(UtilConstants.SESSION_USER_NAME);
			//userService.UpdateKeyValues(userName,"groupId",groupId);
			//userService.UpdateKeyValues(userName,"userName",userName);
		}
		//返回结果
		return new Result<Boolean>(c);
	}

	/**
	 * 创建群组
	 * 
	 * @param groupId
	 * @param password
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public Result<String> create(String groupId,String groupName, String password) throws Exception {
		if(StringUtils.isEmpty(password) || password.length()!=6 || !StringUtils.isNumeric(password)) {
			throw new BaseException("请输入6位数字口令");
		}
		if(StringUtils.isEmpty(groupId) || groupId.length()>10) {
			throw new BaseException("群组名称不合法");
		}
		String url = projectPattern + UtilConstants.SESSION_GROUP_INDEX_URL;// 分享的链接
		try {
			//判断群组是否存在，如果群组存在不可以创建，如果存在并且人数为零可以创建
			Group exGroup = groupRepository.findByGroupId(groupId);
			//Integer currentCount = exGroup.getCurrentCount();
			if(exGroup!=null) {
				throw new BaseException("该群聊已经存在!");
			}
		} catch (Exception e) {
			
		}
		
		
		// 创建群组信息
		String encrypt = RSAEncrypt.encrypt(groupId, RSAEncrypt.PUBLICKEY_STRING);
		url += com.xiaofeng.utils.string.StringUtils.encodeBase64String(encrypt.getBytes());
		GroupToken groupToken = new GroupToken(password, AESUtils.generateDesKey(), encrypt);
		groupToken.setLink(url);
//		GroupContext.GROUP_KEYS.put(groupId,groupToken );
		//创建小组
		Group group = new Group();
		group.setGroupId(groupId);
		group.setToken(groupToken);
		group.setGroupName(groupName);
		groupRepository.saveGroup(group);
		//GroupContext.GROUPS.put(groupId,group);
		return new Result<String>(url);
	}


	/**
	 * 解密编码
	 * 
	 * @param code
	 * @throws Exception
	 */
	@RequestMapping(value = "/getGroupInfo")
	public Result<Map<String, String>> getGroupInfo(String code,HttpServletRequest request) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		HttpSession session = request.getSession();
		try {
			byte[] decodeBase64 = com.xiaofeng.utils.string.StringUtils.decodeBase64(code);
			code = new String(decodeBase64, "utf-8");
			log.info("解密:" + code);
			String groupId = RSAEncrypt.decrypt(code, RSAEncrypt.PRIVATE_STRING);
			//GroupToken groupToken = GroupContext.GROUP_KEYS.get(groupId);
		//	Group group = GroupContext.GROUPS.get(groupId);
			Group group = groupRepository.findByGroupId(groupId);
			GroupToken groupToken = group.getToken();
			//验证密码
			Object password = session.getAttribute(UtilConstants.SESSION_GROUP_PASSWORD);
			Object userNameObj = session.getAttribute(UtilConstants.SESSION_USER_NAME);


			if(password==null || userNameObj==null || !groupToken.getKey().equals(password.toString())){
				throw new BaseException("没有权限进入");
			}
			// 返回小组公钥
			map.put("key", groupToken.getAesKey());
			map.put("n", groupId);
			map.put("userName",userNameObj.toString());
			map.put("websocket", projectWebSocketPattern + UtilConstants.NettyConfigConstants.HOST_PATH);
			session.setAttribute(UtilConstants.SESSION_GROUP_AES_KEY, groupId);
			// 返回小组aeskey
		} catch (Exception e) {
			log.error("解密失败{}",e.getMessage());
		}
		return new Result<Map<String, String>>(map);
	}
	/**
	 * 
	 * @Title: getGroupName   
	 * @Description:获取小组基本资料
	 * @param: @param code
	 * @param: @param request
	 * @param: @return
	 * @param: @throws Exception      
	 * @return: Result<Map<String,String>>      
	 * @throws
	 */
	@RequestMapping(value = "/getGroupName")
	public Result<Map<String, String>> getGroupName(String code,HttpServletRequest request) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		HttpSession session = request.getSession();
		try {
			byte[] decodeBase64 = com.xiaofeng.utils.string.StringUtils.decodeBase64(code);
			code = new String(decodeBase64, "utf-8");
			//log.info("解密:" + code);
			String groupId = RSAEncrypt.decrypt(code, RSAEncrypt.PRIVATE_STRING);
			map.put("n", groupId);
			//存到session中
			session.setAttribute(UtilConstants.SESSION_GROUP_ID, groupId);
		} catch (Exception e) {
			log.error("解密失败{}",e.getMessage());
		}
		return new Result<Map<String, String>>(map);
	}
}
