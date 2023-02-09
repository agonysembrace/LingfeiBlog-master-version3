package com.lingfei.Controller;

import com.lingfei.domain.ResponseResult;
import com.lingfei.domain.entity.Menu;
import com.lingfei.domain.vo.MenuTreeVo;
import com.lingfei.domain.vo.MenuVo;
import com.lingfei.domain.vo.RoleMenuTreeSelectVo;
import com.lingfei.service.MenuService;
import com.lingfei.utils.BeanCopyUtils;
import com.lingfei.utils.SystemConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author lingfei Wang
 * @version 1.0
 * @date 2022/12/13 10:03
 * @Decription:
 */
@RestController
//@RequestMapping("/system/menu")
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping("list")
    public ResponseResult getlist(Menu menu){
        List<Menu> menus = menuService.getlist(menu);
        return ResponseResult.okResult(menus);
    }

    //根据菜单Id查询菜单信息
    @GetMapping("{menuId}")
    public ResponseResult getMenusById(@PathVariable Long menuId){
        return ResponseResult.okResult(menuService.getById(menuId)) ;
    }

    @PutMapping
    public ResponseResult edit(@RequestBody Menu menu) {
        if (menu.getId().equals(menu.getParentId())) {
            return ResponseResult.errorResult(500,"修改菜单'" + menu.getMenuName() + "'失败，上级菜单不能选择自己");
        }
        menuService.updateById(menu);
        return ResponseResult.okResult();
    }
    //添加菜单
    @PostMapping
    public ResponseResult addMenu(@RequestBody Menu menu){
        menuService.save(menu);
        return ResponseResult.okResult();
    }


    @DeleteMapping("{menuId}")
    public ResponseResult delete(@PathVariable Long menuId) {
        if(menuService.getById(menuId).getChildren()!=null){
            return ResponseResult.errorResult(500,"删除菜单'" + menuService.getById(menuId).getMenuName() + "'失败，当前菜单还包含子菜单");
        }
        menuService.removeById(menuId);
        return ResponseResult.okResult();
    }

    //用于新增用户时查询出菜单树
    @GetMapping("treeselect")
    public ResponseResult getMenuTree() {
        List<Menu> menus = menuService.getlist(new Menu());
        List<MenuVo> menuVos = BeanCopyUtils.copyBeanList(menus, MenuVo.class);
        return ResponseResult.okResult(menuVos);
    }
    //根据角色id获取角色树
    @GetMapping("roleMenuTreeselect/{roleId}")
    public ResponseResult getMenuTreeById(@PathVariable Long roleId){
        List<Menu> menus = menuService.getlist(new Menu());
//        List<MenuTreeVo> menuTreeVos = BeanCopyUtils.copyBeanList(menus, MenuTreeVo.class);
        List<MenuTreeVo> menuTreeVos = SystemConverter.buildMenuSelectTree(menus);
        List<Long> list = menuService.getMenuTreeByRoleId(roleId);

        RoleMenuTreeSelectVo vo = new RoleMenuTreeSelectVo(list, menuTreeVos);
        return ResponseResult.okResult(vo);
    }

}
