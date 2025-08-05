package com.rngad33.aiguide.rag.documentloader;

import com.rngad33.aiguide.manager.MarkdownManager;
import com.rngad33.aiguide.model.enums.app.SystemPromptsEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 海龟汤应用文档加载器
 */
@Component
@Slf4j
public class TetosoupAppDocumentLoader {

    private final ResourcePatternResolver resourcePatternResolver;

    public TetosoupAppDocumentLoader(ResourcePatternResolver resourcePatternResolver) {
        this.resourcePatternResolver = resourcePatternResolver;
    }

    /**
     * 加载本地 Markdown 文档
     *
     * @return
     */
    public List<Document> loadMarkdowns() {
        return MarkdownManager.doLoad(resourcePatternResolver, SystemPromptsEnum.TETO.getName());
    }

}