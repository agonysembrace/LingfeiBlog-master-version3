package com.lingfei.Controller;

import com.lingfei.domain.ResponseResult;
import com.lingfei.domain.entity.Role;
import com.lingfei.domain.entity.User;
import com.lingfei.domain.vo.UserInfoAndRoleIdsVo;
import com.lingfei.dto.TagListDto;
import com.lingfei.enums.AppHttpCodeEnum;
import com.lingfei.exception.SystemException;
import com.lingfei.service.RoleService;
import com.lingfei.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author lingfei Wang
 * @version 1.0
 * @date 2022/12/12 10:02
 * @Decription:
 */
@RestController
//@RequestMapping("/system/user")
@RequestMapping("/user")
public class UserController{

    @Autowired
    UserService userService;
    @GetMapping("/getInfo/list")
    public ResponseResult listUserByPage(Integer pageNum, Integer pageSize, User user){
        return userService.listUserByPage(pageNum, pageSize, user);
    }



    @PostMapping
    public  ResponseResult addUser(@RequestBody User user){
        //如果没有传入用户名，则抛出必须传入用户名异常
        if(!StringUtils.hasText(user.getUserName()))
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        if(!userService.checkUserNameUnique(user.getUserName()))
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        if(!userService.checkPhoneUnique(user))
            throw new SystemException(AppHttpCodeEnum.PHONENUMBER_EXIST);
        if(!userService.checkEmailUnique(user))
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
//      TODO:创建用户时添加时间  user.setCreateTime(Date.now());

        return userService.addUser(user);

    }
//根据Id删除用户
    @DeleteMapping("{id}")
    public  ResponseResult deleteUser(@PathVariable Long id){
        userService.removeById(id);
        return ResponseResult.okResult();
    }
    //修改用户，先查后改
    @Autowired
    RoleService roleService;
    @GetMapping("{id}")
    public  ResponseResult getUserInfo(@PathVariable Long id){
        List<Role> roleList = roleService.selecRoleList();
        List<Long> rolelistbyId = roleService.selectRoleIdByUserId(id);
        User user = userService.getById(id);
        UserInfoAndRoleIdsVo vo = new UserInfoAndRoleIdsVo(user, roleList, rolelistbyId);
        return ResponseResult.okResult(vo);
//
//        return userService.getUserInfo(id);
    }

    @PutMapping
    public  ResponseResult updateUser(@RequestBody User user){
         userService.updateUser(user);
         return ResponseResult.okResult();

    }

}
