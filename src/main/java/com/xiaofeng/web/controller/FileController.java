package com.xiaofeng.web.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.xiaofeng.entity.Group;
import com.xiaofeng.global.GroupContext;
import com.xiaofeng.utils.EncryptMessage;
import com.xiaofeng.utils.Result;
import com.xiaofeng.utils.exception.BaseException;
import com.xiaofeng.utils.file.FileUploadUtils;

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

	//上传文件保存路径
	@Value(value = "${file.upload.path}")
	private String uploadPath;
	
	/**
	 * 文件上传
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("upload")
	private Result<String> upload(MultipartFile file,String groupId) throws Exception {
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
			Group group = GroupContext.GROUPS.get(groupId);
			String aesKey = group.getToken().getAesKey();
			tagetUrl = EncryptMessage.encrypt(urlString, aesKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new Result(tagetUrl);
	}
	
}
