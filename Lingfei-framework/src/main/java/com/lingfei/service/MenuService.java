package com.lingfei.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lingfei.domain.entity.Menu;

import java.util.List;


/**
 * 菜单权限表(Menu)表服务接口
 *
 * @author makejava
 * @since 2022-12-07 12:56:27
 */
public interface MenuService extends IService<Menu> {

    List<String> selectPermsByUserId(Long id);

    List<Menu> selectRouterMenuTreeByUserId(Long userId);

    List<Menu> getlist(Menu menu);

    List<Long> getMenuTreeByRoleId(Long roleId);
}

