package com.xiaofeng.web.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
<<<<<<< HEAD
	private Result upload(MultipartFile file) {
		log.info(" >>> 文件上传  <<< ");
        if (file == null) {
        }
        String fileName = "";
        File filePath = new File(uploadPath);
        if (!filePath.mkdir()) {
            filePath.mkdirs();
        }
        try {
            byte[] bytes = file.getBytes();
            //获取上传文件类型
            String fileUrl = file.getOriginalFilename();
            String fileType = fileUrl.substring(fileUrl.lastIndexOf(".")).toLowerCase();
            //自定义上传文件的名字
            fileName = UUID.randomUUID().toString().replace("-", "") + fileType;
            System.out.println("临时保存文件名:"+fileName);
            //截取文件格式
            String type = fileUrl.substring(fileUrl.lastIndexOf(".") + 1).toLowerCase();
            String destPath = filePath + File.separator + fileName;
            //保存到一个目标文件中
            file.transferTo(new File(destPath));
            log.info(" >>> 文件上传结束  <<< ");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("You failed to upload " + " => " + e.getMessage());
        }
		return new Result(fileName);
=======
	private Result<String> upload(MultipartFile file) throws Exception {
		log.info(" >>> 文件上传入口  <<< ");
		if(file==null)
			throw new BaseException("请上传文件");
		FileUploadUtils fileUploadUtils = new FileUploadUtils(file);
		//fileUploadUtils.setUploadPath("D:\\broadband_img\\pic\\");
		fileUploadUtils.setSuffixes(new String[] { ".png", ".jpeg", ".jpg", ".gif", ".docx", ".doc", ".xlsx", ".pdf","mp4","mp3" });
		fileUploadUtils.setUploadPath(uploadPath);
		fileUploadUtils.save();
		return new Result(fileUploadUtils.getUploadPath());
>>>>>>> f7d80f39c4607cfc8215a448ecf0b85d8f725f14
	}
	
}
