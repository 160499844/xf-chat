package com.xiaofeng.utils.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.InvalidAlgorithmParameterException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.xiaofeng.utils.aes.SymmetricEncoder;
import com.xiaofeng.utils.exception.BaseException;

/**
 * 天讯瑞达通信技术有限公司 版权所有
 *
 * 文件上传工具类
 * 
 * @author 严伟峰
 * @since 2018/7/24
 */
@SuppressWarnings({ "unused", "WeakerAccess" })
public class FileUploadUtils {

	private final static String SEPARATOR = File.separator;

	/**
	 * 原始文件名
	 */
	private String name;

	/**
	 * 上传路径
	 */
	private String uploadPath;

	/**
	 * 上传后文件名
	 */
	private String uploadFileName;

	/**
	 * 后缀名,例如.jpg/.png/.gif
	 */
	private String suffix;

	/**
	 * 能够上传的后缀名,例如.jpg/.png/.gif
	 */
	private String[] suffixes = new String[3];

	/**
	 * 文件大小，单位为kb
	 */
	private Long size;

	/**
	 * 文件大小最小限制，单位为kb
	 */
	private Long minSize;

	/**
	 * 文件大小最大限制，单位为kb
	 */
	private Long maxSize;

	/**
	 * 上传的文件对象，springmvc已经封装好
	 */
	private MultipartFile uploadFile;

	/**
	 * 构造方法，创建一个文件上传对象 默认值: 文件最大为5 * 1025 * 1024kb, 允许上传文件的后缀格式：.jpg/.png/.gif
	 *
	 * @param file 对象
	 */
	public FileUploadUtils(MultipartFile file) {
		if (!file.isEmpty()) {
			// 获取文件名
			this.name = file.getOriginalFilename();
			// 获得文件大小
			this.size = file.getSize();
			this.uploadFile = file;
			// 获取文件的后缀名
			this.suffix = this.name.substring(this.name.lastIndexOf("."));
			// 默认文件小小限制
			this.minSize = 0L;
			this.maxSize = 50 * 1025 * 1024L;
			/*
			 * this.suffixes[0] = ".gif"; this.suffixes[1] = ".jpg"; this.suffixes[2] =
			 * ".png";
			 */
		}
	}

	/**
	 * 保存文件到磁盘 @Title: save @param @return @param @throws Exception 参数 @return
	 * 返回布尔类型：true表示上传成功，否则失败 @throws
	 */
	@SuppressWarnings("ResultOfMethodCallIgnored")
	public void save() throws Exception {
		// 判断上传文件格式是否合法
		String suffix2 = getSuffix();
		List<String> asList = Arrays.asList(getSuffixes());
		boolean contains;
		if (asList.size() != 0) {
			contains = asList.contains(suffix2);
		} else {
			contains = true;
		}
		// 判断大小是否合法
		boolean canUpload = size();
		// 上传成功返回文件名
		String newName;
		if (contains && canUpload) {
			// 文件上传后的路径
			// 解决中文问题，liunx下中文路径，图片显示问题
			newName = (UUID.randomUUID() + suffix2).replaceAll("-", "");
			setUploadFileName(newName);
			Date date = new Date();
			String dataForm = new SimpleDateFormat("yyyyMMdd").format(date);
			// 上传后的相对路径+文件名
			String uploadPath = dataForm + SEPARATOR + newName;
			// 页面展示的相对路径
			String uploadData = dataForm + "/" + newName;
			File uploadNewName = new File(getUploadPath() + SEPARATOR + uploadPath);
			// 检测是否存在目录
			if (!uploadNewName.getParentFile().exists()) {
				uploadNewName.getParentFile().mkdirs();
			}
			getUploadFile().transferTo(uploadNewName);
			setUploadPath(uploadData);
		} else {
			if (!contains) {
				throw new BaseException("上传文件格式不合法");
			}
			throw new BaseException("上传文件大小不合法");
		}
	}
	@SuppressWarnings("ResultOfMethodCallIgnored")
	public void writeEncode() throws Exception {
		// 判断上传文件格式是否合法
		String suffix2 = getSuffix();
		List<String> asList = Arrays.asList(getSuffixes());
		boolean contains;
		if (asList.size() != 0) {
			contains = asList.contains(suffix2);
		} else {
			contains = true;
		}
		// 判断大小是否合法
		boolean canUpload = size();
		// 上传成功返回文件名
		String newName;
		if (contains && canUpload) {
			// 文件上传后的路径
			// 解决中文问题，liunx下中文路径，图片显示问题
			newName = (UUID.randomUUID() + suffix2).replaceAll("-", "");
			setUploadFileName(newName);
			Date date = new Date();
			String dataForm = new SimpleDateFormat("yyyyMMdd").format(date);
			// 上传后的相对路径+文件名
			String uploadPath = dataForm + SEPARATOR + newName;
			// 页面展示的相对路径
			String uploadData = dataForm + "/" + newName;
			String newFilePath = getUploadPath()  + uploadPath;
			File uploadNewName = new File(getUploadPath() + SEPARATOR + uploadPath);
			// 检测是否存在目录
			if (!uploadNewName.getParentFile().exists()) {
				uploadNewName.getParentFile().mkdirs();
			}
			String key = "aaaabbbbccccdddd";
			getUploadFile().transferTo(uploadNewName);
			SymmetricEncoder.encryptfile(uploadNewName, key, newFilePath);
			setUploadPath(uploadData);
		} else {
			if (!contains) {
				throw new BaseException("上传文件格式不合法");
			}
			throw new BaseException("上传文件大小不合法");
		}
	}

	public static void writeDecode() throws FileNotFoundException, InvalidAlgorithmParameterException {
		// key： 加密密钥
		String key = "aaaabbbbccccdddd";
		try {
			// 对给出的密文的解密过程。
			// 密文保存在“密文.txt”中
			// 将解密结果保存在“明文.txt”中
			File file = new File("D:\\broadband_img\\private\\20200220\\9b5ef2de510e42ebb0378872c3ccc239.png");
			String fileName1 = "D:\\broadband_img\\private\\20200220\\9b5ef2de510e42ebb0378872c3ccc239.png";
			SymmetricEncoder.decriptfile(file, key, fileName1);
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws FileNotFoundException, InvalidAlgorithmParameterException {
		//writeEncode();
		//writeDecode();
	}
	/**
	 * 判断文件大小 @Title: size @param @return 参数 @return boolean 返回类型 @throws
	 */
	public boolean size() {
		return !(size > this.maxSize || size < this.minSize);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 文件上传后的保存路径 @Title: getUploadPath @param @return 参数 @return String
	 * 返回类型 @throws
	 */
	public String getUploadPath() {
		return uploadPath;
	}

	public void setUploadPath(String uploadPath) {
		this.uploadPath = uploadPath;
	}

	/**
	 * 获得上传后的文件名 @Title: getUploadFileName @author 严伟峰 @param @return 参数 @return
	 * String 返回类型 @throws
	 */
	public String getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String[] getSuffixes() {
		return suffixes;
	}

	/**
	 * 限制上传文件后缀格式 @Title: setsuffixes @author 严伟峰 @param @param suffixes 参数 @return
	 * void 返回类型 @throws
	 */
	public void setSuffixes(String[] suffixes) {
		this.suffixes = suffixes;
	}

	/**
	 * 获得上传文件大小，单位kb @Title: getSize @author 严伟峰 @param @return 参数 @return Long
	 * 返回类型 @throws
	 */
	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public Long getMinSize() {
		return minSize;
	}

	public void setMinSize(Long minSize) {
		this.minSize = minSize;
	}

	public Long getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(Long maxSize) {
		this.maxSize = maxSize;
	}

	public MultipartFile getUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(MultipartFile uploadFile) {
		this.uploadFile = uploadFile;
	}

}
