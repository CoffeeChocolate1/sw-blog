package com.sw.blog.controler;

import com.sw.blog.service.LoginService;
import com.sw.blog.vo.Result;
import com.sw.blog.vo.params.LoginParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("logout")
public class LogoutController {
    //    @Autowired
//    private SysUserService sysUserService;
    @Autowired
    private LoginService loginService;

    @GetMapping
    public Result logout(@RequestHeader("Authorization") String token){
        return loginService.logout(token);
    }
}
