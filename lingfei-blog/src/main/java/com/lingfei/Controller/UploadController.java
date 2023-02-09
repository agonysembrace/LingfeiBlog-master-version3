package com.lingfei.Controller;

import com.lingfei.domain.ResponseResult;
import com.lingfei.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.ws.Response;

/**
 * @author lingfei Wang
 * @version 1.0
 * @date 2022/12/6 12:17
 * @Decription:
 */

@RestController
public class UploadController {

    @Autowired
    private UploadService uploadService;

    @PostMapping("/upload")
    public ResponseResult uploadImg(MultipartFile img){
        return uploadService.uploadImg(img);
    }
}
