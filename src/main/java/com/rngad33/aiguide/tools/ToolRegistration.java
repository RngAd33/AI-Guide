package com.rngad33.aiguide.tools;

import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbacks;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 中央工具注册器
 */
@Configuration
public class ToolRegistration {

    @Value("${search.api-key}")
    private String searchApiKey;

    /**
     * 初始化工具集成Bean
     *
     * @return
     */
    @Bean
    public ToolCallback[] allTools() {
        FileOperatingTool fileOperationTool = new FileOperatingTool();
        WebSearchTool webSearchTool = new WebSearchTool(searchApiKey);
        WebScrapingTool webScrapingTool = new WebScrapingTool();
        ResourceDownloadTool resourceDownloadTool = new ResourceDownloadTool();
        TerminalOperatingTool terminalOperationTool = new TerminalOperatingTool();
        PDFGeneratingTool pdfGeneratingTool = new PDFGeneratingTool();
        return ToolCallbacks.from(
                fileOperationTool,
                webSearchTool,
                webScrapingTool,
                resourceDownloadTool,
                terminalOperationTool,
                pdfGeneratingTool
        );
    }

}