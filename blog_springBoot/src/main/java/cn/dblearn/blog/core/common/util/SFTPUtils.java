package cn.dblearn.blog.core.common.util;


import com.jcraft.jsch.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.lang.reflect.Field;
import java.util.Properties;

/**
 * @Date: 2018/12/18 16:58
 * @Description:
 */
public class SFTPUtils {

    /**
     * 默认端口
     */

    private final static int DEFAULT_PORT = 22;

    private final static String HOST = "localhost";

    private final static String PORT = "22";

    private final static String USER_NAME = "root";

    private final static String PASSWORD = "123456";



    /**
     * 服务端保存的文件夹
     */
    private String remote;

    /**
     * 服务端保存的路径
     */
    private String remotePath;

    /**
     * 本地文件
     */
    private File local;

    /**
     * 主机地址
     */
    private String host;

    /**
     * 端口
     */
    private int port = DEFAULT_PORT;

    /**
     * 登录名
     */
    private String userName;

    /**
     * 登录密码
     */
    private String password;

    private ChannelSftp sftp;


    /**
     *
     * @param host   主机地址
     * @param port   主机端口
     * @param userName   服务器用户名
     * @param password   服务器密码
     * @param savePath   文件保存路径
     */
    public SFTPUtils(String host, int port, String userName, String password, String savePath) {
        this.init(host, port, userName, password,savePath);
    }


    /**
     * 初始化
     *
     * @param host
     * @param port
     * @param userName
     * @param password
     * @date 2018/12/18
     */
    private void init(String host, int port, String userName, String password,String savePath) {
        this.host = host;
        this.port = port;
        this.userName = userName;
        this.password = password;
        this.remotePath = savePath;
    }

    /**
     * 连接sftp
     *
     * @throws JSchException
     * @date 2018/12/18
     */
    private void connect() throws JSchException, NoSuchFieldException, IllegalAccessException, SftpException {
        JSch jsch = new JSch();
        // 取得一个SFTP服务器的会话
        Session session = jsch.getSession(userName, host, port);
        // 设置连接服务器密码
        session.setPassword(password);
        Properties sessionConfig = new Properties();
        // StrictHostKeyChecking
        // "如果设为"yes"，ssh将不会自动把计算机的密匙加入"$HOME/.ssh/known_hosts"文件，
        // 且一旦计算机的密匙发生了变化，就拒绝连接。
        sessionConfig.setProperty("StrictHostKeyChecking", "no");
        // 设置会话参数
        session.setConfig(sessionConfig);
        // 连接
        session.connect();
        // 打开一个sftp渠道
        Channel channel = session.openChannel("sftp");
        sftp = (ChannelSftp) channel;
        channel.connect();

        Class cl = ChannelSftp.class;
        Field f =cl.getDeclaredField("server_version");
        f.setAccessible(true);
        f.set(sftp, 2);
        sftp.setFilenameEncoding("GBK");
    }

    /**
     * 上传文件
     * @date 2018/12/18
     */
    public void uploadFile() throws Exception {
        FileInputStream inputStream = null;
        try {
            connect();
            if (isEmpty(remote)) {
                remote = local.getName();
            }
            if (!isEmpty(remotePath)) {
                sftp.cd(remotePath);
            }
            inputStream = new FileInputStream(local);
            sftp.put(inputStream, remote);
        } catch (Exception e) {
            throw e;
        } finally {
            sftp.disconnect();
            close(inputStream);
        }
    }

    public void uploadFile(InputStream inputStream) throws Exception {
        try {
            connect();
            if (isEmpty(remote)) {
                remote = local.getName();
            }
            if (!isEmpty(remotePath)) {
                createDir(remotePath);
            }

            sftp.put(inputStream, remote);
        } catch (Exception e) {
            throw e;
        } finally {
            sftp.disconnect();
            close(inputStream);
        }
    }

    @Async("taskExecutor")
    public void uploadFile(String filename, String filePath, MultipartFile file) throws Exception {
        try {
            connect();
            if (!isEmpty( filePath )) {
                createDir( filePath );
            }
            sftp.put( file.getInputStream(), filename );
        }catch (Exception e) {
            throw e;
        } finally {
            sftp.disconnect();
            close(file.getInputStream());
        }

    }

    public boolean isDirExist(String directory) throws Exception {
        boolean isDirExistFlag = false;
        try {
            SftpATTRS sftpATTRS = sftp.lstat(directory);
            isDirExistFlag = true;
            return sftpATTRS.isDir();
        } catch (Exception e) {
            e.printStackTrace();
            if (e.getMessage().toLowerCase().equals("no such file")) {
                isDirExistFlag = false;
            }
        }
        return isDirExistFlag;
    }

    public void createDir(String createpath) throws Exception {
        try {
            if (isDirExist(createpath)) {
                this.sftp.cd(createpath);
            } else {
                String pathArry[] = createpath.split("/");

                for (String path : pathArry) {
                    if (path.equals("")) {
                        continue;
                    }

                    if (isDirExist(path.toString())) {
                        sftp.cd(path.toString());
                    } else {
                        // 建立目录
                        sftp.mkdir(path.toString());
                        // 进入并设置为当前目录
                        sftp.cd(path.toString());
                    }
                }

            }
        } catch (SftpException e) {
            throw new Exception("创建路径错误：" + createpath);
        }
    }

    /**
     * 下载
     * @date 2018/12/18
     */
    public void download() throws Exception {
        FileOutputStream output = null;
        try {
            this.connect();
            if (null != remotePath || !("".equals(remotePath))) {
                sftp.cd(remotePath);
            }
            output = new FileOutputStream(local);
            sftp.get(remote, output);
        } catch (Exception e) {
            throw e;
        } finally {
            sftp.disconnect();
            close(output);
        }
    }

    public void download(OutputStream outputStream) throws Exception {

        try {
            this.connect();
            if (null != remotePath || !("".equals(remotePath))) {
                sftp.cd(remotePath);
            }
            sftp.get(remote, outputStream);
        } catch (Exception e) {
            throw e;
        } finally {
            sftp.disconnect();
        }
    }

    public static boolean isEmpty(String str) {
        if (null == str || "".equals(str)) {
            return true;
        }
        return false;
    }

    public static void close(OutputStream... outputStreams) {
        for (OutputStream outputStream : outputStreams) {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void close(InputStream... inputStreams) {
        for (InputStream inputStream : inputStreams) {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setRemote(String remote) {
        this.remote = remote;
    }

    public void setRemotePath(String remotePath) {
        this.remotePath = remotePath;
    }

    public void setLocal(File local) {
        this.local = local;
    }
}

