package com.rngad33.aiguide;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 项目启动入口
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@MapperScan("com.rngad33.aiguide.mapper")
public class AiGuideApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiGuideApplication.class, args);
        System.out.println("后端服务已启动>>>");
    }
}