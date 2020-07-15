package com.xiaofeng.utils;

import java.security.InvalidAlgorithmParameterException;

/**
 * 
 * @ClassName:  EncryptMessage   
 * @Description: 加密消息内容 
 * @date:   2020年7月15日 上午11:49:12
 */
public class EncryptMessage {
	/**
	 * @throws InvalidAlgorithmParameterException 
	 * 
	 * @Title: encrypt   
	 * @Description: 加密
	 * @param: @param str 原始消息
	 * @param: @return      
	 * @return: String  加密后的信息
	 * @throws
	 */
	public static String encrypt(String str) throws InvalidAlgorithmParameterException {
		String string = "";
		string = SymmetricEncoder.AESEncode(str);
		return string;
	}
}
