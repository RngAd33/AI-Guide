package com.rngad33.aiguide.rag.config;

import com.rngad33.aiguide.rag.MyKeywordEnricher;
import com.rngad33.aiguide.rag.MyTokenTextSplitter;
import com.rngad33.aiguide.rag.PsychologyAppDocumentLoader;
import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 向量数据库配置
 */
@Configuration
public class AppVectorStoreConfig {

    @Resource
    private PsychologyAppDocumentLoader psychologyAppDocumentLoader;

    @Resource
    private MyTokenTextSplitter myTokenTextSplitter;

    @Resource
    private MyKeywordEnricher myKeywordEnricher;

    /**
     * 初始化基于内存的向量数据库 Bean
     *
     * @param ollamaEmbeddingModel
     * @return
     */
    @Bean
    VectorStore psychologyAppVectorStore(@Qualifier("ollamaEmbeddingModel") EmbeddingModel ollamaEmbeddingModel) {
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(ollamaEmbeddingModel).build();
        // 加载文档
        List<Document> documents = psychologyAppDocumentLoader.loadMarkdowns();
        // 切割文档
        List<Document> splitDocuments = myTokenTextSplitter.splitDocuments(documents);
        // 元信息增强
        List<Document> enrichedDocuments = myKeywordEnricher.enrichDocuments(splitDocuments);
        // simpleVectorStore.add(splitDocuments);
        simpleVectorStore.add(enrichedDocuments);
        return simpleVectorStore;
    }

}