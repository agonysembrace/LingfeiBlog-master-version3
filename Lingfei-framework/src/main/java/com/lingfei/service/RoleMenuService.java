package com.lingfei.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lingfei.domain.entity.RoleMenu;


/**
 * 角色和菜单关联表(RoleMenu)表服务接口
 *
 * @author makejava
 * @since 2022-12-13 16:58:10
 */
public interface RoleMenuService extends IService<RoleMenu> {

    void deleteDataByRoleId(Long id);
}

