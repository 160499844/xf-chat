package com.xiaofeng.utils.string;

import java.security.InvalidAlgorithmParameterException;
import java.util.Base64;
import java.util.UUID;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import com.alibaba.fastjson.JSONObject;
import com.xiaofeng.utils.EncryptMessage;
import com.xiaofeng.utils.MessageVo;

/**
 * ' 字符串处理工具类
 * 
 * @author xiaofeng
 *
 */
public class StringUtils extends RandomUtil {

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
		// System.out.println("BASE64加密：" + result);
		return result;
	}

	/**
	 * base64解码
	 * 
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
	 * @Title: toJsonDecode @Description: 解密消息 @param: @param
	 * content @param: @return @return: MessageVo @throws
	 */
	public static MessageVo jsonToMessageVo(String content) {
		MessageVo messageVo = null;
		messageVo = JSONObject.parseObject(content, MessageVo.class);
		return messageVo;
	}

	/**
	 * 
	 * @Title: toDecodeJson @Description: 将对象加密转成字符串 @param: @param
	 * obj @param: @return @return: String @throws
	 */
	public static String toJson(MessageVo obj) {
		String result = "";
		result = JSONObject.toJSONString(obj);
		/*try {
			//String msg = EncryptMessage.encrypt(obj.getMsg(), key);
			//obj.setMsg(msg);
			//result = JSONObject.toJSONString(obj);
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}*/
		return result;
	}

	public static String getUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}
}
