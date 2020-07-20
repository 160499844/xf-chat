package com.xiaofeng.utils;

import org.apache.commons.codec.binary.Base64;
import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RSAEncrypt {
	
	public static final String PUBLICKEY_STRING = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCO04XgnPuf0vZzxEyus/5rxKbSf0ra5+PMD7K6T+xpIcYdy6roWP+1xw6IYeSPjtL0brk/jAHIYRIr6oN/JLX8g3K1641CL9fkfeoJ2mfF7xjDTFEY/LwGDF0aSxulAjp7TPKNFiQzkSi0eAP3yaynEFcuh7p3u1NgdBZ827nmwQIDAQAB";
	public static final String PRIVATE_STRING = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAI7TheCc+5/S9nPETK6z/mvEptJ/Strn48wPsrpP7Gkhxh3LquhY/7XHDohh5I+O0vRuuT+MAchhEivqg38ktfyDcrXrjUIv1+R96gnaZ8XvGMNMURj8vAYMXRpLG6UCOntM8o0WJDORKLR4A/fJrKcQVy6Hune7U2B0FnzbuebBAgMBAAECgYEAh6rMxGrZaRtPFvAjlt9KkLufatuXExCqBS/0KZ25hrOaKLtJgKLpYpXObCXwoZydLanvNTmch+YNCMIeXitU4TP2rBmMBSBEMEDdlD4SdFwaoCab5OLrOd809VbdZHqvbX7VxoXIjUcQpqyZSOsbE1BzWE4fnIJ285qUmjFD0kECQQDeB8Nv4t6Z9hgsN1H7xmHLqDbjk/jSZO+dc3dTmQPjOH1Wi8mUEs3JE4qrosEodVoZe0KPv3nmsbvO97Y9N2L9AkEApK2VOzNNPwZDlqVbXEJFf9bg2sgImTUy6dOPNkT67w78eQcwF25fatLmAXpoThCiiQybxb6lD9FvrAtlO1hoFQJAD3/dPc9JmNfcIiIOgBP+ObhZdJOc2Bshuw0XdGeHJPKJlWQWw1Z4tSO/F3I6J7fhfUvkfqJwQRGBOdaDC0z1hQJAKh2+0dIwClNWy9tkfzbvwV86SJnqIQzLBaQ2t0FzD9q8VVOyR/vRaWegrXYZ4QU+HGDeIfe9DUuaDN0pOcFFdQJBALN++ShTtf1aAtVKQ9vJCvuO8xjb3ubXg0aZsCZweKYoc6ozSKBjJZ4xOLW1qunpQ1VdvqXrQ7a/3AFvcRt+x8s=";
	
	private static Map<Integer, String> keyMap = new ConcurrentHashMap<Integer, String>();  //用于封装随机产生的公钥与私钥
	
	
	public static void main(String[] args) throws Exception {
		//生成公钥和私钥
		genKeyPair();
		//加密字符串
		String message = "测试";
		System.out.println("随机生成的公钥为:" + keyMap.get(0));
		System.out.println("随机生成的私钥为:" + keyMap.get(1));
		String messageEn = encrypt(message,keyMap.get(0));
		System.out.println(message + "\t加密后的字符串为:" + messageEn);
		String messageDe = decrypt(messageEn,keyMap.get(1));
		System.out.println("还原后的字符串为:" + messageDe);
	}

	/** 
	 * 随机生成密钥对 
	 * @throws NoSuchAlgorithmException 
	 */  
	public static void genKeyPair() throws NoSuchAlgorithmException {  
		// KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象  
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");  
		// 初始化密钥对生成器，密钥大小为96-1024位  
		keyPairGen.initialize(1024,new SecureRandom());  
		// 生成一个密钥对，保存在keyPair中  
		KeyPair keyPair = keyPairGen.generateKeyPair();  
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();   // 得到私钥  
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  // 得到公钥  
		String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));  
		// 得到私钥字符串  
		String privateKeyString = new String(Base64.encodeBase64((privateKey.getEncoded())));  
		// 将公钥和私钥保存到Map
		keyMap.put(0,publicKeyString);  //0表示公钥
		keyMap.put(1,privateKeyString);  //1表示私钥
	}  
	/** 
	 * RSA公钥加密 
	 *  
	 * @param str 
	 *            加密字符串
	 * @param publicKey 
	 *            公钥 
	 * @return 密文 
	 * @throws Exception 
	 *             加密过程中的异常信息 
	 */  
	public static String encrypt( String str, String publicKey ) throws Exception{
		//base64编码的公钥
		byte[] decoded = Base64.decodeBase64(publicKey);
		RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
		//RSA加密
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, pubKey);
		String outStr = Base64.encodeBase64String(cipher.doFinal(str.getBytes("UTF-8")));
		return outStr;
	}

	/** 
	 * RSA私钥解密
	 *  
	 * @param str 
	 *            加密字符串
	 * @param privateKey 
	 *            私钥 
	 * @return 铭文
	 * @throws Exception 
	 *             解密过程中的异常信息 
	 */  
	public static String decrypt(String str, String privateKey) throws Exception{
		//64位解码加密后的字符串
		byte[] inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
		//base64编码的私钥
		byte[] decoded = Base64.decodeBase64(privateKey);  
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));  
		//RSA解密
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, priKey);
		String outStr = new String(cipher.doFinal(inputByte));
		return outStr;
	}

}
