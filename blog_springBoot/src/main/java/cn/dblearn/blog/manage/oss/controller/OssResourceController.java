package cn.dblearn.blog.manage.oss.controller;


import cn.dblearn.blog.core.common.Result;
import cn.dblearn.blog.core.common.exception.MyException;
import cn.dblearn.blog.core.common.util.SFTPUtils;
import cn.dblearn.blog.core.entity.oss.OssResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 * 云存储资源表 前端控制器
 * </p>
 *
 * @author bobbi
 * @since 2018-11-30
 */
@RestController
@RequestMapping("/admin/oss/resource")
public class OssResourceController {

    @Value("${sftp.ip}")
    private String SFTP_ADDRESS;

    @Value("${sftp.port}")
    private Integer SFTP_PORT;

    @Value("${sftp.visit-port}")
    private Integer SFTP_VISIT_PORT;

    @Value("${sftp.visit-path}")
    private String SFTP_VISIT_PATH;

    @Value("${sftp.username}")
    private String SFTP_USERNAME;

    @Value("${sftp.password}")
    private String SFTP_PASSWORD;

    @Value("${sftp.upload-path}")
    private String remotePath;

    @PostMapping("/upload")
    public Result uploadCover(MultipartFile file) throws Exception{
        if (file!=null && file.isEmpty()) {
            throw new MyException("上传文件不能为空");
        }
        //上传文件
        String fileName = file.getOriginalFilename();
//        Date currentTime = new Date();
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
//        String fileDir = formatter.format(currentTime);

        SFTPUtils sftpUtils = new SFTPUtils(SFTP_ADDRESS,SFTP_PORT,SFTP_USERNAME,SFTP_PASSWORD,remotePath);
        //异步上传文件
        sftpUtils.uploadFile( file.getOriginalFilename(),remotePath,file);
        String url = SFTP_ADDRESS+":"+SFTP_VISIT_PORT+SFTP_VISIT_PATH+fileName;
        OssResource resource = new OssResource(url,fileName);
        return Result.ok().put("resource", resource);
    }

    @PostMapping("/downLoad")
    public Result downLoad(MultipartFile file) throws Exception{
        return Result.ok();
    }
}
