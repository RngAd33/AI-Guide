package com.rngad33.aiguide.rag.custom;

import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 文档切割器（官方特供）
 */
@Component
public class MyTokenTextSplitter {

    /**
     * 默认配置切割参数
     *
     * @param documents
     * @return
     */
    public List<Document> splitDocuments(List<Document> documents) {
        TokenTextSplitter splitter = new TokenTextSplitter();
        return splitter.apply(documents);
    }

    /**
     * 自定义切配置割参数
     *
     * @param documents
     * @return
     */
    public List<Document> splitCustomized(List<Document> documents) {
        TokenTextSplitter splitter = new TokenTextSplitter(200, 50, 5, 10000, true);
        return splitter.apply(documents);
    }

}