package com.rngad33.aiguide.rag.documentloader;

import org.springframework.ai.document.Document;

import java.util.List;

/**
 * 文档加载器接口
 */
public interface DocumentLoader {

    /**
     * 加载本地 Markdown 文档
     *
     * @return
     */
    List<Document> loadMarkdowns();

}