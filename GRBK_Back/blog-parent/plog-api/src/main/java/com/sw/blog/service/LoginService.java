package com.sw.blog.service;

import com.sw.blog.dao.pojo.SysUser;
import com.sw.blog.vo.Result;
import com.sw.blog.vo.params.LoginParam;

public interface LoginService {
    Result login(LoginParam loginParam);

    SysUser checkToken(String token);

    Result logout(String token);

    Result register(LoginParam loginParam);
}
