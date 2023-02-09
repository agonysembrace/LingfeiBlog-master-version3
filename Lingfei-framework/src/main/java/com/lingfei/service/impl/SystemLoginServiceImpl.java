//package com.lingfei.com.lingfei.service.impl;
//
//import com.lingfei.domain.ResponseResult;
//import com.lingfei.domain.entity.LoginUser;
//import com.lingfei.domain.entity.User;
//import com.lingfei.domain.vo.BlogUserLoginVo;
//import com.lingfei.domain.vo.UserInfoVo;
//import com.lingfei.service.BlogLoginService;
//import com.lingfei.utils.BeanCopyUtils;
//import com.lingfei.utils.JwtUtil;
//import com.lingfei.utils.RedisCache;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Service;
//
//import java.util.Objects;
//
///**
// * @author lingfei Wang
// * @version 1.0
// * @date 2022/12/3 12:52
// * @Decription:
// */
//
//@Service
//public class BlogLoginServiceImpl implements BlogLoginService {
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//    @Autowired
//    private RedisCache redisCache;
//    @Override
//    public ResponseResult login(User user) {
//        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
//        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
//        //判断是否认证通过
//        if(Objects.isNull(authenticate)){
//            throw new RuntimeException("用户名或密码");
//        }
//        //获取userId，生成token
//        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
//        String userId = loginUser.getUser().getId().toString();
//        String jwt = JwtUtil.createJWT(userId);
//        //把用户信息存入Redis
//        redisCache.setCacheObject("blogLogin"+userId,loginUser);
//        //把token和userinfo封装，返回
//        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
//        BlogUserLoginVo vo = new BlogUserLoginVo(jwt, userInfoVo);
//        return ResponseResult.okResult(vo);
//    }
//}
package com.lingfei.service.impl;
import com.lingfei.domain.ResponseResult;
import com.lingfei.domain.entity.LoginUser;
import com.lingfei.domain.entity.User;
import com.lingfei.service.LoginService;
import com.lingfei.utils.JwtUtil;
import com.lingfei.utils.RedisCache;
import com.lingfei.utils.RedisUtil;
import com.lingfei.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Objects;

/**
 * @Author: cola99year
 * @Date: 2022/10/19 10:03
 */
@Service
public class SystemLoginServiceImpl implements LoginService {
    //调用authenticate方法来进行用户认证
    //默认是没有这个bean的，我们要自己去编写
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private RedisCache redisCache;
    @Override
    public ResponseResult login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //判断是否认证通过
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误");
        }
        //获取userid 生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        //把用户信息存入redis
        redisCache.setCacheObject("login:"+userId,loginUser);

        HashMap<String, String> map = new HashMap<>();
        map.put("token",jwt);

        return ResponseResult.okResult(map);
    }

    @Override
    public ResponseResult logout() {
        Long userId = SecurityUtils.getUserId();
        redisCache.deleteObject("login:"+userId);
        return ResponseResult.okResult();
    }


}
