package com.xiaofeng.utils.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
	public final static String separator = File.separator;
	public final static String PDF_HEADER = "application/pdf";
	public final static String EXCEL_HEADER = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	public final static String HTML_HEADER = "application/html";
	public final static String DOC_HEADER = "application/msword";
	public final static String DOCX_HEADER = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
	public final static String IMG_HEADER = "image/jpeg";
	public final static String GIF_HEADER = "image/gif";
	public final static String PNG_HEADER = "image/png";

	/**
	 * 通用
	 * 
	 * @param path
	 * @param response
	 * @return
	 */
	public static HttpServletResponse downloadForContentType(String path, String contentType,
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

	public static HttpServletResponse downloadStream(String path, String contentType, HttpServletResponse response,
			boolean isImg) throws Exception {
		long startTime = System.currentTimeMillis(); // 获取开始时间
		String key = "aaaabbbbccccdddd";
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
		FileInputStream fileInputStream = null;
		InputStream fis = null;
		BufferedOutputStream bos = null;
		try {
			fileInputStream = new FileInputStream(path);
			fis = new BufferedInputStream(fileInputStream);
			response.setContentType(contentType);
			response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
			response.addHeader("Content-Length", "" + file.length());
			byte[] inputStream = SymmetricEncoder.getInputStream(SymmetricEncoder.ONE, file, key,
					SymmetricEncoder.ivParameter, response);
			long endTime = System.currentTimeMillis(); // 获取结束时间
			System.out.println("解密消耗时间：" + (endTime - startTime) + "ms"); // 输出程序运行时间
			if (false) {
				// 如果是图片,设置水印
				//DownloadUtils.createWatermark(inputStream, response);
				long endTime2 = System.currentTimeMillis(); // 获取结束时间
				System.out.println("生成水印总消耗时间：" + (endTime2 - endTime) + "ms"); // 输出程序运行时间
			} else {
				// 不是图片不能使用水印
				bos = new BufferedOutputStream(response.getOutputStream());

				bos.write(inputStream);

			}
			endTime = System.currentTimeMillis(); // 获取结束时间
			System.out.println("輸出流消耗时间：" + (endTime - startTime) + "ms"); // 输出程序运行时间
			response.reset();
			fis.close();
		} catch (Exception e) {
			e.getMessage();
		} finally {
			if (bos != null)
				bos.close();
			if (fileInputStream != null)
				fileInputStream.close();
			if (fis != null)
				fis.close();

		}
		return response;

		/*
		 * byte[] buffer = new byte[fis.available()]; fis.read(buffer); fis.close(); //
		 * 清空response //response.reset(); // 设置response的Header OutputStream toClient =
		 * new BufferedOutputStream(response.getOutputStream()); toClient.write(buffer);
		 * toClient.flush(); return null;
		 */
	}

	public static String downloadStream(String path)
			throws Exception {
		String newFileName = UUID.randomUUID().toString().replace("-", "");
		long startTime = System.currentTimeMillis(); // 获取开始时间
		String key = "aaaabbbbccccdddd";
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
		FileInputStream fileInputStream = null;
		InputStream fis = null;
		BufferedOutputStream bos = null;
		//创建新文件
		String newFilePath = path.replace(filename, newFileName);

		try {
			
			// byte[] inputStream = SymmetricEncoder.getInputStream(SymmetricEncoder.ONE,
			// file, key, SymmetricEncoder.ivParameter, response);
			SymmetricEncoder.decriptfile(file, key, newFilePath);
			
			//downloadForContentType(newFilePath, contentType, response);
			System.out.println("返回");
			//删除临时文件
			//deleteFile(newFileName);
		} catch (Exception e) {
			e.getMessage();
		} finally {
			if (bos != null)
				bos.close();
			if (fis != null)
				fis.close();
			if (fileInputStream != null)
				fileInputStream.close();
			if (fis != null)
				fis.close();

		}
		return newFilePath;

		/*
		 * byte[] buffer = new byte[fis.available()]; fis.read(buffer); fis.close(); //
		 * 清空response //response.reset(); // 设置response的Header OutputStream toClient =
		 * new BufferedOutputStream(response.getOutputStream()); toClient.write(buffer);
		 * toClient.flush(); return null;
		 */
	}
	/**
	 * 
	 * @Title: deleteFile   
	 * @Description: 删除文件
	 * @param: @param newFilePath      
	 * @return: void      
	 * @throws
	 */
	public static void deleteFile(String newFilePath) {
		File deleteFile = new File(newFilePath);
		if (deleteFile.exists()) {
			deleteFile.delete();
			System.out.println("删除临时文件:"+newFilePath);
		}
	}
	/**
	 * 
	 * @param path
	 * @param response
	 * @param type
	 * @return
	 */
	public static HttpServletResponse download(String path, String format, HttpServletResponse response) {
		if (!StringUtils.isEmpty(format)) {
			// pdf
			if ("P".equals(format.trim().toUpperCase())) {
				return downloadForContentType(path, PDF_HEADER, response);
			}
			// excel类型
			if ("E".equals(format.trim().toUpperCase())) {
				return downloadForContentType(path, EXCEL_HEADER, response);
			}
			// html类型
			if ("H".equals(format.trim().toUpperCase())) {
				return downloadForContentType(path, HTML_HEADER, response);
			}
			// word类型
			if ("W".equals(format.trim().toUpperCase())) {
				return downloadForContentType(path, DOCX_HEADER, response);
			}
			// word类型
			if ("I".equals(format.trim().toUpperCase())) {
				return downloadForContentType(path, IMG_HEADER, response);
			}
		}
		return downloadForContentType(path, IMG_HEADER, response);
	}

	/**
	 * 
	 * @param path
	 * @param format
	 * @param fileName
	 * @param response
	 * @return
	 */
	public static HttpServletResponse download(String path, String format, String fileName,
			HttpServletResponse response) {
		if (!StringUtils.isEmpty(format)) {
			download(path, format, response);
		} else if (!StringUtils.isEmpty(fileName)) {
			String[] split = fileName.split("\\.");
			if (split.length > 1) {
				format = split[1];
			}
			// pdf
			if ("PDF".equals(format.trim().toUpperCase())) {
				return downloadForContentType(path, PDF_HEADER, response);
			}
			// excel类型
			if ("XLSX".equals(format.trim().toUpperCase())) {
				return downloadForContentType(path, EXCEL_HEADER, response);
			}
			// html类型
			if ("HTML".equals(format.trim().toUpperCase())) {
				return downloadForContentType(path, HTML_HEADER, response);
			}
			// word类型
			if ("DOC".equals(format.trim().toUpperCase())) {
				return downloadForContentType(path, DOC_HEADER, response);
			}
			if ("DOCX".equals(format.trim().toUpperCase())) {
				return downloadForContentType(path, DOCX_HEADER, response);
			}
			if ("JPG".equals(format.trim().trim().toUpperCase())) {
				return downloadForContentType(path, IMG_HEADER, response);
			}
			if ("GIF".equals(format.trim().toUpperCase())) {
				return downloadForContentType(path, GIF_HEADER, response);
			}
			if ("PNG".equals(format.trim().toUpperCase())) {
				return downloadForContentType(path, PNG_HEADER, response);
			}
		}
		return downloadForContentType(path, IMG_HEADER, response);
	}
	/**
	 * 
	 * @Title: getContentType   
	 * @Description: 根据后缀获取响应头
	 * @param: @param path 路径
	 * @param: @param format 后缀
	 * @param: @param fileName 文件名
	 * @param: @return      
	 * @return: HttpServletResponse      
	 * @throws
	 */
	public static String getContentType(String path, String format, String fileName) {
		if (StringUtils.isNotEmpty(fileName)) {
			String[] split = fileName.split("\\.");
			if (split.length > 1) {
				format = split[split.length-1];
			}
			// pdf
			if ("PDF".equals(format.trim().toUpperCase())) {
				return PDF_HEADER;
			}
			// excel类型
			if ("XLSX".equals(format.trim().toUpperCase())) {
				return EXCEL_HEADER;
			}
			// html类型
			if ("HTML".equals(format.trim().toUpperCase())) {
				return HTML_HEADER;
			}
			// word类型
			if ("DOC".equals(format.trim().toUpperCase())) {
				return DOC_HEADER;
			}
			if ("DOCX".equals(format.trim().toUpperCase())) {
				return DOCX_HEADER;
			}
			if ("JPG".equals(format.trim().trim().toUpperCase())) {
				return IMG_HEADER;
			}
			if ("GIF".equals(format.trim().toUpperCase())) {
				return GIF_HEADER;
			}
			if ("PNG".equals(format.trim().toUpperCase())) {
				return PNG_HEADER;
			}
			if ("JPEG".equals(format.trim().toUpperCase())) {
				return IMG_HEADER;
			}
		}
		throw new BaseException("不支持格式类型!");
	}
	/*public static String createWatermark(String url) {
		UserContext userContext = UserContextHolder.getContextHolder();
		Graphics2D g = null;
		FileOutputStream outImgStream = null;
		BufferedOutputStream bufferedOutputStream = null;
		try {
			File file = new File(url);
			BufferedImage buImage = ImageIO.read(file);
			int width = buImage.getWidth();
			int height = buImage.getHeight();
			String oa = userContext.getEmpOA() != null ? "OA：" + userContext.getEmpOA() : "";
			String userName = "姓名：" + userContext.getUserName();
			String phone = "手机：" + userContext.getEmpSms();
			String[] markstr = { userName, phone, oa };
			BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
			g = bufferedImage.createGraphics();
			g.drawImage(buImage, 0, 0, width, height, null);

			Font f = new Font("微软雅黑", Font.ITALIC, width / 30);
			g.setFont(f);
			GlyphVector v;
			Shape shape;
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			for (int i = 0; i <= markstr.length - 1; i++) {
				int x = width / 5;
				int y = ((int) height / 6) + (i * 10);
				System.out.println("x=" + 10 + ",y=" + y + ",width=" + (width / 30));

				g.translate(x, y);
				v = f.createGlyphVector(g.getFontMetrics(f).getFontRenderContext(), markstr[i]);
				shape = v.getOutline();
				g.setColor(new Color(255, 255, 255, 60));
				g.fill(shape);
				g.setColor(new Color(0, 0, 0, 80));
				g.setStroke(new BasicStroke(2));
				g.draw(shape);
			}
			String newUrl = url.replace(file.getName(), UUID.randomUUID().toString().replace("-", ""));
			outImgStream = new FileOutputStream(newUrl);
            ImageIO.write(bufferedImage, "jpg", outImgStream);
            outImgStream.flush();
            return newUrl;
			response.addHeader("Content-Disposition", "attachment;filename=" + new String(file.getName().getBytes()));
			response.addHeader("Content-Length", "" + file.length());
			response.setContentType(IMG_HEADER);
			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
			response.setContentType(IMG_HEADER);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			ImageIO.write(bufferedImage, "jpg", stream);
			toClient.write(stream.toByteArray());
			toClient.flush();
			stream.close();
			toClient.close();
		} catch (Exception e) {
			log.error("生成水印失败：" + e);
		} finally {
			try {
				if (g != null) {
					g.dispose();
				}
			} catch (Exception e) {
			}

			try {
				if (outImgStream != null) {
					outImgStream.close();
				}
			} catch (Exception e) {
			}

			try {
				if (bufferedOutputStream != null) {
					bufferedOutputStream.close();
				}
			} catch (Exception e) {
			}

		}
		return "";
	}*/

	/*public static void createWatermark(byte[] buf, HttpServletResponse response) throws IOException {
		//UserContext userContext = UserContextHolder.getContextHolder();
		Graphics2D g = null;
		FileOutputStream outImgStream = null;
		BufferedImage buImage = null;
		BufferedOutputStream bufferedOutputStream = null;
		BufferedImage bufferedImage = null;
		InputStream in = null;
		try {
			long start = System.currentTimeMillis(); // 获取结束时间
			in = new ByteArrayInputStream(buf);
			buImage = ImageIO.read(in);
			int width = buImage.getWidth();
			int height = buImage.getHeight();
			String oa = userContext.getEmpOA() != null ? "OA：" + userContext.getEmpOA() : "";
			String userName = "姓名：" + userContext.getUserName();
			String phone = "手机：" + userContext.getEmpSms();
			String[] markstr = { userName, phone, oa };
			bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
			g = bufferedImage.createGraphics();
			g.drawImage(buImage, 0, 0, width, height, null);

			Font f = new Font("微软雅黑", Font.ITALIC, width / 30);
			g.setFont(f);
			GlyphVector v;
			Shape shape;
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			for (int i = 0; i <= markstr.length - 1; i++) {
				int x = width / 5;
				int y = ((int) height / 6) + (i * 10);
				System.out.println("x=" + 10 + ",y=" + y + ",width=" + (width / 30));

				g.translate(x, y);
				v = f.createGlyphVector(g.getFontMetrics(f).getFontRenderContext(), markstr[i]);
				shape = v.getOutline();
				g.setColor(new Color(255, 255, 255, 60));
				g.fill(shape);
				g.setColor(new Color(0, 0, 0, 80));
				g.setStroke(new BasicStroke(2));
				g.draw(shape);
			}
			long end = System.currentTimeMillis(); // 获取结束时间
			System.out.println("画布画水印消耗时间：" + (end - start) + "ms"); // 输出程序运行时间
			// response.addHeader("Content-Disposition", "attachment;filename=" + new
			// String(file.getName().getBytes()));
			// response.addHeader("Content-Length", "" + file.length());
			response.setContentType(IMG_HEADER);
			bufferedOutputStream = new BufferedOutputStream(response.getOutputStream());
			// outImgStream = new FileOutputStream(newUrl);
			throw new BaseException("測試");
			// ImageIO.write(bufferedImage, "jpg", bufferedOutputStream);

			// System.out.println("图片输出流消耗时间：" + (end - System.currentTimeMillis()) + "ms");
			// //输出程序运行时间

			// outImgStream.flush();
		} catch (Exception e) {
			log.error("生成水印失败：" + e);
		} finally {
			if (in != null) {
				in.close();
			}
			if (bufferedOutputStream != null) {
				bufferedOutputStream.close();
			}
			if (g != null) {
				g.dispose();
			}

			if (outImgStream != null) {
				outImgStream.close();
			}
		}
	}*/
}
