package com.xiaofeng.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.xiaofeng.global.GroupContext;
import com.xiaofeng.utils.RSAEncrypt;
import com.xiaofeng.utils.Result;
import com.xiaofeng.utils.aes.AESUtils;
import com.xiaofeng.utils.user.GroupToken;

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
		GroupToken groupToken = GroupContext.GROUP_KEYS.get(groupId);
		if (groupToken != null && groupToken.getKey().equals(password)) {
			c = true;
		}
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
	public Result<String> create(String groupId, String password) throws Exception {
		String url = "http://127.0.0.1:8000/index.html?code=";// 分享的链接
		// 创建群组信息
		String encrypt = RSAEncrypt.encrypt(groupId, RSAEncrypt.PUBLICKEY_STRING);
		url += com.xiaofeng.utils.string.StringUtils.encodeBase64String(encrypt.getBytes());
		GroupContext.GROUP_KEYS.put(groupId, new GroupToken(password, AESUtils.generateDesKey(), encrypt));
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
		byte[] decodeBase64 = com.xiaofeng.utils.string.StringUtils.decodeBase64(code);
		code = new String(decodeBase64, "utf-8");
		log.info("解密:" + code);
		Map<String, String> map = new HashMap<String, String>();
		String groupId = RSAEncrypt.decrypt(code, RSAEncrypt.PRIVATE_STRING);
		GroupToken groupToken = GroupContext.GROUP_KEYS.get(groupId);
		// 返回小组公钥
		map.put("key", groupToken.getAesKey());
		map.put("n", groupId);
		// 返回小组aeskey
		return new Result<Map<String, String>>(map);
	}
}
