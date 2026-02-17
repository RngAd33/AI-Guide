package com.rngad33.aiguide;

import cn.hutool.core.date.StopWatch;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.ai.autoconfigure.vectorstore.mariadb.MariaDbStoreAutoConfiguration;
import org.springframework.ai.autoconfigure.vectorstore.pgvector.PgVectorStoreAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 项目启动入口
 */
@SpringBootApplication(exclude = {PgVectorStoreAutoConfiguration.class,
        MariaDbStoreAutoConfiguration.class
})
@MapperScan("com.rngad33.aiguide.mapper")
public class AiGuideApplication {
    public static void main(String[] args) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        SpringApplication.run(AiGuideApplication.class, args);
        stopWatch.stop();
        System.out.println("后端服务已启动>>> 启动时间：" + stopWatch.getTotalTimeSeconds());
    }
}