package com.xiaofeng.utils.aes;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.codec.binary.Base64;

import com.xiaofeng.netty.server.NettyServer;
import com.xiaofeng.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/*
 * AES对称加密和解密
 */
@Slf4j
public class AESUtils {

	//private static String key = "1538663015386630";
	//private static String key = generateDesKey();

	/**
	 * AES 解密操作
	 *
	 * @param content
	 * @param key
	 * @return
	 */
	public static String decrypt(String content,String key) {
		SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
		try {
			// 实例化
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

			// 使用密钥初始化，设置为解密模式
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);

			// 执行操作
			byte[] result = cipher.doFinal(StringUtils.decodeBase64(content));

			return new String(result, "utf-8");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	/**
	 * AES 加密操作
	 *
	 * @param content 待加密内容
	 * @param key     加密密码
	 * @return 返回Base64转码后的加密数据
	 */
	public static String encrypt(String content,String key) {
		try {
			SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");// 创建密码器

			byte[] byteContent = content.getBytes("utf-8");

			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);// 初始化为加密模式的密码器

			byte[] result = cipher.doFinal(byteContent);// 加密

			return StringUtils.encodeBase64String(result);// 通过Base64转码返回
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}


	/**
	 * 字符串转化成为16进制字符串
	 * 
	 * @param s
	 * @return
	 */
	public static String strTo16(String s) {
		String str = "";
		for (int i = 0; i < s.length(); i++) {
			int ch = (int) s.charAt(i);
			String s4 = Integer.toHexString(ch);
			str = str + s4;
		}
		return str;
	}

	/**
	 * 将16进制转换为二进制
	 *
	 * @param hexStr
	 * @return
	 */
	private static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1) {
			return null;
		}
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}

	/**
	 * 16进制直接转换成为字符串(无需Unicode解码)
	 * 
	 * @param hexStr
	 * @return
	 */
	public static String hexStr2Str(String hexStr) {
		String str = "0123456789ABCDEF";
		char[] hexs = hexStr.toCharArray();
		byte[] bytes = new byte[hexStr.length() / 2];
		int n;
		for (int i = 0; i < bytes.length; i++) {
			n = str.indexOf(hexs[2 * i]) * 16;
			n += str.indexOf(hexs[2 * i + 1]);
			bytes[i] = (byte) (n & 0xff);
		}
		return new String(bytes);
	}
	/**
	 * 
	 * @Title: generateDesKey   
	 * @Description: 生成key
	 * @param: @param length 长度
	 * @param: @return
	 * @param: @throws Exception      
	 * @return: byte[]      
	 * @throws
	 */
	public static String generateDesKey() {
		String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		int length = 16;
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(str.length());
            sb.append(str.charAt(number));
        }
        String key = sb.toString();
        System.out.println("随机密钥:"+key);
        return key;  
    }
	
	/*public static void main(String[] args) {
		String encrypt = encrypt("测试");
		System.out.println("加密后:" + encrypt);
		String decrypt = decrypt(encrypt);
		System.out.println("解密后:" + decrypt);
	}*/
	
}
