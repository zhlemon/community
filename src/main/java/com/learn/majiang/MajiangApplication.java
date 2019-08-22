package com.learn.majiang;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.learn.majiang.mapper")
public class MajiangApplication {
    public static void main(String[] args) {
        SpringApplication.run(MajiangApplication.class, args);
    }
}
