package com.lingfei.utils;

import com.lingfei.domain.entity.Menu;
import com.lingfei.domain.vo.MenuTreeVo;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author lingfei Wang
 * @version 1.0
 * @date 2022/12/13 15:55
 * @Decription:
 */
public class SystemConverter {
    private SystemConverter() {
    }


    public static List<MenuTreeVo> buildMenuSelectTree(List<Menu> menus) {
        List<MenuTreeVo> MenuTreeVos = menus.stream()
                .map(m -> new MenuTreeVo(m.getId(), m.getMenuName(), m.getParentId(), null))
                .collect(Collectors.toList());
        List<MenuTreeVo> options = MenuTreeVos.stream()
                .filter(o -> o.getParentId().equals(0L))
                .map(o -> o.setChildren(getChildList(MenuTreeVos, o)))
                .collect(Collectors.toList());


        return options;
    }
    private static List<MenuTreeVo> getChildList(List<MenuTreeVo> list, MenuTreeVo option) {
        List<MenuTreeVo> options = list.stream()
                .filter(o -> Objects.equals(o.getParentId(), option.getId()))
                .map(o -> o.setChildren(getChildList(list, o)))
                .collect(Collectors.toList());
        return options;

    }
}
