package com.xiaofeng.utils.file;

import org.apache.commons.codec.binary.Base64;
import sun.misc.BASE64Decoder;
 
import java.io.*;
/**
 * 
 * @ClassName:  ImgBase64   
 * @Description: 图片转成base64
 * @author: 小峰
 * @date:   2020年7月27日 上午11:34:02
 */
public class ImgBase64 {
    /**
     * 将图片转换成Base64编码
     * @param imgFile 待处理图片
     * @return
     */
	private static String getImgStr(String imgFile) {
        // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
 
        InputStream in = null;
        byte[] data = null;
        // 读取图片字节数组
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Base64.encodeBase64String(data);
    }
 
    /**
     * 对字节数组字符串进行Base64解码并生成图片
     * @param imgStr 图片数据
     * @param imgFilePath 保存图片全路径地址
     * @return
     */
    private static boolean generateImage(String imgStr, String imgFilePath) {
        //
        if (imgStr == null) // 图像数据为空
            return false;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // Base64解码
            byte[] b = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }
            // 生成jpg图片
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    /**
     * 
     * @Title: imgToBase64   
     * @Description: 图片转成base64
     * @param: @param url
     * @param: @return      
     * @return: String      
     * @throws
     */
    public static String imgToBase64(String url) {
    	String imgStr = getImgStr(url);
    	String img_path="data:image/jpeg;base64,"+imgStr ;
		return img_path;
    }
    
}