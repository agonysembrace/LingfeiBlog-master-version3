package com.lingfei.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lingfei.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;


/**
 * 用户表(User)表数据库访问层
 *
 * @author makejava
 * @since 2022-12-02 21:14:44
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}

