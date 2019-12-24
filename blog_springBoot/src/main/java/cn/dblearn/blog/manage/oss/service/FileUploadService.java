package cn.dblearn.blog.manage.oss.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传服务接口.
 *
 * @author renrk
 * @description
 * @date 2018-03-19 8:04 PM
 **/
public interface FileUploadService {

    String fileUpload(MultipartFile multipartFile, String fileType);

    String base64ImgUpload(String base64Data, String fileName, String fileType);
}
