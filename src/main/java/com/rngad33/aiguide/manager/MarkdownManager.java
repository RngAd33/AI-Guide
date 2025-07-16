package com.rngad33.aiguide.manager;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 通用文档加载器
 */
@Slf4j
public class MarkdownManager {

    /**
     * 加载本地 Markdown 文档
     *
     * @param resourcePatternResolver
     * @param packName
     * @return
     */
    public static List<Document> doLoad(ResourcePatternResolver resourcePatternResolver, String packName) {
        List<Document> allDocuments = new ArrayList<>();
        try {
            // 这里可以修改为要加载的多个Markdown文件的路径模式
            String path = StrUtil.format("classpath:document/{}/*.md", packName);
            Resource[] resources = resourcePatternResolver.getResources(path);
            for (Resource resource : resources) {
                String fileName = resource.getFilename();
                // 提取文档名称的倒数第二、三个字作为标签（表示应对场景）
                String scene = fileName.substring(fileName.length() - 6, fileName.length() - 4);
                MarkdownDocumentReaderConfig config = MarkdownDocumentReaderConfig.builder()
                        .withHorizontalRuleCreateDocument(true)
                        .withIncludeCodeBlock(false)
                        .withIncludeBlockquote(false)
                        .withAdditionalMetadata("filename", fileName)
                        .withAdditionalMetadata("scene", scene)
                        .build();
                MarkdownDocumentReader reader = new MarkdownDocumentReader(resource, config);
                allDocuments.addAll(reader.get());
            }
        } catch (IOException e) {
            log.error("——！Markdown文档加载失败！——", e);
        }
        return allDocuments;
    }

}