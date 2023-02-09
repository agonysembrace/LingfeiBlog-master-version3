package com.lingfei.Controller;

import com.lingfei.domain.ResponseResult;
import com.lingfei.domain.entity.LoginUser;
import com.lingfei.domain.entity.Menu;
import com.lingfei.domain.entity.User;
import com.lingfei.domain.vo.AdminUserInfo;
import com.lingfei.domain.vo.RoutersVo;
import com.lingfei.domain.vo.UserInfoVo;
import com.lingfei.enums.AppHttpCodeEnum;
import com.lingfei.exception.SystemException;
import com.lingfei.service.BlogLoginService;
import com.lingfei.service.LoginService;
import com.lingfei.service.MenuService;
import com.lingfei.service.RoleService;
import com.lingfei.utils.BeanCopyUtils;
import com.lingfei.utils.RedisCache;
import com.lingfei.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lingfei Wang
 * @version 1.0
 * @date 2022/12/3 12:49
 * @Decription:
 */

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleService roleService;


    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user) {
        if (!StringUtils.hasText(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return loginService.login(user);
    }
    @GetMapping("/user/getInfo")
    public ResponseResult<AdminUserInfo> getInfo(){
        //获取当前登陆用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        List<String> perms =  menuService.selectPermsByUserId(loginUser.getUser().getId());
        List<String> roleKeyList = roleService.selectRoleKeyByUserId(loginUser.getUser().getId());

        AdminUserInfo adminUserInfo = new AdminUserInfo(perms,roleKeyList,
                BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class));
        return ResponseResult.okResult(adminUserInfo);
    }

    @GetMapping("getRouters")
    public ResponseResult<RoutersVo> getRouters(){
        Long userId = SecurityUtils.getUserId();
        //查询menu 结果是tree的形式
        List<Menu> menus = menuService.selectRouterMenuTreeByUserId(userId);
        //封装数据返回
        return ResponseResult.okResult(new RoutersVo(menus));
    }

    @PostMapping("/user/logout")
    public ResponseResult logout(){
        return loginService.logout();
    }
}
