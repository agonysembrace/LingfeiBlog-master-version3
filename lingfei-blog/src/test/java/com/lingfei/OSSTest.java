package com.lingfei;

import com.google.gson.Gson;
import com.lingfei.domain.entity.User;
import com.lingfei.service.UserService;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.apache.poi.util.SystemOutLogger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * @author lingfei Wang
 * @version 1.0
 * @date 2022/12/5 18:37
 * @Decription:
 */
//@Component
//@SpringBootTest
//@ConfigurationProperties(prefix = "oss1")
//public class OSSTest {
//    private String accessKey;
//    private String secretKey;
//    private String bucket;
//    public void setAccessKey(String accessKey) {
//        this.accessKey = accessKey;
//    }
//
//    public void setSecretKey(String secretKey) {
//        this.secretKey = secretKey;
//    }
//
//    public void setBucket(String bucket) {
//        this.bucket = bucket;
//    }
//
//
//    @Test
//    public void test(){
//        //构造一个带指定 Region 对象的配置类
//        Configuration cfg = new Configuration(Region.autoRegion());
//        UploadManager uploadManager = new UploadManager(cfg);
////默认不指定key的情况下，以文件内容的hash值作为文件名
//        String key = null;
//        try {
//            InputStream inputStream = new FileInputStream("H:\\桌面\\乱七八糟\\1.png");
//            Auth auth = Auth.create(accessKey, secretKey);
//            String upToken = auth.uploadToken(bucket);
//
//            try {
//                Response response = uploadManager.put(inputStream,key,upToken,null, null);
//                //解析上传成功的结果
//                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
//                System.out.println(putRet.key);
//                System.out.println(putRet.hash);
//            } catch (QiniuException ex) {
//                Response r = ex.response;
//                System.err.println(r.toString());
//                try {
//                    System.err.println(r.bodyString());
//                } catch (QiniuException ex2) {
//                }
//            }
//        } catch (Exception ex) {
//        }
//
//    }
//
//    @Autowired
//    UserService userService;
//    @Test
//    public void test1(){
//        User user = userService.getById(14);
//        System.out.println(user);
//    }
//}
