package com.lingfei.Controller;

import com.lingfei.domain.ResponseResult;
import com.lingfei.domain.entity.Role;
import com.lingfei.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import static net.sf.jsqlparser.util.validation.metadata.NamedObject.role;

/**
 * @author lingfei Wang
 * @version 1.0
 * @date 2022/12/12 16:24
 * @Decription:
 */
@RestController
//@RequestMapping("/system/role")
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    //查询所有角色
    @GetMapping("/listAllRole")
    public ResponseResult listAllRole(){
        return roleService.selecAllRole();
    }

    @GetMapping("list")
    public ResponseResult getRoleByPage(Role role,Integer pageNum,Integer pageSize){
        return roleService.getRoleByPage(role,pageNum,pageSize);
    }

    @PostMapping
    public ResponseResult addRole(@RequestBody Role role){
        roleService.save(role);
        return ResponseResult.okResult();
    }

    //改
    @GetMapping("{id}")
    public ResponseResult getById(@PathVariable("id") Long id){
        Role role = roleService.getById(id);
        return ResponseResult.okResult(role);
    }
    @PutMapping
    public ResponseResult updateRole(@RequestBody Role role) {
        //不能直接updateById，因为这样只改变了Role表，与其关联的Menu表需要一并修改
//        roleService.updateById(role);
        roleService.editRole(role);

        return ResponseResult.okResult();
    }
    @DeleteMapping("{id}")
    public ResponseResult deleteRole(@PathVariable("id") Long id) {
        roleService.removeById(id);
        return ResponseResult.okResult();
    }
}
