package com.lingfei.service.impl;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectResult;
import com.google.gson.Gson;
import com.lingfei.domain.ResponseResult;
import com.lingfei.enums.AppHttpCodeEnum;
import com.lingfei.exception.SystemException;
import com.lingfei.service.UploadService;
import com.lingfei.utils.PathUtils;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author lingfei Wang
 * @version 1.0
 * @date 2022/12/6 12:21
 * @Decription:
 */
@Service
@Data
@ConfigurationProperties(prefix = "aliyun-oss")
public class OssUploadService implements UploadService {
    @Override
    public ResponseResult uploadImg(MultipartFile img) {
        //TODO 判断文件类型或大小
        //获取原始文件名
        String originalFilename = img.getOriginalFilename();
        if(!originalFilename.endsWith(".png")&&!originalFilename.endsWith(".jpg")){
            throw new SystemException(AppHttpCodeEnum.FILE_TYPE_ERROR);
        }

        //判断通过就上传
        String filePath = PathUtils.generateFilePath(originalFilename);
        String url = uploadOss(img,filePath);
        return ResponseResult.okResult(url);

    }
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;

    private String uploadOss(MultipartFile imgfile, String objectName){
        //构造一个带指定 Region 对象的配置类
        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        String endpoint = "https://oss-cn-hangzhou.aliyuncs.com";
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
//        String accessKeyId = "yourAccessKeyId";
//        String accessKeySecret = "yourAccessKeySecret";
        // 填写Bucket名称，例如examplebucket。
//        String bucketName = "examplebucket";
        // 填写Object完整路径，例如exampledir/exampleobject.txt。Object完整路径中不能包含Bucket名称。
//        String objectName = "exampledir/exampleobject.txt";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            InputStream inputStream = imgfile.getInputStream();
            PutObjectResult result = ossClient.putObject(bucketName, objectName, inputStream);
            System.out.println("endpoint+objectName");
            return "https://aliyun.dawn13.com/"+objectName;
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return "www";
    }
}
