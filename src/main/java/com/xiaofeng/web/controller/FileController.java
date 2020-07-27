package com.xiaofeng.web.controller;

import java.security.InvalidAlgorithmParameterException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.xiaofeng.entity.Group;
import com.xiaofeng.global.UtilConstants;
import com.xiaofeng.utils.EncryptMessage;
import com.xiaofeng.utils.RedisUtil;
import com.xiaofeng.utils.Result;
import com.xiaofeng.utils.SessionUtils;
import com.xiaofeng.utils.aes.AESUtils;
import com.xiaofeng.utils.exception.BaseException;
import com.xiaofeng.utils.file.FileUploadUtils;
import com.xiaofeng.utils.file.ImgBase64;
import com.xiaofeng.web.repository.GroupRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * 文件处理
 * @author xiaofeng
 *
 */
@Slf4j
@RestController
@RequestMapping("file")
public class FileController {
	
	private String sepa = java.io.File.separator;
	@Autowired
	private GroupRepository groupRepository;
	//上传文件保存路径
	@Value(value = "${file.upload.path}")
	private String uploadPath;
	@Autowired
	private RedisUtil redisUtil;
	
	/**
	 * 文件上传
	 * 使用redis临时存储,设置有效期1天
	 * key加密的访问路径，使用aesKey加密
	 * value真实的物理文件路径,使用流读取文件
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("upload")
	private Result<String> upload(MultipartFile file, String groupId, HttpServletRequest request) throws Exception {
		log.info(" >>> 文件上传入口  <<< ");
		if(file==null)
			throw new BaseException("请上传文件");
		FileUploadUtils fileUploadUtils = new FileUploadUtils(file);
		//fileUploadUtils.setUploadPath("D:\\broadband_img\\pic\\");
		fileUploadUtils.setSuffixes(new String[] { ".png", ".jpeg", ".jpg", ".gif", ".docx", ".doc", ".xlsx", ".pdf","mp4","mp3" });
		fileUploadUtils.setUploadPath(uploadPath);
		fileUploadUtils.save();
		String urlString = fileUploadUtils.getUploadPath();
		String tagetUrl = "";
		//加密链接
		try {
			groupId = SessionUtils.getValue(request, UtilConstants.SESSION_GROUP_ID);
			Group group = groupRepository.findByGroupId(groupId);
			String aesKey = group.getToken().getAesKey();
			tagetUrl = EncryptMessage.encrypt(urlString, AESUtils.key);//系统加密
			log.info("文件上传系统加密路径:"+tagetUrl);
			tagetUrl = EncryptMessage.encrypt(tagetUrl, aesKey);//正常加密
			log.info("文件上传正常加密路径:"+tagetUrl);
			String locPath = uploadPath + sepa + urlString;
			//locPath = EncryptMessage.encrypt(locPath, aesKey);
			//存到redis中
			redisUtil.set(tagetUrl, locPath ,UtilConstants.REDIS_TIMEOUT);
			log.info("文件上传真实路径:"+locPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new Result(tagetUrl);
	}
	
	/**
	 * @throws InvalidAlgorithmParameterException 
	 * 
	 * @Title: dowload   
	 * @Description: 下载文件
	 * @param: @param response      
	 * @return: void      
	 * @throws
	 */
	@RequestMapping("img")
	public Result<String> dowload(String fileName, HttpServletRequest request,HttpServletResponse response) throws InvalidAlgorithmParameterException {
		/*String groupId;
		groupId = SessionUtils.getValue(request, UtilConstants.SESSION_GROUP_ID);
		Group group = groupRepository.findByGroupId(groupId);
		String aesKey = group.getToken().getAesKey();*/
		fileName = EncryptMessage.decrypt(fileName, AESUtils.key);
		log.info("图片解密:"+ fileName);
		fileName = uploadPath + sepa + fileName;
		log.info("图片路径:" + fileName);
		String imgToBase64 = ImgBase64.imgToBase64(fileName);
		return new Result<String>(imgToBase64);
		//DownloadUtils.download(fileName, fileName, response);
	}
}
