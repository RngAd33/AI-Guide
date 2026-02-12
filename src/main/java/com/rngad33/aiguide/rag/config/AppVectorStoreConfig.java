package com.rngad33.aiguide.rag.config;

import com.rngad33.aiguide.rag.documentloader.GameAppDocumentLoader;
import com.rngad33.aiguide.rag.documentloader.LoveAppDocumentLoader;
import com.rngad33.aiguide.rag.documentloader.PsychologyAppDocumentLoader;
import com.rngad33.aiguide.rag.factory.VectorStoreFactory;
import com.rngad33.aiguide.utils.AiModelUtils.MyEmbeddingModel;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 本地文档式知识库配置
 */
@Slf4j
@Configuration
public class AppVectorStoreConfig {

    @Resource
    private VectorStoreFactory vectorStoreFactory;

    @Resource
    private LoveAppDocumentLoader loveAppDocumentLoader;

    @Resource
    private PsychologyAppDocumentLoader psychologyAppDocumentLoader;

    @Resource
    private GameAppDocumentLoader gameAppDocumentLoader;

    @Bean("loveAppVectorStore")
    public VectorStore loveAppVectorStore(MyEmbeddingModel embeddingModel) {
        List<Document> documents = loveAppDocumentLoader.loadMarkdowns();
        return vectorStoreFactory.getVectorStore(embeddingModel, documents);
    }

    @Bean("psychologyAppVectorStore")
    public VectorStore psychologyAppVectorStore(MyEmbeddingModel embeddingModel) {
        List<Document> documents = psychologyAppDocumentLoader.loadMarkdowns();
        return vectorStoreFactory.getVectorStore(embeddingModel, documents);
    }

    @Bean("gameAppVectorStore")
    public VectorStore gameAppVectorStore(MyEmbeddingModel embeddingModel) {
        List<Document> documents = gameAppDocumentLoader.loadMarkdowns();
        return vectorStoreFactory.getVectorStore(embeddingModel, documents);
    }

}