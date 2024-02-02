package com.sw.blog;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@Slf4j
@SpringBootApplication
public class BlogApp {
    public static void main(String[] args){
        SpringApplication.run(BlogApp.class,args);
        log.info("博客项目启动完成");

    }
}
