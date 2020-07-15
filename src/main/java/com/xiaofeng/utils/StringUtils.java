package com.xiaofeng.utils;

import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

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
		System.out.println("BASE64加密：" + result);
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
}
