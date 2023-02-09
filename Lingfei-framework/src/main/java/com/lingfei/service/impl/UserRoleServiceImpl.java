package com.lingfei.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lingfei.domain.entity.UserRole;
import com.lingfei.mapper.UserRoleMapper;
import com.lingfei.service.UserRoleService;
import org.springframework.stereotype.Service;

/**
 * 用户和角色关联表(UserRole)表服务实现类
 *
 * @author makejava
 * @since 2022-12-12 11:00:28
 */
@Service("userRoleService")
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

}
