package com.lingfei.service;

import com.lingfei.domain.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author lingfei Wang
 * @version 1.0
 * @date 2022/12/6 12:20
 * @Decription:
 */
public interface UploadService {
    ResponseResult uploadImg(MultipartFile img);
}
