package com.rngad33.aiguide.config;

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
 * 心理咨询应用向量数据库配置（初始化基于内存的向量数据库 Bean）
 */
@Configuration
public class PsychologyAppVSConfig {

    @Resource
    private PsychologyAppDocumentLoader psychologyAppDocumentLoader;

    @Bean
    VectorStore psychologyAppVectorStore(@Qualifier("ollamaEmbeddingModel") EmbeddingModel dash) {
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(dash).build();
        List<Document> documentList = psychologyAppDocumentLoader.loadMarkdowns();
        simpleVectorStore.add(documentList);
        return simpleVectorStore;
    }

}