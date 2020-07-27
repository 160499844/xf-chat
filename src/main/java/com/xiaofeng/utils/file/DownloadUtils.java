package com.xiaofeng.utils.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.xiaofeng.utils.aes.SymmetricEncoder;
import com.xiaofeng.utils.exception.BaseException;

import lombok.extern.slf4j.Slf4j;

/**
 * 文件下载
 *
 * @author 小峰(ywf)
 *
 * @date 2018年10月23日
 */
@Slf4j
public class DownloadUtils {

	private final static String separator = File.separator;

	private final static String PDF_HEADER = "application/pdf";
	private final static String EXCEL_HEADER = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	private final static String DOC_HEADER = "application/msword";
	private final static String DOCX_HEADER = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
	private final static String IMG_HEADER = "image/jpeg";
	private final static String GIF_HEADER = "image/gif";
	private final static String PNG_HEADER = "image/png";

	public final static Map<String, String> RESPOSE_HEADERS = new HashMap<String, String>(20);
	static {
		RESPOSE_HEADERS.put("jpg", IMG_HEADER);
		RESPOSE_HEADERS.put("gif", GIF_HEADER);
		RESPOSE_HEADERS.put("png", PNG_HEADER);
		RESPOSE_HEADERS.put("pdf", PDF_HEADER);
		RESPOSE_HEADERS.put("docx", DOCX_HEADER);
		RESPOSE_HEADERS.put("doc", DOC_HEADER);
		RESPOSE_HEADERS.put("xlsx", EXCEL_HEADER);
	}

	/**
	 * 通用
	 * 
	 * @param path
	 * @param response
	 * @return
	 */
	private static HttpServletResponse downloadForContentType(String path, String contentType,
			HttpServletResponse response) {
		try {
			log.info("downloadForContentType 路径：" + path);
			// path是指欲下载的文件的路径。
			File file = new File(path);
			if (!file.exists()) {
				log.error("!!!路径不存在：" + path);
				throw new BaseException(1, "文件不存在!");
			}
			// 取得文件名。
			String filename = file.getName();
			// 取得文件的后缀名。
			// String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();

			// 以流的形式下载文件。
			InputStream fis = new BufferedInputStream(new FileInputStream(path));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			// 清空response
			// response.reset();
			// 设置response的Header
			response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
			response.addHeader("Content-Length", "" + file.length());
			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
			response.setContentType(contentType);
			toClient.write(buffer);
			toClient.flush();
			toClient.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return response;
	}

	

	/**
	 * 
	 * @Title: deleteFile @Description: 删除文件 @param: @param newFilePath @return:
	 * void @throws
	 */
	public static void deleteFile(String newFilePath) {
		File deleteFile = new File(newFilePath);
		if (deleteFile.exists()) {
			deleteFile.delete();
			System.out.println("删除临时文件:" + newFilePath);
		}
	}


	/**
	 * 
	 * @param path
	 * @param format
	 * @param fileName
	 * @param response
	 * @return
	 */
	public static HttpServletResponse download(String path,String fileName,
			HttpServletResponse response) {
		String format = "";
		String[] split = fileName.split("\\.");
		if (split.length > 1) {
			format = split[split.length-1];
		}
		String resHeader = RESPOSE_HEADERS.get(format.trim().toUpperCase());
		// pdf
		return downloadForContentType(path, resHeader, response);
	}
	
}
