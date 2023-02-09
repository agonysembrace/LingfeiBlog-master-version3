package com.lingfei.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lingfei.domain.ResponseResult;
import com.lingfei.domain.entity.Role;
import com.lingfei.domain.entity.User;

import com.lingfei.domain.entity.UserRole;
import com.lingfei.domain.vo.PageVo;
import com.lingfei.domain.vo.UserInfoAndRoleIdsVo;
import com.lingfei.domain.vo.UserInfoVo;

import com.lingfei.enums.AppHttpCodeEnum;
import com.lingfei.exception.SystemException;
import com.lingfei.mapper.UserMapper;
import com.lingfei.service.RoleService;
import com.lingfei.service.UserRoleService;
import com.lingfei.service.UserService;
import com.lingfei.utils.BeanCopyUtils;
import com.lingfei.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2022-12-04 18:16:10
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    PasswordEncoder passwordEncoder;
    @Override
    public ResponseResult userInfo() {
        //获取当前用户id
        Long userId = SecurityUtils.getUserId();
        //根据用户id查询用户信息
        User user = getById(userId);
        //封装成UserInfoVo
//        UserInfoVo vo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        UserInfoVo vo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        return ResponseResult.okResult(vo);


    }

    @Override
    public ResponseResult updateUserInfo(User user) {
        //更严谨的方法是只更新符合要求的列，编写querywapper
        //这里从简了，直接更新整个user
        updateById(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult register(User user) {
        //对数据进行非空判断
        if(StringUtils.hasText(user.getUserName()))
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        if(StringUtils.hasText(user.getPassword()))
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        if(StringUtils.hasText(user.getEmail()))
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        if(StringUtils.hasText(user.getNickName()))
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        //对数据进行查重判断
        if(userNameExist(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        //密码加密
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);


        //存入数据库
        save(user);
        return ResponseResult.okResult();


    }

    @Override
    public ResponseResult listUserByPage(Integer pageNum, Integer pageSize, User user) {
        //分页查询
        LambdaQueryWrapper<User> queryWrapper= new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(user.getUserName()),User::getUserName,user.getUserName());
        queryWrapper.eq(StringUtils.hasText(user.getPhonenumber()),User::getPhonenumber,user.getPhonenumber());
        queryWrapper.eq(StringUtils.hasText(user.getStatus()),User::getStatus,user.getStatus());


        Page<User> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page,queryWrapper);


        PageVo pageVo = new PageVo(page.getRecords(), page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    private boolean userNameExist(String userName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,userName);
        return count(queryWrapper)>0;
    }

    @Override
    public boolean checkUserNameUnique(String userName) {
        return count(Wrappers.<User>lambdaQuery().eq(User::getUserName,userName))==0;
    }

    @Override
    public boolean checkPhoneUnique(User user) {
        return count(Wrappers.<User>lambdaQuery().eq(User::getPhonenumber,user.getPhonenumber()))==0;
    }

    @Override
    public boolean checkEmailUnique(User user) {
        return count(Wrappers.<User>lambdaQuery().eq(User::getEmail,user.getEmail()))==0;
    }

    @Override
    public ResponseResult addUser(User user) {
        //存入MYSQL时 对密码进行加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        //先在用户表中存入用户
        save(user);
        //新增用户时我们直接关联角色，在用户类中新建roleId属性
        if(user.getRoleIds()!=null&&user.getRoleIds().length>0){
            //向角色连接表中插入数据
            insertUserRole(user);
        }
        return ResponseResult.okResult();
    }

    @Autowired
    RoleService roleService;
    @Override
    public ResponseResult getUserInfo(Long id) {
        List<Role> roleList = roleService.selecRoleList();
        List<Long> rolelistbyId = roleService.selectRoleIdByUserId(id);
        User user = getById(id);
        UserInfoAndRoleIdsVo vo = new UserInfoAndRoleIdsVo(user, roleList, rolelistbyId);
        return ResponseResult.okResult(vo);
    }

    @Override
    public void updateUser(User user) {
        updateById(user);
        if(user.getRoleIds()!=null&&user.getRoleIds().length>0){
            //向角色连接表中插入数据
           insertUserRole(user);
        }
    }

    @Autowired
    private UserRoleService userRoleService;

    private void insertUserRole(User user) {
        List<UserRole> sysUserRoles = Arrays.stream(user.getRoleIds())
                .map(roleId -> new UserRole(user.getId(), roleId)).collect(Collectors.toList());
        userRoleService.saveBatch(sysUserRoles);
    }


}
