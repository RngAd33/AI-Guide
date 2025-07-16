package com.rngad33.aiguide.rag;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.model.transformer.KeywordMetadataEnricher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 元信息增强器
 */
@Component
public class MyKeywordEnricher {

    private final ChatModel chatModel;

    MyKeywordEnricher(@Qualifier("ollamaChatModel") ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    /**
     * 元信息增强
     *
     * @param documents 待处理文档列表
     * @return 增强后的文档列表
     */
    public List<Document> enrichDocuments(List<Document> documents) {
        KeywordMetadataEnricher enricher = new KeywordMetadataEnricher(this.chatModel, 5);
        return enricher.apply(documents);
    }

}