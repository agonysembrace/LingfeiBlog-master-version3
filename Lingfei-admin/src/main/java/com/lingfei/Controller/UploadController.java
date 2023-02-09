package com.lingfei.Controller;

import com.lingfei.domain.ResponseResult;
import com.lingfei.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author lingfei Wang
 * @version 1.0
 * @date 2022/12/13 12:26
 * @Decription:
 */

@RestController
@RequestMapping("/upload")
public class UploadController {

    @Autowired
    UploadService uploadService;
    @PostMapping
    public ResponseResult uploadImg(MultipartFile img){
        return uploadService.uploadImg(img);
    }


}
