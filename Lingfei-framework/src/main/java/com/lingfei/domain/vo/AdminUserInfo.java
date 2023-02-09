package com.lingfei.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author lingfei Wang
 * @version 1.0
 * @date 2022/12/7 13:06
 * @Decription:
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
//生成setter方法返回this（也就是返回的是对象），代替了默认的返回void。
@Accessors(chain = true)
public class AdminUserInfo {
     private List<String> permissions;
     private List<String> roles;
//     private UserInfoVo user;
     private UserInfoVo user;
}

