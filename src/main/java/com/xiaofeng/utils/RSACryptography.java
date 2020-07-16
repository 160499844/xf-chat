package com.xiaofeng.utils;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

public class RSACryptography {

	public static String data = "hello world";

	public static void main(String[] args) throws Exception {

		/*
		 * KeyPair keyPair = genKeyPair(1024);
		 * 
		 * // 获取公钥，并以base64格式打印出来 PublicKey publicKey = keyPair.getPublic();
		 * System.out.println("公钥：" + new
		 * String(Base64.getEncoder().encode(publicKey.getEncoded())));
		 * 
		 * // 获取私钥，并以base64格式打印出来 PrivateKey privateKey = keyPair.getPrivate();
		 * System.out.println("私钥：" + new
		 * String(Base64.getEncoder().encode(privateKey.getEncoded())));
		 * 
		 * // 公钥加密 byte[] encryptedBytes = encrypt(data.getBytes(), publicKey);
		 * System.out.println("加密后：" + new String(encryptedBytes));
		 */

		String cipher = "sx2Rwuokxwm6UnnsEjMBUCAee5Lc7lg8K9XU1EYzXmEuBvhyyNxxBwsl25dFridOeYhxuVuMbxIqK1xhroyzb0d+tdFrAalQ6DK+OMaUIHjKfP1v3JIVJyTVE8BdLoTd+982PwPbe0TeKfCq5HYSjqb+5AWv5dYyVyFha4v373+HmWQxG+nTFN2lnlnPBbG0cWO+ykdmtfKRtAyhxJ5uYnWYhFzbbDfExmTMi4rwkJOWH5YmuxRRb6v1o6x32+Fr+WgtCXq6Nkq58epSzz80GPGn4BfOd4mj9wxs62GWj8O6sHm+yBgfNOB+rPE82PGcNKkL7eHoXpHGlY51KTKE2A==";

		String priKey = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDdIFr0qbNFWF3KxzqDNKi7Fof6YaXnGEhW7wA+747duRjjCWKeVzo7hkMYdLSQkdjX8qf0PXGPOmeTumMs5E++mBmZy5gXkEl1Au2sDEgvBunLjZ2dGg+ztBFKIbawpyOycqhFKzNEivvSUU4XTlH1S26ZrLkcU5YWJBqqEe3CndJ+9PbnRtGjw0oHkm0JuwpAMWxY09xXtzKr8IpeJsaN56XFN1ZHtcEh4GGMxNZg7xcGHOUkDwx0a8PD4Rv5OIX/vhiqGgMCuJQqHfqKo5s8oWSBni45x++h7Xk3EmLr4kVqG1HcXC/G6RhYGVOA+CE5phojthfdVMC8GhHNQz3bAgMBAAECggEBAL+zfSb+t9w2b5mMfr2guWb4cYovfZSLCKvVJ3FoXYL7JQgu6sA6/tD75M8e3is3RbZxOAoV+o0hJQp9W91fUYL6ebdLOd+zxvsaPtIZE7sxUx6U9z9riJFSWmraL3eWbuwVWqwcd6XEl+wNcJXj4Em3y3qOsyxLiqJHSpaGVImSi8xgIgNpnij9lbR2ljnv/4SWWyx8Uz4837MhClWDdAbsA5demnBNbMzw2vgvBOF6PrMxCZnw+aL7t8g3HeSS6jSUXTYxPL10yNTHX4Bt7LAY6lDfpZQ4IrH+BWRobpbLeeyjM2zkzaNXtTSKq/W/+4lGfxNQfOIUTQCd++erdIECgYEA/OiZgB4E1SE9Ekh+lXWsTkOU3Tb4NJlTwuV4RufoOBLexLbclz82j+4C6E3NUuaacSo9KLaYknUATzrKGDHN3icJIcG4575vzsvSzOn/cJk0o/Jc4uVX2cEoPxaXIkMN+21j9KqxYLQ3xGY5wFXR/6CMku2YuyyFu97gHRSglSUCgYEA39RNjrRxWA4vtnm9yamDVlDcbxxF663g9W88E+xp6cZe+2032Golhbhhp8bjV0hyWWFj5VObJ8tqwgRfcKhpy8luBHxR4b4vgqPKL4nxm1Phtg/lIZIowUSdeIxE9BnTId4Ekx5hqOZCe8G3WFba7uAZ/sS3Ils6zR1Wzv3Plv8CgYEA84b+GBHP0XJaHjrUORLAQfRtab2+rtddgnP3pz8zqprxCzaRnsntvhYPkqUoKsWGvaVQkt2QeKZVD/WqGDYM5/dqoaiqZexIOTam204O+9tqhtufeZQhTbrjCCy2hvVOh2ZEM5oRhu7CLEnLHlsFBUMRWYGT6dfrRoZMA3STVEkCgYEAw6SKdiX6vIEJ63HWFiL5DcV4KBaKd0pwy5cC6FdI7HHcK+B3Y87EJXHYyhHqPSyx5rZd3uGJSOtg0V2JHqvWba4PYBRabfsTBBmubIcijCZxr/WMzh83SFd1XR0eWE8KYRvy27U+n6dEjW/xlmG00/9GOY4wlPMxHoB6lEr93HkCgYBovoZekXRiiYjVXfDDxh5RgKXXbpESuW+5M/NoRje07uj9KwXiCagvyr71Ahx0m626ysRATSDKJ9xR6y28hmuD0e8ju29jtsR/r66vetfb5KdpmQ2CbsT73OLS0gwicjPEEZj3D0w1bc5Hx6Kurjh2Q9Dx4RqAErhqCFkOO3sWGw==";

	}

	// 生成密钥对
	public static KeyPair genKeyPair(int keyLength) throws Exception {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(1024);
		return keyPairGenerator.generateKeyPair();
	}

	// 公钥加密
	public static byte[] encrypt(byte[] content, PublicKey publicKey) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");// java默认"RSA"="RSA/ECB/PKCS1Padding"
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		return cipher.doFinal(content);
	}

	// 私钥解密
	public static byte[] decrypt(byte[] content, PrivateKey privateKey) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return cipher.doFinal(content);
	}
	//将base64编码后的私钥字符串转成PrivateKey实例
		public static PrivateKey getPrivateKey(String modulusStr, String exponentStr) throws Exception{
			BigInteger modulus=new BigInteger(modulusStr);
			BigInteger exponent=new BigInteger(exponentStr);
		RSAPrivateKeySpec privateKeySpec=new RSAPrivateKeySpec(modulus, exponent);
			KeyFactory keyFactory=KeyFactory.getInstance("RSA");
			return keyFactory.generatePrivate(privateKeySpec);
		}

}
