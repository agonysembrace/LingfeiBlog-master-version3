package com.lingfei.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lingfei.constants.SystemConstants;
import com.lingfei.domain.entity.Menu;
import com.lingfei.domain.vo.MenuTreeVo;
import com.lingfei.mapper.MenuMapper;
import com.lingfei.service.MenuService;
import com.lingfei.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2022-12-07 12:56:27
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Override
    public List<String> selectPermsByUserId(Long id) {
        //根据用户id查询权限字
        //如果是管理员，返回所有权限
        if(id==1L){
            LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(Menu::getMenuType, SystemConstants.MENU,SystemConstants.BUTTON);
            wrapper.eq(Menu::getStatus,SystemConstants.STATUS_NORMAL);
            List<Menu> menus = list(wrapper);
            List<String> perms = menus.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
            return perms;
        }
        //如果不是管理员，我们要先判断用户的角色（查role表

        return getBaseMapper().selectPermsByUserId(id);
    }

    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {
        MenuMapper menuMapper = getBaseMapper();
        List<Menu> menus = null;
        //判断是否是管理员
        if(SecurityUtils.isAdmin()) {
            //如果是，返回所有符合要求的menu
            menus = menuMapper.selectAllRouterMenu();
//            return menus;
        }
        else {
            //否则 当前用户所具有的menu
            menus = menuMapper.selectRouterMenuTreeByUserId(userId);
        }
        //构建tree
        List<Menu> menuTree = builderMenuTree(menus,0L);
        return menuTree;
    }

    @Override
    public List<Menu> getlist(Menu menu) {
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        //根据菜单名进行模糊查询
        wrapper.like(StringUtils.hasText(menu.getMenuName()),Menu::getMenuName,menu.getMenuName());
        wrapper.eq(StringUtils.hasText(menu.getStatus()),Menu::getStatus,menu.getStatus());
        //排序
        wrapper.orderByAsc(Menu::getParentId,Menu::getOrderNum);
        List<Menu> list = list(wrapper);
//        List<MenuVo> menuVos = BeanCopyUtils.copyBeanList(list, MenuVo.class);
        return list;
    }

    @Override
    public List<Long> getMenuTreeByRoleId(Long roleId) {
        return getBaseMapper().selectMenuListByRoleId(roleId);
    }

    private List<Menu> builderMenuTree(List<Menu> menus,Long parentId) {
        //先找出第一层的菜单，然后去找他们的子菜单，设置到children属性中
        List<Menu> menuTree = menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> menu.setChildren(getChildren(menu, menus)))
                .collect(Collectors.toList());
        return menuTree;
    }
//获取传入参数的子菜单
    private List<Menu> getChildren(Menu menu, List<Menu> menus) {
        List<Menu> childrenList = menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                .collect(Collectors.toList());
        return childrenList;
    }
}
