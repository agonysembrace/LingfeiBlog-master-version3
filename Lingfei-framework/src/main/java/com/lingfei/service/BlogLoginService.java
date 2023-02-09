package com.lingfei.service;

import com.lingfei.domain.ResponseResult;
import com.lingfei.domain.entity.User;

/**
 * @author lingfei Wang
 * @version 1.0
 * @date 2022/12/3 12:51
 * @Decription:
 */
public interface BlogLoginService {

    ResponseResult login(User user);
}
