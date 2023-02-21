package com.lingfei.utils;

import com.lingfei.domain.entity.LoginUser;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @Author 三更  B站： https://space.bilibili.com/663528522
 */
public class SecurityUtils
{

    /**
     * 获取用户
     **/
    public static LoginUser getLoginUser()
    {
//        return (LoginUser) getAuthentication().getPrincipal();
        Object principal = null;
        try{
            principal = getAuthentication().getPrincipal();
            LoginUser loginUser = (LoginUser) principal;
            return loginUser;
        }catch (Exception e){
//            System.out.println(e);
        }
        return null;
    }

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static Boolean isAdmin(){
        Long id = getLoginUser().getUser().getId();
        return id != null && id.equals(1L);
    }

    public static Long getUserId() {
        LoginUser loginUser = getLoginUser();
        if(loginUser!=null)

        return getLoginUser().getUser().getId();
        return -1L;
    }
}