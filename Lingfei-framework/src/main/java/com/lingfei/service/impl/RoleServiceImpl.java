package com.lingfei.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lingfei.constants.SystemConstants;
import com.lingfei.domain.ResponseResult;
import com.lingfei.domain.entity.Role;
import com.lingfei.domain.entity.RoleMenu;
import com.lingfei.domain.entity.User;
import com.lingfei.domain.vo.PageVo;
import com.lingfei.mapper.RoleMapper;
import com.lingfei.service.RoleMenuService;
import com.lingfei.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2022-12-07 13:02:47
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
        //判断是否是管理员，如果是，返回集合中只需要有admin
        if(id==1L) {
            List<String> roleKeys = new ArrayList<>();
            roleKeys.add("admin");
            return roleKeys;
        }
        //否则查询用户所具有的角色信息

        return getBaseMapper().selectRoleKeyByUserId(id);
    }

    @Override
    public ResponseResult selecAllRole() {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getStatus, SystemConstants.STATUS_NORMAL);
        List<Role> list = list(wrapper);
        return ResponseResult.okResult(list);
    }

    @Override
    public List<Role> selecRoleList() {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getStatus, SystemConstants.STATUS_NORMAL);
        List<Role> list = list(wrapper);
        return list;
    }


        @Override
        public List<Long> selectRoleIdByUserId(Long userId) {
            return getBaseMapper().selectRoleIdByUserId(userId);
        }

    @Override
    public ResponseResult getRoleByPage(Role role, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper< Role> queryWrapper= new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(role.getRoleName()),Role::getRoleName,role.getRoleName());
        queryWrapper.eq(StringUtils.hasText(role.getStatus()),Role::getStatus,role.getStatus());


        Page<Role> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page,queryWrapper);


        PageVo pageVo = new PageVo(page.getRecords(), page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Autowired
    private RoleMenuService roleMenuService;
    @Override
    public void editRole(Role role) {
        //先更新Role表
        updateById(role);
        //再删除所有role本来具有的权限
        roleMenuService.deleteDataByRoleId(role.getId());
        //再插入从role中获取的权限
        insertRoleMenuByRole(role);
    }

    private void insertRoleMenuByRole(Role role) {
        List<RoleMenu> roleMenuList = Arrays.stream(role.getMenuIds())
                .map(memuId -> new RoleMenu(role.getId(), memuId))
                .collect(Collectors.toList());
        roleMenuService.saveBatch(roleMenuList);
    }
}

