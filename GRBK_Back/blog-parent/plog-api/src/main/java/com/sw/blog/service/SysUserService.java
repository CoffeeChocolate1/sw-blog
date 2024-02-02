package com.sw.blog.service;

import com.sw.blog.dao.pojo.SysUser;
import com.sw.blog.vo.Result;
import com.sw.blog.vo.UserVo;

public interface SysUserService {

    SysUser findUserById(Long id);

    SysUser findUser(String account, String password);

    Result findUserByToken(String token);

    SysUser findUserByAccount(String account);

    void save(SysUser sysUser);

    UserVo findUserVoById(Long authorId);

//    SysUser findUserById(Long id);
//
//    SysUser findUser(String account, String password);
//
//    /**
//     * 根据token查询用户信息
//     * @param token
//     * @return
//     */
//    Result findUserByToken(String token);
//
//    /**
//     * 根据账户查找用户
//     * @param account
//     * @return
//     */
//    SysUser findUserByAccount(String account);
//
//    /**
//     * 保存用户
//     * @param sysUser
//     */
//    void save(SysUser sysUser);
}
