package com.Lynn.common.fdfs;

import org.apache.commons.io.FilenameUtils;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.springframework.core.io.ClassPathResource;

/**
 * Created by FantasmYii on 2018/3/22.
 */
public class FastDFSUtils {
    public static String uploadPic(byte[] pic ,String name,long size) {
        String path=null;
        //ClinetGloble 读取配置文件
        ClassPathResource resource = new ClassPathResource("fdfs_client.conf");
        try {
            ClientGlobal.init(resource.getClassLoader().getResource("fdfs_client.conf").getPath());
            //老大客户端
            TrackerClient trackerClient = new TrackerClient();
            //返回的是小弟的地址
            TrackerServer trackerServer = trackerClient.getConnection();
            //创建小弟客户端,StorageServer没有用
            StorageServer storageServer=null;
            StorageClient1 storageClient1=new StorageClient1(trackerServer,storageServer);
            //meta_list是图片信息，此处因为没有，所以设为null值
            String ext= FilenameUtils.getExtension(name);
            NameValuePair[] meta_list = new NameValuePair[3];
            meta_list[0] = new NameValuePair("fileName",name);
            meta_list[1] = new NameValuePair("fileExt",ext);
            meta_list[2] = new NameValuePair("fileSize",String.valueOf(size));
            path=storageClient1.upload_file1(pic, ext, meta_list);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }
}
