package com.lingfei.service.impl;

import com.lingfei.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lingfei Wang
 * @version 1.0
 * @date 2022/12/8 13:07
 * @Decription:
 */
@Service("ps")
public class PermissionsService {
    public boolean hasPermission(String permission){
        //如果是管理员，直接返回true
        if(SecurityUtils.isAdmin()){
            return true;
        }
        //否则，查询当前登陆用户的所有权限，然后在list内查询是否有入参权限
        List<String> permissions = SecurityUtils.getLoginUser().getPermissions();
        return permissions.contains(permission);
    }
}
