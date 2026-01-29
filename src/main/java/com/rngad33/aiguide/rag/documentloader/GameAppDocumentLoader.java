package com.rngad33.aiguide.rag.documentloader;

import com.rngad33.aiguide.enums.app.SystemPromptsEnum;
import com.rngad33.aiguide.utils.MarkdownUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 游戏王应用文档加载器
 */
@Component
@Slf4j
public class GameAppDocumentLoader {

    private final ResourcePatternResolver resourcePatternResolver;

    public GameAppDocumentLoader(ResourcePatternResolver resourcePatternResolver) {
        this.resourcePatternResolver = resourcePatternResolver;
    }

    /**
     * 加载本地 Markdown 文档
     *
     * @return
     */
    public List<Document> loadMarkdowns() {
        return MarkdownUtils.doLoad(resourcePatternResolver, SystemPromptsEnum.GAME.getName());
    }

}