package com.xiaofeng.utils;

import java.security.InvalidAlgorithmParameterException;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import com.alibaba.fastjson.JSONObject;

/**
 * ' 字符串处理工具类
 * 
 * @author xiaofeng
 *
 */
public class StringUtils {

	/**
	 * base64编码
	 * 
	 * @param b
	 * @return
	 */
	public static String encodeBase64String(byte[] b) {
		Encoder encoder = Base64.getEncoder();
		byte[] data = encoder.encode(b);
		String result = new String(data);
		//System.out.println("BASE64加密：" + result);
		return result;
	}
	/**
	 * base64解码
	 * @param content
	 * @return
	 */
	public static byte[] decodeBase64(String content) {
		// BASE64解密
		Decoder decoder = Base64.getDecoder();
		byte[] bytes = decoder.decode(content);
		return bytes;
	}
	/**
	 * 
	 * @Title: toJsonDecode   
	 * @Description:  解密消息
	 * @param: @param content
	 * @param: @return      
	 * @return: MessageVo      
	 * @throws
	 */
	public static MessageVo toJsonDecode(String content,String key) {
		String result = "";
		MessageVo messageVo = null;
		try {
			result = EncryptMessage.decrypt(content,key);
			messageVo = JSONObject.parseObject(result,MessageVo.class);
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}
		return messageVo;
	}
	/**
	 * 
	 * @Title: toDecodeJson   
	 * @Description: 将对象加密转成字符串
	 * @param: @param obj
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	public static String toJsonEncrypt(Object obj,String key) {
		String result = "";
		String jsonString = JSONObject.toJSONString(obj);
		try {
			result = EncryptMessage.encrypt(jsonString,key);
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}
		return result;
	}
}
