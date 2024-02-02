package com.sw.blog.handler;

import com.sw.blog.vo.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


//改注解用于对添加@controller注解的方法进行拦截处理，实现了AOP
@ControllerAdvice
public class AllExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody//返回json数据
    public Result doException(Exception e){
        e.printStackTrace();
        return Result.fail(-999,"系统错误");
    }
}
