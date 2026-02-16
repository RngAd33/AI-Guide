package com.rngad33.aiguide.rag.config;

import com.rngad33.aiguide.model.enums.app.AppNameEnum;
import com.rngad33.aiguide.rag.documentloader.DocumentLoader;
import com.rngad33.aiguide.rag.factory.VectorStoreFactory;
import com.rngad33.aiguide.utils.AiModelUtils.MyEmbeddingModel;
import jakarta.annotation.Resource;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.springframework.ai.vectorstore.mariadb.MariaDBVectorStore.MariaDBDistanceType.COSINE;

/**
 * MarisDB 向量数据库配置
 */
@Configuration
public class MariaDbVectorStoreConfig {

    @Resource
    private VectorStoreFactory vectorStoreFactory;

    @Resource
    private DocumentLoader loveAppDocumentLoader;

    @Resource
    private DocumentLoader psychologyAppDocumentLoader;

    @Resource
    private DocumentLoader gameAppDocumentLoader;

    @Bean("loveMariaDbVectorStore")
    public VectorStore loveMariaDbVectorStore(JdbcTemplate jdbcTemplate, MyEmbeddingModel embeddingModel) {
        VectorStore vectorStore = vectorStoreFactory.getVectorStore(
                jdbcTemplate, embeddingModel, String.format("%s_app_vector_store", AppNameEnum.LOVE.getName()), COSINE);
        vectorStore.add(loveAppDocumentLoader.loadMarkdowns());
        return vectorStore;
    }

    @Bean("psychologyMariaDbVectorStore")
    public VectorStore psychologyMariaDbVectorStore(JdbcTemplate jdbcTemplate, MyEmbeddingModel embeddingModel) {
        VectorStore vectorStore = vectorStoreFactory.getVectorStore(
                jdbcTemplate, embeddingModel, String.format("%s_app_vector_store", AppNameEnum.PSYCHOLOGY.getName()), COSINE);
        vectorStore.add(psychologyAppDocumentLoader.loadMarkdowns());
        return vectorStore;
    }

    @Bean("gameMariaDbVectorStore")
    public VectorStore gameMariaDbVectorStore(JdbcTemplate jdbcTemplate, MyEmbeddingModel embeddingModel) {
        VectorStore vectorStore = vectorStoreFactory.getVectorStore(
                jdbcTemplate, embeddingModel, String.format("%s_app_vector_store", AppNameEnum.GAME.getName()), COSINE);
        vectorStore.add(gameAppDocumentLoader.loadMarkdowns());
        return vectorStore;
    }

}