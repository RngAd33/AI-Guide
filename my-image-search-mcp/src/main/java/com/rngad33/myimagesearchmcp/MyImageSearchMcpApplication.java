package com.rngad33.myimagesearchmcp;

import com.rngad33.myimagesearchmcp.tools.ImageSearchTool;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MyImageSearchMcpApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyImageSearchMcpApplication.class, args);
        System.out.println("MCP服务已启动>>>");
    }

    /**
     * 注册工具
     *
     * @param imageSearchTool
     * @return
     */
    @Bean
    public ToolCallbackProvider imageSearchTools(ImageSearchTool imageSearchTool) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(imageSearchTool)
                .build();
    }

}