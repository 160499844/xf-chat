package com.xiaofeng.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.xiaofeng.entity.Group;
import com.xiaofeng.entity.GroupToken;
import com.xiaofeng.global.GroupContext;
import com.xiaofeng.utils.RSAEncrypt;
import com.xiaofeng.utils.Result;
import com.xiaofeng.utils.aes.AESUtils;
import com.xiaofeng.utils.exception.BaseException;

import lombok.extern.log4j.Log4j;
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
	/**
	 * 校验群 组密码
	 * 
	 * @param groupId
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "/checkPassword", method = RequestMethod.POST)
	public Result<Boolean> checkPassword(String groupId, String password) {
		boolean c = false;
		//GroupToken groupToken = GroupContext.GROUP_KEYS.get(groupId);
		Group group = GroupContext.GROUPS.get(groupId);
		if(group==null) {
			//小组不存在,直接返回
			return new Result<Boolean>(c);
		}
		GroupToken groupToken = group.getToken();
		//校验小组口令
		if (groupToken != null && groupToken.getKey().equals(password)) {
			c = true;
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
		String url = projectPattern + "index.html?code=";// 分享的链接
		// 创建群组信息
		String encrypt = RSAEncrypt.encrypt(groupId, RSAEncrypt.PUBLICKEY_STRING);
		url += com.xiaofeng.utils.string.StringUtils.encodeBase64String(encrypt.getBytes());
		GroupToken groupToken = new GroupToken(password, AESUtils.generateDesKey(), encrypt);
//		GroupContext.GROUP_KEYS.put(groupId,groupToken );
		//创建小组
		Group group = new Group();
		group.setGroupId(groupId);
		group.setToken(groupToken);
		group.setGroupName(groupName);
		GroupContext.GROUPS.put(groupId,group);
		return new Result<String>(url);
	}

	/**
	 * 加入群组
	 * 
	 * @param groupId
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(String groupId, String username) {
		// 小组人数+1
		GroupContext.groupAddCount(groupId);

	}

	/**
	 * 解密编码
	 * 
	 * @param code
	 * @throws Exception
	 */
	@RequestMapping(value = "/getGroupInfo")
	public Result<Map<String, String>> getGroupInfo(String code) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		try {
			byte[] decodeBase64 = com.xiaofeng.utils.string.StringUtils.decodeBase64(code);
			code = new String(decodeBase64, "utf-8");
			log.info("解密:" + code);
			String groupId = RSAEncrypt.decrypt(code, RSAEncrypt.PRIVATE_STRING);
			//GroupToken groupToken = GroupContext.GROUP_KEYS.get(groupId);
			Group group = GroupContext.GROUPS.get(groupId);
			GroupToken groupToken = group.getToken();
			// 返回小组公钥
			map.put("key", groupToken.getAesKey());
			map.put("n", groupId);
			// 返回小组aeskey
		} catch (Exception e) {
			log.error("解密失败{}",e.getMessage());
		}
		return new Result<Map<String, String>>(map);
	}
}
