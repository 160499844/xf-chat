package com.xiaofeng.utils.string;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang3.StringUtils;

/**
 * 天讯瑞达通信技术有限公司 版权所有
 *
 * 随机(数字、字母) 工具类
 * @author zwk
 * @since 2019/1/9
 */
public class RandomUtil extends StringUtils{

    private static ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
    
    public static String[] chars = new String[] {
    	     "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
    	     "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
    	     "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V","W", "X", "Y", "Z" 
    	        };
    
    /**
     * 获取指定长度的随机(字母+数字)
     * @param length
     *                  指定字符串长度
     */
    public static String randomString(int length) {
        StringBuilder sb = new StringBuilder();
        int count = 0;
        while (count ++ < length) {
            if (threadLocalRandom.nextBoolean()) {
                if (threadLocalRandom.nextBoolean()) {
                    // 大写字母
                    sb.append((char) (65 + threadLocalRandom.nextInt(26)));
                } else {
                    // 小写字母
                    sb.append((char) (65 + 32 + threadLocalRandom.nextInt(26)));
                }
            } else {
                // 数字
                sb.append(String.valueOf(threadLocalRandom.nextInt(10)));
            }
        }
        return sb.toString();
    }
    
    public static String getShortUuid() {  
        StringBuffer stringBuffer = new StringBuffer();  
        String uuid = UUID.randomUUID().toString().replace("-", "");  
        for (int i = 0; i < 8; i++) { // 32 -> 8
                String str = uuid.substring(i * 4, i * 4 + 4);
                // 16进制为基解析
                int strInteger = Integer.parseInt(str, 16);
                // 0x3E -> 字典总数 62
                stringBuffer.append(chars[strInteger % 0x3E]);
        }
        return stringBuffer.toString();
     }
        
    public static void main(String[] args) {
    	for(int i=0;i<100;i++) {
    		System.out.println(getShortUuid());
    	}
	}
}
