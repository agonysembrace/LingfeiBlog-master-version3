package com.lingfei.Controller;

import com.lingfei.domain.ResponseResult;
import com.lingfei.domain.entity.LoginUser;
import com.lingfei.domain.entity.User;
import com.lingfei.enums.AppHttpCodeEnum;
import com.lingfei.exception.SystemException;
import com.lingfei.service.BlogLoginService;
import com.lingfei.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lingfei Wang
 * @version 1.0
 * @date 2022/12/3 12:49
 * @Decription:
 */

@RestController
public class BlogLoginController {

    @Autowired
    private BlogLoginService blogLoginService;

    @Autowired
    private RedisCache redisCache;
    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user) {
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return blogLoginService.login(user);
    }
    @PostMapping("/logout")
    public ResponseResult logout() {
        //获取token，解析userid
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        //获取uesrid
        Long userId = loginUser.getUser().getId();
        //删除redis中的用户信息
        redisCache.deleteObject("bloglogin:"+userId);
        return ResponseResult.okResult();
    }
}
