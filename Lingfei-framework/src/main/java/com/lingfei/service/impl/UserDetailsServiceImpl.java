package com.lingfei.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lingfei.constants.SystemConstants;
import com.lingfei.domain.entity.LoginUser;
import com.lingfei.domain.entity.User;
import com.lingfei.mapper.MenuMapper;
import com.lingfei.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author lingfei Wang
 * @version 1.0
 * @date 2022/12/3 13:11
 * @Decription:
 */

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MenuMapper menuMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询用户信息
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User :: getUserName,username);
        User user = userMapper.selectOne(queryWrapper);
        //判断是否查到用户，没查到的话抛出异常
        if(Objects.isNull(user)){
            throw new RuntimeException("用户不存在");
        }
        //查到的话返回用户信息给LoginUser
        //TODO 查询权限信息

        //如果是一个后台用户
        if(user.getType().equals(SystemConstants.ADMAIN)){
            List<String> list = menuMapper.selectPermsByUserId(user.getId());
            return new LoginUser(user,list);
        }
        //如果是普通用户，无后台权限，赋值null
        return new LoginUser(user,null);
    }
}
