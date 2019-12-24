package cn.dblearn.blog.core.common.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 文件工具类.
 *
 * @author renrk
 * @description
 * @date 2018-03-19 9:48 PM
 **/
public class FileUtil {

    public static void uploadFile(byte[] file, String filePath, String fileName) throws Exception {
        File targetFile = new File(filePath);
        if (!targetFile.exists()) {
            boolean flag = targetFile.mkdirs();
            if (!flag) {
                throw new RuntimeException("create directory failed:" + targetFile);
            }
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filePath + File.separator + fileName);
            fos.write(file);
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != fos) {
                fos.close();
            }
        }
    }
}
