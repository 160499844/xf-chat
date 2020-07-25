package com.xiaofeng.web.controller;

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
import com.xiaofeng.utils.exception.BaseException;
import com.xiaofeng.utils.file.FileUploadUtils;
import com.xiaofeng.web.repository.GroupRepository;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
			HttpSession session = request.getSession();
			Object grouIdObj = session.getAttribute(UtilConstants.SESSION_GROUP_ID);
			if(grouIdObj==null){
				throw new BaseException("当前会话已经过期");
			}else{
				groupId = grouIdObj.toString();
			}
			//Group group = GroupContext.GROUPS.get(groupId);
			Group group = groupRepository.findByGroupId(groupId);
			String aesKey = group.getToken().getAesKey();
			tagetUrl = EncryptMessage.encrypt(urlString, aesKey);
			String locPath = uploadPath + sepa + urlString;
			locPath = EncryptMessage.encrypt(locPath, aesKey);
			//存到redis中
			redisUtil.set(tagetUrl, locPath ,UtilConstants.REDIS_TIMEOUT);
			log.info("文件保存成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new Result(tagetUrl);
	}
	
}
