package com.lingfei.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lingfei.domain.ResponseResult;
import com.lingfei.domain.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2022-12-07 13:02:47
 */
public interface RoleService extends IService<Role> {

    List<String> selectRoleKeyByUserId(Long id);

    ResponseResult selecAllRole();

    List<Role> selecRoleList();

    List<Long> selectRoleIdByUserId(Long userId);

    ResponseResult getRoleByPage(Role role, Integer pageNum, Integer pageSize);

    void editRole(Role role);
}

