package com.rngad33.aiguide.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbacks;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

/**
 * 中央工具注册器
 */
@Configuration
@Slf4j
public class ToolRegistration {

    @Value("${search.api-key}")
    private String searchApiKey;

    /**
     * 初始化工具集成Bean
     *
     * @return
     */
    @Bean("allTools")
    public ToolCallback[] allTools() {
        FileOperatingTool fileOperationTool = new FileOperatingTool();
        WebSearchTool webSearchTool = new WebSearchTool(searchApiKey);
        WebScrapingTool webScrapingTool = new WebScrapingTool();
        ResourceDownloadTool resourceDownloadTool = new ResourceDownloadTool();
        TerminalOperatingTool terminalOperationTool = new TerminalOperatingTool();
        PDFGeneratingTool pdfGeneratingTool = new PDFGeneratingTool();

        ToolCallback[] tools = ToolCallbacks.from(
                fileOperationTool,
                webSearchTool,
                webScrapingTool,
                resourceDownloadTool,
                terminalOperationTool,
                pdfGeneratingTool
        );
        return this.validateAndDeduplicateTools(tools);
    }

    /**
     * 验证和去重工县数组
     *
     * @param tools 原始工具数组
     * @return 验证后的工具数组
     */
    private ToolCallback[] validateAndDeduplicateTools(ToolCallback[] tools) {
        if (tools == null || tools.length == 0) {
            log.warn("工具数组为空");
            return new ToolCallback[0];
        }

        // 基于工具名称的去重（这是Spring AI框架关心的）
        Map<String, ToolCallback> toolByName = new LinkedHashMap<>();
        List<String> duplicateNames = new ArrayList<>();
        List<String> toolInfo = new ArrayList<>();

        for (ToolCallback tool : tools) {
            String toolName = extractToolName(tool);
            String toolInfoStr = String.format("%s (class: %s, instance: %s)",
                    toolName, tool.getClass().getSimpleName(), System.identityHashCode(tool));
            toolInfo.add(toolInfoStr);

            if (toolByName.containsKey(toolName)) {
                duplicateNames.add(toolName);
                log.warn("发现重复工具名称: {}，现有工具: {}, 冲突工具: {}",
                        toolName,
                        toolByName.get(toolName).getClass().getSimpleName(),
                        tool.getClass().getSimpleName());
            } else {
                toolByName.put(toolName, tool);
                log.debug("注册工具: {}", toolInfoStr);
            }
        }

        // 特殊处理：如果检测到重复且是doWrite工具
        if (duplicateNames.contains("doWrite")) {
            log.error("🚨 关键问题：doWrite工具名称重复！");
            log.error("所有工具信息：");
            toolInfo.forEach(info -> log.error("  - {}", info));

            // 尝试找出具体是哪个工具类造成了问题
            List<ToolCallback> writeTools = new ArrayList<>();
            for (ToolCallback tool : tools) {
                if ("doWrite".equals(extractToolName(tool))) {
                    writeTools.add(tool);
                }
            }
            log.error("doWrite相关的工具实例：");
            writeTools.forEach(tool -> log.error("  - {} @ {}",
                    tool.getClass().getSimpleName(), System.identityHashCode(tool)));
        }

        if (!duplicateNames.isEmpty()) {
            log.info("发现重复工具名称: {}", String.join(", ", duplicateNames));
        }

        ToolCallback[] result = toolByName.values().toArray(new ToolCallback[0]);
        log.info("工具去重完成 - 原始: {}, 去重后: {}", tools.length, result.length);

        return result;
    }

    /**
     * 反射提取工具名称的核心方法
     *
     * @param tool
     * @return
     */
    private String extractToolName(ToolCallback tool) {
        try {
            // 检查工具类上的注解
            Class<?> toolClass = tool.getClass();

            // 如果是代理类，获取原始类
            if (toolClass.getName().contains("$$EnhancerBySpringCGLIB$$") ||
                    toolClass.getName().contains("$$SpringCGLIB$$")) {
                toolClass = toolClass.getSuperclass();
                log.debug("检测到代理类，使用原始类: {}", toolClass.getSimpleName());
            }

            // 查找带有@Tool注解的方法
            java.lang.reflect.Method[] methods = toolClass.getDeclaredMethods();
            for (java.lang.reflect.Method method : methods) {
                if (method.isAnnotationPresent(Tool.class)) {
                    Tool toolAnnotation = method.getAnnotation(Tool.class);
                    String name = toolAnnotation.name();
                    if (!name.isEmpty()) {
                        log.debug("从{}的{}方法提取工具名称: {}",
                                toolClass.getSimpleName(), method.getName(), name);
                        return name;
                    }
                }
            }

            // 如果没有找到@Tool注解，使用类名作为后备
            String fallbackName = toolClass.getSimpleName().toLowerCase()
                    .replace("tool", "")
                    .replace("callback", "");
            log.debug("使用后备名称: {} (来自类 {})", fallbackName, toolClass.getSimpleName());
            return fallbackName;

        } catch (Exception e) {
            log.warn("提取工具名称时出错，使用默认名称: {}", e.getMessage());
            return "unknown_tool_" + System.identityHashCode(tool);
        }
    }

}