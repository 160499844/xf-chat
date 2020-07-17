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
	public static String encrypt(String str,String key) throws InvalidAlgorithmParameterException {
		String string = "";
		string = SymmetricEncoder.encrypt(str,key);
		return string;
	}
	
	
	/**
	 * 解密
	 * @param str 需要解密的字符串
	 * @return
	 * @throws InvalidAlgorithmParameterException
	 */
	public static String decrypt(String str,String key) throws InvalidAlgorithmParameterException {
		String string = "";
		string = SymmetricEncoder.decrypt(str,key);
		return string;
	}
}
