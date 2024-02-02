package com.sw.blog.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class AdminApp {
    public static void main(String[] args) {
        SpringApplication.run(AdminApp.class,args);
        log.info("AdminApp启动啦");
    }
}
