package com.lingfei.Controller;

import com.lingfei.annotation.Systemlog;
import com.lingfei.domain.ResponseResult;
import com.lingfei.domain.entity.User;
import com.lingfei.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

/**
 * @author lingfei Wang
 * @version 1.0
 * @date 2022/12/4 20:03
 * @Decription:
 */

@RestController
@RequestMapping("/user")

public class UserController {

    @Autowired
    private UserService userService;
    @GetMapping("/userInfo")
    public ResponseResult userInfo(){
        return userService.userInfo();
    }

    @PutMapping("/userInfo")
    @Systemlog(businessName = "更新用户信息")
    public ResponseResult updateUserInfo(@RequestBody User user){
        return userService.updateUserInfo(user);
    }

    @PostMapping("/register")
    public ResponseResult register(@RequestBody User user){
        return userService.register(user);
    }
}
