package com.zhanyan.aipassage;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@MapperScan("com.zhanyan.aipassage.mapper")
@EnableAspectJAutoProxy(exposeProxy = true)
public class AiPassageApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiPassageApplication.class, args);
    }

}
