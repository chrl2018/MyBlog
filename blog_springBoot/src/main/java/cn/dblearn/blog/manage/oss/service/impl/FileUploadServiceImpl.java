package cn.dblearn.blog.manage.oss.service.impl;

import cn.dblearn.blog.core.common.util.FileUtil;
import cn.dblearn.blog.manage.oss.service.FileUploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 文件上传服务接口实现.
 *
 * @author renrk
 * @description
 * @date 2018-03-19 8:06 PM
 **/
@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Value("${web.upload-path}")
    private String uploadPath;

//    @Value("${web.static-path}")
//    private String staticPath;

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadServiceImpl.class);

    @Override
    public String fileUpload(MultipartFile multipartFile, String fileType) {
        if (multipartFile == null || StringUtils.isEmpty(multipartFile.getOriginalFilename())) {
            LOGGER.warn("file is empty");
            return null;
        }
        //获得文件名、文件上传路径
        String fileName = multipartFile.getOriginalFilename();
        if(StringUtils.isEmpty(fileName)){
            LOGGER.warn("file is empty");
            return null;
        }
        String extension = fileName.substring(fileName.lastIndexOf("."));
        fileName = Long.toString(System.currentTimeMillis()) + extension;
        String datePath = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String filePath = uploadPath + File.separator + "upload" + File.separator + fileType + File.separator + datePath;
        //上传文件
        try {
            LOGGER.info("ready to upload file");
            FileUtil.uploadFile(multipartFile.getBytes(), filePath, fileName);
        } catch (Exception e) {
            LOGGER.warn("file upload failed");
            return null;
        }
        LOGGER.info("file upload succeed");
        //返回文件访问路径
//        String fileUrl = uploadPath + "/upload/" + fileType + "/" + datePath + "/" + fileName;
        return filePath;
    }

    @Override
    public String base64ImgUpload(String base64Data, String fileName, String fileType) {
        // 图像数据为空
        LOGGER.info("===============输入数据为:" + base64Data);
        if (base64Data == null || StringUtils.isEmpty(fileName)) {
            LOGGER.warn("file is empty");
            return null;
        }

        String extension = fileName.substring(fileName.lastIndexOf("."));
        fileName = Long.toString(System.currentTimeMillis()) + extension;
        String datePath = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String filePath = uploadPath + File.separator + "upload" + File.separator + fileType + File.separator + datePath;
        //上传文件
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            // Base64解码
            byte[] bytes = decoder.decodeBuffer(base64Data);
            for (int i = 0; i < bytes.length; ++i) {
                if (bytes[i] < 0) {// 调整异常数据
                    bytes[i] += 256;
                }
            }
            // 生成jpeg图片
            FileUtil.uploadFile(bytes, filePath, fileName);
        } catch (Exception e) {
            LOGGER.warn("file upload failed");
            LOGGER.info("file upload failed", e);
            return null;
        }
        LOGGER.info("file upload succeed");
        //返回文件访问路径
//        String fileUrl = staticPath + "/upload/" + fileType + "/" + datePath + "/" + fileName;
        return filePath;
    }
}
