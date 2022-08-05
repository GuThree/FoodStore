package cn.edu.scnu.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.easymall.common.utils.UploadUtil;
import com.easymall.common.vo.PicUploadResult;

@Service
public class PicService {

	@Value("${pic.pathDirPrefix}")
	private String pathDirPrefix;
	@Value("${pic.urlPrefix}")
	private String urlPreparePrefix;
	
	public PicUploadResult picUpload(MultipartFile pic) {
		PicUploadResult result = new PicUploadResult();
		
		//1.判断后缀合法
		String originName = pic.getOriginalFilename();
		String extName = originName.substring(originName.lastIndexOf("."));
		boolean isok = extName.matches(".(jpg|png|gif|JPG|PNG|GIF)$");
		if(!isok){
			result.setError(1);
			return result;
		}
		
		//2.判断是否木马
		try {
			BufferedImage bufImg = ImageIO.read(pic.getInputStream());
			bufImg.getWidth();
			bufImg.getHeight();
		} catch (Exception e) {
			e.printStackTrace();
			result.setError(1);
			return result;
		}
		
		//3.创建以upload开始的路径
		String dir = UploadUtil.getUploadPath(originName, "upload") + "/";
		
		//4.创建nginx访问的静态目录，pathDir，pathDirPrefix
		//（D:\java\easymall_image\\upload\2\2\c\a\b\0\e\b\）
		String pathDir = this.pathDirPrefix + dir;
		System.out.println(pathDir);
		File file = new File(pathDir);
		if(!file.exists()){
			file.mkdirs();
		}
		
		//5.创建urlPrefix
		String urlPrefix = this.urlPreparePrefix + dir;
		
		//6.拼接图片名称，将图片重命名uuid表示图片存储访问的名称
		String fileName = UUID.randomUUID().toString() + extName;
		
		//7.上传文件到磁盘路径
		try {
			pic.transferTo(new File(pathDir + fileName));
		} catch (Exception e) {
			e.printStackTrace();
			result.setError(1);
			return result;
		}
		
		//8.返回urlPrefix+图片名称
		result.setUrl(urlPrefix + fileName);
		return result;
	}

}
